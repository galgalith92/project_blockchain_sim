package blockchain;

import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.Queue;
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
			return -e1.compareTo(e2);
		}
	};

	/*
	 * @requires none
	 * @modifies this
	 * @effects Creates and initializes a new Simulation object
	 */
	Simulation() {
		this.time = 0;
		this.eventsQueue = new PriorityQueue<Event>(MAXIMAL_NUMBER_OF_EVENTS_IN_QUEUE, eventComparator);
	}

	/*
	 * @requires newEvent is not null
	 * @modifies this
	 * @effects Insert a new event into the queue
	 */
	public void scheduleEvent(Event newEvent) { // Exposure? 
		eventsQueue.add(newEvent);
	}

	/*
	 * @requires none
	 * @modifies this
	 * @effects Runs the simulation - execute the events in the queue
	 */
	public void run() throws InterruptedException {
		while (!eventsQueue.isEmpty()) {
			Event nextEvent = eventsQueue.poll();
			time = nextEvent.time;
			nextEvent.processEvent();
		}
	}
	
	/*
	 * @requires none
	 * @modifies none
	 * @effects Returns the current time of the simulation
	 */
	public double getCurrentTime()
	{
		return this.time;
	}
};
