package Interfaces;

public interface Server {

	public void run();
	public void handleRequest();
	public void updateLog(String newData);
}
