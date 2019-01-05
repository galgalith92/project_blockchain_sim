package blockchain;

public class TheorySimulationWindow {

	private int windowNumber;
	private double startTime;
	private double endTime;
	private double deltaTime;
	private int deltaBlocks;
	private double avarageMineTime;
	private double fixRate;

	public TheorySimulationWindow(int windowNumber, double startTime, double endTime, double deltaTime, int deltaBlocks,
			double avarageMineTime, double fixRate) {
		this.windowNumber = windowNumber;
		this.startTime = startTime;
		this.endTime = endTime;
		this.deltaTime = deltaTime;
		this.deltaBlocks = deltaBlocks;
		this.avarageMineTime = avarageMineTime;
		this.fixRate = fixRate;
	}
	
	public String csvString()
	{
		String res = this.windowNumber + "," +
				String.format("%.4f",this.startTime) + "," +
				String.format("%.4f",this.endTime) + "," +
				String.format("%.4f",this.deltaTime) + "," +
				this.deltaBlocks + "," +
				String.format("%.4f",this.avarageMineTime) + "," +
				String.format("%.4f",this.fixRate) + "," +
				Blockchain.getTotalMinersMachineNumber() + "\n";
		return res;
	}
	
}
