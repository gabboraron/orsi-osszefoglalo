import java.io.*;
import java.util.*;
import java.net.*;

class Szerver {
	public static void main(String[] args) throws Exception {
		
		HashMap<String, Cikk> news = new HashMap<>();
		Set<ClientData> otherWriters = new HashSet<>();
		Set<ClientData> otherReaders = new HashSet<>();

		Runnable r1 = new ServerSocketReader(Integer.parseInt(args[0]));
		Runnable r2 = new ServerSocketWriter(Integer.parseInt(args[1]));
		 
		new Thread(() -> {
			
		}).start();
		new Thread(r2).start();
	} 
}
		class ServerSocketReader implements Runnable{
		    // constructor
		     
		    public void run() {
		        while(true) {
		            Socket s = serverSocket.accept();
		            Runnable r = new SocketHandler(s);
		            new Thread(r);
		        }
		    }
		}


		// protokoll
			// interfesz
		// 0-65535 (1024-65535)
		try (
				//writer
				int PORT = Integer.parseInt(args[0]);
				ServerSocket ss = new ServerSocket(PORT);
				Socket s = ss.accept();
				Scanner sc = new Scanner(s.getInputStream());
				PrintWriter pw = new PrintWriter(s.getOutputStream());

				//reader
				int PORT2 = Integer.parseInt(args[1]);
				ServerSocket ssReader = new ServerSocket(PORT2);
				Socket sR = ssReader.accept();
				Scanner scR = new Scanner(sR.getInputStream());
				PrintWriter pwR = new PrintWriter(sR.getOutputStream());				
			) {
			while (true){
				ClientData writer = new ClientData(ss);
				ClientData reader = new ClientData(ssReader);
				synchronized (otherWriters) {
					otherWriters.add(client);
				}
				new Thread(() -> {
					String title = sc.nextLine();
					Cikk tmp = new Cikk();
					tmp.title = title;
					while(sc.hasNextLine()){
						String line = sc.nextLine();
						String[] lineParts = line.split(" ");
						
						for (int idx = 0; idx < lineParts.length; idx++) { 
							tmp.words.add(lineParts[idx]);				
						}	//for
					}	//while hasnextline

					if(news.get(title) != null){
						news.get(title).updateWords(tmp.words);
					} else{
						news.put(title, tmp);
					}
					
					synchronized (otherWriters) {
						otherWriters.remove(client);
						try {
							client.close();
						} catch (Exception e) {
							// won't happen
						}
					}
				}).start();	
			}	//while true
			
			for (Map.Entry<String, Cikk> entry : news.entrySet()) {
				System.out.println(entry.getValue());	//LOG
			}
			System.out.println("***\t***\t***\n");	//LOG
		}	//try
	}	//main
}	//class

class Cikk{
	public String 		title 	= "";
	public List<String>	words	= new ArrayList<>();
	
	public Cikk(){}

	@Override
    public String toString()
    {
    	String wordList = "";
    	for (String word : words) {
    		wordList += word + " ";
    	}
        return "title: " + title + "\n" + wordList + "\n";
    }

    public void updateWords(List<String> newWords){
    	words.clear();
    	for (String word : newWords) {
    		words.add(word);
    	}
    }
}