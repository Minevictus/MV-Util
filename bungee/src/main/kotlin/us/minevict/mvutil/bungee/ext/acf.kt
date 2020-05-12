package us.minevict.mvutil.bungee.ext

import co.aikar.commands.*
import co.aikar.commands.contexts.ContextResolver
import co.aikar.commands.contexts.IssuerAwareContextResolver
import co.aikar.commands.contexts.IssuerOnlyContextResolver
import co.aikar.commands.contexts.OptionalContextResolver

typealias AcfAsyncCompleter = CommandCompletions.AsyncCommandCompletionHandler<BungeeCommandCompletionContext>
typealias AcfCompleter = CommandCompletions.CommandCompletionHandler<BungeeCommandCompletionContext>
typealias AcfContextResolver<T> = ContextResolver<T, BungeeCommandExecutionContext>
typealias AcfOptionalContextResolver<T> = OptionalContextResolver<T, BungeeCommandExecutionContext>
typealias AcfIssuerAwareContextResolver<T> = IssuerAwareContextResolver<T, BungeeCommandExecutionContext>
typealias AcfIssuerOnlyContextResolver<T> = IssuerOnlyContextResolver<T, BungeeCommandExecutionContext>
typealias AcfCommandCondition = CommandConditions.Condition<BungeeCommandIssuer>
typealias AcfParameterCondition<P> = CommandConditions.ParameterCondition<P, BungeeCommandExecutionContext, BungeeCommandIssuer>
