package us.minevict.mvutil.spigot.utils;

/**
 * Utilities regarding GUIs, mainly for use with InventoryGUI.
 */
public class GuiUtils {
  private GuiUtils() throws IllegalAccessException {
    throw new IllegalAccessException(getClass().getSimpleName() + " cannot be instantiated.");
  }

  /**
   * All the characters rows may contain.
   * <p>
   * There are in total 54 characters, fitting a chunk of 9 characters at a time.
   */
  public static final String GUI_CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789abcdefghijklmnopqr";

  /**
   * Calculates how many rows a specific amount of items may need.
   * <p>
   * This grows by default and uses 6 rows at most.
   *
   * @param items How many items are to be stored in this inventory.
   * @return The calculated rows.
   */
  public static String[] calculateRows(int items) {
    return calculateRows(items, 6, true);
  }

  /**
   * Calculates how many rows a specific amount of items may need.
   * <p>
   * This grows by default.
   *
   * @param items         How many items are to be stored in this inventory.
   * @param inventoryRows The max amount of rows allowed. This is capped to 6 at most and 1 at least.
   * @return The calculated rows.
   */
  public static String[] calculateRows(int items, int inventoryRows) {
    return calculateRows(items, inventoryRows, true);
  }

  /**
   * Calculates how many rows a specific amount of items may need.
   *
   * @param items         How many items are to be stored in this inventory.
   * @param inventoryRows The max amount of rows allowed. This is capped to 6 at most and 1 at least.
   * @param growing       Whether the rows should grow per the amount of items.
   * @return The calculated rows.
   */
  public static String[] calculateRows(int items, int inventoryRows, boolean growing) {
    var maximumPageRows = Math.max(1, Math.min(inventoryRows, 6));
    int requiredRows;
    if (!growing) {
      requiredRows = maximumPageRows;
    } else {
      requiredRows = items > maximumPageRows * 9
          ? maximumPageRows
          : items / (maximumPageRows * 9);
    }

    var rows = new String[requiredRows + (items > maximumPageRows * 9 ? 1 : 0)];
    for (int i = 0; i < requiredRows; ++i) {
      rows[i] = GUI_CHARACTERS.substring(i, i + 8);
    }

    if (items < requiredRows * 9) {
      var last = Math.abs(items - requiredRows * 9);
      rows[rows.length - 1] = rows[rows.length - 1].substring(0, last - 1) + " ".repeat(last);
    }

    return rows;
  }

  /**
   * Calculates how many rows a specific amount of items may need with regard to a paging bar at the bottom.
   * <p>
   * This uses the paging bar <pre>"<   ~   >"</pre> by default and always attempts to fit any overflowing items in the
   * 6th row if a paging bar may be omitted.
   * <p>
   * This also grows the amount of rows necessary per the amount of items stored. By default, it will attempt to cap at
   * 5 rows.
   *
   * @param items How many items are to be stored in this inventory.
   * @return The calculated rows.
   */
  public static String[] calculatePagingRows(int items) {
    return calculatePagingRows(items, 5);
  }

  /**
   * Calculates how many rows a specific amount of items may need with regard to a paging bar at the bottom.
   * <p>
   * This uses the paging bar <pre>"<   ~   >"</pre> by default and always attempts to fit any overflowing items in the
   * 6th row if a paging bar may be omitted.
   * <p>
   * This also grows the amount of rows necessary per the amount of items stored.
   *
   * @param items    How many items are to be stored in this inventory.
   * @param pageRows The max amount of rows allowed in a single page. This is capped to 5 at most and 1 at least.
   * @return The calculated rows.
   */
  public static String[] calculatePagingRows(int items, int pageRows) {
    return calculatePagingRows(items, pageRows, true);
  }

  /**
   * Calculates how many rows a specific amount of items may need with regard to a paging bar at the bottom.
   * <p>
   * This uses the paging bar <pre>"<   ~   >"</pre> by default and always attempts to fit any overflowing items in the
   * 6th row if a paging bar may be omitted.
   *
   * @param items    How many items are to be stored in this inventory.
   * @param pageRows The max amount of rows allowed in a single page. This is capped to 5 at most and 1 at least.
   * @param growing  Whether the rows should grow per the amount of items.
   * @return The calculated rows.
   */
  public static String[] calculatePagingRows(int items, int pageRows, boolean growing) {
    return calculatePagingRows(items, pageRows, growing, true);
  }

  /**
   * Calculates how many rows a specific amount of items may need with regard to a paging bar at the bottom.
   * <p>
   * This uses the paging bar <pre>"<   ~   >"</pre> by default.
   *
   * @param items      How many items are to be stored in this inventory.
   * @param pageRows   The max amount of rows allowed in a single page. This is capped to 5 at most and 1 at least.
   * @param growing    Whether the rows should grow per the amount of items.
   * @param attemptFit Whether we should attempt to fit any overflowing items in the 6th row instead of using a paging
   *                   bar.
   * @return The calculated rows.
   */
  public static String[] calculatePagingRows(int items, int pageRows, boolean growing, boolean attemptFit) {
    return calculatePagingRows(items, pageRows, growing, attemptFit, "<   ~   >");
  }

  /**
   * Calculates how many rows a specific amount of items may need with regard to a paging bar at the bottom.
   *
   * @param items      How many items are to be stored in this inventory.
   * @param pageRows   The max amount of rows allowed in a single page. This is capped to 5 at most and 1 at least.
   * @param growing    Whether the rows should grow per the amount of items.
   * @param attemptFit Whether we should attempt to fit any overflowing items in the 6th row instead of using a paging
   *                   bar.
   * @param pageBar    The format for the paging bar.
   * @return The calculated rows.
   */
  public static String[] calculatePagingRows(
      int items,
      int pageRows,
      boolean growing,
      boolean attemptFit,
      String pageBar
  ) {
    var requiredRows = Math.max(1, Math.min(pageRows, 5));

    var pagingBar = items > requiredRows * 9;
    if (attemptFit && items <= 6 * 9) {
      pagingBar = false;
    }

    var calculatingRows = items <= requiredRows * 9
        ? requiredRows
        : (pagingBar
            ? requiredRows + 1
            : (int) Math.ceil((double) items / 9));

    var rows = calculateRows(items, pagingBar ? requiredRows : calculatingRows, growing);

    if (pagingBar) {
      rows[rows.length - 1] = pageBar;
    }

    return rows;
  }
}