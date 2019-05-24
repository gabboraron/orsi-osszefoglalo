import java.io.*;
import java.util.*;
import java.net.*;

class Szerver {
	public static void main(String[] args) throws Exception {
		
		HashMap<String, Cikk> news = new HashMap<>();

		// protokoll
			// interfesz
		// 0-65535 (1024-65535)
		int PORT = Integer.parseInt(args[0]);
		// try-with-resources (AutoCloseable)
		while (true){
			try (
				ServerSocket ss = new ServerSocket(PORT);
				Socket s 		= ss.accept();
				Scanner sc 		= new Scanner(s.getInputStream());
				PrintWriter pw 	= new PrintWriter(s.getOutputStream());
			) {
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
			}	//try
			
			for (Map.Entry<String, Cikk> entry : news.entrySet()) {
				System.out.println(entry.getValue());	//LOG
			}
			System.out.println("***\t***\t***\n");	//LOG
		}	//while true
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