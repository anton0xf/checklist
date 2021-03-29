package checklist.util;

import java.util.Random;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class RandomHashGeneratorTest {
    private RandomHashGenerator createGen(int seed, int hashLen) {
        return new RandomHashGenerator(new Random(seed), hashLen);
    }

    @Test
    void genSixteenHexDigits() {
        RandomHashGenerator gen = createGen(0, 16);
        String hash = gen.next();
        assertEquals(16, hash.length());
        assertEquals("60b420bb3851d9d4", hash);
    }

    @Test
    void genOtherSixteenHexDigits() {
        assertEquals("73d51abbd89cb819", createGen(1, 16).next());
    }

    @Test
    void genOneHundredHexDigits() {
        RandomHashGenerator gen = createGen(0, 100);
        String hash = gen.next();
        assertTrue(hash.matches("[0-9a-f]{100}"));
    }
}