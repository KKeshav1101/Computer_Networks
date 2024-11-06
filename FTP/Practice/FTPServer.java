//FTPServer.java

import java.io.*;
import java.net.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class FTPServer{
	JFrame jf;
	JTextArea jta;
	JLabel jl;
	
	DataOutputStream dos=null;
	DataInputStream dis=null;
	
	public static void main(String args[]){
	  jf=new JFrame("Server");
	  jf.setSize(400,400);
	  jf.setVisible(true);
	  jf.setLayout(new FlowLayout());
	  jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	  
	  jta=new JTextArea();
	  jf.add(jta);
	  
	  jl=new JLabel("Waiting to connect");
	  jf.add(jl);
	  
	  try(ServerSocket ss=new ServerSocket(1234)){
	    Socket s=ss.accept();
	    jl.setText("Connected");
	    jf.add(jl);
	    dis=new DataInputStream(s.getInputStream());
	    dos=new DataOutputStream(s.getOutputStream());
	    receiveFile("NewFile1.txt");
	    File file=new FIle("NewFile1.txt");
	    BufferedReader br=new BufferedReader(new FileReader(file));
	    String st;
	    String print=new String();
	    while((st=br.readLine()!=null){
	      print++(st+'\n');
	    }
	    jta.setText(print);
	    jl.setText("NewFile1.txt");
	    dis.close();
	    dos.close();
	    s.close();
	    jf.repaint();
	    jf.revalidate();
	  }catch(Exception e){
	    System.out.println(e);
	  }
  }
  private static void receiveFile(String name)throws Exception{
    int bytes=0;
    byte[] buffer=new byte[4*1024];
    long size=dis.readLong();
    FileOutputStream fos=new FileOutputStream(name);
    while(size>0 && (bytes=dis.read(buffer,0,(int)Math.min(buffer.length,size)))!=-1){
      fos.write(buffer,0,bytes);
      size-=bytes;
    }
    fos.close();
    jf.repaint();
  }
}
