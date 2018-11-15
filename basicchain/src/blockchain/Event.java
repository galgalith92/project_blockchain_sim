package blockchain;

public abstract class Event  implements Comparable<Event>
{
	public final double time;
	public Event(double time)
	{
		this.time = time;
	}
	
	
	// Execute event by invoking this method.
	public abstract void processEvent() throws InterruptedException;

	public int compareTo(Event event) 
	{
	  return Double.compare(this.time,event.time);
	}
}


