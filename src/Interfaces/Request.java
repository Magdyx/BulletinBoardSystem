package Interfaces;

public interface Request extends Runnable {
	public void readData();
	public void writeData(String data);
}
