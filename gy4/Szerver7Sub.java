
import java.io.*;
import java.util.*;
import java.net.*;

public class Szerver7Sub {
	public static void main(String[] args) throws Exception {
		int PORT = Integer.parseInt(args[0]);

		int[] numbers = { PORT, PORT * 2 };

		try (
			ServerSocket ss = new ServerSocket(PORT);
			Socket s = ss.accept();
			// Scanner sc = new Scanner(s.getInputStream());
			PrintWriter pw = new PrintWriter(s.getOutputStream());
		) {
			for (int number : numbers) {
				pw.println(number);
			}
			pw.flush();
		}
	}

}
