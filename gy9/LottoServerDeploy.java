import java.util.*;
import java.rmi.registry.*;
import java.util.Random;

public class LottoServerDeploy
{
    public static void main(String args[])
        throws Exception
    {
        final int PORT = 12345;
        final int numberOfAllNumbers 	= (int) Integer.parseInt(args[0]);
        final int numberOfWiningNumbers = (int) Integer.parseInt(args[1]);

        Registry registry = LocateRegistry.createRegistry(PORT);
        // Registry registry = LocateRegistry.getRegistry();
        // Create winnners
	    Set<Integer> winnersList = new HashSet<Integer>(); 
		//Iterator<CurrentAccount> itr = winnersList.iterator();
		//for(int idx = 0; idx<numberOfWiningNumbers; ++idx){
		while(winnersList.size()<numberOfWiningNumbers){
        	Random rand = new Random();
            int nr = rand.nextInt(numberOfAllNumbers);
            winnersList.add(nr);
		}
        System.out.println(winnersList);

        // Create LottoServers
        for(int idx = 0; idx<numberOfAllNumbers; ++idx){
        	if (winnersList.contains(idx)) {
        		registry.rebind(String.valueOf(idx), new LottoServer(idx,true));
			} else {
        		registry.rebind(String.valueOf(idx), new LottoServer(idx,false));
			}
        }
     }
}
