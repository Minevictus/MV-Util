package us.minevict.mvutil.common.text;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.*;
import net.md_5.bungee.api.chat.ComponentBuilder.FormatRetention;
import net.md_5.bungee.api.chat.HoverEvent.Action;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;
import java.util.Optional;

/**
 * A wrapper for {@link ComponentBuilder} to have even easier building of text.
 *
 * @since 0.2.0
 */
@SuppressWarnings("unused")
public class ComponentBuilderV2 {
  /**
   * The inner {@link ComponentBuilder}.
   */
  @NotNull
  private final ComponentBuilder builder;

  /**
   * Create a new {@link ComponentBuilderV2} using the given existing {@link ComponentBuilder}.
   *
   * @param builder The existing {@link ComponentBuilder}.
   */
  public ComponentBuilderV2(@NotNull final ComponentBuilder builder) {
    Objects.requireNonNull(builder, "the inner builder cannot be null!");

    this.builder = builder;
  }

  /**
   * Create a new {@link ComponentBuilderV2} using the given existing {@link ComponentBuilder}.
   *
   * @param builder The existing {@link ComponentBuilder}.
   * @return The constructed {@link ComponentBuilderV2}.
   */
  @NotNull
  public static ComponentBuilderV2 of(@NotNull final ComponentBuilder builder) {
    return new ComponentBuilderV2(builder);
  }

  /**
   * Create a new {@link ComponentBuilderV2} using a given piece of text as the base text.
   *
   * @param base The base text to use for this builder.
   */
  public ComponentBuilderV2(@NotNull final String base) {
    this(new ComponentBuilder(Objects.requireNonNull(base, "the given base cannot be null!")));
  }

  /**
   * Create a new {@link ComponentBuilderV2} using a given piece of text as the base text.
   *
   * @param base The base text to use for this builder.
   * @return The constructed {@link ComponentBuilderV2}.
   */
  @NotNull
  public static ComponentBuilderV2 of(@NotNull final String base) {
    return new ComponentBuilderV2(base);
  }

  /**
   * Create a new {@link ComponentBuilderV2} using the given existing {@link BaseComponent components}.
   *
   * @param components The existing {@link BaseComponent}s.
   */
  public ComponentBuilderV2(@NotNull final BaseComponent... components) {
    this(new ComponentBuilder());
    Objects.requireNonNull(components, "the given components cannot be null!");
  }

  /**
   * Create a new {@link ComponentBuilderV2} using the given existing {@link BaseComponent components}.
   *
   * @param components The existing {@link BaseComponent}s.
   * @return The constructed {@link ComponentBuilderV2}.
   */
  @NotNull
  public static ComponentBuilderV2 of(@NotNull final BaseComponent... components) {
    return new ComponentBuilderV2(components);
  }

  /**
   * Creates an array of {@link BaseComponent} from the options given to this builder.
   *
   * @return The built {@link BaseComponent BaseComponent[]}.
   */
  @NotNull
  public BaseComponent[] build() {
    return builder.create();
  }

  /**
   * Creates an array of {@link BaseComponent} from the options given to this builder.
   *
   * @return The built {@link BaseComponent BaseComponent[]}.
   */
  @NotNull
  public BaseComponent[] create() {
    return builder.create();
  }

  /**
   * Resets the cursor to index of the last element.
   *
   * @return This {@link ComponentBuilderV2} for chaining.
   */
  @NotNull
  public ComponentBuilderV2 resetCursor() {
    builder.resetCursor();
    return this;
  }

  /**
   * Sets the position of the current {@link BaseComponent component} to be modified.
   *
   * @param pos The cursor position synonymous to an element position for a list.
   * @return This {@link ComponentBuilderV2} for chaining.
   * @throws IndexOutOfBoundsException if the index is out of range ({@code index < 0 || index >= size()}).
   */
  @NotNull
  public ComponentBuilderV2 setCursor(int pos) throws IndexOutOfBoundsException {
    builder.setCursor(pos);
    return this;
  }

  /**
   * Appends a {@link BaseComponent component} to the builder and makes it the current target for formatting. The
   * component will have all the formatting from previous part.
   *
   * @param component The {@link BaseComponent component} to append.
   * @return This {@link ComponentBuilderV2} for chaining.
   */
  @NotNull
  public ComponentBuilderV2 append(BaseComponent component) {
    builder.append(component);
    return this;
  }

