package blockchain;
import java.util.Collection;

/*
 * ProofOfWorkEvent is a class which represent the situation of miners 
 * who compete to add the next block in the blockchain by trying to 
 * solve a cryptographic puzzle. the first to solve the puzzle wins.
 */
public class ProofOfWorkEvent extends Event {
	String blockData;

	/*
	 * @requires none
	 * @modifies this
	 * @effects Creates and initializes a new ProofOfWorkEvent object
	 */
	ProofOfWorkEvent(double time, String blockData) {
		super(time);
		this.blockData = blockData;
	}

	/*
	 * @requires none
	 * @modifies Blockchain
	 * @effects Iterate over the miners in the blockchain - the miner 
	 * 			who solve the puzzle first insert the next block to the
	 * 			blockchain 
	 */
	@Override
	public void processEvent() throws InterruptedException {
		Collection<Miner> minersCollection = Blockchain.getMinersCollection();
		double minMiningTime = Double.MAX_VALUE;
		Miner winnerMiner = null;
		double currMiningTime;
		// Iterate over the miners and get the mining time from each one of them
		for (Miner miner : minersCollection) {
			currMiningTime = miner.mineBlock();
			if (currMiningTime < minMiningTime) {
				minMiningTime = currMiningTime;
				winnerMiner = miner;
			}
		}
		// The miner who solve the puzzle first - insert the next block to the blockchain
		Event insertBlockEvent = new InsertBlockEvent(this.blockData, (this.time + minMiningTime), 												  winnerMiner.getID());
		Blockchain.scheduleEvent(insertBlockEvent);
	}

}
