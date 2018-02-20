package Interfaces;

public interface IRequest extends Runnable {
	
	public void readData();
	public void writeData(String data);
	
}
