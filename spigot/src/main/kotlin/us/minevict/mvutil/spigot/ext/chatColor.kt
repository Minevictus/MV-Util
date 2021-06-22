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
package us.minevict.mvutil.spigot.ext

import us.minevict.mvutil.common.ext.BungeeChatColor

fun BukkitChatColor.bungee(): BungeeChatColor = when (this) {
    BukkitChatColor.AQUA -> BungeeChatColor.AQUA
    BukkitChatColor.DARK_AQUA -> BungeeChatColor.DARK_AQUA

    BukkitChatColor.RED -> BungeeChatColor.RED
    BukkitChatColor.DARK_RED -> BungeeChatColor.DARK_RED

    BukkitChatColor.BLUE -> BungeeChatColor.BLUE
    BukkitChatColor.DARK_BLUE -> BungeeChatColor.DARK_BLUE

    BukkitChatColor.GRAY -> BungeeChatColor.GRAY
    BukkitChatColor.DARK_GRAY -> BungeeChatColor.DARK_GRAY

    BukkitChatColor.GREEN -> BungeeChatColor.GREEN
    BukkitChatColor.DARK_GREEN -> BungeeChatColor.DARK_GREEN

    BukkitChatColor.LIGHT_PURPLE -> BungeeChatColor.LIGHT_PURPLE
    BukkitChatColor.DARK_PURPLE -> BungeeChatColor.DARK_PURPLE

    BukkitChatColor.YELLOW -> BungeeChatColor.YELLOW
    BukkitChatColor.GOLD -> BungeeChatColor.GOLD

    BukkitChatColor.BLACK -> BungeeChatColor.BLACK
    BukkitChatColor.WHITE -> BungeeChatColor.WHITE

    BukkitChatColor.BOLD -> BungeeChatColor.BOLD
    BukkitChatColor.ITALIC -> BungeeChatColor.ITALIC
    BukkitChatColor.MAGIC -> BungeeChatColor.MAGIC
    BukkitChatColor.UNDERLINE -> BungeeChatColor.UNDERLINE
    BukkitChatColor.STRIKETHROUGH -> BungeeChatColor.STRIKETHROUGH
    BukkitChatColor.RESET -> BungeeChatColor.RESET
}

fun BungeeChatColor.bukkit(): BukkitChatColor = when (this) {
    BungeeChatColor.AQUA -> BukkitChatColor.AQUA
    BungeeChatColor.DARK_AQUA -> BukkitChatColor.DARK_AQUA

    BungeeChatColor.RED -> BukkitChatColor.RED
    BungeeChatColor.DARK_RED -> BukkitChatColor.DARK_RED

    BungeeChatColor.BLUE -> BukkitChatColor.BLUE
    BungeeChatColor.DARK_BLUE -> BukkitChatColor.DARK_BLUE

    BungeeChatColor.GRAY -> BukkitChatColor.GRAY
    BungeeChatColor.DARK_GRAY -> BukkitChatColor.DARK_GRAY

    BungeeChatColor.GREEN -> BukkitChatColor.GREEN
    BungeeChatColor.DARK_GREEN -> BukkitChatColor.DARK_GREEN

    BungeeChatColor.LIGHT_PURPLE -> BukkitChatColor.LIGHT_PURPLE
    BungeeChatColor.DARK_PURPLE -> BukkitChatColor.DARK_PURPLE

    BungeeChatColor.YELLOW -> BukkitChatColor.YELLOW
    BungeeChatColor.GOLD -> BukkitChatColor.GOLD

    BungeeChatColor.BLACK -> BukkitChatColor.BLACK
    BungeeChatColor.WHITE -> BukkitChatColor.WHITE

    BungeeChatColor.BOLD -> BukkitChatColor.BOLD
    BungeeChatColor.ITALIC -> BukkitChatColor.ITALIC
    BungeeChatColor.MAGIC -> BukkitChatColor.MAGIC
    BungeeChatColor.UNDERLINE -> BukkitChatColor.UNDERLINE
    BungeeChatColor.STRIKETHROUGH -> BukkitChatColor.STRIKETHROUGH
    BungeeChatColor.RESET -> BukkitChatColor.RESET

    else -> BukkitChatColor.WHITE // Default to white
}
