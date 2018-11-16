package blockchain;
/*
 * Block is an immutable class which represents a block in the blockchain
 */
public class Block {

	private Block previousBlock; // pointer to previous block
	private String data;
	private double creationTime; // simulation time
	private String creatorID; // the creator miner ID

	/*
	 * @requires data, previousBlock and creatorID are not null
	 * @modifies this
	 * @effects Creates and initializes a new Block object
	 */
	public Block(String data, Block previousBlock, double time, String creatorID) {
		this.data = data;
		this.previousBlock = previousBlock;
		this.creationTime = time;
		this.creatorID = creatorID;
	}
	
	/*
	 * @requires block is not null
	 * @modifies this
	 * @effects Creates and initializes a new Block object (Copy constructor)
	 */
	public Block(Block block) {
		this.data = block.data;
		this.previousBlock = block.previousBlock;
		this.creationTime = block.creationTime;
		this.creatorID = block.creatorID;
	}
	
	/*
	 * @requires none
	 * @modifies none
	 * @effects Returns the previous block of this block
	 */
	public Block getPrevBlock() {
		return new Block(this.previousBlock);
	}
	
	/*
	 * @requires none
	 * @modifies none
	 * @effects Returns the creation time of this block
	 */
	public double getCreationTime() {
		return this.creationTime;
	}
	
	/*
	 * @requires none
	 * @modifies none
	 * @effects Returns the data this block stores
	 */
	public String getData() {
		return this.data;
	}
	
	/*
	 * @requires none
	 * @modifies none
	 * @effects Returns the creator miner ID of this block
	 */
	public String getCreatorID() {
		return this.creatorID;
	}

}