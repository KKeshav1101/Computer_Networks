import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Calculator extends Remote{
	public long addition(long a,long b)throws RemoteException;
	public long subtraction(long a,long b)throws RemoteException;
	public long multiplication(long a,long b)throws RemoteException;
	public long division(long a,long b)throws RemoteException;
	public int gcd(int a,int b)throws RemoteException;
	public boolean isArmstrong(int a)throws RemoteException;
}
