package blockchain;

public class ProofOfWorkEvent extends Event {
	String blockData;

	ProofOfWorkEvent(long time, String blockData) {
		super(time);
		this.blockData = blockData;
	}

	@Override
	public void processEvent() throws InterruptedException {
		double minMiningTime = Double.MAX_VALUE;
		Miner winnerMiner = null;
		double currMiningTime;
		for (Miner miner : Blockchain.minersArray) {
			currMiningTime = miner.mineBlock();
			if (currMiningTime < minMiningTime) {
				minMiningTime = currMiningTime;
				winnerMiner = miner;
			}
		}
		
		Event insertBlockEvent = new InsertBlockEvent(this.blockData, this.time + 1, winnerMiner.getID());
		Blockchain.Sim.scheduleEvent(insertBlockEvent);
	}

}
