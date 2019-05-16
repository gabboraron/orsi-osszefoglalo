
import java.io.*;
import java.util.*;
import java.net.*;

public class Szerver3 {
	public static void main(String[] args) throws Exception {
		int PORT = 12345;

		try (
			ServerSocket ss = new ServerSocket(PORT);
			Socket s = ss.accept();
			Scanner sc = new Scanner(s.getInputStream());
			PrintWriter pw = new PrintWriter(s.getOutputStream());
		) {
			v1(sc, pw);
			// v2(sc, pw);
		}
	}

	static void v1(Scanner sc, PrintWriter pw) {
		while (sc.hasNextInt()) {
			int number = sc.nextInt();

			if (number == 0)   break;

			int retval = f(number);

			pw.println(retval);
			// pw.flush();
		}

		pw.flush();
	}

	static void v2(Scanner sc, PrintWriter pw) {
		// int[], Integer[], List<Integer>, Set<Integer>, Map<Integer, ..>

		List<Integer> input = getNumbers(sc);
		List<Integer> output = transform(input);
		sendReply(output, pw);

		// sendReply(transform(getNumbers(sc)), pw);
	}

	static List<Integer> getNumbers(Scanner sc) {
		List<Integer> retval = new ArrayList<>();

		while (sc.hasNextLine()) {
			String line = sc.nextLine();

			// if (line == "0")    break;		// rossz
			// if ("0".equals(line))    break;
			if (line.equals("0"))    break;

			int number = Integer.parseInt(line);
			retval.add(number);
		}

		return retval;
	}

	static List<Integer> transform(List<Integer> input) {
		List<Integer> retval = new ArrayList<>();

		for (int number : input) {
			retval.add(f(number));
		}

		return retval;
	}

	static void sendReply(List<Integer> output, PrintWriter pw) {
		for (int number : output) {
			pw.println(number);
		}
		pw.flush();
	}

	static int f(int n) {
		return 3 * n + 1;
	}
}