  /**
   * Appends a {@link BaseComponent component} to the builder and makes it the current target for formatting. You can
   * specify the amount of formatting retained from previous part.
   *
   * @param component The {@link BaseComponent component} to append.
   * @param retention The formatting to retain.
   * @return This {@link ComponentBuilderV2} for chaining.
   */
  @NotNull
  public ComponentBuilderV2 append(BaseComponent component, FormatRetention retention) {
    builder.append(component, retention);
    return this;
  }

  /**
   * Appends the {@link BaseComponent components} to the builder and makes the last element the current target for
   * formatting. The components will have all the formatting from previous part.
   *
   * @param components The {@link BaseComponent components} to append.
   * @return This {@link ComponentBuilderV2} for chaining.
   */
  @NotNull
  public ComponentBuilderV2 append(BaseComponent[] components) {
    builder.append(components);
    return this;
  }

  /**
   * Appends the {@link BaseComponent components} to the builder and makes the last element the current target for
   * formatting. You can specify the amount of formatting retained from previous part.
   *
   * @param components The {@link BaseComponent components} to append.
   * @param retention  The formatting to retain.
   * @return This {@link ComponentBuilderV2} for chaining.
   */
  @NotNull
  public ComponentBuilderV2 append(BaseComponent[] components, FormatRetention retention) {
    builder.append(components, retention);
    return this;
  }

  /**
   * Appends the text to the builder and makes it the current target for formatting. The text will have all the
   * formatting from previous part.
   *
   * @param text The text to append.
   * @return This {@link ComponentBuilderV2} for chaining.
   */
  @NotNull
  public ComponentBuilderV2 append(@NotNull String text) {
    Objects.requireNonNull(text, "the provided text cannot be null!");

    builder.append(text);
    return this;
  }

  /**
   * Appends a {@link String#valueOf(Object)} of the object to the builder and makes it the current target for
   * formatting. The text will have all the formatting from previous part.
   *
   * @param object The object to append.
   * @return This {@link ComponentBuilderV2} for chaining.
   */
  @NotNull
  public ComponentBuilderV2 append(@Nullable Object object) {
    builder.append(String.valueOf(object));
    return this;
  }

  /**
   * Parse text to {@link BaseComponent BaseComponent[]} with colours and format, appends the text to the builder and
   * makes it the current target for formatting. The component will have all the formatting from previous part.
   *
   * @param text The text to append.
   * @return This {@link ComponentBuilderV2} for chaining.
   */
  @NotNull
  public ComponentBuilderV2 appendLegacy(String text) {
    builder.appendLegacy(text);
    return this;
  }

  /**
   * Appends the text to the builder and makes it the current target for formatting. You can specify the amount of
   * formatting retained from previous part.
   *
   * @param text      The text to append.
   * @param retention The formatting to retain.
   * @return This {@link ComponentBuilderV2} for chaining.
   */
  @NotNull
  public ComponentBuilderV2 append(@NotNull String text, @NotNull FormatRetention retention) {
    Objects.requireNonNull(text, "the provided text cannot be null!");
    Objects.requireNonNull(retention, "the provided retention cannot be null!");

    builder.append(text, retention);
    return this;
  }

  /**
   * Appends a {@link String#valueOf(Object)} of the object to the builder and makes it the current target for
   * formatting. You can specify the amount of formatting retained from previous part.
   *
   * @param object    The object to append.
   * @param retention The formatting to retain.
   * @return This {@link ComponentBuilderV2} for chaining.
   */
  @NotNull
  public ComponentBuilderV2 append(@Nullable Object object, @NotNull FormatRetention retention) {
    Objects.requireNonNull(retention, "the provided retention cannot be null!");

    builder.append(String.valueOf(object));
    return this;
  }

  /**
   * Allows joining additional components to this builder using the given {@link JoinerV2} and {@link
   * FormatRetention#ALL}.
   * <p>
   * Simply executes the provided joiner on this instance to facilitate a chain pattern.
   *
   * @param joiner {@link JoinerV2} used for operation.
   * @return This {@link ComponentBuilderV2} for chaining.
   */
  @NotNull
  public ComponentBuilderV2 append(JoinerV2 joiner) {
    return joiner.join(this, FormatRetention.ALL);
  }

