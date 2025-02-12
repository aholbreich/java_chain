package org.holbreich.chain.validation;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.holbreich.chain.Blockchain;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ChainValidatorTest {

    private Blockchain testChain;
    private ChainValidator underTest;

    @BeforeEach
    void setUp() {
        testChain = new Blockchain();
        underTest = new ChainValidator(testChain);
    }

    @Test
    void testGenesisChainIsAlwaysValid() {
        assertTrue(underTest.isChainValid());
    }

    @Test
    void testCorrectBlockDetected() {
        testChain.addBlock("Transaction 1: Alice pays Bob 10 BTC");
        assertTrue(underTest.isChainValid());
    }

}
