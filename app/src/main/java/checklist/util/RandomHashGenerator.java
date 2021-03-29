package checklist.util;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Random;

public class RandomHashGenerator {
    public static final int HEX_RADIX = 16;
    public static final int BITS_IN_HEX_DIGIT = 4; // = log_2(16)

    private final Random random;
    private final int hashLen;

    public RandomHashGenerator(int hashLen) {
        this.hashLen = hashLen;
        random = new SecureRandom();
    }

    public RandomHashGenerator(Random random, int hashLen) {
        this.random = random;
        this.hashLen = hashLen;
    }

    public String next() {
        return new BigInteger(hashLen * BITS_IN_HEX_DIGIT, random).toString(HEX_RADIX);
    }
}