  /**
   * Allows joining additional components to this builder using the given {@link JoinerV2}.
   * <p>
   * Simply executes the provided joiner on this instance to facilitate a chain pattern.
   *
   * @param joiner    {@link JoinerV2} used for operation.
   * @param retention The formatting to retain.
   * @return This {@link ComponentBuilderV2} for chaining.
   */
  @NotNull
  public ComponentBuilderV2 append(JoinerV2 joiner, FormatRetention retention) {
    return joiner.join(this, retention);
  }

  /**
   * Remove the component part at the position of given index.
   *
   * @param pos The index to remove at.
   * @return Whether the removal was successful.
   */
  public boolean removeComponent(int pos) {
    if (pos < 0 || pos >= builder.getParts().size()) {
      return false;
    }
    try {
      builder.removeComponent(pos);
      return true;
    } catch (IndexOutOfBoundsException ignored) {
      return false;
    }
  }

  /**
   * Gets the {@link BaseComponent component} at the position of given index.
   *
   * @param pos The index to find.
   * @return The found {@link BaseComponent} wrapped in an {@link Optional}.
   */
  @NotNull
  public Optional<BaseComponent> getComponent(int pos) {
    if (pos < 0 || pos >= builder.getParts().size()) {
      return Optional.empty();
    }

    try {
      return Optional.of(builder.getComponent(pos));
    } catch (IndexOutOfBoundsException ignored) {
      return Optional.empty();
    }
  }

  /**
   * Gets the {@link BaseComponent component} at the position of the cursor.
   *
   * @return The active {@link BaseComponent component} or an empty {@link Optional} if the builder is empty.
   */
  @NotNull
  public Optional<BaseComponent> getCurrentComponent() {
    return Optional.ofNullable(builder.getCurrentComponent());
  }

  /**
   * Sets the colour of the current part.
   *
   * @param colour The new colour.
   * @return This {@link ComponentBuilderV2} for chaining.
   */
  @NotNull
  public ComponentBuilderV2 color(ChatColor colour) {
    builder.color(colour);
    return this;
  }

  /**
   * Sets the colour of the current part.
   *
   * @param colour The new colour.
   * @return This {@link ComponentBuilderV2} for chaining.
   */
  @NotNull
  public ComponentBuilderV2 colour(ChatColor colour) {
    builder.color(colour);
    return this;
  }

  /**
   * Sets the colour of the current part to be black.
   *
   * @return This {@link ComponentBuilderV2} for chaining.
   */
  @NotNull
  public ComponentBuilderV2 black() {
    return colour(ChatColor.BLACK);
  }

  /**
   * Sets the colour of the object provided to be black.
   *
   * @param object Object to add to the builder before setting the colour.
   * @return This {@link ComponentBuilderV2} for chaining.
   */
  @NotNull
  public ComponentBuilderV2 black(@Nullable Object object) {
    return append(object).black();
  }

  /**
   * Sets the colour of the current part to be dark blue.
   *
   * @return This {@link ComponentBuilderV2} for chaining.
   */
  @NotNull
  public ComponentBuilderV2 darkBlue() {
    return colour(ChatColor.DARK_BLUE);
  }

  /**
   * Sets the colour of the object provided to be dark blue.
   *
   * @param object Object to add to the builder before setting the colour.
   * @return This {@link ComponentBuilderV2} for chaining.
   */
  @NotNull
  public ComponentBuilderV2 darkBlue(@Nullable Object object) {
    return append(object).darkBlue();
  }

  /**
   * Sets the colour of the current part to be dark green.
   *
   * @return This {@link ComponentBuilderV2} for chaining.
   */
  @NotNull
  public ComponentBuilderV2 darkGreen() {
    return colour(ChatColor.DARK_GREEN);
  }

  /**
   * Sets the colour of the object provided to be dark green.
   *
   * @param object Object to add to the builder before setting the colour.
   * @return This {@link ComponentBuilderV2} for chaining.
   */
  @NotNull
  public ComponentBuilderV2 darkGreen(@Nullable Object object) {
    return append(object).darkGreen();
  }

