package Main;

import com.jcraft.jsch.*;
import java.awt.*;
import javax.swing.*;
import java.io.*;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.HashMap;

public class Start {

	public static void main(String[] arg) {
		/*
		 * 1-read configuration file and fill hashMap with values 2-using
		 * hashmap start server thread on local machine by change directory to
		 * server folder on desktop, compiling and running using make file send
		 * to server number of readers and number of writers 3-use ssh to log in
		 * other hosts as specified in config file, change directory to client
		 * folder on desktop, compile and run using make file
		 */

		Process p;
		String command, s, args;
		int rdCount, wrCount;

		// read configuration
		ConfigFileHandler fh = new ConfigFileHandler();
		HashMap<String, String> props = fh.readConfiguration();
		printConfigFile(props);

		// run server
		// cd to server folder on desktop
		makeScript(true); // true server
		// append server args
		try {
			args = "";
			args += " ";
			args += props.get("srvPort");
			args += " ";
			args += props.get("rdCount");
			args += " ";
			args += props.get("wrCount");

			Files.write(Paths.get("srv_script"), args.getBytes(),
					StandardOpenOption.APPEND);
		} catch (IOException e) {
			// exception handling left as an exercise for the reader
		}

		ServerThread srvTh = new ServerThread();
		Thread myThread = new Thread(srvTh);
		myThread.start();
		// try {
		// Runtime.getRuntime().exec("chmod 755 srv_script");
		// p = Runtime.getRuntime().exec("./srv_script");
		// BufferedReader stdInput = new BufferedReader(new
		// InputStreamReader(p.getInputStream()));
		//
		// BufferedReader stdError = new BufferedReader(new
		// InputStreamReader(p.getErrorStream()));
		//
		// // read the output from the command
		// System.out.println("Here is the standard output of the command:\n");
		// while ((s = stdInput.readLine()) != null) {
		// System.out.println(s);
		// }
		//
		// // read any errors from the attempted command
		// System.out.println("Here is the standard error of the command (if any):\n");
		// while ((s = stdError.readLine()) != null) {
		// System.out.println(s);
		// }
		// } catch (IOException e) {
		// e.printStackTrace();
		// }
		System.out.println("Samor");
		// execute clients
		rdCount = Integer.parseInt(props.get("rdCount"));
		wrCount = Integer.parseInt(props.get("wrCount"));

		try {
			Thread.sleep(1000);
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		ReadersFactory rf = new ReadersFactory(rdCount, props);
		WritersFactory wf = new WritersFactory(wrCount, rdCount, props);
		
		Thread readersThread = new Thread(rf);
		Thread writersThread = new Thread(wf);
		
		readersThread.start();
		writersThread.start();
		
//		for (int i = 0; i < rdCount; i++) {
//			String temp, user, host, password;
//			temp = props.get("rd" + i);
//			user = temp.substring(0, temp.indexOf('@'));
//			host = temp.substring(temp.indexOf('@') + 1);
//			password = props.get("rdPass" + i);
//			makeScript(false);
//			// append args
//			
//			String commands[] = { "cd $HOME/Desktop/client;pwd; javac *.java;java MyClient" };
//			args = commands[0];
//			args += " ";
//			args += props.get("srvIp");
//			args += " ";
//			args += props.get("srvPort");
//			args += " ";
//			args += i; // TODO: fix id
//			args += " ";
//			args += props.get("acCount");
//			args += " ";
//			args += "r";
//			commands[0] = args;
//			/*
//			 * transfer client script file to remote in client folder cd to
//			 * client folder execute script file
//			 */
//			createClient(user, host, password, commands);
//
//		}

//		for (int i = 0; i < wrCount; i++) {
//			String temp, user, host, password;
//			int id = rdCount;
//			temp = props.get("wr" + i);
//			user = temp.substring(0, temp.indexOf('@'));
//			host = temp.substring(temp.indexOf('@') + 1);
//			password = props.get("wrPass" + i);
//			makeScript(false);
//			// append args
//
//			String commands[] = { "cd $HOME/Desktop/client; pwd; javac *.java;java MyClient" };
//			args = commands[0];
//			args += " ";
//			args += props.get("srvIp");
//			args += " ";
//			args += props.get("srvPort");
//			args += " ";
//			args += id; // TODO: fix id
//			args += " ";
//			args += props.get("acCount");
//			args += " ";
//			args += "w";
//			commands[0] = args;
//			/*
//			 * transfer client script file to remote in client folder cd to
//			 * client foldere execute script file
//			 */
//			createClient(user, host, password, commands);
//
//		}

	}

	

	private static void createClient(String user, String host, String password,
			String commands[]) {
		Session session = null;
		try {
			JSch jsch = new JSch();
			session = jsch.getSession(user, host, 22);
			session.setPassword(password);
			java.util.Properties config = new java.util.Properties();
			config.put("StrictHostKeyChecking", "no");
			session.setConfig(config);
			session.connect();
			for (int it = 0; it < commands.length; it++) {
				Channel channel = session.openChannel("exec");
				((ChannelExec) channel).setCommand(commands[it]);

				channel.setInputStream(null);

				((ChannelExec) channel).setErrStream(System.err);

				InputStream in = channel.getInputStream();

				channel.connect();

				byte[] tmp = new byte[1024];
				while (true) {
					while (in.available() > 0) {
						int i = in.read(tmp, 0, 1024);
						if (i < 0)
							break;
						System.out.print(new String(tmp, 0, i));
					}
					if (channel.isClosed()) {
						if (in.available() > 0)
							continue;
						System.out.println("exit-status: "
								+ channel.getExitStatus());
						break;
					}
					try {
						Thread.sleep(1000);
					} catch (Exception ee) {
					}
				}
				channel.disconnect();
			}
			session.disconnect();

		} catch (Exception e) {

		}

	}

	private static void makeScript(boolean type) {
		String file1, file2;
		if (type) {
			file1 = "srv_temp";
			file2 = "srv_script";
		} else {
			file1 = "clt_temp";
			file2 = "clt_script";
		}
		try {
			FileChannel src = new FileInputStream(file1).getChannel();
			FileChannel dest = new FileOutputStream(file2).getChannel();
			dest.transferFrom(src, 0, src.size());
			src.close();
			dest.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private static void printConfigFile(HashMap<String, String> props) {
		for (String key : props.keySet()) {
			System.out.println(key + " " + props.get(key));
		}
	}

	static class ServerThread implements Runnable {
		public void run() {
			String s;
			Process p;
			try {
				Runtime.getRuntime().exec("chmod 755 srv_script");
				p = Runtime.getRuntime().exec("./srv_script");
				BufferedReader stdInput = new BufferedReader(
						new InputStreamReader(p.getInputStream()));

				BufferedReader stdError = new BufferedReader(
						new InputStreamReader(p.getErrorStream()));

				// read the output from the command
				System.out
						.println("Here is the standard output of the command:\n");
				while ((s = stdInput.readLine()) != null) {
					System.out.println(s);
				}

				// read any errors from the attempted command
				System.out
						.println("Here is the standard error of the command (if any):\n");
				while ((s = stdError.readLine()) != null) {
					System.out.println(s);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	static class ReadersFactory implements Runnable{

		private int rdCount;
		HashMap<String, String> props;
		
		public ReadersFactory(int rdCount, HashMap<String, String> props) {
			this.props = props;
			this.rdCount = rdCount;
		}
		
		@Override
		public void run() {
			for (int i = 0; i < rdCount; i++) {
				String temp, user, host, password, args;
				
				temp = props.get("rd" + i);
				user = temp.substring(0, temp.indexOf('@'));
				host = temp.substring(temp.indexOf('@') + 1);
				password = props.get("rdPass" + i);

				String commands[] = { "cd $HOME/Desktop/client;pwd; javac *.java;java MyClient" };
				
				args = commands[0];
				args += " ";
				args += props.get("srvIp");
				args += " ";
				args += props.get("srvPort");
				args += " ";
				args += i; // TODO: fix id
				args += " ";
				args += props.get("acCount");
				args += " ";
				args += "r";
				commands[0] = args;
								
				waitRndTime();
				
				createReader(user, host, password, commands);

			}

		}
		
		private void createReader(String user,String host, String password, String [] commands) {
			ClientFactory cf = new ClientFactory(user, host, password, commands);
			Thread myThread = new Thread(cf);
			myThread.start();
		}
		
		private void waitRndTime() {
			try {
				long time = (long) (Math.random() * 10000);
				System.out.println("time sleep start readerfactory " + time);
				Thread.sleep(time);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}
	
	static class WritersFactory implements Runnable{

		private int wrCount, rdCount;
		HashMap<String, String> props;
		
		public WritersFactory(int wrCount, int rdCount, HashMap<String, String> props) {
			this.props = props;
			this.wrCount = wrCount;
			this.rdCount = rdCount;
		}
		
		@Override
		public void run() {
			for (int i = 0; i < wrCount; i++) {
				String temp, user, host, password, args;
				int id = rdCount;
				temp = props.get("wr" + i);
				user = temp.substring(0, temp.indexOf('@'));
				host = temp.substring(temp.indexOf('@') + 1);
				password = props.get("wrPass" + i);
				// append args

				String commands[] = { "cd $HOME/Desktop/client; pwd; javac *.java;java MyClient" };
				args = commands[0];
				args += " ";
				args += props.get("srvIp");
				args += " ";
				args += props.get("srvPort");
				args += " ";
				args += id; // TODO: fix id
				args += " ";
				args += props.get("acCount");
				args += " ";
				args += "w";
				commands[0] = args;
				
				waitRndTime();
				
				createWriter(user, host, password, commands);

			}

		}
		
		private void createWriter(String user,String host, String password, String [] commands) {
			ClientFactory cf = new ClientFactory(user, host, password, commands);
			Thread myThread = new Thread(cf);
			myThread.start();
		}
		
		private void waitRndTime() {
			try {
				long time = (long) (Math.random() * 10000);
				System.out.println("time sleep start writerfactory " + time);
				Thread.sleep(time);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	
	private static class ClientFactory implements Runnable {

		private String user, host, password, commands[];

		public ClientFactory(String user, String host, String password, String commands[]) {
			this.user = user;
			this.host = host;
			this.password = password;
			this.commands = commands;
		}
		
		@Override
		public void run() {
			createClient(user, host, password, commands);
		}
		
	}

	private static class ConfigFileHandler {
		private static final String CONFIG_FILE = "system.properities";

		/**
		 * read configuration file system.properities and sets global variables
		 */
		public HashMap<String, String> readConfiguration() {
			HashMap<String, String> props = new HashMap<String, String>();
			BufferedReader bf = null;

			try {
				bf = new BufferedReader(new FileReader(CONFIG_FILE));
				String sCurrentLine;

				while ((sCurrentLine = bf.readLine()) != null) {
					System.out.println(sCurrentLine);
					int splitIndex = sCurrentLine.indexOf('=');
					String key = sCurrentLine.substring(0, splitIndex);
					String value = sCurrentLine.substring(splitIndex + 1);
					key = reformulateKeyString(key);
					props.put(key, value);
				}
				bf.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			return props;
		}

		private String reformulateKeyString(String key) {
			char num;
			String res;
			boolean hasNum;
			HashMap<String, String> mappingKeys;

			mappingKeys = getMappingKeys();
			hasNum = checkStrHasNo(key);

			if (hasNum) {
				num = key.charAt(key.length() - 1);
				res = mappingKeys.get(key.substring(0, key.length() - 1));
				res += num;
			} else {
				res = mappingKeys.get(key);
			}
			return res;
		}

		private HashMap<String, String> getMappingKeys() {
			HashMap<String, String> mappingKeys = new HashMap<String, String>();
			mappingKeys.put("RW.server", "srvIp");
			mappingKeys.put("RW.server.port", "srvPort");
			mappingKeys.put("RW.numberOfReaders", "rdCount");
			mappingKeys.put("RW.numberOfWriters", "wrCount");
			mappingKeys.put("RW.numberOfAccesses", "acCount");
			mappingKeys.put("RW.reader", "rd");
			mappingKeys.put("RW.writer", "wr");
			mappingKeys.put("RW.password.reader", "rdPass");
			mappingKeys.put("RW.password.writer", "wrPass");
			return mappingKeys;
		}

		private boolean checkStrHasNo(String txt) {
			return txt.matches(".*\\d+.*");
		}
	}

}
