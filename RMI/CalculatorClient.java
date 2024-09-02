import java.rmi.Naming;

public class CalculatorClient{
	public static void main(String args[]){
		try{
			Calculator c=(Calculator)Naming.lookup("//127.0.0.1:1099/CalculatorService");
			System.out.println("Addition :"+c.addition(10,5));
			System.out.println("Subtraction :"+c.subtraction(10,5));
			System.out.println("Multiplication :"+c.multiplication(10,5));
			System.out.println("Division :"+c.division(10,5));
			System.out.println("gcd :"+c.gcd(10,5));
			System.out.println("isArmstrong?(125) :"+c.isArmstrong(125));
		}
		catch(Exception e){
			System.out.println("Exception"+e);
		}
	}
}