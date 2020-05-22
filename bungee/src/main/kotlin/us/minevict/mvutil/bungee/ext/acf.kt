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
