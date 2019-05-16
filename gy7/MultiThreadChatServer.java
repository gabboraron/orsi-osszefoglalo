
import java.util.*;
import java.io.*;
import java.net.*;

public class MultiThreadChatServer {
	public static void main(String[] args) throws Exception {
		try (
			ServerSocket ss = new ServerSocket(12345);

			Socket s = ss.accept();
			Scanner sc = new Scanner(s.getInputStream());
			PrintWriter pw = new PrintWriter(s.getOutputStream());

			Socket s2 = ss.accept();
			Scanner sc2 = new Scanner(s2.getInputStream());
			PrintWriter pw2 = new PrintWriter(s2.getOutputStream());
		) {
			Thread t1 = makeCommunicationThread(sc, pw2);
			Thread t2 = makeCommunicationThread(sc2, pw);

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
