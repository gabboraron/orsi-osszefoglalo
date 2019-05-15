
import java.io.*;
import java.util.*;
import java.net.*;

class ElsoSzerver {
	public static void main(String[] args) throws Exception {
		// protokoll
			// interfesz
		// 0-65535 (1024-65535)
		int PORT = 12345;
		// try-with-resources (AutoCloseable)
		try (
			ServerSocket ss = new ServerSocket(PORT);
			Socket s = ss.accept(); // blokkoló hívás, addig vár amíg egy kliens meg nem jelenik, akár évekig...
			Scanner sc = new Scanner(s.getInputStream());
			PrintWriter pw = new PrintWriter(s.getOutputStream());
		) {//erőforrás kezelő try blokk
			String name = sc.nextLine();

			pw.println("Hello, " + name);
			pw.flush();
		} //itt szabadíja fel a lefogalalt erőforrásokat
	}
}
