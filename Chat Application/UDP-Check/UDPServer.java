import java.io.*;
import java.net.*;

class UDPServer{
	public static void main(String args[])throws Exception{
		DatagramSocket ss=new DatagramSocket(1234);
		byte[] rec=new byte[1024];
		byte[] send=new byte[1024];
		while(true){
			DatagramPacket recpkt=new DatagramPacket(rec,rec.length);
			ss.receive(recpkt);
			String sent1=new String(recpkt.getData());
			InetAddress IPA=recpkt.getAddress();
			int port=recpkt.getPort();
			String sent2=sent1.toUpperCase()+'\n';
			send=sent2.getBytes();
			DatagramPacket sendpkt=new DatagramPacket(send,send.length,IPA,port);
			ss.send(sendpkt);
		}
	}
}
