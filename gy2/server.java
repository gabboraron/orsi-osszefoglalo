
import java.io.*;
import java.util.*;
import java.net.*;

class server {
	public static void main(String[] args) throws Exception {
		// protokoll
			// interfesz
		// 0-65535 (1024-65535)
		int PORT = 12345;
		// try-with-resources (AutoCloseable)
		
		Vector<Integer> tmp = new Vector<Integer>();
		while (true){
			try (
				ServerSocket ss = new ServerSocket(PORT);
				Socket s = ss.accept();
				Scanner sc = new Scanner(s.getInputStream());
				PrintWriter pw = new PrintWriter(s.getOutputStream());
			) {
				while(sc.hasNextInt()){
					Integer nr = sc.nextInt();

					//if (nr != 0) {
						tmp.add(toInt(nr));					
					//} else {
						//answer(tmp, pw);
					//}
					//pw.println(toInt(nr));
					// pw.flush();
				}
				answer(tmp, pw);
			}
		}
	}

	public static void answer(Vector<Integer> tmp, PrintWriter pw){
		/*for (Integer nr : tmp) {
			pw.println(nr);
		}*/
		tmp.forEach((n) -> pw.println(n));
		pw.flush();
        tmp.clear();
	}

	public static Integer toInt(Integer nr){
		return 2*nr+1;
	}
}
