
import java.rmi.*;
import java.rmi.registry.*;

public class RMIClient
{
    public static void main(String args[])
        throws Exception
    {
        String srvAddr = "localhost";
        int srvPort    = 12345;
        String srvName = args[0];
        String text    = args[1];

        Registry registry = LocateRegistry.getRegistry(srvAddr, srvPort);
        // Registry registry = LocateRegistry.getRegistry();

        RemoteAppendTxtInterface rmiServer =
            (RemoteAppendTxtInterface)(registry.lookup(srvName));

        System.out.println(rmiServer.getClass().getName());

        String reply = rmiServer.appendTxt(text);
        System.out.println(reply);
    }
}
