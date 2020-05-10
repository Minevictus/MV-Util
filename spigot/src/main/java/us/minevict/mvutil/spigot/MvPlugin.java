package us.minevict.mvutil.spigot;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.PaperCommandManager;
import co.aikar.idb.Database;
import co.aikar.taskchain.BukkitTaskChainFactory;
import co.aikar.taskchain.TaskChain;
import co.aikar.taskchain.TaskChainFactory;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;
import org.jetbrains.annotations.NotNull;
import us.minevict.mvutil.common.IMvPlugin;
import us.minevict.mvutil.common.LazyValue;
import us.minevict.mvutil.common.acf.AcfCooldowns;
import us.minevict.mvutil.common.channel.IPacketChannel;

/**
 * The base plugin for Spigot plugins using MV-Util.
 *
 * @since 3.4.0
 */
@SuppressWarnings("RedundantThrows") // They exist to show what is allowed to be thrown.
public abstract class MvPlugin
    extends JavaPlugin
    implements IMvPlugin<PaperCommandManager, Listener, MinevictusUtilsSpigot> {
  private final LazyValue<PaperCommandManager> acf = new LazyValue<>(this::constructAcf);
  private final List<BukkitTask> tasks = new ArrayList<>();
  private final List<IPacketChannel<?, ?>> channels = new ArrayList<>();
  private PluginErrorState errorState = null;
  private TaskChainFactory taskChainFactory = null;
  private String databaseName;
  private final LazyValue<Database> database = new LazyValue<>(this::constructDatabase);

  @Override
  public final void onLoad() {
    databaseName = getName().toLowerCase();

    try {
      if (!load()) {
        errorState = PluginErrorState.LOAD;
        getLogger().warning("Disabling plugin...");
        setEnabled(false);
      }
    } catch (Exception ex) {
      errorState = PluginErrorState.LOAD;
      getLogger().severe("Encountered exception upon loading:");
      ex.printStackTrace();
      getLogger().warning("Disabling plugin...");
      setEnabled(false);
    }

    if (errorState != null) {
      shutdownSafely(false);
    }
  }

  @Override
  public final void onEnable() {
    if (errorState != null) {
      return;
    }

    taskChainFactory = BukkitTaskChainFactory.create(this);

    try {
      if (!enable()) {
        errorState = PluginErrorState.ENABLE;
        getLogger().warning("Disabling plugin...");
        setEnabled(false);
      }
    } catch (Exception ex) {
      errorState = PluginErrorState.ENABLE;
      getLogger().severe("Encountered exception upon enabling:");
      ex.printStackTrace();
      getLogger().warning("Disabling plugin...");
      setEnabled(false);
    }

    if (!isEnabled()) {
      shutdownSafely(true);
    }
  }

  @Override
  public final void onDisable() {
    if (errorState != null) {
      return;
    }

    shutdownSafely(false);

    try {
      disable();
    } catch (Exception ex) {
      getLogger().severe("Encountered exception upon disabling:");
      ex.printStackTrace();
    }

    taskChainFactory.shutdown(5, TimeUnit.SECONDS);
  }

  private void shutdownSafely(boolean doChain) {
    if (doChain) {
      taskChainFactory.shutdown(5, TimeUnit.SECONDS);
    }
    tasks.forEach(BukkitTask::cancel);
    HandlerList.unregisterAll(this);
    acf.getIfInitialised().ifPresent(PaperCommandManager::unregisterCommands);
    channels.forEach(IPacketChannel::unregisterIncoming);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  @NotNull
  public MinevictusUtilsSpigot getMvUtil() {
    return MinevictusUtilsSpigot.getInstance();
  }

  @NotNull
  private PaperCommandManager constructAcf() {
    return getMvUtil().prepareAcf(new PaperCommandManager(this));
  }

  @NotNull
  private Database constructDatabase() {
    return getMvUtil().prepareDatabase(this.databaseName, this);
  }

  /**
   * Gets a {@link PaperCommandManager} linked to this plugin.
   * <p>
   * This has already been {@link MinevictusUtilsSpigot#prepareAcf prepared} and is only constructed once gotten.
   *
   * @return A newly constructed or cached {@link PaperCommandManager} for this plugin.
   */
  @Override
  @NotNull
  public final PaperCommandManager getAcf() {
    return acf.getValue();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  @NotNull
  public Database getDatabase() {
    return database.getValue();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  @NotNull
  public String getDatabaseName() {
    return databaseName;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setDatabaseName(String databaseName) {
    this.databaseName = databaseName;
  }

  /**
   * Registers commands to this plugin's {@link PaperCommandManager}.
   * <p>
   * This overwrites other commands in the same names by default.
   *
   * @param commands The commands to register.
   */
  @Override
  public final void registerCommands(@NotNull BaseCommand... commands) {
    registerCommands(true, commands);
  }

  /**
   * Registers commands to this plugin's {@link PaperCommandManager}.
   *
   * @param force    Whether to overwrite other commands in the same names.
   * @param commands The commands to register.
   */
  public final void registerCommands(boolean force, @NotNull BaseCommand... commands) {
    var acf = getAcf();
    for (var command : commands) {
      acf.registerCommand(command, force);
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void listeners(@NotNull Listener... listeners) {
    var pluginManager = getServer().getPluginManager();
    for (var listener : listeners) {
      pluginManager.registerEvents(listener, this);
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  @NotNull
  public String getVersion() {
    return getDescription().getVersion();
  }

  /**
   * Add the given tasks to a cancelling routine upon disable.
   *
   * @param tasks The tasks to cancel.
   */
  public final void tasks(@NotNull BukkitTask... tasks) {
    this.tasks.addAll(Arrays.asList(tasks));
  }

  /**
   * Creates a new task chain for this plugin.
   *
   * @param <T> The type of the chain.
   * @return A newly constructed task chain.
   */
  public <T> TaskChain<T> newTaskChain() {
    return taskChainFactory.newChain();
  }

  /**
   * Creates a new task chain for this plugin.
   *
   * @param name The shared name for this chain.
   * @param <T>  The type of the chain.
   * @return A newly constructed task chain.
   */
  public <T> TaskChain<T> newTaskChain(@NotNull String name) {
    return taskChainFactory.newSharedChain(name);
  }

  /**
   * Set up the tables for these cooldowns.
   *
   * @param database  The database to set up the tables within.
   * @param cooldowns The cooldowns to setup tables for.
   * @since 3.6.0
   */
  public void setupCooldowns(@NotNull Database database, @NotNull AcfCooldowns[] cooldowns) {
    for (var cooldown : cooldowns) {
      cooldown.setupTable(database).exceptionally(ex -> {
        getLogger().warning("Exception when setting up cooldown");
        ex.printStackTrace();
        return null;
      });
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  @NotNull
  public <P, C extends IPacketChannel<P, ?>> C packetChannel(@NotNull C channel) {
    channels.add(channel);
    return channel;
  }
}
