package blockchain;

public class changeMinersAmountEvent extends Event{
	private int deltaMiners;
	public changeMinersAmountEvent(double time, int deltaMiners)
	{
		super(time);
		this.deltaMiners = deltaMiners;
	}

	@Override
	public void processEvent() throws InterruptedException {
		if (this.deltaMiners > 0 )
		{
			Blockchain.addMiners(this.deltaMiners);
		}
		else if(this.deltaMiners < 0)
		{
			Blockchain.removeMiners(Math.abs(this.deltaMiners));
		}
		
	}
}
