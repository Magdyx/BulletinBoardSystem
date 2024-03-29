package Main;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import Interfaces.IRequest;

public class Writer extends Thread implements IRequest {
	
	private Socket socket;
	private String writerID;
	private String value;
	private int seqNum;
	
	
	public Writer(Socket socket, String readerID, String value , int seqNum) {
		this.socket = socket;
		this.writerID = readerID;
		this.value = value;
		this.seqNum = seqNum;
	}
	

	public void run() {
		// TODO Auto-generated method stub
		StringBuilder log = new StringBuilder(); 
		
		log.append(Integer.toString(seqNum));
		log.append("\t");
		log.append(value);
		log.append("\t");
		log.append(writerID);
		
		try {
			PrintWriter out;
			try {
				out = new PrintWriter(socket.getOutputStream(), true);
				writeData(value);
				out.println(Integer.toString(seqNum));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
            try {
                socket.close();
                Server.write = false;
                Server.numberOfWriter--;
                
                while(true){
        			if (!Server.writerLog){
        				Server.writerLog = true;
        				Server.updateLogWriter(new String(log));
        			}else{
        				try {
							Thread.sleep(1000);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
        			}
        		}
                
            } catch (IOException e) {
               // error happen
            }
            // finish
        }
		
		
	}

	public String readData() throws InterruptedException{
		// TODO Auto-generated method stub
		return null;
	}

	public void writeData(String data) throws InterruptedException {
		// TODO Auto-generated method stub
		while(true){
			if (!Server.write){
				Server.write = true;
				Server.news = value;
				return;
			}else{
				Thread.sleep(1000);
			}
		}
	}

	
	
}
