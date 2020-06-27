/**
 * MV-Util
 * Copyright (C) 2020 Mariell Hoversholm, Nahuel Dolores
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published
 * by the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
@file:Suppress("NOTHING_TO_INLINE")

package us.minevict.mvutil.common.ext

import de.themoep.minedown.MineDown
import net.md_5.bungee.api.ChatColor
import net.md_5.bungee.api.chat.BaseComponent

/**
 * Apply the colour codes to this string using [char] as the colour character.
 *
 * @param char The character to use for colour application.
 * @return The same string with colour and formatting applied.
 * @since 5.2.0
 */
inline fun String.colour(char: Char = '&'): String =
    BungeeChatColor.translateAlternateColorCodes(char, this)

/**
 * Apply the colour codes to this string using [char] as the colour character.
 *
 * @param char The character to use for colour application.
 * @return The same string with colour and formatting applied.
 * @since 5.2.0
 */
inline fun String.color(char: Char = '&'): String =
    BungeeChatColor.translateAlternateColorCodes(char, this)

/**
 * Strip all colour and formatting from this string.
 *
 * @return The same string with no colour and formatting applied.
 * @since 5.2.0
 */
inline fun String.stripColour(): String =
    ChatColor.stripColor(this)

/**
 * Strip all colour and formatting from this string.
 *
 * @return The same string with no colour and formatting applied.
 * @since 5.2.0
 */
inline fun String.stripColor(): String =
    ChatColor.stripColor(this)

/**
 * Parse the string using MineDown and output components.
 *
 * @param placeholders Placeholders to replace in the MineDown text. These are surrounded by `%`s.
 * @return Parsed MineDown text.
 * @since 6.1.0
 */
fun String.minedown(vararg placeholders: Pair<String, Any?>): Array<BaseComponent> =
    MineDown.parse(
        this,
        *placeholders.mapTo(mutableListOf()) { listOf(it.first, it.second.toString()) }
            .flatten()
            .toTypedArray()
    )
