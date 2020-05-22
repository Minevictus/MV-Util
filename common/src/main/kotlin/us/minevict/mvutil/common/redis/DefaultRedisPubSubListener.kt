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
package us.minevict.mvutil.common.redis

import io.lettuce.core.pubsub.RedisPubSubListener

/**
 * Interface for redis pub/sub listeners.
 *
 * @param K Key type.
 * @param V Value type.
 * @since 5.0.5
 */
interface DefaultRedisPubSubListener<K, V> : RedisPubSubListener<K, V> {
    override fun message(channel: K, message: V) = Unit
    override fun message(pattern: K, channel: K, message: V) = message(channel, message)
    override fun psubscribed(pattern: K, count: Long) = Unit
    override fun subscribed(channel: K, count: Long) = Unit
    override fun unsubscribed(channel: K, count: Long) = Unit
    override fun punsubscribed(pattern: K, count: Long) = Unit
}