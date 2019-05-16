
import java.util.*;
import java.io.*;
import java.net.*;

public class MultiThreadClientV2 {
	public static void main(String[] args) throws Exception {
		try (
			Socket s = new Socket("localhost", 12345);
			Scanner sc = new Scanner(s.getInputStream());
			PrintWriter pw = new PrintWriter(s.getOutputStream());

			Scanner scIn = new Scanner(System.in);
		) {
			Thread t1 = makeCommunicationThread(scIn, pw);
			Thread t2 = makeCommunicationThread(sc, new PrintWriter(System.out));

			t1.start();
			t2.start();

			t1.join();
			t2.join();
		}
	}

	static Thread makeCommunicationThread(Scanner sc, PrintWriter pw) {
		return new Thread(() -> {
			while (sc.hasNextLine()) {
				String line = sc.nextLine();
				pw.println(line);
				pw.flush();
			}
		});
	}
}
