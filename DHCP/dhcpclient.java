import java.io.*;
import java.net.*;

public class dhcpclient{
	private static final String SERVER_ADDRESS="localhost";
	private static final int PORT=12345;
	public static void main(String[] args){
		try(Socket socket=new Socket(SERVER_ADDRESS,PORT);
			PrintWriter out=new PrintWriter(socket.getOutputStream(),true);
			BufferedReader in=new BufferedReader(new InputStreamReader(socket.getInputStream()))){
				out.println("REQUEST_IP");
				String response=in.readLine();
				System.out.println("ReceivedIpadress"+response);
			}catch(IOException e){
				System.out.println("Client error:"+e.getMessage());
			}
	}
}
