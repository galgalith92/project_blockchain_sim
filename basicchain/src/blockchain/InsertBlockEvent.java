package blockchain;

public class InsertBlockEvent extends Event
{
	String transaction;
	String lastBlockHash;
	int challengeSol;
	
	InsertBlockEvent(long time,String transaction,int challengeSol)
	{
		super(time);
		this.transaction = transaction;
		this.challengeSol = challengeSol;
		this.lastBlockHash = Blockchain.blockchain.get(Blockchain.blockchain.size()-1).hash;
	}
	
	@Override
	public void processEvent() throws InterruptedException 
	{
		Blockchain.blockchain.add(new Block(transaction,lastBlockHash, challengeSol));
	}

}