  /**
   * Sets the colour of the current part to be dark aqua.
   *
   * @return This {@link ComponentBuilderV2} for chaining.
   */
  @NotNull
  public ComponentBuilderV2 darkAqua() {
    return colour(ChatColor.DARK_AQUA);
  }

  /**
   * Sets the colour of the object provided to be dark aqua.
   *
   * @param object Object to add to the builder before setting the colour.
   * @return This {@link ComponentBuilderV2} for chaining.
   */
  @NotNull
  public ComponentBuilderV2 darkAqua(@Nullable Object object) {
    return append(object).darkAqua();
  }

  /**
   * Sets the colour of the current part to be dark red.
   *
   * @return This {@link ComponentBuilderV2} for chaining.
   */
  @NotNull
  public ComponentBuilderV2 darkRed() {
    return colour(ChatColor.DARK_RED);
  }

  /**
   * Sets the colour of the object provided to be dark red.
   *
   * @param object Object to add to the builder before setting the colour.
   * @return This {@link ComponentBuilderV2} for chaining.
   */
  @NotNull
  public ComponentBuilderV2 darkRed(@Nullable Object object) {
    return append(object).darkRed();
  }

  /**
   * Sets the colour of the current part to be purple.
   *
   * @return This {@link ComponentBuilderV2} for chaining.
   */
  @NotNull
  public ComponentBuilderV2 purple() {
    return colour(ChatColor.DARK_PURPLE);
  }

  /**
   * Sets the colour of the object provided to be purple.
   *
   * @param object Object to add to the builder before setting the colour.
   * @return This {@link ComponentBuilderV2} for chaining.
   */
  @NotNull
  public ComponentBuilderV2 purple(@Nullable Object object) {
    return append(object).purple();
  }

  /**
   * Sets the colour of the current part to be purple.
   *
   * @return This {@link ComponentBuilderV2} for chaining.
   * @deprecated Use {@link #purple()} instead.
   */
  @NotNull
  @Deprecated
  public ComponentBuilderV2 darkPurple() {
    return colour(ChatColor.DARK_PURPLE);
  }

  /**
   * Sets the colour of the object provided to be purple.
   *
   * @param object Object to add to the builder before setting the colour.
   * @return This {@link ComponentBuilderV2} for chaining.
   * @deprecated Use {@link #purple(Object)} instead.
   */
  @NotNull
  @Deprecated
  public ComponentBuilderV2 darkPurple(@Nullable Object object) {
    return append(object).purple();
  }

  /**
   * Sets the colour of the current part to be gold.
   *
   * @return This {@link ComponentBuilderV2} for chaining.
   */
  @NotNull
  public ComponentBuilderV2 gold() {
    return colour(ChatColor.GOLD);
  }

  /**
   * Sets the colour of the object provided to be gold.
   *
   * @param object Object to add to the builder before setting the colour.
   * @return This {@link ComponentBuilderV2} for chaining.
   */
  @NotNull
  public ComponentBuilderV2 gold(@Nullable Object object) {
    return append(object).gold();
  }

  /**
   * Sets the colour of the current part to be grey.
   *
   * @return This {@link ComponentBuilderV2} for chaining.
   */
  @NotNull
  public ComponentBuilderV2 grey() {
    return colour(ChatColor.GRAY);
  }

  /**
   * Sets the colour of the object provided to be grey.
   *
   * @param object Object to add to the builder before setting the colour.
   * @return This {@link ComponentBuilderV2} for chaining.
   */
  @NotNull
  public ComponentBuilderV2 grey(@Nullable Object object) {
    return append(object).grey();
  }

  /**
   * Sets the colour of the current part to be grey.
   *
   * @return This {@link ComponentBuilderV2} for chaining.
   */
  @NotNull
  public ComponentBuilderV2 gray() {
    return colour(ChatColor.GRAY);
  }

  /**
   * Sets the colour of the object provided to be gray.
   *
   * @param object Object to add to the builder before setting the colour.
   * @return This {@link ComponentBuilderV2} for chaining.
   */
  @NotNull
  public ComponentBuilderV2 gray(@Nullable Object object) {
    return append(object).gray();
  }

