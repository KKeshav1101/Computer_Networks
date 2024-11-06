//Calculator Interface
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Calculator extends Remote {
  public long addition(long a,long b)throws RemoteException;
  public int gcd(int a,int b)throws RemoteException;
}
