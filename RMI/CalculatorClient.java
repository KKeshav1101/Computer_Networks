import java.rmi.Naming;

public class CalculatorClient{
	public static void main(String args[]){
		try{
			Calculator c=(Calculator)Naming.lookup("//127.0.0.1:1099/CalculatorService");
			System.out.println("Addition"+c.addition(10,5));
			System.out.println("GCD:"+c.gcd(10,5));
		}catch(Exception e){
			System.out.println("Exception :"+e);
		}
	}
}
