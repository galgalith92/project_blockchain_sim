package blockchain;

import java.awt.Color;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;

import blockchain.ChangeMachineAmountEvent.NonValidDeltaMachineException;
/*
 * Simulation is a class which provides the structure for the simulation activities.
 */
class Simulation {
	public double time; // the current simulation time
	private Queue<Event> eventsQueue;
	private static final int MAXIMAL_NUMBER_OF_EVENTS_IN_QUEUE = 100;
	
	
	// Comparator Event class implementation
	private static Comparator<Event> eventComparator = new Comparator<Event>() {
		@Override
		public int compare(Event e1, Event e2) {
			// The event with the lower time has higher priority
			return e1.compareTo(e2);
		}
	};

	/*
	 * @requires none
	 * @modifies this
	 * @effects Creates and initializes a new Simulation object
	 */
	public Simulation() {
		this.time = 0;
		this.eventsQueue = new PriorityQueue<Event>(MAXIMAL_NUMBER_OF_EVENTS_IN_QUEUE, eventComparator);
	}

	/*
	 * @requires newEvent is not null
	 * @modifies this
	 * @effects Insert a new event into the queue
	 */
	public void scheduleEvent(Event newEvent) { // Exposure? 
		this.eventsQueue.add(newEvent);
	}

	/*
	 * @requires none
	 * @modifies this
	 * @effects Runs the simulation - execute the events in the queue
	 */
	public void run() throws InterruptedException, IOException {
		/// debug 
		BufferedWriter logFile = new BufferedWriter(new FileWriter("simulation_log.txt"));
		logFile.write(Blockchain.getSimulationDetails());
		logFile.newLine();
		
		List<Double> createdBlocksNumber = new ArrayList<Double>(); // ??how many blocks were at the blockchain in each window
		List<Double> blockRateList = new ArrayList<Double>(); // ??what was the block rate in each window
		List<SimulationWindow> simulationWindows = new ArrayList<SimulationWindow>();
		
		
		int windowStartNumOfBlocks = 0;
		double windowStartTime = this.time;
		double currWindowBlockRate = Double.MAX_VALUE;
		
		/*Math.abs(estimatedWindowLambda - OPTIMAL_BLOCKS_LAMBDA)>MAX_LAMBDA_ACCURACY */
		while (!this.eventsQueue.isEmpty() && Blockchain.getBlockchainSize() < Blockchain.MAX_BLOCK_NUM) 
		{
			Event nextEvent = this.eventsQueue.poll();
			time = nextEvent.time;
			try {
				nextEvent.processEvent();
			} catch (NonValidDeltaMachineException e) {
				e.printStackTrace();
			}
			if((Blockchain.getBlockchainSize() - windowStartNumOfBlocks) == Blockchain.BLOCKS_WINDOW_SIZE)
			{
				double mineBlockRate = Blockchain.getDifficulty();
				double windowEndTime = this.time;
				double deltaTime = windowEndTime - windowStartTime;
				double actualBlockRate = mineBlockRate * Blockchain.getTotalMinersMachineNumber(); // minimum of exponential random variables
				currWindowBlockRate = Blockchain.BLOCKS_WINDOW_SIZE/deltaTime;
				double fixRate = Blockchain.calculateFixRate(currWindowBlockRate);
				Blockchain.setDifficulty(mineBlockRate + mineBlockRate * fixRate);

				//debug
				SimulationWindow currWindow = new SimulationWindow(simulationWindows.size() + 1,
						windowStartTime, windowEndTime, deltaTime,  Blockchain.BLOCKS_WINDOW_SIZE, actualBlockRate, 
						currWindowBlockRate, fixRate);
				simulationWindows.add(currWindow);
				deltaTime = 0;
				windowStartNumOfBlocks =Blockchain.getBlockchainSize();
				windowStartTime = this.time;
//				createdBlocksNumber.add((double)Blockchain.getBlockchainSize());
				createdBlocksNumber.add((double)simulationWindows.size());
				blockRateList.add(actualBlockRate);
			}
		}

		for(SimulationWindow window : simulationWindows)
		{
			logFile.write(window.toString());
			logFile.newLine();
		}
		logFile.close();
		
		Plot plot = Plot.plot(Plot.plotOpts().
		        title("Blocks Creation Rate vs Number of Blocks").
		        legend(Plot.LegendFormat.BOTTOM)).
		    xAxis("Window Number", Plot.axisOpts().
		        range(0, simulationWindows.size())). /* Blockchain.getBlockchainSize() */
		    yAxis("Blocks/Minute", Plot.axisOpts().
		        range(0, 1)). /* Collections.max(blockRateList)) */
		    series("Data", Plot.data().xy(createdBlocksNumber,blockRateList),
		        Plot.seriesOpts().
		            marker(Plot.Marker.CIRCLE).
		            markerColor(Color.BLUE).
		            color(Color.BLACK));
		plot.save("blocks_graph", "png");
	}
	
	public boolean removeMinerEvent(String minerID)
	{
		boolean isEventRemoved = false;
		Iterator<Event> iter = this.eventsQueue.iterator();
		while (iter.hasNext())
		{
			Object currEvent = iter.next();
			if (currEvent instanceof InsertBlockEvent)
			{
				InsertBlockEvent currInsertBlockEvent = (InsertBlockEvent)currEvent;
				if(currInsertBlockEvent.getCreatorID() == minerID)
				{
					this.eventsQueue.remove(currEvent);
					isEventRemoved = true;
				}
			}
		}
		return isEventRemoved;
	}
	
	public int getEventsQueueSize()
	{
		return this.eventsQueue.size();
	}
	
	public double getCurrTime()
	{
		return this.time;
	}
};
