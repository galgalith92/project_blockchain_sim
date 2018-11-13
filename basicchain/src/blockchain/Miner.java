package blockchain;

import java.util.Random;
import java.util.UUID;
public class Miner {
	
	private String uniqueID; //Miner unique ID
	private Random r;
	
	Miner(long seed)
	{
		this.uniqueID = UUID.randomUUID().toString();
		r = new Random(seed);
	}
	
	public double mineBlock()
	{
		return this.getExpRandom();
	}
	
	public String getID()
	{
		return this.uniqueID;
	}
	
	private double getExpRandom() 
	{ 
		double difficulty = Blockchain.getDifficulty();
        return -(Math.log(r.nextDouble()) / difficulty); 
    } 
	
}
