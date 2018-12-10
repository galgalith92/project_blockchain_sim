package blockchain;

public class SimulationWindow {
	private int windowNumber;
	private double startTime;
	private double endTime;
	private double deltaTime;
	private int deltaBlocks;
	private double avarageMineTimeTheory;
	private double averageMineTimeEmpirical;
	private double fixRate;
	
	public SimulationWindow(int windowNumber, double startTime, double endTime, double deltaTime,
							int deltaBlocks, double avarageMineTimeTheory, double averageMineTimeEmpirical, 
							double fixRate)
	{
		this.windowNumber = windowNumber;
		this.startTime = startTime;
		this.endTime = endTime;
		this.deltaTime = deltaTime;
		this.deltaBlocks = deltaBlocks;
		this.avarageMineTimeTheory = avarageMineTimeTheory;
		this.averageMineTimeEmpirical = averageMineTimeEmpirical;
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
				"Theory Block Creation Time : " + String.format("%.4f",this.avarageMineTimeTheory) + "\n" +
				"Window Block Creation Time : " + String.format("%.4f",this.averageMineTimeEmpirical) + "\n" +
				"Fix Rate : " + String.format("%.4f",this.fixRate) + "\n";
		return res;
	}
	
	public String csvString()
	{
		String res = this.windowNumber + "," +
				String.format("%.4f",this.startTime) + "," +
				String.format("%.4f",this.endTime) + "," +
				String.format("%.4f",this.deltaTime) + "," +
				this.deltaBlocks + "," +
				String.format("%.4f",this.avarageMineTimeTheory) + "," +
				String.format("%.4f",this.averageMineTimeEmpirical) + "," +
				String.format("%.4f",this.fixRate) + "\n";
		return res;
	}
	
}
