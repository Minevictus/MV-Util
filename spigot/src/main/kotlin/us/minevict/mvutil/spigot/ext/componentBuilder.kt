package us.minevict.mvutil.spigot.ext

import net.md_5.bungee.api.chat.ComponentBuilder
import org.bukkit.inventory.ItemStack
import us.minevict.mvutil.spigot.text.ItemHoverJoiner

fun ComponentBuilder.hover(item: ItemStack): ComponentBuilder = append(ItemHoverJoiner(item))