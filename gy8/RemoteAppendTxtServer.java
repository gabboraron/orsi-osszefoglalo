
import java.rmi.*;

public class RemoteAppendTxtServer
    extends java.rmi.server.UnicastRemoteObject
    implements RemoteAppendTxtInterface
{
    String appendTxt;

    public RemoteAppendTxtServer() throws RemoteException
    {
        this("default");
    }

    public RemoteAppendTxtServer(String appendTxt) throws RemoteException
    {
        this.appendTxt = appendTxt;
    }

    public String appendTxt(String str) throws RemoteException
    {
        return str + appendTxt;
    }
}