  /**
   * Sets the colour of the current part to be dark grey.
   *
   * @return This {@link ComponentBuilderV2} for chaining.
   */
  @NotNull
  public ComponentBuilderV2 darkGrey() {
    return colour(ChatColor.DARK_GRAY);
  }

  /**
   * Sets the colour of the object provided to be dark grey.
   *
   * @param object Object to add to the builder before setting the colour.
   * @return This {@link ComponentBuilderV2} for chaining.
   */
  @NotNull
  public ComponentBuilderV2 darkGrey(@Nullable Object object) {
    return append(object).darkGrey();
  }

  /**
   * Sets the colour of the current part to be dark grey.
   *
   * @return This {@link ComponentBuilderV2} for chaining.
   */
  @NotNull
  public ComponentBuilderV2 darkGray() {
    return colour(ChatColor.DARK_GRAY);
  }

  /**
   * Sets the colour of the object provided to be dark gray.
   *
   * @param object Object to add to the builder before setting the colour.
   * @return This {@link ComponentBuilderV2} for chaining.
   */
  @NotNull
  public ComponentBuilderV2 darkGray(@Nullable Object object) {
    return append(object).darkGray();
  }

  /**
   * Sets the colour of the current part to be blue.
   *
   * @return This {@link ComponentBuilderV2} for chaining.
   */
  @NotNull
  public ComponentBuilderV2 blue() {
    return colour(ChatColor.BLUE);
  }

  /**
   * Sets the colour of the object provided to be blue.
   *
   * @param object Object to add to the builder before setting the colour.
   * @return This {@link ComponentBuilderV2} for chaining.
   */
  @NotNull
  public ComponentBuilderV2 blue(@Nullable Object object) {
    return append(object).blue();
  }

  /**
   * Sets the colour of the current part to be green.
   *
   * @return This {@link ComponentBuilderV2} for chaining.
   */
  @NotNull
  public ComponentBuilderV2 green() {
    return colour(ChatColor.GREEN);
  }

  /**
   * Sets the colour of the object provided to be green.
   *
   * @param object Object to add to the builder before setting the colour.
   * @return This {@link ComponentBuilderV2} for chaining.
   */
  @NotNull
  public ComponentBuilderV2 green(@Nullable Object object) {
    return append(object).green();
  }

  /**
   * Sets the colour of the current part to be aqua.
   *
   * @return This {@link ComponentBuilderV2} for chaining.
   */
  @NotNull
  public ComponentBuilderV2 aqua() {
    return colour(ChatColor.AQUA);
  }

  /**
   * Sets the colour of the object provided to be aqua.
   *
   * @param object Object to add to the builder before setting the colour.
   * @return This {@link ComponentBuilderV2} for chaining.
   */
  @NotNull
  public ComponentBuilderV2 aqua(@Nullable Object object) {
    return append(object).aqua();
  }

  /**
   * Sets the colour of the current part to be red.
   *
   * @return This {@link ComponentBuilderV2} for chaining.
   */
  @NotNull
  public ComponentBuilderV2 red() {
    return colour(ChatColor.RED);
  }

  /**
   * Sets the colour of the object provided to be red.
   *
   * @param object Object to add to the builder before setting the colour.
   * @return This {@link ComponentBuilderV2} for chaining.
   */
  @NotNull
  public ComponentBuilderV2 red(@Nullable Object object) {
    return append(object).red();
  }

  /**
   * Sets the colour of the current part to be pink.
   *
   * @return This {@link ComponentBuilderV2} for chaining.
   */
  @NotNull
  public ComponentBuilderV2 pink() {
    return colour(ChatColor.LIGHT_PURPLE);
  }

  /**
   * Sets the colour of the object provided to be pink.
   *
   * @param object Object to add to the builder before setting the colour.
   * @return This {@link ComponentBuilderV2} for chaining.
   */
  @NotNull
  public ComponentBuilderV2 pink(@Nullable Object object) {
    return append(object).pink();
  }

  /**
   * Sets the colour of the current part to be pink.
   *
   * @return This {@link ComponentBuilderV2} for chaining.
   * @deprecated Use {@link #pink} instead.
   */
  @NotNull
  @Deprecated
  public ComponentBuilderV2 lightPurple() {
    return colour(ChatColor.LIGHT_PURPLE);
  }

