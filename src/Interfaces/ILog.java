package Interfaces;

public interface ILog {
	 
	/**
	 * @param columnCount number of columns to be preserved in log
	 * @param titles the title of each column in order
	 * @return new log instance with specified parameters
	 */
	public ILog getInstance(int columnCount, String titles[]);
	
	/**
	 * if this function is called updates will be reserved until written when client exits
	 * if not called will write to the terminal and updates are write instantaneously to output stream
	 * @param filename log concatenated with the id of client
	 */
	public void setLogFile(String filename);
	
	/**
	 * add new piece of news to the log
	 * @param news array of strings where each string corresponds to a column respectively
	 */
	public void updateLog(String news[]);
}
