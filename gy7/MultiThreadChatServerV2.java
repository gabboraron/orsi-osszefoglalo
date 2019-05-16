
import java.util.*;
import java.io.*;
import java.net.*;

public class MultiThreadChatServerV2 {
	public static void main(String[] args) throws Exception {
		try (
			ServerSocket ss = new ServerSocket(12345);
			ClientData client1 = new ClientData(ss);
			ClientData client2 = new ClientData(ss);
		) {
			Thread t1 = makeCommunicationThread(client1, client2);
			Thread t2 = makeCommunicationThread(client2, client1);

			t1.start();
			t2.start();

			t1.join();
			t2.join();
		}
	}

	static Thread makeCommunicationThread(ClientData client1, ClientData client2) {
		return new Thread(() -> {
			while (client1.sc.hasNextLine()) {
				String line = client1.sc.nextLine();
				client2.pw.println(line);
				client2.pw.flush();
			}
		});
	}
}


class ClientData implements AutoCloseable {
	Socket s;
	Scanner sc;
	PrintWriter pw;

	ClientData(ServerSocket ss) throws Exception {
		s = ss.accept();
		sc = new Scanner(s.getInputStream());
		pw = new PrintWriter(s.getOutputStream());
	}

	public void close() throws Exception {
		if (s == null) return;
		s.close();
	}
}

