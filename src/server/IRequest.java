
public interface IRequest {

	public String readData() throws InterruptedException;
	public void writeData(String data) throws InterruptedException;

}
