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
package us.minevict.mvutil.spigot.channel

import org.bukkit.entity.Player
import us.minevict.mvutil.common.channel.IPacketChannel
import us.minevict.mvutil.common.channel.IPluginMessagePacketHandler

/**
 * Extension of [IPacketChannel] with Bukkit generics.
 *
 * @param P The type of packets to flow through this channel and its handler.
 * @since 5.0.0
 */
typealias PacketChannel<P> = IPacketChannel<P, Player>

/**
 * Extension of [IPluginMessagePacketHandler] with Bukkit generics.
 *
 * @param P The type of packets to handle.
 * @since 5.0.0
 */
typealias PluginMessagePacketHandler<P> = IPluginMessagePacketHandler<P, Player, Player>