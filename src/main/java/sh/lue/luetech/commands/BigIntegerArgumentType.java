package sh.lue.luetech.commands;

import com.google.gson.JsonObject;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.synchronization.ArgumentTypeInfo;
import net.minecraft.network.FriendlyByteBuf;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Collection;
import java.util.Objects;
import java.util.Optional;

public class BigIntegerArgumentType implements ArgumentType<BigInteger> {
    private static final Collection<String> EXAMPLES = Arrays.asList("0", "123", "-123");

    @Nullable
    private final BigInteger minimum;
    @Nullable
    private final BigInteger maximum;

    private BigIntegerArgumentType(@Nullable BigInteger minimum, @Nullable BigInteger maximum) {
        this.minimum = minimum;
        this.maximum = maximum;
    }

    @NotNull
    public static BigIntegerArgumentType bigInteger() {
        return new BigIntegerArgumentType(null, null);
    }

    @NotNull
    public static BigIntegerArgumentType bigInteger(final @Nullable BigInteger minimum,
                                                    final @Nullable BigInteger maximum) {
        return new BigIntegerArgumentType(minimum, maximum);
    }

    @NotNull
    public static BigIntegerArgumentType bigIntegerMin(final @Nullable BigInteger minimum) {
        return new BigIntegerArgumentType(minimum, null);
    }

    @NotNull
    public static BigIntegerArgumentType bigIntegerMax(final @Nullable BigInteger maximum) {
        return new BigIntegerArgumentType(null, maximum);
    }

    @Nullable
    public BigInteger getMinimum() {
        return minimum;
    }

    @Nullable
    public BigInteger getMaximum() {
        return maximum;
    }

    @Override
    @NotNull
    public BigInteger parse(StringReader reader) throws CommandSyntaxException {
        final int start = reader.getCursor();
        final BigInteger result = readBigInteger(reader);
        if (minimum != null && result.compareTo(minimum) < 0) {
            reader.setCursor(start);
            throw CommandSyntaxException.BUILT_IN_EXCEPTIONS.integerTooLow().createWithContext(reader, result, minimum);
        }
        if (maximum != null && result.compareTo(maximum) > 0) {
            reader.setCursor(start);
            throw CommandSyntaxException.BUILT_IN_EXCEPTIONS.integerTooHigh().createWithContext(reader, result, maximum);
        }
        return result;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (!(o instanceof final BigIntegerArgumentType that)) return false;
        return Objects.equals(this.maximum, that.maximum) && Objects.equals(this.minimum, that.minimum);
    }

    @Override
    public int hashCode() {
        return 31 * Objects.hashCode(minimum) + Objects.hashCode(maximum);
    }

    @Override
    @NotNull
    public String toString() {
        if (minimum == null && maximum == null) {
            return "bigInteger()";
        } else if (minimum != null && maximum == null) {
            return "bigIntegerMin(" + minimum + ")";
        } else if (minimum == null) {
            return "bigIntegerMax(" + maximum + ")";
        } else {
            return "bigInteger(" + minimum + ", " + maximum + ")";
        }
    }

    @Override
    @NotNull
    public Collection<String> getExamples() {
        return EXAMPLES;
    }

    private static BigInteger readBigInteger(final StringReader reader) throws CommandSyntaxException {
        final int start = reader.getCursor();
        while (reader.canRead() && StringReader.isAllowedNumber(reader.peek())) {
            reader.skip();
        }
        final String number = reader.getString().substring(start, reader.getCursor());
        if (number.isEmpty()) {
            throw CommandSyntaxException.BUILT_IN_EXCEPTIONS.readerExpectedInt().createWithContext(reader);
        }
        try {
            return new BigInteger(number);
        } catch (final NumberFormatException ex) {
            reader.setCursor(start);
            throw CommandSyntaxException.BUILT_IN_EXCEPTIONS.readerInvalidInt().createWithContext(reader, number);
        }
    }

    public static class Info implements ArgumentTypeInfo<BigIntegerArgumentType, Info.Template> {
        @Override
        public void serializeToNetwork(@NotNull Template template, @NotNull FriendlyByteBuf buffer) {
            buffer.writeOptional(Optional.ofNullable(template.minimum),
                    (b, value) -> b.writeByteArray(value.toByteArray()));
            buffer.writeOptional(Optional.ofNullable(template.maximum),
                    (b, value) -> b.writeByteArray(value.toByteArray()));
        }

        @Override
        @NotNull
        public Template deserializeFromNetwork(@NotNull FriendlyByteBuf buffer) {
            var minimum = buffer.readOptional(b -> b.readByteArray())
                    .map(BigInteger::new)
                    .orElse(null);
            var maximum = buffer.readOptional(b -> b.readByteArray())
                    .map(BigInteger::new)
                    .orElse(null);
            return this.new Template(minimum, maximum);
        }

        @Override
        public void serializeToJson(@NotNull Template template, @NotNull JsonObject jsonObject) {
            if (template.minimum != null) {
                jsonObject.addProperty("minimum", template.minimum.toString());
            }
            if (template.maximum != null) {
                jsonObject.addProperty("maximum", template.maximum.toString());
            }
        }

        @Override
        @NotNull
        public Template unpack(BigIntegerArgumentType type) {
            return this.new Template(type.getMinimum(), type.getMaximum());
        }

        public class Template implements ArgumentTypeInfo.Template<BigIntegerArgumentType> {
            @Nullable
            final BigInteger minimum;
            @Nullable
            final BigInteger maximum;

            private Template(@Nullable BigInteger minimum, @Nullable BigInteger maximum) {
                this.minimum = minimum;
                this.maximum = maximum;
            }

            @Override
            @NotNull
            public BigIntegerArgumentType instantiate(@NotNull CommandBuildContext context) {
                return BigIntegerArgumentType.bigInteger(minimum, maximum);
            }

            @Override
            @NotNull
            public ArgumentTypeInfo<BigIntegerArgumentType, ?> type() {
                return Info.this;
            }
        }
    }
}
