
import java.io.*;
import java.util.*;
import java.net.*;

public class Server {
	public static void main(String[] args) throws Exception {
		// protokoll
			// interfesz
		// 0-65535 (1024-65535)
		int PORT = 12345;
		ServerSocket ss = new ServerSocket(PORT);
		
		// try-with-resources (AutoCloseable)
		
		//Vector<Integer> tmp = new Vector<Integer>();
		while (true){
			try (
				Socket s1 = ss.accept();
				Socket s2 = ss.accept();
				
				Scanner sc1 = new Scanner(s1.getInputStream());
				PrintWriter pw1 = new PrintWriter(s1.getOutputStream());
				
				Scanner sc2 = new Scanner(s2.getInputStream());
				PrintWriter pw2 = new PrintWriter(s2.getOutputStream());
			) {
				
//				List<String> list = new ArrayList<String>();
//				String filename = sc.next();
//				readFromFile(list, filename);

				boolean idWasSent = false;
				String uid1="", uid2="", msg="";
				while(sc1.hasNextLine() || sc2.hasNextLine()){
					if(idWasSent){
						msg = sc1.nextLine();
						sndMsg(msg, pw2, uid1);

						msg = sc2.nextLine();
						sndMsg(msg, pw1, uid2);
					}else{
						uid1 = sc1.next();						
						uid2 = sc2.next();
						idWasSent = true;
					}

					if(!sc1.hasNextLine()){
						pw2.println(uid1 + " has disconnected, waiting for users...");
						idWasSent = false;
					}
					
					if(!sc2.hasNextLine()){
						pw1.println(uid2 + " has disconnected, waiting for users...");
						idWasSent = false;
					}
				}

			}
		}
	}

	public static void sndMsg(String msg, PrintWriter pw, String senderID){
            /*for (nr : tmp) {
		pw.println(nr);
            }*/
            pw.println(senderID + ": " + msg);
            System.out.println(senderID + ": " + msg);
            pw.flush();
	}
}