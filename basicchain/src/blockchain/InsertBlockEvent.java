package blockchain;

public class InsertBlockEvent extends Event {
	private String data;
	// private long blockimeStamp; // creation simulation time
	private String creatorID;
	Block previousBlock;

	InsertBlockEvent(String data, long time, String creatorID) {
		super(time);
		this.data = data;
		this.creatorID = creatorID;
//		this.lastBlock = Blockchain.blockchain.get(Blockchain.blockchain.size()-1).getPrevBlock();
	}

	@Override
	public void processEvent() throws InterruptedException {
		long timeStamp = this.time + 1;
		Blockchain.addBlock(this.data, timeStamp, this.creatorID);
	}

}
