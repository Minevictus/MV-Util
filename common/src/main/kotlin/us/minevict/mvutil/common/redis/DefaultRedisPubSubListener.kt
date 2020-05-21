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