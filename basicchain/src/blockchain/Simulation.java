package blockchain;

//import java.awt.Color;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Random;

import blockchain.ChangeMachineAmountEvent.NonValidDeltaMachineException;
/*
 * Simulation is a class which provides the structure for the simulation activities.
 */
class Simulation {
	public double time; // the current simulation time
	private Queue<Event> eventsQueue;
	private static final int MAXIMAL_NUMBER_OF_EVENTS_IN_QUEUE = 100;
	Random rand;
	
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
		this.rand = new Random(1);
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
		BufferedWriter logFile = new BufferedWriter(new FileWriter("simulation_log3.txt"));
		logFile.write(Blockchain.getSimulationDetails());
		logFile.newLine();
		PrintWriter pw = new PrintWriter(new File("results1.csv"));
        StringBuilder sb = new StringBuilder();
        sb.append("Window Number,Start Time,End Time,Delta Time,Delta Blocks,Theory Block Creation Time,"
        		+ "Window Block Creation Time,Fix Rate, Machine Amount\n");
        
        PrintWriter pwTheory = new PrintWriter(new File("theoretical_results1.csv"));
        StringBuilder sbTheory = new StringBuilder();
        sbTheory.append("Window Number,Start Time,End Time,Delta Time,Delta Blocks,Theory Block Creation Time,"
        		+ "Fix Rate, Machine Amount\n");
		
		List<SimulationWindow> simulationWindows = new ArrayList<SimulationWindow>();
		
		double mineMachineTimeTheory = Blockchain.getMineBlockTime();
		
		int windowStartNumOfBlocks = 0;
		double windowStartTime = this.time;
		double empiricAvgMineTime = Double.MAX_VALUE;
		int sign = 1;
		
		double windowStartTimeTheory = this.time;
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
				double mineBlockTime = Blockchain.getMineBlockTime();
				double windowEndTime = this.time;
				double deltaTime = windowEndTime - windowStartTime;
				double theorAvgMineTime = 1.0*mineBlockTime/Blockchain.getTotalMinersMachineNumber(); // minimum of exponential random variables
				empiricAvgMineTime = deltaTime/Blockchain.BLOCKS_WINDOW_SIZE;
				double fixRate = Blockchain.calculateFixRate(empiricAvgMineTime);
				Blockchain.setMineBlockTime(mineBlockTime + mineBlockTime * fixRate);

				//debug
				SimulationWindow currWindow = new SimulationWindow(simulationWindows.size() + 1,
						windowStartTime, windowEndTime, deltaTime,  Blockchain.BLOCKS_WINDOW_SIZE, theorAvgMineTime, 
						empiricAvgMineTime, fixRate);
				simulationWindows.add(currWindow);
				sb.append(currWindow.csvString());
				
				deltaTime = 0;
				windowStartNumOfBlocks =Blockchain.getBlockchainSize();
				windowStartTime = this.time;
				
//				sign = (sign > 0)? -1:1;
//				Blockchain.scheduleEvent(new ChangeMachineAmountEvent(this.time + rand.nextDouble()*60,sign*rand.nextInt(2)));
				
				//Theory
				double AvgMineTimeTheory = 1.0*mineMachineTimeTheory/Blockchain.getTotalMinersMachineNumber(); // minimum of exponential random variables
				double windowEndTimeTheory = windowStartTimeTheory + AvgMineTimeTheory*Blockchain.BLOCKS_WINDOW_SIZE;
				double deltaTimeTheory = windowEndTimeTheory - windowStartTimeTheory;
				double fixRateTheory = Blockchain.calculateFixRate(AvgMineTimeTheory);
				mineMachineTimeTheory = mineMachineTimeTheory + mineMachineTimeTheory*fixRateTheory;

//				//debug
				TheorySimulationWindow currTheoryWindow = new TheorySimulationWindow(simulationWindows.size(), windowStartTimeTheory, 
						windowEndTimeTheory, deltaTimeTheory, Blockchain.BLOCKS_WINDOW_SIZE,
						AvgMineTimeTheory, fixRateTheory);
				sbTheory.append(currTheoryWindow.csvString());			
				windowStartTimeTheory = windowEndTimeTheory;
			}
		}

		for(SimulationWindow window : simulationWindows)
		{
			logFile.write(window.toString());
			logFile.newLine();
		}
		logFile.close();
		pw.write(sb.toString());
        pw.close();
        pwTheory.write(sbTheory.toString());
        pwTheory.close();
        System.out.println("Done!");
	}
	
	public boolean removeMinerEvent(String minerID)
	{
		boolean isEventRemoved = false;
		for(Iterator<Event> it = this.eventsQueue.iterator(); it.hasNext(); ) 
		{
			Object currEvent = it.next();
			if (currEvent instanceof InsertBlockEvent)
			{
				InsertBlockEvent currInsertBlockEvent = (InsertBlockEvent)currEvent;
				if(currInsertBlockEvent.getCreatorID() == minerID)
				{
					it.remove();
					isEventRemoved = true;
				}
			}

		}
		return isEventRemoved;
	}
	
	public double getCurrTime()
	{
		return this.time;
	}
};
