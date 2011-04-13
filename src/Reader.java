import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;


public class Reader {
	
	private BufferedReader br;
	private static Reader reader;
	
	private Reader() {
		br = new BufferedReader(new InputStreamReader(System.in));
	}
	
	public static Reader getInstance() {
		if (reader == null) 
			reader = new Reader();
		return reader;
	}
	
	public String read() {
		try {
			return br.readLine();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
		return null;
	}

}
