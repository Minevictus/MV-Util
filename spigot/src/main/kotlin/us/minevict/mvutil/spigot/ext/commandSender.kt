package us.minevict.mvutil.spigot.ext

import org.bukkit.Bukkit
import org.bukkit.command.CommandSender

fun CommandSender.executeCommand(command: String) = Bukkit.dispatchCommand(this, command)
