package Main;

import java.net.ServerSocket;

import Interfaces.IServer;


class Server implements IServer {

	public void run() {
		// TODO Auto-generated method stub
		System.out.println("The capitalization server is running.");
        int clientNumber = 0;
        //ServerSocket listener = new ServerSocket(9898);
        try {
            while (true) {
                //new Capitalizer(listener.accept(), clientNumber++).start();
                //System.out.println("herrree");
            }
        } finally {
          //  listener.close();
        }
		
	}

	public void handleRequest() {
		// TODO Auto-generated method stub
		
	}

	public void updateLog(String newData) {
		// TODO Auto-generated method stub
		
	}
	
}