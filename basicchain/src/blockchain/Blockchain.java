package blockchain;

import java.util.List;
import java.util.Map;
import java.util.Random;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

public class Blockchain {
	private static List<Block> blockchain = new ArrayList<Block>();
	private static Map<String, Miner> minersMap = new HashMap<String, Miner>();
	private static Simulation Sim = new Simulation();
	private static int minerNum = 0;
	private static double mineBlockTime = 10.0*(90); // 1/lambda in exponential distribution of one machine
	private static int initialMinersNumber = 20;
	private static int minerInitialMachinesAmount = 5;
	public static final int BLOCKS_WINDOW_SIZE = 3000;
	public static final double MAX_FIX_RATE = 0.05;
	public static final double OPTIMAL_BLOCK_CREATION_TIME = (10);
	public static final double MAX_BLOCK_NUM = 3000000;
	public static Random rand = new Random(1);
	public static void main(String[] args) throws InterruptedException, IOException 
	{

        
		// Create the first block in the blockchain
		String firstBlockData = "first block";
		long firstBlockTimeStamp = 0;
		Blockchain.addBlock(firstBlockData, firstBlockTimeStamp, null);

		// Create miners
		Blockchain.addMiners(initialMinersNumber,minerInitialMachinesAmount);

		// Simulation events
		Blockchain.scheduleEvent(new ChangeMachineAmountEvent(2603678,50));
		Blockchain.scheduleEvent(new ChangeMachineAmountEvent(4954209,-75));

//		Blockchain.scheduleEvent(new ChangeMachineAmountEvent(1505446,1000));
//		Blockchain.scheduleEvent(new ChangeMachineAmountEvent(2927383,-1500));
//		Blockchain.scheduleEvent(new ChangeMachineAmountEvent(4834876,500));
//		Blockchain.scheduleEvent(new ChangeMachineAmountEvent(6435882,-750));
//		Blockchain.scheduleEvent(new ChangeMachineAmountEvent(9722968,100));
//		Blockchain.scheduleEvent(new ChangeMachineAmountEvent(6823253,-500));
//		Blockchain.scheduleEvent(new ChangeMachineAmountEvent(1505446,-8000));


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

	public static double getExpRandomTime(double lambda)
	{
		return (-1 / lambda) * Math.log(1-Blockchain.rand.nextDouble());
	}
	public static double getMineBlockTime() {
		return Blockchain.mineBlockTime;
	}
	
	public static void setMineBlockTime(double mineBlockTime)
	{
		Blockchain.mineBlockTime = mineBlockTime;
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
				Blockchain.minerNum--;
				isEventRemoved = isEventRemoved || Sim.removeMinerEvent(deleteMinerID);
			}
		}
		
		if(isEventRemoved)
		{
			String transaction = "Transaction Number " + Blockchain.getBlockchainSize();
			Blockchain.scheduleEvent(new ProofOfWorkEvent(Sim.getCurrTime(), transaction));
		}
	}

	public static double calculateFixRate(double empiricAvgMineTime)
	{
		if(empiricAvgMineTime == OPTIMAL_BLOCK_CREATION_TIME)
		{
			return 0;
		}
		double fixRate =  (OPTIMAL_BLOCK_CREATION_TIME - empiricAvgMineTime)/empiricAvgMineTime;
		fixRate = (fixRate > MAX_FIX_RATE)? MAX_FIX_RATE : fixRate;
		fixRate = (fixRate < -MAX_FIX_RATE)? -MAX_FIX_RATE : fixRate;
		return fixRate;
	}
	
	public static String getSimulationDetails()
	{
		String simulationDetails = "***** SIMULATION DETAILS *****" + "\n" +
//				"Exponential distribution is specified by a single parameter Lambda" + "\n" +
				"Initial Number of Miners : " + Blockchain.initialMinersNumber + "\n" +
				"Initial Number of Machines per Miner : " + Blockchain.minerInitialMachinesAmount + "\n" +
				"Initial Block Creation Time of one Machine : " + String.format("%.4f", Blockchain.mineBlockTime) + "\n" +
				"Number of Blocks in one Window : " + Blockchain.BLOCKS_WINDOW_SIZE + "\n" +
				"Maximal Fix Rate : " + String.format("%.4f",Blockchain.MAX_FIX_RATE) + "\n" +
				"Optimal Block Creation Time of the System : " + String.format("%.4f",Blockchain.OPTIMAL_BLOCK_CREATION_TIME) + "\n";
		return simulationDetails;
		
	}
}