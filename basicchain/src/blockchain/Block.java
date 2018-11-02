package blockchain;

import java.util.Date;

public class Block {
	
	public String hash;
	public String previousHash; 
	private String data;
	private long timeStamp;
	private int challengeSol;
	
	// Block Constructor
	public Block(String data,String previousHash ,int challengeSol) {
		this.data = data;
		this.previousHash = previousHash;
		this.timeStamp = new Date().getTime();
		this.hash = calculateHash(); 
		this.challengeSol = challengeSol;
	}
	
	//Calculate new hash based on blocks contents
	public String calculateHash() {
		String calculatedhash = Hash_SHA256.applySha256( 
				previousHash +
				Long.toString(timeStamp) +
				Integer.toString(challengeSol) + 
				data 
				);
		return calculatedhash;
	}
	
}