import java.io.*;
import java.util.*;
import java.net.*;

class Szerver {
	public static void main(String[] args) throws Exception {
		int PORT = 2121;
		// try-with-resources (AutoCloseable)
		
		while (true){
			try (
				ServerSocket ss = new ServerSocket(PORT);
				Socket s = ss.accept();
				Scanner sc = new Scanner(s.getInputStream());
				PrintWriter pw = new PrintWriter(s.getOutputStream());
			) {
				Integer initTime = (int) (long) System.currentTimeMillis();

				String team1 = sc.nextLine();
				String team2 = sc.nextLine();
				Merkozes tmp = new Merkozes();
				tmp.team1 = team1;
				tmp.team2 = team2;
				boolean ended = false;
				while(sc.hasNextLine() && (!ended)){
					String line = sc.nextLine();
					if (line.equals("jatek_vege")) {
						ended = true;
					} else {
						Integer	team = Integer.valueOf(line);
						Integer time = (int) (long) System.currentTimeMillis();
						Pont point = new Pont();
						point.whichOne = team;
						point.timecode = time;
						tmp.points.add(point);
					}
					System.out.println(tmp.currentStatus());
				}	//while
				//answer(tmp, pw);
			}
		}
	}
}

class Pont{
	public Integer 	whichOne = 0;
	public Integer 	timecode = 0;
	
	public Pont(){}

	@Override
    public String toString(){
        return "whichOne: " + String.valueOf(whichOne) + "\ttimecode: " + String.valueOf(timecode);
    }
}

class Merkozes{
	public String 	team1 = "";
	public String 	team2 = "";
	public List<Pont>	points	= new ArrayList<>();
	
	public Merkozes(){}

	@Override
    public String toString(){
		String pointList = "";
    	for (Pont point : points) {
    		pointList += "\t" + point.toString() + "\n";
    	}
        return "team1:\t" + team1 + "\nteam2:\t" + team2 + "\n" + pointList;
    }

    public String currentStatus(){
    	int idx = 0;
    	int jdx = 0;

    	for (Pont point : points) {
    		if (point.whichOne == 1) {
    			++idx;
    		} else {
    			++jdx;
    		}
    	}
    	return team1 + ": " + String.valueOf(idx) + team2 + ": " + String.valueOf(jdx);
    }
}