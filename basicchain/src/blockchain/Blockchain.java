package blockchain;
import com.google.gson.GsonBuilder;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

public class Blockchain {
	private static List<Block> blockchain = new ArrayList<Block>();
	private static Map<String, Miner> minersMap = new HashMap<String, Miner>();
	private static Simulation Sim = new Simulation();
	private static int minerNum = 10;
	private static double lambda = (1.0/10)/minerNum; //  lambda in exponential distribution of one miner
	private static double blocksWindowSize = 5;
	//private static double simulationDuration = 10;
	private static final double MAX_FIX_RATE = 0.5;
	private static final double OPTIMAL_BLOCKS_LAMBDA = (1.0/10);

	public static void main(String[] args) throws InterruptedException 
	{
		// Create the first block in the blockchain
		String firstBlockData = "first block";
		long firstBlockTimeStamp = 0;
		Blockchain.addBlock(firstBlockData, firstBlockTimeStamp, null);

		// Create miners
		for (long seed = 1; seed <= minerNum; seed++) 
		{
			Miner newMiner = new Miner(seed);
			minersMap.put(newMiner.getID(), newMiner);
		}
		
		int deltaBlocks = 0;
		double deltaTime = 0;
		double windowStartTime = Sim.getCurrentTime();
		double windowEndTime;
		double fixRate = -1;
		while (Math.abs(fixRate)>0.001) 
		{
			String transaction = "Transaction Number " + blockchain.size();
			Blockchain.scheduleEvent(new ProofOfWorkEvent(Sim.getCurrentTime(), transaction));
			deltaBlocks++;
			Sim.run();
			if(deltaBlocks == blocksWindowSize)
			{
				windowEndTime = Sim.getCurrentTime();
				deltaTime = windowEndTime - windowStartTime;
				fixRate = calculateFixRate(deltaTime, deltaBlocks);
				lambda += (lambda * fixRate);
				deltaTime = 0;
				deltaBlocks =0;
				windowStartTime = Sim.getCurrentTime();
			}
		}
//		String blockchainJson = new GsonBuilder().setPrettyPrinting().create().toJson(blockchain);
//		System.out.println("\nThe block chain: ");
//		System.out.println(blockchainJson);
		return;
	}

	public static boolean addBlock(String data, double timeStamp, String creatorID) {
		Block previousBlock;
		if (blockchain.size() > 0) {
			if (blockchain.get(blockchain.size() - 1).getCreationTime() > timeStamp) {
				System.out.println("The block has a non valid creation time!");
				return false;
			}
			if (!minersMap.containsKey(creatorID)) {
				System.out.println("The miner ID is not valid!");
				return false;
			}
			previousBlock = blockchain.get(blockchain.size() - 1);
		} else {
			previousBlock = null;
		}

		Block block = new Block(data, previousBlock, timeStamp, creatorID);
		blockchain.add(block);
		return true;
	}

	public static double getDifficulty() {
		return Blockchain.lambda;
	}

	public static Collection<Miner> getMinersCollection() {
		return Blockchain.minersMap.values();
	}

	public static void scheduleEvent(Event event) {
		Blockchain.Sim.scheduleEvent(event);
	}

	private static double calculateFixRate(double windowDuration, int numberOfblocksInWindow)
	{
		double estimatedWindowLambde = numberOfblocksInWindow/windowDuration;
		if(estimatedWindowLambde == OPTIMAL_BLOCKS_LAMBDA)
		{
			return 0;
		}
		double blocksRateDiffRatio = Math.abs(estimatedWindowLambde - OPTIMAL_BLOCKS_LAMBDA)/estimatedWindowLambde;
		double fixRate = (blocksRateDiffRatio > MAX_FIX_RATE)? MAX_FIX_RATE : blocksRateDiffRatio;
		fixRate = (estimatedWindowLambde > OPTIMAL_BLOCKS_LAMBDA) ? (-fixRate) : fixRate;
		return fixRate/minerNum;
	}
}
