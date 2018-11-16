
package blockchain;

/*
 * Event is an abstract class that represents event, records the time 
 * at which the event will take place, contains an abstract function 
 * named processEvent that is invoked to execute the event. 
 * This class is comparable
 */
public abstract class Event implements Comparable<Event> {
	public final double time;

	/*
	 * @requires none
	 * @modifies this
	 * @effects Creates and initializes a new Event object
	 */
	public Event(double time) {
		this.time = time;
	}

	/*
	 * @requires none
	 * @modifies none
	 * @effects Executes the event (abstract function)
	 * @throws InterruptedException
	 */
	public abstract void processEvent() throws InterruptedException;

	/*
	 * @requires event is not null
	 * @modifies none
	 * @effects Compares this Event object with other Event object 
	 */
	public int compareTo(Event event) {
		return Double.compare(this.time, event.time);
	}
}
