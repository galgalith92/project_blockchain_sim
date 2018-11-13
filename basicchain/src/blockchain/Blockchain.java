package blockchain;
//import com.google.gson.GsonBuilder;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

public class Blockchain {
	private static List<Block> blockchain = new ArrayList<Block>();
	private static Map<String,Miner> minersMap = new HashMap<String,Miner>();
	private static Simulation Sim = new Simulation();
	private static int minerNum = 10;
	private static double difficulty = 5;

	public static void main(String[] args) throws InterruptedException {
		// Create the first block in the blockchain
		String firstBlockData = "first block";
		long firstBlockTimeStamp = 0;
		Blockchain.addBlock(firstBlockData, firstBlockTimeStamp, null);

		// Create miners
		long seed = 1;
		for (int i = 0; i < minerNum; i++) {
			Miner newMiner =new Miner(seed);
			minersMap.put(newMiner.getID(), newMiner);
			seed++;
		}

		String transaction = "Transaction " + blockchain.size();
		Sim.scheduleEvent(new ProofOfWorkEvent(1, transaction));
		Sim.run();
	}

	public static boolean addBlock(String data, long timeStamp, String creatorID) {
		Block previousBlock;
		if (blockchain.size()>0)
		{
			if (blockchain.get(blockchain.size() - 1).getCreationTime() >= timeStamp) {
				System.out.println("The block has a non valid creation time!");
				return false;
			}
			if (!minersMap.containsKey(creatorID))
			{
				System.out.println("The miner ID is not valid!");
				return false;
			}
			previousBlock = blockchain.get(blockchain.size()-1);
		}
		else {
			previousBlock = null;
		}
		
		Block block = new Block(data, previousBlock, timeStamp, creatorID);
		blockchain.add(block);
		return true;
	}

	public static double getDifficulty() {
		return Blockchain.difficulty;
	}

	public static Collection<Miner> getMinersCollection()
	{
		return Blockchain.minersMap.values();
	}
	
	public static void scheduleEvent(Event event)
	{
		Blockchain.Sim.scheduleEvent(event);
	}
	/*
	 * public static Boolean isChainValid() { Block currentBlock; Block
	 * previousBlock; String hashTarget = new String(new
	 * char[difficulty]).replace('\0', '0');
	 * 
	 * //loop through blockchain to check hashes: for(int i=1; i <
	 * blockchain.size(); i++) { currentBlock = blockchain.get(i); previousBlock =
	 * blockchain.get(i-1); //compare registered hash and calculated hash:
	 * if(!currentBlock.hash.equals(currentBlock.calculateHash()) ){
	 * System.out.println("Current Hashes not equal"); return false; } //compare
	 * previous hash and registered previous hash
	 * if(!previousBlock.hash.equals(currentBlock.previousHash) ) {
	 * System.out.println("Previous Hashes not equal"); return false; } //check if
	 * hash is solved if(!currentBlock.hash.substring( 0,
	 * difficulty).equals(hashTarget)) {
	 * System.out.println("This block hasn't been mined"); return false; } } return
	 * true; }
	 */

}
