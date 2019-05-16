
import java.io.*;
import java.util.*;
import java.net.*;

public class Szerver4 {
	public static void main(String[] args) throws Exception {
		int PORT = 12345;

		try (
			ServerSocket ss = new ServerSocket(PORT);
		) {
			while (true) {
				try (
					Socket s = ss.accept();
					Scanner sc = new Scanner(s.getInputStream());
					PrintWriter pw = new PrintWriter(s.getOutputStream());
				) {
					simpleFtp(sc, pw);
				}
			}
		}
	}

	static void simpleFtp(Scanner sc, PrintWriter pw) {
		String filename = sc.nextLine();

		try (Scanner scFile = new Scanner(new File(filename))) {
			while (scFile.hasNextLine()) {
				String line = scFile.nextLine();
				pw.println(line);
			}
		} catch (IOException e) {
			pw.println("Error: " + e);
		} finally {
			pw.flush();
		}
	}

}
