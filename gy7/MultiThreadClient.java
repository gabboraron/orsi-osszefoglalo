
import java.util.*;
import java.io.*;
import java.net.*;

public class MultiThreadClient {
	public static void main(String[] args) throws Exception {
		try (
			Socket s = new Socket("localhost", 12345);
			Scanner sc = new Scanner(s.getInputStream());
			PrintWriter pw = new PrintWriter(s.getOutputStream());

			Scanner scIn = new Scanner(System.in);
		) {
			// user -> srv
			Thread t1 = new Thread(() -> {
				while (scIn.hasNextLine()) {
					String line = scIn.nextLine();
					pw.println(line);
					pw.flush();
				}
				// pw.flush();
			});

			// srv -> user
			Thread t2 = new Thread(() -> {
				while (sc.hasNextLine()) {
					String line = sc.nextLine();
					System.out.println(line);
					// System.out.flush();
				}
			});

			t1.start();
			t2.start();

			t1.join();
			t2.join();
		}
	}
}
