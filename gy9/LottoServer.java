import java.rmi.*;
import java.util.Random;

public class LottoServer
    extends java.rmi.server.UnicastRemoteObject
    implements LottoInterface
{
    boolean winner = false;
    int id = 0;

    public boolean nyeroszamE() throws RemoteException
    {
        return winner;    
    }


    public LottoServer(int idx, boolean isAWinner) throws RemoteException
    {
        id      = idx;
        winner  = isAWinner;
    }
}