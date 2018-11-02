package basicchain;
import java.util.Date;

public class Event 
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
	void processEvent () {}

	public int compareTo(Event event) {
	  return Long.compare(this.time,event.time);
	}
}


