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