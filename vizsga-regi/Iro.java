import java.io.*;
import java.util.*;
import java.net.*;

public class Iro {

    public static void main(String[] args) throws Exception {
        String  MACHINE = args[0];
        int     PORT    = Integer.parseInt(args[1]);
        
        try (
                Socket s        = new Socket(MACHINE, PORT);
                Scanner sc      = new Scanner(s.getInputStream());
                PrintWriter pw  = new PrintWriter(s.getOutputStream());
                Scanner scIn    = new Scanner(System.in);
            ){
            String title = scIn.nextLine();
            pw.println(title);
            pw.flush();

            while (scIn.hasNextLine()) {
                String line = scIn.nextLine();
                
                pw.println(line);
                pw.flush();
            }
        }
    }
}