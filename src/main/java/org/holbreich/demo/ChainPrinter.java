package org.holbreich.demo;

import org.holbreich.chain.Block;
import org.holbreich.chain.Blockchain;

public class ChainPrinter {

	public static void printChain(Blockchain chain) {

		Block currentBlock;
		for (int i = 0; i < chain.getSize(); i++) {
			currentBlock = chain.getBlock(i);
			System.out.println("---- Block " + i + " ----");
			System.out.println("-Hash: " + currentBlock.getHash() + " Timestamp " + currentBlock.getTimestamp());
			System.out.println(currentBlock.getData());
		}
	}

}