  /**
   * Sets the colour of the object provided to be pink.
   *
   * @param object Object to add to the builder before setting the colour.
   * @return This {@link ComponentBuilderV2} for chaining.
   * @deprecated Use {@link #pink(Object)} instead.
   */
  @NotNull
  @Deprecated
  public ComponentBuilderV2 lightPurple(@Nullable Object object) {
    return append(object).pink();
  }

  /**
   * Sets the colour of the current part to be yellow.
   *
   * @return This {@link ComponentBuilderV2} for chaining.
   */
  @NotNull
  public ComponentBuilderV2 yellow() {
    return colour(ChatColor.YELLOW);
  }

  /**
   * Sets the colour of the object provided to be yellow.
   *
   * @param object Object to add to the builder before setting the colour.
   * @return This {@link ComponentBuilderV2} for chaining.
   */
  @NotNull
  public ComponentBuilderV2 yellow(@Nullable Object object) {
    return append(object).yellow();
  }

  /**
   * Sets the colour of the current part to be white.
   *
   * @return This {@link ComponentBuilderV2} for chaining.
   */
  @NotNull
  public ComponentBuilderV2 white() {
    return colour(ChatColor.WHITE);
  }

  /**
   * Sets the colour of the object provided to be white.
   *
   * @param object Object to add to the builder before setting the colour.
   * @return This {@link ComponentBuilderV2} for chaining.
   */
  @NotNull
  public ComponentBuilderV2 white(@Nullable Object object) {
    return append(object).white();
  }

  /**
   * Sets whether the current part is bold.
   *
   * @param bold Whether this part is bold.
   * @return This {@link ComponentBuilderV2} for chaining.
   */
  @NotNull
  public ComponentBuilderV2 bold(boolean bold) {
    builder.bold(bold);
    return this;
  }

  /**
   * Sets the current part to be bold.
   *
   * @return This {@link ComponentBuilderV2} for chaining.
   */
  @NotNull
  public ComponentBuilderV2 bold() {
    return bold(true);
  }

  /**
   * Sets whether the current part is italic.
   *
   * @param italic Whether this part is italic.
   * @return This {@link ComponentBuilderV2} for chaining.
   */
  @NotNull
  public ComponentBuilderV2 italic(boolean italic) {
    builder.italic(italic);
    return this;
  }

  /**
   * Sets the current part to be italic.
   *
   * @return This {@link ComponentBuilderV2} for chaining.
   */
  @NotNull
  public ComponentBuilderV2 italic() {
    return italic(true);
  }

  /**
   * Sets whether the current part is underlined.
   *
   * @param underlined Whether this part is underlined.
   * @return This {@link ComponentBuilderV2} for chaining.
   */
  @NotNull
  public ComponentBuilderV2 underlined(boolean underlined) {
    builder.underlined(underlined);
    return this;
  }

  /**
   * Sets the current part to be underlined.
   *
   * @return This {@link ComponentBuilderV2} for chaining.
   */
  @NotNull
  public ComponentBuilderV2 underlined() {
    return underlined(true);
  }

  /**
   * Sets whether the current part is struck through.
   *
   * @param strikethrough Whether this part is struck through.
   * @return This {@link ComponentBuilderV2} for chaining.
   */
  @SuppressWarnings("SpellCheckingInspection")
  @NotNull
  public ComponentBuilderV2 strikethrough(boolean strikethrough) {
    builder.strikethrough(strikethrough);
    return this;
  }

  /**
   * Sets the current part to be struck through.
   *
   * @return This {@link ComponentBuilderV2} for chaining.
   */
  @SuppressWarnings("SpellCheckingInspection")
  @NotNull
  public ComponentBuilderV2 strikethrough() {
    return strikethrough(true);
  }

  /**
   * Sets whether the current part is obfuscated.
   *
   * @param obfuscated Whether this part is obfuscated.
   * @return This {@link ComponentBuilderV2} for chaining.
   */
  @NotNull
  public ComponentBuilderV2 obfuscated(boolean obfuscated) {
    builder.obfuscated(obfuscated);
    return this;
  }

