@file:Suppress("NOTHING_TO_INLINE")

package us.minevict.mvutil.common.ext

import net.md_5.bungee.api.ChatColor

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