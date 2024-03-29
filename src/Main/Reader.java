/************Remove on copy***************/
package Main;

import Interfaces.*;
/**************************************/

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

public class Reader extends Thread implements IRequest{
	private Socket socket;
	private String readerID;
	private int seqNum;





	public Reader(Socket socket, String readerID , int seqNum) {
		this.socket = socket;
		this.readerID= readerID;
		this.seqNum = seqNum;
	}

	public void run() {
		 StringBuilder log = new StringBuilder();

		try {
			String readedNews = readData();

			PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

			out.println(readedNews);
			out.println(Integer.toString(seqNum));

			log.append(Integer.toString(seqNum));
			log.append("\t");
			log.append(readedNews);
			log.append("\t");
			log.append(readerID);
			log.append(Integer.toString(Server.numberOfReader));


		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
            try {
                socket.close();
                Server.numberOfReader--;

                while (true){
                	if (!Server.readerLog){
                		Server.readerLog = true;
                		Server.updateLogReader(new String(log));
                		break;
                	}else {
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

	public String readData() throws InterruptedException {
		// TODO Auto-generated method stub
		while (true){
			if (!Server.write){
				return Server.news;
			}else {
				Thread.sleep(1000);
			}
		}
	}

	public void writeData(String data) {
		// TODO Auto-generated method stub
		// not required here
	}



}
