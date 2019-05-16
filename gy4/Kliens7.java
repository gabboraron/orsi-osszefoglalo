
import java.io.*;
import java.util.*;
import java.net.*;

public class Kliens7 {
	static String MACHINE = "localhost";
	static int PORT = 12345;

	public static void main(String[] args) throws Exception {
		int sum = 0;

		try (
			Socket s = new Socket(MACHINE, PORT);
			Scanner sc = new Scanner(s.getInputStream());
			PrintWriter pw = new PrintWriter(s.getOutputStream());

			PrintWriter pwFile = new PrintWriter(new File("out.txt"));
		) {
			while (sc.hasNextInt()) {
				int port2 = sc.nextInt();

				sum += getNumbersFromSubServer(port2);

				pwFile.println(sum);
				// pwFile.flush();
			}
			pwFile.flush();
		}
	}

	static int getNumbersFromSubServer(int port) throws Exception {
		int sum = 0;
		try (
			Socket s = new Socket(MACHINE, port);
			Scanner sc = new Scanner(s.getInputStream());
		) {
			while (sc.hasNextInt()) {
				sum += sc.nextInt();
			}
		}

		return sum;
	}

}
