package blockchain;
/**
 * 
 * @author eitan102
 *
 */
public class UpdateDifficultyEvent extends Event
{
	double difficulty;

	UpdateDifficultyEvent(long time,double difficulty)
	{
		super(time);
		this.difficulty = difficulty;
	}
	
	@Override
	public void processEvent() throws InterruptedException {
		// TODO Auto-generated method stub
		
	}
	
}
