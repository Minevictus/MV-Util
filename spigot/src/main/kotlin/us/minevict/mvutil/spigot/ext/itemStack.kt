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
@file:Suppress("UNCHECKED_CAST")

package us.minevict.mvutil.spigot.ext

import org.bukkit.block.BlockState
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.BlockStateMeta
import org.bukkit.inventory.meta.ItemMeta

/**
 * Edit the item meta of an item then set it back.
 *
 * @since 5.0.0
 */
fun ItemStack.editMeta(block: (ItemMeta) -> Unit): ItemStack {
    itemMeta = itemMeta.also(block)
    return this
}

/**
 * Edit the item meta of an item then set it back.
 *
 * @since 5.0.0
 */
fun <T : ItemMeta> ItemStack.editMetaAs(block: (T) -> Unit): ItemStack {
    itemMeta = (itemMeta as? T)?.also(block) ?: return this
    return this
}

/**
 * Edit the block state of an item containing one then set it back.
 *
 * @since 5.0.0
 */
fun BlockStateMeta.editState(block: (BlockState) -> Unit): BlockStateMeta {
    blockState = blockState.also(block)
    return this
}

/**
 * Edit the block state of an item containing one then set it back.
 *
 * @since 5.0.0
 */
fun <T : BlockState> BlockStateMeta.editStateAs(block: (T) -> Unit): BlockStateMeta {
    blockState = (blockState as? T)?.also(block) ?: return this
    return this
}