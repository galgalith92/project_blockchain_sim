package blockchain;

import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.Queue;

class Simulation {
	public long time;
	private Queue<Event> eventsQueue;

	Simulation() {
		this.time = 0;
		this.eventsQueue = new PriorityQueue<Event>(100, eventComparator);
	}

	// Comparator anonymous class implementation
	public static Comparator<Event> eventComparator = new Comparator<Event>() {
		@Override
		public int compare(Event e1, Event e2) {
			return e1.compareTo(e2);
		}
	};

	public void scheduleEvent(Event newEvent) {
		eventsQueue.add(newEvent);
	}

	public void run() throws InterruptedException {
		while (!eventsQueue.isEmpty()) {
			Event nextEvent = eventsQueue.poll();
			time = nextEvent.time;
			nextEvent.processEvent();
		}
	}

};
