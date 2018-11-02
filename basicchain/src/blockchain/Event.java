package blockchain;
import java.util.Date;

public abstract class Event  implements Comparable<Event>
{
	public final long time;
	public Event(long time)
	{
		this.time = time;
	}
	
	public Event()
	{
		this.time = new Date().getTime();
	}
	
	// Execute event by invoking this method.
	public abstract void processEvent() throws InterruptedException;

	public int compareTo(Event event) {
	  return Long.compare(this.time,event.time);
	}
}


