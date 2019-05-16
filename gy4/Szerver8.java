
import java.io.*;
import java.util.*;
import java.net.*;

public class Szerver8 {
	public static void main(String[] args) throws Exception {
		int PORT = 12345;

		try (
			ServerSocket ss = new ServerSocket(PORT);
				ServerSocket ss2 = new ServerSocket(54321);

			Socket s1 = ss.accept();
			Scanner sc1 = new Scanner(s1.getInputStream());
			PrintWriter pw1 = new PrintWriter(s1.getOutputStream());

				Socket s2 = ss2.accept();
			//Socket s2 = ss.accept();
			Scanner sc2 = new Scanner(s2.getInputStream());
			PrintWriter pw2 = new PrintWriter(s2.getOutputStream());
		) {
			String name1 = sc1.nextLine();
			String name2 = sc2.nextLine();

			while (true) {
				if (!sc1.hasNextLine())   break;
				sendMsg(name1, sc1, pw2);

				if (!sc2.hasNextLine())   break;
				sendMsg(name2, sc2, pw1);
			}
		}
	}

	static void sendMsg(String name, Scanner sc, PrintWriter pw) {
		String line = sc.nextLine();
		pw.println(name + ": " + line);
		pw.flush();
	}

}
