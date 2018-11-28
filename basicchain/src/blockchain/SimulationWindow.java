package blockchain;

public class SimulationWindow {
	private int windowNumber;
	private double startTime;
	private double endTime;
	private double deltaTime;
	private int deltaBlocks;
	private double actualBlockRate;
	private double windowBlockRate;
	private double fixRate;
	
	public SimulationWindow(int windowNumber, double startTime, double endTime, double deltaTime,
							int deltaBlocks, double actualBlockRate, double windowBlockRate, 
							double fixRate)
	{
		this.windowNumber = windowNumber;
		this.startTime = startTime;
		this.endTime = endTime;
		this.deltaTime = deltaTime;
		this.deltaBlocks = deltaBlocks;
		this.actualBlockRate = actualBlockRate;
		this.windowBlockRate = windowBlockRate;
		this.fixRate = fixRate;
	}
	
	@Override
	public String toString()
	{
		String res = "*** Window Number " + this.windowNumber + " ***" + "\n" +
				"Start Time : " + String.format("%.4f",this.startTime) + "\n" +
				"End Time : " + String.format("%.4f",this.endTime) + "\n" +
				"Delta Time : " + String.format("%.4f",this.deltaTime) + "\n" +
				"Delta Blocks : " + this.deltaBlocks + "\n" +
				"Actual Block Rate : " + String.format("%.4f",this.actualBlockRate) + "\n" +
				"Window Block Rate : " + String.format("%.4f",this.windowBlockRate) + "\n" +
				"Fix Rate : " + String.format("%.4f",this.fixRate) + "\n";
		return res;
	}
	
	
}