  /**
   * Sets the current part to be obfuscated.
   *
   * @return This {@link ComponentBuilderV2} for chaining.
   */
  @NotNull
  public ComponentBuilderV2 obfuscated() {
    return obfuscated(true);
  }

  /**
   * Sets whether the current part is obfuscated.
   *
   * @param obfuscated Whether this part is obfuscated.
   * @return This {@link ComponentBuilderV2} for chaining.
   */
  @NotNull
  public ComponentBuilderV2 magic(boolean obfuscated) {
    builder.obfuscated(obfuscated);
    return this;
  }

  /**
   * Sets the current part to be obfuscated.
   *
   * @return This {@link ComponentBuilderV2} for chaining.
   */
  @NotNull
  public ComponentBuilderV2 magic() {
    return obfuscated(true);
  }

  /**
   * Sets the insertion text for the current part.
   *
   * @param insertion The insertion text.
   * @return This {@link ComponentBuilderV2} for chaining.
   */
  @NotNull
  public ComponentBuilderV2 insertion(@Nullable String insertion) {
    builder.insertion(insertion);
    return this;
  }

  /**
   * Sets the {@link ClickEvent} for the current part.
   *
   * @param clickEvent The {@link ClickEvent}.
   * @return This {@link ComponentBuilderV2} for chaining.
   */
  @NotNull
  public ComponentBuilderV2 event(ClickEvent clickEvent) {
    builder.event(clickEvent);
    return this;
  }

  /**
   * Sets the {@link ClickEvent} for the current part.
   *
   * @param clickEvent The {@link ClickEvent}.
   * @return This {@link ComponentBuilderV2} for chaining.
   */
  @NotNull
  public ComponentBuilderV2 click(ClickEvent clickEvent) {
    return event(clickEvent);
  }

  /**
   * Sets the {@link ClickEvent} for the current part.
   *
   * @param action The {@link ClickEvent.Action Action} for this {@link ClickEvent}.
   * @param value  The value for this {@link ClickEvent}.
   * @return This {@link ComponentBuilderV2} for chaining.
   */
  @NotNull
  public ComponentBuilderV2 click(@NotNull ClickEvent.Action action, @NotNull String value) {
    return event(new ClickEvent(action, value));
  }

  /**
   * Sets the {@link HoverEvent} for the current part.
   *
   * @param hoverEvent The {@link HoverEvent}.
   * @return This {@link ComponentBuilderV2} for chaining.
   */
  @NotNull
  public ComponentBuilderV2 event(HoverEvent hoverEvent) {
    builder.event(hoverEvent);
    return this;
  }

  /**
   * Sets the {@link HoverEvent} for the current part.
   *
   * @param hoverEvent The {@link HoverEvent}.
   * @return This {@link ComponentBuilderV2} for chaining.
   */
  @NotNull
  public ComponentBuilderV2 hover(HoverEvent hoverEvent) {
    return event(hoverEvent);
  }

  /**
   * Sets the {@link HoverEvent} for the current part.
   *
   * @param action The {@link HoverEvent.Action Action} for this {@link HoverEvent}.
   * @param value  The {@link BaseComponent BaseComponent[]} value for this {@link HoverEvent}.
   * @return This {@link ComponentBuilderV2} for chaining.
   */
  @NotNull
  public ComponentBuilderV2 hover(@NotNull HoverEvent.Action action, @NotNull BaseComponent[] value) {
    return event(new HoverEvent(action, value));
  }

  /**
   * Sets the {@link HoverEvent} for the current part.
   *
   * @param action The {@link HoverEvent.Action Action} for this {@link HoverEvent}.
   * @param value  The value for this {@link HoverEvent}; this will just be a {@link TextComponent} of the value.
   * @return This {@link ComponentBuilderV2} for chaining.
   */
  @NotNull
  public ComponentBuilderV2 hover(@NotNull HoverEvent.Action action, @NotNull String value) {
    return event(new HoverEvent(action, new BaseComponent[]{new TextComponent(value)}));
  }

  /**
   * Sets the {@link HoverEvent} for the current part.
   *
   * @param value The value for this {@link HoverEvent}; this will just be a {@link TextComponent} of the value.
   * @return This {@link ComponentBuilderV2} for chaining.
   */
  @NotNull
  public ComponentBuilderV2 hoverText(@NotNull String value) {
    return event(new HoverEvent(Action.SHOW_TEXT, new BaseComponent[]{new TextComponent(value)}));
  }

