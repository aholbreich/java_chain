package org.holbreich.chain;

import org.holbreich.chain.hashing.IHasher;

/**
 * Block Represents Block of the Data. Not further Specified yet. Block are
 * organized are chained together.
 */
public class Block {

	private String hash;
	private String previousHash;

	public String getPreviousHash() {
		return previousHash;
	}

	private String data;
	private long timestamp;

	public Block(String data, String previousHash, long timestamp) {
		this.data = data;
		this.previousHash = previousHash;
		this.timestamp = timestamp;
	}

	public String getData() {
		return data;
	}

	public long getTimestamp() {
		return timestamp;
	}

	public boolean isFinal() {
		return this.hash != null;
	}

	public void finalizeBlock(IHasher hasher) {
		if (this.hash != null) {
			throw new IllegalStateException("Block is already finalized");
		}
		this.hash = hasher.generateHash(timestamp + data + previousHash); // order need to stay same
	}

	public String getHash() {
		return hash;
	}

}
