package blockchain;

import java.util.Random;
import java.util.UUID;

/*
 * Miner is an immutable class which represents a miner in the blockchain
 */
public class Miner {
	
	private String uniqueID; //Miner unique ID
	private Random rand;
	private int machineNumber;
	
	/*
	 * @requires none
	 * @modifies this
	 * @effects Create and initiate a new Miner object
	 */
	public Miner(long seed, int machineNumber)
	{
		this.uniqueID = UUID.randomUUID().toString();
		this.rand = new Random(seed);
		this.machineNumber = machineNumber;
	}
	
	/*
	 * @requires none
	 * @modifies none
	 * @effects Returns a random exponentially distributed mining time
	 */
	public double mineBlock()
	{
		double lambda =  this.machineNumber*1.0/Blockchain.getMineBlockTime();
//        return  (-1 / lambda) * Math.log(1-this.rand.nextDouble()); // random!
		return Blockchain.getExpRandomTime(lambda);
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
	
	public int getMachineNumber()
	{
		return this.machineNumber;
	}
	
	public void changeMachineAmount(int deltaMachines)
	{
		this.machineNumber+= deltaMachines;
	}
}
