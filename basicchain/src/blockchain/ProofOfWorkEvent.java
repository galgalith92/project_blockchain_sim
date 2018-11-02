package blockchain;

import java.util.Random;

public class ProofOfWorkEvent extends Event
{
	private Random r;
	private double lambda;
	
	ProofOfWorkEvent(long time,long seed, double difficulty)
	{
		super(time);
		r = new Random(seed);
		lambda = 1/difficulty;
	}
	@Override
	public void processEvent() throws InterruptedException 
	{
		Thread.sleep((long) (1000*getExpRandom()));
		// Create a broadcastEvent
	}
	
	private double getExpRandom() 
	{ 
        return -(Math.log(r.nextDouble()) / lambda); 
    } 
	
}
