package us.minevict.mvutil.bungee.ext

import net.md_5.bungee.api.CommandSender
import net.md_5.bungee.api.ProxyServer

fun CommandSender.executeCommand(command: String) =
    ProxyServer.getInstance().pluginManager.dispatchCommand(this, command)
