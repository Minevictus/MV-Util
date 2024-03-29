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
package us.minevict.mvutil.common.ext

import java.time.Duration
import java.util.*
import java.util.concurrent.TimeUnit

private val timePattern = Regex("(\\d+)\\s*(mo|ms|µs|ns|[smhdwy])\\s*", RegexOption.IGNORE_CASE)

/**
 * Parse a [Duration] from a [String].
 * This extension is intended to parse durations expressed on a "2d10h" (two days, 10 hours) format.
 *
 * The characters used represent the following time frame:
 * - "ns": nanoseconds
 * - "µs": microseconds
 * - "ms": milliseconds
 * - "s": seconds
 * - "m": minutes
 * - "h": hours
 * - "d": days
 * - "w": weeks (7 days)
 * - "mo": months (30 days)
 * - "y": years (365 days)
 *
 * Empty strings will return [Duration.ZERO].
 *
 * @since 5.0.3
 */
fun String.parseDuration(): Duration {
    var duration = Duration.ZERO

    if (this.isBlank())
        return duration

    val matcher = timePattern.findAll(this.trim())

    for (result in matcher) {
        val amount = result.groups[1]!!.value.toLong()

        when (result.groups[2]!!.value.lowercase()) {
            "ns" -> duration += Duration.ofNanos(amount)
            "µs" -> duration += Duration.ofNanos(TimeUnit.MICROSECONDS.toNanos(amount))
            "ms" -> duration += Duration.ofMillis(amount)
            "s" -> duration += Duration.ofSeconds(amount)
            "m" -> duration += Duration.ofMinutes(amount)
            "h" -> duration += Duration.ofHours(amount)
            "d" -> duration += Duration.ofDays(amount)
            // 1 week being 7 days is standard according to ISO-8601.
            "w" -> duration += Duration.ofDays(amount * 7)
            // 1 month being 30 days is standard according to ISO-8601.
            "mo" -> duration += Duration.ofDays(amount * 30)
            // 1 year being 365 days is standard according to ISO-8601.
            "y" -> duration += Duration.ofDays(amount * 365)
        }
    }

    return duration
}