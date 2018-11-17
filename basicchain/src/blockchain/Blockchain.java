package blockchain;

import java.util.List;
import java.util.Map;
import java.awt.Color;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
//import com.google.gson.GsonBuilder;

public class Blockchain {
	private static List<Block> blockchain = new ArrayList<Block>();
	private static Map<String, Miner> minersMap = new HashMap<String, Miner>();
	private static Simulation Sim = new Simulation();
	private static int minerNum = 10;
	private static double minerLambda = (5.05/100); //lambda in exponential distribution of one miner
	private static int blocksWindowSize = 10000;
	private static List<SimulationWindow> simulationWindows = new ArrayList<SimulationWindow>();
	//private static double simulationDuration = 10;
	private static final double MAX_FIX_RATE = 0.5;
	private static final double OPTIMAL_BLOCKS_LAMBDA = (1.0/10);

	public static void main(String[] args) throws InterruptedException, IOException 
	{
		/// debug 
		BufferedWriter logFile = new BufferedWriter(new FileWriter("simulation_log.txt"));
		logFile.write(Blockchain.getSimulationDetails());
		logFile.newLine();
		List<Double> createdBlocksNumber = new ArrayList<Double>();
		List<Double> actualLambdaList = new ArrayList<Double>();
		
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
		double windowStartTime = Sim.getCurrentTime();
		double estimatedWindowLambda = Double.MAX_VALUE;
		while (Math.abs(estimatedWindowLambda - OPTIMAL_BLOCKS_LAMBDA)>0.001 && blockchain.size() < 1000000) 
		{
			String transaction = "Transaction Number " + blockchain.size();
			Blockchain.scheduleEvent(new ProofOfWorkEvent(Sim.getCurrentTime(), transaction));
			deltaBlocks++;
			Sim.run();
			if(deltaBlocks == blocksWindowSize)
			{
				double windowEndTime = Sim.getCurrentTime();
				double deltaTime = windowEndTime - windowStartTime;
				estimatedWindowLambda = deltaBlocks/deltaTime;
				double fixRate = calculateFixRate(estimatedWindowLambda);
				minerLambda += (minerLambda * fixRate);
				double actualLambda = minerLambda * minerNum;
				SimulationWindow currWindow = new SimulationWindow(simulationWindows.size() + 1,
						windowStartTime, windowEndTime, deltaTime, deltaBlocks, actualLambda, 
						estimatedWindowLambda, fixRate);
				simulationWindows.add(currWindow);
				deltaTime = 0;
				deltaBlocks =0;
				windowStartTime = Sim.getCurrentTime();
				createdBlocksNumber.add((double)blockchain.size());
				actualLambdaList.add(actualLambda);
			}
		}
		
		for(SimulationWindow window : simulationWindows)
		{
			logFile.write(window.toString());
			logFile.newLine();
			System.out.println(window.toString());
		}
		logFile.close();
//		String blockchainJson = new GsonBuilder().setPrettyPrinting().create().toJson(blockchain);
//		System.out.println("\nThe block chain: ");
//		System.out.println(blockchainJson);
		
		Plot plot = Plot.plot(Plot.plotOpts().
		        title("Estimated Lambda vs Number of Blocks").
		        legend(Plot.LegendFormat.BOTTOM)).
		    xAxis("Blocks", Plot.axisOpts().
		        range(0, Blockchain.blockchain.size())).
		    yAxis("Estimated Lambda", Plot.axisOpts().
		        range(0, Collections.max(actualLambdaList))).
		    series("Data", Plot.data().xy(createdBlocksNumber,actualLambdaList),
		        Plot.seriesOpts().
		            marker(Plot.Marker.CIRCLE).
		            markerColor(Color.BLUE).
		            color(Color.BLACK));
		plot.save("blocks_graph", "png");
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

	public static Collection<Miner> getMinersCollection() {
		return Blockchain.minersMap.values();
	}

	public static void scheduleEvent(Event event) {
		Blockchain.Sim.scheduleEvent(event);
	}

	private static double calculateFixRate(double estimatedWindowLambda)
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
		String res = "***** SIMULATION DETAILS *****" + "\n" +
				"Exponential distribution is specified by a single parameter Lambda" + "\n" +
				"Number of Miners : " + Blockchain.minerNum + "\n" +
				"Initial Lambda of one Miner : " + String.format("%.4f", Blockchain.minerLambda) + "\n" +
				"Number of Blocks in one Window : " + Blockchain.blocksWindowSize + "\n" +
				"Maximal Fix Rate : " + String.format("%.4f",Blockchain.MAX_FIX_RATE) + "\n" +
				"Optimal Lambda of the System : " + String.format("%.4f",Blockchain.OPTIMAL_BLOCKS_LAMBDA) + "\n";
		return res;
	}
}