  /**
   * Sets the {@link HoverEvent} for the current part.
   *
   * @param value The {@link BaseComponent BaseComponent[]} value for this {@link HoverEvent}.
   * @return This {@link ComponentBuilderV2} for chaining.
   */
  @NotNull
  public ComponentBuilderV2 hoverText(@NotNull BaseComponent[] value) {
    return event(new HoverEvent(Action.SHOW_TEXT, value));
  }

  /**
   * Sets the current part back to normal settings. Only text is kept.
   *
   * @return This {@link ComponentBuilderV2} for chaining.
   */
  @NotNull
  public ComponentBuilderV2 reset() {
    builder.reset();
    return this;
  }

  /**
   * Retains only the specified formatting. Text is not modified.
   *
   * @param retention the formatting to retain
   * @return This {@link ComponentBuilderV2} for chaining.
   */
  @NotNull
  public ComponentBuilderV2 retain(FormatRetention retention) {
    builder.retain(retention);
    return this;
  }

  /**
   * Appends a floating point number with the specified amount of decimals.
   * <p>
   * This rounds up at any equidistant numbers, i.e. {@code 5.55 => 5.6}, {@code 5.54 => 5.5.}
   *
   * @param number   The number to round.
   * @param decimals The amount of decimals to include.
   * @return This {@link ComponentBuilderV2} for chaining.
   */
  @NotNull
  public ComponentBuilderV2 decimal(double number, int decimals) {
    return decimal(number, decimals, RoundingMode.HALF_UP);
  }

  /**
   * Appends a floating point number with the specified amount of decimals.
   * <p>
   * This rounds up at any equidistant numbers, i.e. {@code 5.55 => 5.6}, {@code 5.54 => 5.5.}
   *
   * @param number   The number to round.
   * @param decimals The amount of decimals to include.
   * @return This {@link ComponentBuilderV2} for chaining.
   */
  @NotNull
  public ComponentBuilderV2 decimal(float number, int decimals) {
    return decimal(number, decimals, RoundingMode.HALF_UP);
  }

  /**
   * Appends a floating point number with the specified amount of decimals.
   *
   * @param number       The number to round.
   * @param decimals     The amount of decimals to include.
   * @param roundingMode How the decimal should be rounded.
   * @return This {@link ComponentBuilderV2} for chaining.
   */
  @NotNull
  public ComponentBuilderV2 decimal(double number, int decimals, @NotNull RoundingMode roundingMode) {
    Objects.requireNonNull(roundingMode, "the rounding mode cannot be null");

    builder.append(BigDecimal.valueOf(number).setScale(decimals, roundingMode).toString());
    return this;
  }

  /**
   * Appends a floating point number with the specified amount of decimals.
   *
   * @param number       The number to round.
   * @param decimals     The amount of decimals to include.
   * @param roundingMode How the decimal should be rounded.
   * @return This {@link ComponentBuilderV2} for chaining.
   */
  @NotNull
  public ComponentBuilderV2 decimal(float number, int decimals, @NotNull RoundingMode roundingMode) {
    Objects.requireNonNull(roundingMode, "the rounding mode cannot be null");

    builder.append(BigDecimal.valueOf(number).setScale(decimals, roundingMode).toString());
    return this;
  }

  /**
   * Functional interface to join additional components to a {@link ComponentBuilderV2}.
   */
  @FunctionalInterface
  public interface JoinerV2 {
    /**
     * Joins additional components to the provided {@link ComponentBuilderV2} and then returns it to fulfill a chain
     * pattern.
     * <p>
     * Retention may be ignored and is to be understood as an optional recommendation to the {@link JoinerV2} and not as
     * a guarantee to have a previous component in builder unmodified.
     *
     * @param componentBuilder The {@link ComponentBuilderV2} to append additional components to.
     * @param retention        The formatting to possibly retain.
     * @return Input {@link ComponentBuilderV2} for chaining.
     */
    @NotNull
    ComponentBuilderV2 join(@NotNull ComponentBuilderV2 componentBuilder, @Nullable FormatRetention retention);
  }
}
