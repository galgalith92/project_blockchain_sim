package blockchain;

import java.util.Collection;

public class ProofOfWorkEvent extends Event {
	String blockData;

	ProofOfWorkEvent(double time, String blockData) {
		super(time);
		this.blockData = blockData;
	}

	@Override
	public void processEvent() throws InterruptedException {
		Collection<Miner> minersCollection = Blockchain.getMinersCollection();
		double minMiningTime = Double.MAX_VALUE;
		Miner winnerMiner = null;
		double currMiningTime;
		for (Miner miner : minersCollection) {
			currMiningTime = miner.mineBlock();
			if (currMiningTime < minMiningTime) {
				minMiningTime = currMiningTime;
				winnerMiner = miner;
			}
		}
		Event insertBlockEvent = new InsertBlockEvent(this.blockData, (this.time + minMiningTime), 
													  winnerMiner.getID());
		Blockchain.scheduleEvent(insertBlockEvent);
	}

}
