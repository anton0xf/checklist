package checklist.util;

import org.junit.jupiter.api.Test;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class RandomHashGeneratorTest {
    private RandomHashGenerator createGen(int seed) {
        return new RandomHashGenerator(new Random(seed));
    }

    @Test
    void genSixteenHexDigits() {
        RandomHashGenerator gen = createGen(0);
        String hash = gen.next(16);
        assertEquals(16, hash.length());
        assertEquals("60b420bb3851d9d4", hash);
    }

    @Test
    void genOtherSixteenHexDigits() {
        assertEquals("73d51abbd89cb819", createGen(1).next(16));
    }

    @Test
    void genOneHundredHexDigits() {
        RandomHashGenerator gen = createGen(0);
        String hash = gen.next(100);
        assertTrue(hash.matches("[0-9a-f]{100}"));
    }
}