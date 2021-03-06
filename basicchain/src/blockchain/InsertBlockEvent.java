package blockchain;

/*
 * InsertBlockEvent is a class which represent the situation of adding
 * a new block to the blockchain
 */
public class InsertBlockEvent extends Event {
	private String data;
	private String creatorID;
	Block previousBlock;

	/*
	 * @requires data and creatorID are not null
	 * @modifies noBlockchainne
	 * @effects Creates and initiate a new InsertBlockEvent object
	 */
	public InsertBlockEvent(String data, double time, String creatorID) {
		super(time);
		this.data = data;
		this.creatorID = creatorID;
	}

	/*
	 * @requires none
	 * @modifies noBlockchainne
	 * @effects Adds a new block to the blockchain
	 */
	@Override
	public void processEvent() throws InterruptedException {
		double timeStamp = this.time ;
		Blockchain.addBlock(this.data, timeStamp, this.creatorID);
		String transaction = "Transaction Number " + Blockchain.getBlockchainSize();
		Blockchain.scheduleEvent(new ProofOfWorkEvent(this.time, transaction));
	}
	
	public String getCreatorID()
	{
		return this.creatorID;
	}

}
