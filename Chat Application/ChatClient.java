import java.io.*;
import java.net.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

class GUIClient extends JFrame implements ActionListener{
	JTextField in;
	JLabel out,label;
	JButton submit;
	Socket s;
	GUIClient(){
		in=new JTextField("send");
		label=new JLabel("Server said :");
		out=new JLabel("______");
		submit=new JButton("send");
		add(in);
		add(label);
		add(out);
		add(submit);
		submit.addActionListener(this);
		
		try{
			System.out.println("Establishing connection...");
			s=new Socket("localhost",1234);
		}catch(Exception e){
			System.out.println(e);
		}
		
		setSize(300,400);
		setLayout(new FlowLayout());
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setVisible(true);
		revalidate();
		repaint();
	}
	public void actionPerformed(ActionEvent ae){
		if(ae.getSource()==submit){
			try{
				if(s!=null){
					String rec,snd;
					snd=in.getText();
					DataOutputStream outToServer=new DataOutputStream(s.getOutputStream());
					outToServer.writeBytes(snd+'\n');
					BufferedReader inFromServer=new BufferedReader(new InputStreamReader(s.getInputStream()));
					rec=inFromServer.readLine();
					out.setText(rec);
					String msg="bye";
					if(rec.equals(msg)){
						s.close();
						s=null;
						System.exit(0);
					}
				}
			}catch(NullPointerException e){
				System.exit(0);
			}catch(Exception e){
				System.out.println(e);
			}
		}
		revalidate();
		repaint();
	}
}
public class ChatClient{
	public static void main(String args[]){
		GUIClient obj=new GUIClient();
	}
}
