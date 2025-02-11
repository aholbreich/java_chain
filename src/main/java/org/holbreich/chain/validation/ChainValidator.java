package org.holbreich.chain.validation;

import org.holbreich.chain.Block;
import org.holbreich.chain.Blockchain;

public class ChainValidator {

	private final Blockchain chain;
	
	public ChainValidator(Blockchain chain) {
		this.chain = chain;
	}

	public boolean isChainValide() {
		if (chain.getSize() > 1) {
			Block previousBlock;
			Block currentBlock;
			String calculatedHash;
			for (int i = 1; i < chain.getSize(); i++) {
				previousBlock  = chain.getBlock(i-1);
				currentBlock  = chain.getBlock(i);
				calculatedHash = chain.getHasher().generateHash(currentBlock.getTimestamp()+ currentBlock.getData()+ previousBlock.getHash());
				if(calculatedHash != currentBlock.getHash()) {
					System.out.print("Corrupted BLOCK Nr. "+i+ " ");
					return false;
				}
			}
		}
		return true;
	}
	

}
