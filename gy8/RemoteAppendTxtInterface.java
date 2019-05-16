
// Remote Method Invocation
// Távoli metódushívás

import java.rmi.*;

public interface RemoteAppendTxtInterface extends Remote
{
    String appendTxt(String str) throws RemoteException;
    // MyData appendTxt(MyData str) throws RemoteException;
}

// class MyData implements Serializable
