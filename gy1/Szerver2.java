
import java.io.*;
import java.util.*;
import java.net.*;

public class Szerver2 {
	public static void main(String[] args) throws Exception {
		int PORT = 12345;

		String toRepeat = "a";

		try (
			ServerSocket ss = new ServerSocket(PORT);
			Socket s = ss.accept();
			Scanner sc = new Scanner(s.getInputStream());
			PrintWriter pw = new PrintWriter(s.getOutputStream());
		) {
			int repeatCount = sc.nextInt();

			// for (int i = 0; i < repeatCount; ++i) {
			//	pw.print(toRepeat);
			// }
			// pw.println();
			// pw.flush();

			String msg = toRepeat;
			for (int i = 1; i < repeatCount; ++i) {
				// msg += msg;
				msg += toRepeat;
			}

			pw.println(msg);
			pw.flush();
		}
	}
}
