package org.holbreich.chain.hashing;

public interface IHasher {

	/**
	 * Generates hash for given value
	 * @param value
	 * @return
	 */
	String generateHash(String value);
}
