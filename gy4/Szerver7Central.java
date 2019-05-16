
import java.io.*;
import java.util.*;
import java.net.*;

public class Szerver7Central {
	public static void main(String[] args) throws Exception {
		int PORT = 12345;

		int[] ports = { 2222, 3333, 4444 };

		try (
			ServerSocket ss = new ServerSocket(PORT);
			Socket s = ss.accept();
			// Scanner sc = new Scanner(s.getInputStream());
			PrintWriter pw = new PrintWriter(s.getOutputStream());
		) {
			for (int port : ports) {
				pw.println(port);
				pw.flush();
			}
		}
	}

}
