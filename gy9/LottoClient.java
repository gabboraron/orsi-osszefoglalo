import java.rmi.*;
import java.rmi.registry.*;

public class LottoClient
{
    public static void main(String args[])
        throws Exception
    {
        String srvAddr = "localhost";
        int srvPort    = 12345;
        /*String srvName = args[0];
        String text    = args[1];*/

        Registry registry = LocateRegistry.getRegistry(srvAddr, srvPort);
        // Registry registry = LocateRegistry.getRegistry();
        
        for (String r : registry.list()) {
            
            LottoInterface rmiServer = (LottoInterface)(registry.lookup(r));
            //System.out.println(rmiServer.nyeroszamE());
            if (rmiServer.nyeroszamE()) {
                System.out.println(r);
                System.out.println(rmiServer.nyeroszamE());
            }
        }
/*
        RemoteAppendTxtInterface rmiServer =
            (RemoteAppendTxtInterface)(registry.lookup(srvName));

        System.out.println(rmiServer.getClass().getName());
        String reply = rmiServer.appendTxt(text);
        System.out.println(reply);*/
    }
}
