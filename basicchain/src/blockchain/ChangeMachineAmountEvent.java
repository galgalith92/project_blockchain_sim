package blockchain;

public class ChangeMachineAmountEvent extends Event{
	private int deltaMachine;
	public class NonValidDeltaMachineException extends Exception 
	{
		public NonValidDeltaMachineException()
		{
			super("Invalid Delta Machine Number in ChangeMachineAmountEvent");
		}
	}
	public ChangeMachineAmountEvent(double time, int deltaMachine)
	{
		super(time);
		this.deltaMachine = deltaMachine;
	}

	@Override
	public void processEvent() throws InterruptedException, NonValidDeltaMachineException {
		if (this.deltaMachine > 0 )
		{
			Blockchain.addMachines(this.deltaMachine);
		}
		else if(this.deltaMachine < 0)
		{
			if(Math.abs(deltaMachine) > Blockchain.getTotalMinersMachineNumber())
			{
				System.out.println(Blockchain.getTotalMinersMachineNumber());
				throw new NonValidDeltaMachineException();
			}
			Blockchain.removeMachines(Math.abs(this.deltaMachine));
		}
		
	}
}
