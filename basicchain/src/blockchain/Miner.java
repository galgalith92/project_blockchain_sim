package blockchain;
import java.util.Date;
import java.util.Random;
public class Miner {
	
	public String hash; //Miner ID
	private Random r;
	private long timeStamp;
	private double lambda;
	
	Miner(long seed, double difficulty)
	{
		timeStamp =  new Date().getTime();
		r = new Random(seed);
		lambda = 1/difficulty;
		hash = Hash_SHA256.applySha256(Long.toString(timeStamp) + r.nextDouble());
	}
	
	public void mineBlock(int blockNum, String transaction) throws InterruptedException
	{
		System.out.println("Trying to Mine block " + blockNum + "... ");
//		Thread.sleep((long) (1000*getExpRandom())); // EVENT!!! - insertEvent in main sim
//		Block newBlock = new Block(transaction,Blockchain.getLastHash(), r.nextInt());
//		Blockchain.addBlock(newBlock); // EVENT
//		Blockchain.broadcastMiners(this); //EVENT
		System.out.println("Block Mined!!!");
	}
	
	public void changeDifficulty(double difficulty) // EVENT?
	{
		lambda = 1/difficulty;
	}
	
}
