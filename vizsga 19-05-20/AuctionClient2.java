import java.io.*;
import java.util.*;
import java.net.*;

public class AuctionClient2 {

    public static void main(String[] args) throws Exception {
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
            

                Thread t1 = senderChanel(scIn, pw);
                Thread t2 = getterChanel(sc, new PrintWriter(System.out));

                t1.start();
                t2.start();

                t1.join();
                t2.join();
            }
    }

    static Thread senderChanel (Scanner sc, PrintWriter pw) {
        return new Thread(() -> {
            String[] commands = {"put", "list", "bid"};

            String userName = sc.nextLine();
            pw.println(userName);
            pw.flush();

            while (true) {
                String currentCommand = sc.nextLine();
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
                            /*Integer size = Integer.parseInt(sc.nextLine());
                            System.out.println(String.valueOf(size));

                            for (int idx = 0; idx<size; ++idx ) {
                                System.out.println(sc.nextLine());
                            }*/
                        }

                        if (possibleCommand.equals("bid")) {
                            pw.println(currentCommand);
                            pw.flush();
                        }
                    }   
                }   //while 
            }   //while true
        });
    }
    
    static Thread getterChanel (Scanner sc, PrintWriter pw) {
        return new Thread(() -> {
            while (sc.hasNextLine()) {
                String line = sc.nextLine();
                System.out.println(line);
                //pw.flush();
            }
        });
    }        
}