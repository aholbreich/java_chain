package org.holbreich.demo;

import org.holbreich.chain.Blockchain;
import org.holbreich.chain.validation.ChainValidator;

public class Demo1 {
    public static void main(String[] args) {
        Blockchain myBlockchain = new Blockchain();

        // Add new blocks
        myBlockchain.addBlock("Transaction 1: Alice pays Bob 10 BTC");
        myBlockchain.addBlock("Transaction 2: Bob pays Charlie 5 BTC");
        String ta3 = "Transaction 3: Bob pays Kay 15 BTC";
        String ta4 = "Transaction 4: Kay lost 1BTC";
        myBlockchain.addBlock(ta3+ta4+"Transaction 5: Merlin found 5 BTC");

        ChainValidator validator = new ChainValidator(myBlockchain);

        // Validate the blockchain
        System.out.println("Blockchain is " + (validator.isChainValid()? "valid" : "INVALID!")); 
        ChainPrinter.printChain(myBlockchain);
    }
}