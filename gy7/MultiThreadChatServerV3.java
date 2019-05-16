
import java.util.*;
import java.io.*;
import java.net.*;

public class MultiThreadChatServerV3 {
	public static void main(String[] args) throws Exception {
		Set<ClientData> otherClients = new HashSet<>();

		try (
			ServerSocket ss = new ServerSocket(12345);
		) {
			while (true) {
				ClientData client = new ClientData(ss);
				synchronized (otherClients) {
					otherClients.add(client);
				}

				new Thread(() -> {
					while (client.sc.hasNextLine()) {
						String line = client.sc.nextLine();

						synchronized (otherClients) {
							for (ClientData other : otherClients) {
								other.pw.println(line);
								other.pw.flush();
							}
						}
					}

					synchronized (otherClients) {
						otherClients.remove(client);
						try {
							client.close();
						} catch (Exception e) {
							// won't happen
						}
					}
				}).start();
			}
		}
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

