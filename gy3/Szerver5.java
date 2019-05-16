
import java.io.*;
import java.util.*;
import java.net.*;

public class Szerver5 {
	public static void main(String[] args) throws Exception {
		int PORT = 12345;

		int counter = 1;

		try (
			ServerSocket ss = new ServerSocket(PORT);
		) {
			while (true) {
				try (
					Socket s = ss.accept();
					// Scanner sc = new Scanner(s.getInputStream());
					PrintWriter pw = new PrintWriter(s.getOutputStream());
				) {
					pw.println(counter);
					pw.flush();
				}

				++counter;
			}
		}
	}

}
