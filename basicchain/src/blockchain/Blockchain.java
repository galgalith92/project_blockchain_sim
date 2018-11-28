package blockchain;

import java.util.List;
import java.util.Map;
import java.util.Random;
//import java.awt.Color;
//import java.io.BufferedWriter;
//import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
//import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
//import com.google.gson.GsonBuilder;

public class Blockchain {
	private static List<Block> blockchain = new ArrayList<Block>();
	private static Map<String, Miner> minersMap = new HashMap<String, Miner>();
	private static Simulation Sim = new Simulation();
	private static int minerNum = 0;
	private static double mineBlockRate = (0.3/100); //lambda in exponential distribution of one miner
	
	public static final int BLOCKS_WINDOW_SIZE = 100;
	public static final double MAX_FIX_RATE = 0.2;
	public static final double OPTIMAL_BLOCKS_RATE = (1.0/10);
	public static final double MAX_BLOCK_NUM = 1000000;

	public static void main(String[] args) throws InterruptedException, IOException 
	{
		
		// Create the first block in the blockchain
		String firstBlockData = "first block";
		long firstBlockTimeStamp = 0;
		Blockchain.addBlock(firstBlockData, firstBlockTimeStamp, null);

		// Create miners
		int initialMinersNumber = 100;
		int minerInitialMachinesAmount = 1;
		Blockchain.addMiners(initialMinersNumber,minerInitialMachinesAmount);
		
		// Simulation events
		Blockchain.scheduleEvent(new ChangeMachineAmountEvent(903889,-80));
		Blockchain.scheduleEvent(new ChangeMachineAmountEvent(3554186,150));
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
				System.out.println("Miner ID is not valid!");
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
		return Blockchain.mineBlockRate;
	}
	
	public static void setDifficulty(double mineBlockRate)
	{
		Blockchain.mineBlockRate = mineBlockRate;
	}
	
	public static int getBlockchainSize() {
		return Blockchain.blockchain.size();
	}
	
	public static int getMinersNum() {
		return Blockchain.minersMap.size();
	}

	public static int getTotalMinersMachineNumber() {
		int totalMachineNumber = 0;
		Collection<Miner> minersCollection = Blockchain.getMinersCollection();
		for(Miner miner : minersCollection)
		{
			totalMachineNumber += miner.getMachineNumber();
		}
		return totalMachineNumber;
	}

	public static Collection<Miner> getMinersCollection() {
		return Blockchain.minersMap.values();
	}
	
	public static void scheduleEvent(Event event) {
		Blockchain.Sim.scheduleEvent(event);
	}
	
	public static void addMiners(int minersAmount, int minerMachinesAmount)
	{
		for (long i = 1 ; i <= minersAmount; i++) 
		{
			long seed = Blockchain.minersMap.size() + 1;
			Miner newMiner = new Miner(seed,minerMachinesAmount);
			minersMap.put(newMiner.getID(), newMiner);
		}
		Blockchain.minerNum = Blockchain.minersMap.size();
	}
	
	public static void addMachines(int machineAmount)
	{
		int machinesPerMiner = machineAmount/Blockchain.minerNum;
		int machinesRemainder = machineAmount - machinesPerMiner * Blockchain.minerNum;
		
		Random rand = new Random();
		Object[] keysArray = Blockchain.minersMap.keySet().toArray();
		Blockchain.minersMap.get(keysArray[rand.nextInt(keysArray.length)]).changeMachineAmount(machinesRemainder);
		
		if(machinesPerMiner > 0)
		{
			for(Miner miner : Blockchain.minersMap.values())
			{
				miner.changeMachineAmount(machinesPerMiner);
			}
		}
	}
	
	public static void removeMachines(int machineAmount)
	{
		int machineAmountAfterRemoving = Blockchain.getTotalMinersMachineNumber() - machineAmount;
		int machinesPerMiner = machineAmountAfterRemoving/Blockchain.minerNum;
		int machinesRemainder = machineAmountAfterRemoving - machinesPerMiner * Blockchain.minerNum;
		boolean isEventRemoved = false;
		for(Miner miner : Blockchain.minersMap.values())
		{
			miner.changeMachineAmount(machinesPerMiner - miner.getMachineNumber());
		}
		
		Random rand = new Random();
		Object[] keysArray = Blockchain.minersMap.keySet().toArray();
		Blockchain.minersMap.get(keysArray[rand.nextInt(keysArray.length)]).changeMachineAmount(machinesRemainder);
		
		for(Iterator<Map.Entry<String, Miner>> it = minersMap.entrySet().iterator(); it.hasNext(); ) 
		{
		    Map.Entry<String, Miner> miner = it.next();
		    if(miner.getValue().getMachineNumber() == 0)
			{
				String deleteMinerID = miner.getKey();
				it.remove();
				isEventRemoved = isEventRemoved || Sim.removeMinerEvent(deleteMinerID);
			}
		}
		
		if(isEventRemoved)
		{
			String transaction = "Transaction Number " + Blockchain.getBlockchainSize();
			Blockchain.scheduleEvent(new ProofOfWorkEvent(Sim.getCurrTime(), transaction));
		}
	}

	public static double calculateFixRate(double windowBlockRate)
	{
		if(windowBlockRate == OPTIMAL_BLOCKS_RATE)
		{
			return 0;
		}
		double blocksRateDiffRatio = Math.abs(windowBlockRate - OPTIMAL_BLOCKS_RATE)/windowBlockRate;
		double fixRate = (blocksRateDiffRatio > MAX_FIX_RATE)? MAX_FIX_RATE : blocksRateDiffRatio;
		fixRate = (windowBlockRate > OPTIMAL_BLOCKS_RATE) ? (-fixRate) : fixRate;
		return fixRate/minerNum;
	}
	
	public static String getSimulationDetails()
	{
		String simulationDetails = "***** SIMULATION DETAILS *****" + "\n" +
//				"Exponential distribution is specified by a single parameter Lambda" + "\n" +
				"Number of Miners : " + Blockchain.minerNum + "\n" +
				"Initial Creation Block Rate of one Miner : " + String.format("%.4f", Blockchain.mineBlockRate) + "\n" +
				"Number of Blocks in one Window : " + Blockchain.BLOCKS_WINDOW_SIZE + "\n" +
				"Maximal Fix Rate : " + String.format("%.4f",Blockchain.MAX_FIX_RATE) + "\n" +
				"Optimal Creation Block Rate of the System : " + String.format("%.4f",Blockchain.OPTIMAL_BLOCKS_RATE) + "\n";
		return simulationDetails;
	}
}