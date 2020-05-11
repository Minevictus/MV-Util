package us.minevict.mvutil.spigot.utils

import de.themoep.inventorygui.InventoryGui
import kotlin.math.abs
import kotlin.math.ceil
import kotlin.math.max
import kotlin.math.min

/**
 * Utilities regarding GUIs, mainly for use with [InventoryGui].
 *
 * @since 5.0.0
 */
object GuiUtils {
    /**
     * All the characters rows may contain.
     *
     * There are in total 54 characters, fitting a chunk of 9 characters at a time.
     */
    const val GUI_CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789abcdefghijklmnopqr"

    /**
     * The default value for what how the page bar should look like.
     *
     * This is designed for the use of `<` as the previous page button, `~` as the current page
     * information item, and `>` as the next page button.
     *
     * This is only available for paging calculations.
     *
     * @since 0.3.5
     */
    const val DEFAULT_PAGE_BAR = "<   ~   >"

    /**
     * Calculates how many rows a specific amount of items may need.
     *
     * @param items How many items are to be stored in this inventory.
     * @param inventoryRows The max amount of rows allowed. This is clamped between 1 and 6.
     * @param growing Whether the rows should grow per the amount of items.
     * @param onlyEmptyCharacters Use only spaces instead of [GUI_CHARACTERS].
     * @return The calculated rows.
     */
    fun calculateRows(
        items: Int,
        inventoryRows: Int = 6,
        growing: Boolean = true,
        onlyEmptyCharacters: Boolean = false
    ): Array<String> {
        val maximumPageRows = min(max(1, inventoryRows), 6)
        val requiredRows = if (growing) {
            if (items > maximumPageRows * 9) maximumPageRows
            else ceil(items.toDouble() / 9).toInt()
        } else maximumPageRows

        val characters = if (onlyEmptyCharacters) " ".repeat(45)
        else GUI_CHARACTERS
        val rows = characters.chunked(9).take(requiredRows).toTypedArray()
        if (!onlyEmptyCharacters && items < requiredRows * 9) rows[rows.size - 1] = rows.last()
            .substring(0 until abs(items - (requiredRows * 9)))
        if (!onlyEmptyCharacters && items < requiredRows * 9) {
            val excess = abs(items - (requiredRows * 9))
            rows[rows.size - 1] = rows.last().substring(0 until 9 - excess) + " ".repeat(excess)
        }

        return rows
    }

    /**
     * Calculates how many rows a specific amount of items may need with regard to a paging bar at the bottom.
     *
     * @param items How many items are to be stored in this inventory.
     * @param pageRows The max amount of rows allowed in a single page. This is clamped between 1 and 5.
     * @param growing Whether the rows should grow per the amount of items.
     * @param attemptFit Whether we should attempt to fit any overflowing items in the 6th row instead of using a paging bar.
     * @param onlyEmptyCharacters Use only spaces instead of [GUI_CHARACTERS].
     * @param pageBar The format for the paging bar.
     * @return The calculated rows.
     */
    fun calculatePagingRows(
        items: Int,
        pageRows: Int = 5,
        growing: Boolean = true,
        attemptFit: Boolean = true,
        onlyEmptyCharacters: Boolean = false,
        pageBar: String = DEFAULT_PAGE_BAR
    ): Array<String> {
        val requiredRows = max(1, min(pageRows, 5))
        val usePagingBar = items > requiredRows * 9 && !(attemptFit && items <= 6 * 9)
        val calculatingRows = when {
            items <= requiredRows * 9 -> requiredRows
            usePagingBar -> requiredRows + 1
            else -> ceil(items.toDouble() / 9).toInt()
        }
        val rows = calculateRows(
            items,
            if (usePagingBar) requiredRows else calculatingRows,
            growing,
            onlyEmptyCharacters
        )
        if (usePagingBar) rows[rows.size - 1] = pageBar
        return rows
    }
}