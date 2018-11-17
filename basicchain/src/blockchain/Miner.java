package blockchain;

import java.util.Random;
import java.util.UUID;

/*
 * Miner is an immutable class which represents a miner in the blockchain
 */
public class Miner {
	
	private String uniqueID; //Miner unique ID
	private Random rand;
	
	/*
	 * @requires none
	 * @modifies this
	 * @effects Create and initiate a new Miner object
	 */
	public Miner(long seed)
	{
		this.uniqueID = UUID.randomUUID().toString();
		this.rand = new Random(seed);
	}
	
	/*
	 * @requires none
	 * @modifies none
	 * @effects Returns a random exponentially distributed mining time
	 */
	public double mineBlock()
	{
		double difficulty = Blockchain.getDifficulty();
        return -(Math.log(this.rand.nextDouble()) / difficulty);
	}
	
	/*
	 * @requires none
	 * @modifies none
	 * @effects Returns the ID of this Miner object
	 */
	public String getID()
	{
		return this.uniqueID;
	}
	
}
