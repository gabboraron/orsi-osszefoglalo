import java.io.*;
import java.util.*;
import java.net.*;

public class AuctionClient {

    public static void main(String[] args) throws Exception {
        
        String[] commands = {"put", "list", "bid"};


        String MACHINE = "localhost";

        // String MACHINE = "127.0.0.1";
        // String MACHINE = "::1";
        int PORT = 2121;
        try (
                Socket s = new Socket(MACHINE, PORT);
                Scanner sc = new Scanner(s.getInputStream());
                PrintWriter pw = new PrintWriter(s.getOutputStream());
                Scanner scIn = new Scanner(System.in);
            ){
            /*pw.println("6");
			pw.flush();
                        
			pw.println("2");
			pw.flush();
                        
                        pw.println("a");
			pw.flush();*/
            String userName = scIn.nextLine();
            pw.println(userName);
            pw.flush();

            while (true) {
                String currentCommand = scIn.nextLine();
                Integer cIdx = 0; 
                boolean commandFound = false;
                while ((cIdx<commands.length) && (!commandFound)) {
                    String possibleCommand = commands[cIdx];
                    ++cIdx;
                    String commandPart = currentCommand.split(" ")[0];
                    if (commandPart.equals(possibleCommand)) {
                        if (possibleCommand.equals("put")) {
                            pw.println(currentCommand);
                            pw.flush();
                        }

                        if (possibleCommand.equals("list")) {
                            pw.println(currentCommand);
                            pw.flush();
                            Integer size = Integer.parseInt(sc.nextLine());
                            System.out.println(String.valueOf(size));

                            for (int idx = 0; idx<size; ++idx ) {
                                System.out.println(sc.nextLine());
                            }
                        }

                        if (possibleCommand.equals("bid")) {
                            pw.println(currentCommand);
                            pw.flush();
                        }
                    }   
                }   //while 
            }
        }
    }
}