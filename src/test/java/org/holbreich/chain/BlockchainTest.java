package org.holbreich.chain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class BlockchainTest {

    private Blockchain underTest;

    @BeforeEach
    void setUp() {
        underTest = new Blockchain();
    }

    @Test
    void testChainHasSize() {
        assertEquals(1, underTest.getSize());
    }

    @Test
    void testChainSizeIsCorrectAfterBlocksAreAdded() {
        underTest.addBlock("Transaction 1: Alice pays Bob 10 BTC");
        underTest.addBlock("Transaction 1: Alice pays Bob 10 BTC");
        underTest.addBlock("Transaction 1: Alice pays Bob 10 BTC");
        assertEquals(4, underTest.getSize());
    }


    @Test
    void testChainHasHasher() {
        assertNotNull(underTest.getHasher());
    }

    @Test
    void testEveryChainHasGenesisBlock() {
        Block block = underTest.getBlock(0);
        assertNotNull(block);
       
        assertEquals(Blockchain.GENESIS_BLOCK_DATA, block.getData());
        assertEquals(Blockchain.GENESIS_HASH, block.getPreviousHash());
        assertEquals(64, block.getHash().length());
        assertTrue( block.isFinal());
    }

    @Test
    void testNewBlockHashIsChained() {
        Block genBlock = underTest.getBlock(0);
        underTest.addBlock("TEST DATA");
        Block newBlock = underTest.getBlock(1);
        assertTrue(newBlock.getPreviousHash().equals(genBlock.getHash()));
    }

    @Test
    void testNewBlockHashIsGeneratedCorrectly() {
        Block genBlock = underTest.getBlock(0);
        underTest.addBlock("TEST DATA");
        Block newBlock = underTest.getBlock(1);
        String expString = underTest.getHasher().generateHash("TEST DATA"+genBlock.getHash()+ newBlock.getTimestamp());
        assertEquals(expString, newBlock.getHash());
    }

}
