package org.holbreich.chain;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import org.holbreich.chain.hashing.DefaultHasher;
import org.holbreich.chain.hashing.IHasher;

public class Blockchain {
	
	public static final String GENESIS_BLOCK_DATA = "Genesis Block";
	public static final String GENESIS_HASH = "_";
	private List<Block> chain;
	private final IHasher hasher;

	public Blockchain() {
		chain = new ArrayList<>();
		hasher = new DefaultHasher();
		addFinalized(new Block(GENESIS_BLOCK_DATA, GENESIS_HASH, Instant.now().getEpochSecond()));
	}

	/**
	 * Adds block to a Blockchain
	 * @param data - not further specified String
	 */
	public void addBlock(String data) {
		Block previousBlock = chain.get(chain.size() - 1);
		Block newBlock = new Block(data, previousBlock.getHash(), Instant.now().getEpochSecond());
		addFinalized(newBlock);
	}

	private void addFinalized(Block newBlock) {
		newBlock.finalizeBlock(hasher);
		chain.add(newBlock);
	}
	
	public Block getBlock(int index) {
		return chain.get(index);
	}

	public int getSize() {
		return chain.size();
	}
	
	public IHasher getHasher() {
		return this.hasher;
	}
	

}
