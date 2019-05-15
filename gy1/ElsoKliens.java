
import java.io.*;
import java.util.*;
import java.net.*;

class ElsoKliens {
	public static void main(String[] args) throws Exception {
		String MACHINE = "localhost";
		// String MACHINE = "127.0.0.1";
		// String MACHINE = "::1";
		int PORT = 12345;
		try (
			Socket s = new Socket(MACHINE, PORT);
			Scanner sc = new Scanner(s.getInputStream());
			PrintWriter pw = new PrintWriter(s.getOutputStream());
		) {
			pw.println("AzEnNevem");
			pw.flush(); //atkuldi az utolsó információt a szerverről

			String valasz = sc.nextLine();
			//sc.hasNextLine 	//várja következő sort vagy a halott servert
			//sc.hasNext 		//átugorja az össes whitespacet
			//sc.hasNextInt 	//átugorja a whitespaceket és számot vár válaszként

			System.out.println(valasz);
		}
	}
}
