import java.io.*;
import java.net.*;
public class dhcpserver{
	private static final int PORT = 12345;
	private static final String[] ipAddressPool = {"192.168.1.1","192.168.1.2","192.168.1.3","192.168.1.4"};
	private static int availableCount = ipAddressPool.length;
	public static void main(String[] args){
		try(ServerSocket ss=new ServerSocket(PORT)){
			System.out.println("Server is listening on port"+PORT);
			while(true){
				try (Socket socket=ss.accept();
					PrintWriter out= new PrintWriter(socket.getOutputStream(),true);
					BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))){
						System.out.println("Client Connected..");
						String request=in.readLine();
						if("REQUEST_IP".equals(request)){
							String allocatedIp=allocateIpAddress();
							out.println(allocatedIp);
						}else{
							out.println("Invalid request");
						}
					}catch(IOException e){
						System.out.println("Error handling client:"+e.getMessage());
					}
			}
		}catch(IOException e){
			System.out.println("Server error:"+e.getMessage());
		}
	}
	
	private static synchronized String allocateIpAddress(){
		if(availableCount==0){
			return "no ip address available";
		}
		String allocatedIp=ipAddressPool[0];
		for(int i=0;i<availableCount-1;i++){
			ipAddressPool[i]=ipAddressPool[i+1];
		}
		ipAddressPool[availableCount-1]=null;
		availableCount--;
		return allocatedIp;
	}
}

