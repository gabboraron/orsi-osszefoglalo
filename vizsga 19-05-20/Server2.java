import java.io.*;
import java.util.*;
import java.net.*;
import java.util.concurrent.TimeUnit;

class Server2 {
	public static void main(String[] args) throws Exception {
		
		String[] commands = {"put", "list", "bid"};
		List<ItemOfAuction>	auction	 = new ArrayList<>();
		Set<ClientData> otherClients = new HashSet<>();

		// protokoll
			// interfesz
		// 0-65535 (1024-65535)
		int PORT = 2121;
		// try-with-resources (AutoCloseable)
		
		Vector<Integer> tmp = new Vector<Integer>();
			try (
				ServerSocket ss = new ServerSocket(PORT);
				Socket s = ss.accept();
				Scanner sc = new Scanner(s.getInputStream());
				PrintWriter pw = new PrintWriter(s.getOutputStream());
			) {
				while (true){
					ClientData client = new ClientData(ss);
					synchronized (otherClients) {
						otherClients.add(client);
					}

					new Thread(() -> {
						String name = client.sc.nextLine();
						client.name = name;
						System.out.println(client.name + " has connected!");	//LOG
						client.pw.println("Welcome!");	//LOG
						while(client.sc.hasNextLine()){
							String currentCommand = client.sc.nextLine();
							
							Integer cIdx = 0; 
			   				boolean commandFound = false;
			   				while ((cIdx<commands.length) && (!commandFound)) {
			   					String possibleCommand = commands[cIdx];
								++cIdx;
								String commandPart = currentCommand.split(" ")[0];
								if (commandPart.equals(possibleCommand)) {
									if (possibleCommand.equals("put")) {
										ItemOfAuction item = new ItemOfAuction(currentCommand.split(" ")[1]);			
										synchronized (auction){
											auction.add(item);
											
											//countdown
											new Thread(() -> {
										    	while(item.elapsedTime<30){
													try {
											    		TimeUnit.SECONDS.sleep(1);
											    		item.elapsedTime = item.elapsedTime + 1;
											    		//System.out.println(item.name + " time: " + String.valueOf(item.elapsedTime));	//LOG
											    	} catch (InterruptedException e) {
												        System.err.println("TimeUnit ERROR");
												        e.printStackTrace();
												    }
										    	}
										    	System.out.println("sold out: " + item.name);	//LOG
										    	//Send remainder for the winner
										    	synchronized (otherClients) {
												for (ClientData tmpClient : otherClients) {
														if(tmpClient.name.equals(item.winner)){
															tmpClient.pw.println("you have won: " + item.name);
															tmpClient.pw.flush();
														}
													}
												}	//sync otherclients
											}).start();
										}
									}

									if (possibleCommand.equals("list")) {
										synchronized (auction){
											client.pw.println(auction.size());
											client.pw.flush();
											
											for (ItemOfAuction item : auction) {
												if(item.elapsedTime<30){	//don't show the expired items
													client.pw.println(item);
													client.pw.flush();
												}
											}
										}
									}
									
									if (possibleCommand.equals("bid")) {
										synchronized (auction){
											for (ItemOfAuction item : auction) {
												if (item.name.equals(currentCommand.split(" ")[1])) {
													if(item.elapsedTime<30){
														item.price = item.price + 1;
														item.winner = name;
														break;
													}
												}
											}
										}
									}
								}	//if possible command
			   				}	//while command
						}	//while hasnextline

						synchronized (otherClients) {
							otherClients.remove(client);
							try {
								client.close();
							} catch (Exception e) {
								// won't happen
							}
						}
					}).start();	
			}	//while true 	
		}	//try
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
	public Integer 	elapsedTime = 0;
	
	public ItemOfAuction(String  n){
		name = n;
	}

	@Override
    public String toString()
    {
        return "name: " + name + "\tprice: " + String.valueOf(price) + "\thighest offer: " + winner + "\telapsed time: " + String.valueOf(elapsedTime);
    }
}

class ClientData implements AutoCloseable {
	public String name = "";
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