package org.holbreich.chain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.Instant;

import org.holbreich.chain.hashing.DefaultHasher;
import org.holbreich.chain.hashing.IHasher;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


class BlockTest {

    private Block block;
    private String testData = "Test Data";
    private String previousHash = "PreviousHash123";
    private int timestamp = 12345;

    @BeforeEach
    void setUp() {
        block = new Block(testData, previousHash, timestamp);
    }

    @Test
    void testBlockInitialization() {
        assertEquals(testData, block.getData());
        assertEquals(previousHash, block.getPreviousHash());
        assertTrue(block.getTimestamp() <= Instant.now().getEpochSecond());
        assertNull(block.getHash());
        assertFalse(block.isFinal());
    }

    @Test
    void testFinalizeBlock() {

        block.finalizeBlock(new DefaultHasher());

        assertEquals("4045323aa5a3184a022a26bc1f695aef053cf01e0c92fb2afbfe670725c2b8b5", block.getHash());
        assertTrue(block.isFinal());
    }

    @Test
    void testFinalizeBlockTwiceThrowsException() {

    	IHasher hasher = new DefaultHasher();
        block.finalizeBlock(hasher);

        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
            block.finalizeBlock(hasher);
        });

        assertEquals("Block is already finalized", exception.getMessage());
    }
}
