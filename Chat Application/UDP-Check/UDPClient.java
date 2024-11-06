import java.io.*;
import java.net.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

class GUIClient extends JFrame implements ActionListener{
	JTextField in,out;
	JButton submit;
	GUIClient(){
		in=new JTextField("send");
		out=new JTextField("receive");
		submit=new JButton("submit");
		add(in);
		add(out);
		add(submit);
		setSize(300,400);
		setLayout(new FlowLayout());
		setVisible(true);
		submit.addActionListener(this);
		revalidate();
		repaint();
	}
	public void actionPerformed(ActionEvent ae){
		if(ae.getSource()==submit){
			try{
				String sent1=in.getText();
				DatagramSocket s=new DatagramSocket();
				InetAddress IPA=InetAddress.getByName("localhost");
				byte[] send=new byte[1024];
				byte[] rec=new byte[1024];
				send=sent1.getBytes();
				DatagramPacket spk=new DatagramPacket(send,send.length,IPA,1234);
				s.send(spk);
				DatagramPacket rpk=new DatagramPacket(rec,rec.length);
				s.receive(rpk);
				String sent2=new String(rpk.getData());
				out.setText(sent2);
				s.close();
			}
			catch(Exception e){
				System.out.println(e);
			}
		}
		revalidate();
		repaint();
	}
}
public class UDPClient{
	public static void main(String args[]){
		GUIClient obj=new GUIClient();
		obj.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
}
