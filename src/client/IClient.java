
import java.io.BufferedReader;
import java.io.PrintWriter;

public interface IClient {

	public void doOperation(BufferedReader in , PrintWriter out);
	public void run(String[] args);
	public void writeLog(String value , String seqNum , String rSeq);
	public void openLogsFile();
}
