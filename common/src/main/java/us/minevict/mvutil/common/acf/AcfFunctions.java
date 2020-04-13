package us.minevict.mvutil.common.acf;

import co.aikar.commands.CommandManager;

/**
 * @since 0.1.0
 */
public class AcfFunctions {
  private AcfFunctions() throws IllegalAccessException {
    throw new IllegalAccessException(getClass().getSimpleName() + " cannot be instantiated.");
  }

  /**
   * Enables the help feature in the given {@link CommandManager}.
   *
   * @param commandManager The {@link CommandManager} to enable help for.
   */
  @SuppressWarnings("deprecation") // Developer warning
  public static void enableHelpFeature(CommandManager<?, ?, ?, ?, ?, ?> commandManager) {
    commandManager.enableUnstableAPI("help");
  }
}