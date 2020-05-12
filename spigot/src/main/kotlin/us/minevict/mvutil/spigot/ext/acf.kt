package us.minevict.mvutil.spigot.ext

import co.aikar.commands.*
import co.aikar.commands.contexts.ContextResolver
import co.aikar.commands.contexts.IssuerAwareContextResolver
import co.aikar.commands.contexts.IssuerOnlyContextResolver
import co.aikar.commands.contexts.OptionalContextResolver

typealias AcfAsyncCompleter = CommandCompletions.AsyncCommandCompletionHandler<BukkitCommandCompletionContext>
typealias AcfCompleter = CommandCompletions.CommandCompletionHandler<BukkitCommandCompletionContext>
typealias AcfContextResolver<T> = ContextResolver<T, BukkitCommandExecutionContext>
typealias AcfOptionalContextResolver<T> = OptionalContextResolver<T, BukkitCommandExecutionContext>
typealias AcfIssuerAwareContextResolver<T> = IssuerAwareContextResolver<T, BukkitCommandExecutionContext>
typealias AcfIssuerOnlyContextResolver<T> = IssuerOnlyContextResolver<T, BukkitCommandExecutionContext>
typealias AcfCommandCondition = CommandConditions.Condition<BukkitCommandIssuer>
typealias AcfParameterCondition<P> = CommandConditions.ParameterCondition<P, BukkitCommandExecutionContext, BukkitCommandIssuer>
