import java.io.*;
import java.util.*;
import java.net.*;

class Server {
	public static void main(String[] args) throws Exception {
		
		String[] commands = {"put", "list", "bid"};
		List<ItemOfAuction>	auction	 = new ArrayList<>();

		// protokoll
			// interfesz
		// 0-65535 (1024-65535)
		int PORT = 2121;
		// try-with-resources (AutoCloseable)
		
		Vector<Integer> tmp = new Vector<Integer>();
		while (true){
			try (
				ServerSocket ss = new ServerSocket(PORT);
				Socket s = ss.accept();
				Scanner sc = new Scanner(s.getInputStream());
				PrintWriter pw = new PrintWriter(s.getOutputStream());
			) {
				String name = sc.nextLine();
				while(sc.hasNextLine()){
					String currentCommand = sc.nextLine();
					
					Integer cIdx = 0; 
	   				boolean commandFound = false;
	   				while ((cIdx<commands.length) && (!commandFound)) {
	   					String possibleCommand = commands[cIdx];
						++cIdx;
						String commandPart = currentCommand.split(" ")[0];
						if (commandPart.equals(possibleCommand)) {
							if (possibleCommand.equals("put")) {
								ItemOfAuction item = new ItemOfAuction(currentCommand.split(" ")[1]);			
								auction.add(item);
							}

							if (possibleCommand.equals("list")) {
								pw.println(auction.size());
								pw.flush();
								for (ItemOfAuction item : auction) {
									pw.println(item);
									pw.flush();
								}
							}
							
							if (possibleCommand.equals("bid")) {
								for (ItemOfAuction item : auction) {
									if (item.name.equals(currentCommand.split(" ")[1])) {
										item.price = item.price + 1;
										item.winner = name;
										break;
									}
								}
							}
						}
	   				}


					//Integer nr = sc.nextInt();

					//if (nr != 0) {
						//tmp.add(toInt(nr));					
					//} else {
						//answer(tmp, pw);
					//}
					//pw.println(toInt(nr));
					// pw.flush();
				}
				//answer(tmp, pw);
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

class ItemOfAuction{
	public String 	name = "";
	public Integer 	price = 0;
	public String 	winner = "no one";
	
	public ItemOfAuction(String  n){
		name = n;
	}

	@Override
    public String toString()
    {
        return "name: " + name + "\tprice: " + String.valueOf(price) + "\thighest offer: " + winner;
    }
}