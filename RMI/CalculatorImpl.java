//CalculatorImpl.java

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class CalculatorImpl extends UnicastRemoteObject implements Calculator{
  protected CalculatorImpl()throws RemoteException{
    super();
  }
  public long addition(long a,long b) throws RemoteException{
    return a+b;
  }
  public int gcd(int a,int b)throws RemoteException{
    int gcd=1;
    for(int i=1;i<=Math.min(a,b);i++){
      if(a%i==0 && b%i==0){
        gcd=i;
      }
    }
    return gcd;
  }
}
