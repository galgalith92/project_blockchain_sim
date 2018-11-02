package blockchain;

public class broadcastEvent extends Event {
	private Miner winningMiner;
	
	broadcastEvent(long time,Miner winningMiner)
	{
		super(time);
		this.winningMiner = winningMiner;
	}
	
	@Override
	public void processEvent() throws InterruptedException 
	{
		// notice the loosing miners that someone solve the challenge 
	}

}
