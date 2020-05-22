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

import net.md_5.bungee.api.chat.BaseComponent
import net.md_5.bungee.api.chat.ClickEvent
import net.md_5.bungee.api.chat.ComponentBuilder
import net.md_5.bungee.api.chat.HoverEvent
import org.apache.commons.lang.time.DurationFormatUtils
import java.math.RoundingMode
import java.net.URI
import java.net.URL
import java.util.concurrent.TimeUnit

// These don't require documentation.
// It's obvious what they do and they're mostly one-liners.

fun ComponentBuilder.append(value: Any?): ComponentBuilder = append(value.toString())

fun ComponentBuilder.click(event: ClickEvent?): ComponentBuilder = event(event)
fun ComponentBuilder.click(action: ClickEvent.Action, value: String): ComponentBuilder =
    event(ClickEvent(action, value))

fun ComponentBuilder.click(uri: URI): ComponentBuilder =
    click(ClickEvent.Action.OPEN_URL, uri.toASCIIString())

fun ComponentBuilder.click(url: URL): ComponentBuilder =
    click(ClickEvent.Action.OPEN_URL, url.toString())

fun ComponentBuilder.hover(event: HoverEvent?): ComponentBuilder = event(event)
fun ComponentBuilder.hover(
    action: HoverEvent.Action,
    value: Array<BaseComponent>
): ComponentBuilder =
    event(HoverEvent(action, value))

fun ComponentBuilder.hover(text: String): ComponentBuilder =
    hover(HoverEvent.Action.SHOW_TEXT, ComponentBuilder(text).create())

fun ComponentBuilder.properReset(): ComponentBuilder = reset().click(null).hover(null)

fun ComponentBuilder.decimal(
    number: Float,
    decimals: Int = 2,
    roundingMode: RoundingMode = RoundingMode.HALF_UP
): ComponentBuilder =
    append(number.toBigDecimal().setScale(decimals, roundingMode))

fun ComponentBuilder.decimal(
    number: Double,
    decimals: Int = 2,
    roundingMode: RoundingMode = RoundingMode.HALF_UP
): ComponentBuilder =
    append(number.toBigDecimal().setScale(decimals, roundingMode))

fun ComponentBuilder.duration(
    duration: Long,
    unit: TimeUnit,
    suppressLeadingZeroElements: Boolean = true,
    suppressTrailingZeroElements: Boolean = true
): ComponentBuilder =
    append(
        DurationFormatUtils.formatDurationWords(
            unit.toMillis(duration),
            suppressLeadingZeroElements,
            suppressTrailingZeroElements
        )
    )

fun ComponentBuilder.black(): ComponentBuilder = color(BungeeChatColor.BLACK)
fun ComponentBuilder.black(append: Any?): ComponentBuilder = append(append).black()

fun ComponentBuilder.darkBlue(): ComponentBuilder = color(BungeeChatColor.DARK_BLUE)
fun ComponentBuilder.darkBlue(append: Any?): ComponentBuilder = append(append).darkBlue()

fun ComponentBuilder.darkGreen(): ComponentBuilder = color(BungeeChatColor.DARK_GREEN)
fun ComponentBuilder.darkGreen(append: Any?): ComponentBuilder = append(append).darkGreen()

fun ComponentBuilder.darkAqua(): ComponentBuilder = color(BungeeChatColor.DARK_AQUA)
fun ComponentBuilder.darkAqua(append: Any?): ComponentBuilder = append(append).darkAqua()

fun ComponentBuilder.darkRed(): ComponentBuilder = color(BungeeChatColor.DARK_RED)
fun ComponentBuilder.darkRed(append: Any?): ComponentBuilder = append(append).darkRed()

fun ComponentBuilder.purple(): ComponentBuilder = color(BungeeChatColor.DARK_PURPLE)
fun ComponentBuilder.purple(append: Any?): ComponentBuilder = append(append).purple()

fun ComponentBuilder.gold(): ComponentBuilder = color(BungeeChatColor.GOLD)
fun ComponentBuilder.gold(append: Any?): ComponentBuilder = append(append).gold()

fun ComponentBuilder.grey(): ComponentBuilder = color(BungeeChatColor.GRAY)
fun ComponentBuilder.grey(append: Any?): ComponentBuilder = append(append).grey()

fun ComponentBuilder.gray(): ComponentBuilder = color(BungeeChatColor.GRAY)
fun ComponentBuilder.gray(append: Any?): ComponentBuilder = append(append).grey()

fun ComponentBuilder.darkGrey(): ComponentBuilder = color(BungeeChatColor.DARK_GRAY)
fun ComponentBuilder.darkGrey(append: Any?): ComponentBuilder = append(append).darkGrey()

fun ComponentBuilder.darkGray(): ComponentBuilder = color(BungeeChatColor.DARK_GRAY)
fun ComponentBuilder.darkGray(append: Any?): ComponentBuilder = append(append).darkGrey()

fun ComponentBuilder.blue(): ComponentBuilder = color(BungeeChatColor.BLUE)
fun ComponentBuilder.blue(append: Any?): ComponentBuilder = append(append).blue()

fun ComponentBuilder.green(): ComponentBuilder = color(BungeeChatColor.GREEN)
fun ComponentBuilder.green(append: Any?): ComponentBuilder = append(append).green()

fun ComponentBuilder.aqua(): ComponentBuilder = color(BungeeChatColor.AQUA)
fun ComponentBuilder.aqua(append: Any?): ComponentBuilder = append(append).aqua()

fun ComponentBuilder.red(): ComponentBuilder = color(BungeeChatColor.RED)
fun ComponentBuilder.red(append: Any?): ComponentBuilder = append(append).red()

fun ComponentBuilder.pink(): ComponentBuilder = color(BungeeChatColor.LIGHT_PURPLE)
fun ComponentBuilder.pink(append: Any?): ComponentBuilder = append(append).pink()

fun ComponentBuilder.yellow(): ComponentBuilder = color(BungeeChatColor.YELLOW)
fun ComponentBuilder.yellow(append: Any?): ComponentBuilder = append(append).yellow()

fun ComponentBuilder.white(): ComponentBuilder = color(BungeeChatColor.WHITE)
fun ComponentBuilder.white(append: Any?): ComponentBuilder = append(append).white()

fun ComponentBuilder.bold(): ComponentBuilder = bold(true)
fun ComponentBuilder.italic(): ComponentBuilder = italic(true)
fun ComponentBuilder.underlined(): ComponentBuilder = underlined(true)
fun ComponentBuilder.strikethrough(): ComponentBuilder = strikethrough(true)
fun ComponentBuilder.obfuscated(): ComponentBuilder = obfuscated(true)
fun ComponentBuilder.magic(): ComponentBuilder = obfuscated(true)