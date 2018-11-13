package blockchain;

public class Block {

	private Block previousBlock; // pointer to previous block
	private String data;
	private long timeStamp; // creation simulation time
	private String creatorID;

	// Block Constructor
	public Block(String data, Block previousBlock, long time, String creatorID) {
		this.data = data;
		this.previousBlock = previousBlock;
		this.timeStamp = time;
		this.creatorID = creatorID;
	}
	
	// Copy constructor
	public Block(Block block) {
		this.data = block.data;
		this.previousBlock = block.previousBlock;
		this.timeStamp = block.timeStamp;
		this.creatorID = block.creatorID;
	}
	
	public Block getPrevBlock() {
		return new Block(this.previousBlock);
	}
	
	public long getCreationTime() {
		return this.timeStamp;
	}
	
	public String getData() {
		return this.data;
	}
	
	public String getCreatorID() {
		return this.creatorID;
	}

}