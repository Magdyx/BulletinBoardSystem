
import java.net.Socket;

public interface IServer {

	public void run(String[] args) throws Exception;
	public void readNews();
	public void handleRequest(Socket socket);
	public void openLogsFile();
}
