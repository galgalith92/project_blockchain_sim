package blockchain;

import java.util.List;
import java.util.Map;
//import java.awt.Color;
//import java.io.BufferedWriter;
//import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
//import java.util.Collections;
import java.util.HashMap;
//import com.google.gson.GsonBuilder;

public class Blockchain {
	private static List<Block> blockchain = new ArrayList<Block>();
	private static Map<String, Miner> minersMap = new HashMap<String, Miner>();
	private static Simulation Sim = new Simulation();
	private static int minerNum = 0;
	private static double minerLambda = (0.01/100); //lambda in exponential distribution of one miner
	
	public static final int BLOCKS_WINDOW_SIZE = 1000;
	public static final double MAX_FIX_RATE = 0.5;
	public static final double OPTIMAL_BLOCKS_LAMBDA = (1.0/10);
//	public static final double MAX_LAMBDA_ACCURACY = 0.001;
	public static final double MAX_BLOCK_NUM = 1000000;

	public static void main(String[] args) throws InterruptedException, IOException 
	{
		
		// Create the first block in the blockchain
		String firstBlockData = "first block";
		long firstBlockTimeStamp = 0;
		Blockchain.addBlock(firstBlockData, firstBlockTimeStamp, null);

		// Create miners
		int initialMinersNumber = 100;
		Blockchain.addMiners(initialMinersNumber);
		
		// Simulation events
		Blockchain.scheduleEvent(new changeMinersAmountEvent(1000000,50));
		Blockchain.scheduleEvent(new changeMinersAmountEvent(10000000,50));
		Blockchain.scheduleEvent(new changeMinersAmountEvent(15000000,-100));
		String transaction = "Transaction Number " + Blockchain.getBlockchainSize();
		scheduleEvent(new ProofOfWorkEvent(0, transaction));
		Sim.run();

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
		return Blockchain.minerLambda;
	}
	public static void setDifficulty(double minerLambda)
	{
		Blockchain.minerLambda = minerLambda;
	}
	
	public static int getBlockchainSize() {
		return Blockchain.blockchain.size();
	}
	
	public static int getMinersNum() {
		return Blockchain.minersMap.size();
	}

	public static double getTotalMinersComputationalPower() {
		double totalComputationalPower = 0;
		Collection<Miner> minersCollection = Blockchain.getMinersCollection();
		for(Miner miner : minersCollection)
		{
			totalComputationalPower += miner.getComputationalPower();
		}
		return totalComputationalPower;
	}

	public static Collection<Miner> getMinersCollection() {
		return Blockchain.minersMap.values();
	}
	

	public static void scheduleEvent(Event event) {
		Blockchain.Sim.scheduleEvent(event);
	}
	
	public static boolean addMiners(int minersAmount)
	{
		for (long i = 1 ; i <= minersAmount; i++) 
		{
			long seed = Blockchain.minersMap.size() + 1;
			Miner newMiner = new Miner(seed);
			minersMap.put(newMiner.getID(), newMiner);
		}
		Blockchain.minerNum = Blockchain.minersMap.size();
		return true;
	}
	
	public static boolean removeMiners(int minersAmount)
	{
		if(minersAmount < minersMap.size())
		{
			for (long i = 1 ; i <= minersAmount; i++) 
			{
				String deleteMinerID = minersMap.keySet().iterator().next(); 
				minersMap.remove(deleteMinerID);
				Sim.removeMinerEvent(deleteMinerID);
			}
			Blockchain.minerNum = Blockchain.minersMap.size();
			
			if(Sim.getEventsQueueSize() == 0)
			{
				String transaction = "Transaction Number " + Blockchain.getBlockchainSize();
				Blockchain.scheduleEvent(new ProofOfWorkEvent(Sim.getCurrTime(), transaction));
			}
			return true;
		}
		else
		{
			return false;
		}
	}

	public static double calculateFixRate(double estimatedWindowLambda)
	{
		if(estimatedWindowLambda == OPTIMAL_BLOCKS_LAMBDA)
		{
			return 0;
		}
		double blocksRateDiffRatio = Math.abs(estimatedWindowLambda - OPTIMAL_BLOCKS_LAMBDA)/estimatedWindowLambda;
		double fixRate = (blocksRateDiffRatio > MAX_FIX_RATE)? MAX_FIX_RATE : blocksRateDiffRatio;
		fixRate = (estimatedWindowLambda > OPTIMAL_BLOCKS_LAMBDA) ? (-fixRate) : fixRate;
		return fixRate/minerNum;
	}
	
	public static String getSimulationDetails()
	{
		String simulationDetails = "***** SIMULATION DETAILS *****" + "\n" +
				"Exponential distribution is specified by a single parameter Lambda" + "\n" +
				"Number of Miners : " + Blockchain.minerNum + "\n" +
				"Initial Lambda of one Miner : " + String.format("%.4f", Blockchain.minerLambda) + "\n" +
				"Number of Blocks in one Window : " + Blockchain.BLOCKS_WINDOW_SIZE + "\n" +
				"Maximal Fix Rate : " + String.format("%.4f",Blockchain.MAX_FIX_RATE) + "\n" +
				"Optimal Lambda of the System : " + String.format("%.4f",Blockchain.OPTIMAL_BLOCKS_LAMBDA) + "\n";
		return simulationDetails;
	}
}