package sh.lue.luetech.utils;

import com.mojang.serialization.Codec;
import org.jetbrains.annotations.NotNull;

import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.util.stream.LongStream;

public class BigIntegerUtils {
    @NotNull
    public static BigInteger MAX_LONG = BigInteger.valueOf(Long.MAX_VALUE);
    @NotNull
    public static BigInteger MIN_LONG = BigInteger.valueOf(Long.MIN_VALUE);

    public static Codec<BigInteger> CODEC = Codec.BYTE_BUFFER
            .xmap(b -> new BigInteger(b.array()), i -> ByteBuffer.wrap(i.toByteArray()));

    public static long saturatedValue(@NotNull BigInteger bigInteger) {
        return bigInteger.min(MAX_LONG).max(MIN_LONG).longValue();
    }
}
