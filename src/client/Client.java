
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;


public class Client implements IClient {

	private String serverIP;
	private int serverPort;
	private String clientID;
	private int numberOfAccess;
	private boolean readOrWriter; // false if read
	private static BufferedWriter bwLogClient;
	private FileWriter fwLogClient;

	public void doOperation(BufferedReader in , PrintWriter out) {
		if (!readOrWriter){ // reader
			StringBuilder sb = new StringBuilder();
			sb.append("r\n");
			sb.append(clientID);
			out.println(new String(sb));

			try {
				String value  = in.readLine();
				String seqNum = in.readLine();
				String rSeq = in.readLine();
				System.out.println("hehere" + value + "\nsamir " + seqNum + "\nrSeq " + rSeq);
				writeLog(value  , rSeq ,  seqNum );
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}else { // writer
			StringBuilder sb = new StringBuilder();
			sb.append("w\n");
			sb.append(clientID);
			sb.append("\n");
			sb.append(clientID);
			out.println(new String(sb));
			String seqNum;
			try {
				seqNum = in.readLine();
			    String rSeq = in.readLine();
				writeLog("" , rSeq ,  seqNum );
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public void run(String[] args) {
		// TODO Auto-generated method stub
		serverIP = args[0];
		serverPort = Integer.parseInt(args[1]);
		clientID = args[2];
		numberOfAccess = Integer.parseInt(args[3]);
    String temp = args[4];

    if (temp.equals("r")){
    	readOrWriter = false;
    }else {
    	readOrWriter = true;
    }


		Socket socket = null;
		try {
			openLogsFile();

			while (numberOfAccess != 0){

				socket = new Socket (serverIP , serverPort);

				BufferedReader in = new BufferedReader(
		                new InputStreamReader(socket.getInputStream()));
				PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

				doOperation(in , out);

				numberOfAccess--;

				// sleep
				try {
					socket.close();
					if (numberOfAccess != 0){
						long time = (long) (Math.random() * 10000);
						System.out.println("time sleep client type " + temp + " id " + clientID + " " + time);
						 Thread.sleep(time);
					}
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			try {
				if (numberOfAccess != 0)socket.close();
				if (bwLogClient != null)
					bwLogClient.close();
				if (fwLogClient != null)
					fwLogClient.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}


	}

	public void writeLog(String value , String seqNum , String rSeq) {
		// TODO Auto-generated method stub
		if (!readOrWriter){//reader
			String newData = rSeq+"\t"+seqNum+"\t"+value+"\n";
			try {
				Files.write(Paths.get("log"+clientID+".txt"), newData.getBytes(), StandardOpenOption.APPEND);
			}catch (IOException e) {
				e.printStackTrace();
			}
		}else {
			String newData = rSeq+"\t"+seqNum+"\n";
			try {
				Files.write(Paths.get("log"+clientID+".txt"), newData.getBytes(), StandardOpenOption.APPEND);
			}catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public void openLogsFile() {
		// TODO Auto-generated method stub

		final String FILENAMEREADER = "log" + clientID +".txt";

		try {
			fwLogClient = new FileWriter(FILENAMEREADER);
			bwLogClient = new BufferedWriter(fwLogClient);
			if (!readOrWriter){ // reader
				bwLogClient.write("rSeq\tsSeq\toVal\n");
			}else{
				bwLogClient.write("rSeq\tsSeq\n");
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			try {
				if (bwLogClient != null)
					bwLogClient.close();
				if (fwLogClient != null)
					fwLogClient.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}


	}

}
