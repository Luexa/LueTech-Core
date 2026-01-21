package sh.lue.luetech.utils;

import com.mojang.serialization.Codec;
import org.jetbrains.annotations.NotNull;

import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.util.stream.LongStream;

public class BigIntegerUtils {
    public static Codec<BigInteger> CODEC = Codec.BYTE_BUFFER
            .xmap(b -> new BigInteger(b.array()), i -> ByteBuffer.wrap(i.toByteArray()));
}
