package us.minevict.mvutil.spigot.hidden;

import co.aikar.idb.Database;
import java.util.HashMap;
import java.util.Map;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

public final class CloseDatabaseListener implements Listener {
  private static final Object LOCK = new Object();
  private static CloseDatabaseListener INSTANCE = null;

  @NotNull
  private final Map<Plugin, Database> databases = new HashMap<>();

  private CloseDatabaseListener() {
  }

  @NotNull
  public static CloseDatabaseListener getInstance() {
    if (INSTANCE == null) {
      synchronized (LOCK) {
        if (INSTANCE == null) {
          return INSTANCE = new CloseDatabaseListener();
        }
      }
    }

    return INSTANCE;
  }

  @EventHandler(priority = EventPriority.MONITOR)
  public void onPluginDisable(PluginDisableEvent event) {
    var database = databases.remove(event.getPlugin());
    if (database != null) {
      event.getPlugin().getLogger().info("MV-Util closing database for plugin.");
      database.close();
    }
  }

  @NotNull
  public Map<Plugin, Database> getDatabases() {
    return databases;
  }
}