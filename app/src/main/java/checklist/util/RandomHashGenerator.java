package checklist.util;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Random;

public class RandomHashGenerator {
    public static final int HEX_RADIX = 16;
    public static final int BITS_IN_HEX_DIGIT = 4; // = log_2(16)

    private final Random random;

    public RandomHashGenerator() {
        random = new SecureRandom();
    }

    public RandomHashGenerator(Random random) {
        this.random = random;
    }

    public String next(int len) {
        return new BigInteger(len * BITS_IN_HEX_DIGIT, random).toString(HEX_RADIX);
    }
}
