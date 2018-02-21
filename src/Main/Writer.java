package Main;

import Interfaces.ILog;
import Interfaces.IRequest;

public class Writer extends Thread implements IRequest {

	public void run() {
		// TODO Auto-generated method stub
		
	}

	public void readData() {
		// TODO Auto-generated method stub
		
	}

	public void writeData(String data) {
		// TODO Auto-generated method stub
		
	}

	
	private class WriterLog implements ILog {

		public ILog getInstance(int columnCount, String[] titles) {
			// TODO Auto-generated method stub
			return null;
		}

		public void setLogFile(String filename) {
			// TODO Auto-generated method stub
			
		}

		public void updateLog(String[] news) {
			// TODO Auto-generated method stub
			
		}
		
	}
}
