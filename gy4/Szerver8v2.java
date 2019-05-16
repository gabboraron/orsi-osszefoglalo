
import java.io.*;
import java.util.*;
import java.net.*;

public class Szerver8v2 {
	public static void main(String[] args) throws Exception {
		int PORT = 12345;

		try (
			ServerSocket ss = new ServerSocket(PORT);
			ClientDescriptor client1 = new ClientDescriptor(ss);
			ClientDescriptor client2 = new ClientDescriptor(ss);
		) {
			while (true) {
				if (!client1.canSendMsg())   break;
				client1.sendMsg(client2);

				if (!client2.canSendMsg())   break;
				client2.sendMsg(client1);
			}
		}
	}
}


// AutoCloseable: ettől erőforrás ClientDescriptor
class ClientDescriptor implements AutoCloseable {
	Socket s;
	Scanner sc;
	PrintWriter pw;
	String name;

	public ClientDescriptor(ServerSocket ss) throws Exception {
		this.s  = ss.accept();
		this.sc = new Scanner(s.getInputStream());
		this.pw = new PrintWriter(s.getOutputStream());

		this.name = sc.nextLine();
	}

	public boolean canSendMsg() {
		return this.sc.hasNextLine();
	}

	public void sendMsg(ClientDescriptor other) {
		String line = this.sc.nextLine();
		other.pw.println(this.name + ": " + line);
		other.pw.flush();
	}

	public void close() throws Exception {
		if (s != null) {
			s.close();
		}
	}
}


