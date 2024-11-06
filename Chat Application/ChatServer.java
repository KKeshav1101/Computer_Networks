import java.io.*;
import java.net.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

class GUIServer extends JFrame implements ActionListener{
	JTextField in;
	JLabel out,label;
	JButton submit;
	ServerSocket ss;
	Socket s;
	BufferedReader inFromClient;
	GUIServer(){
		in=new JTextField("send");
		label=new JLabel("Client said :");
		out=new JLabel("_____");
		submit=new JButton("send");
		add(in);
		add(label);
		add(out);
		add(submit);
		submit.addActionListener(this);
		try{
			ss=new ServerSocket(1234);
		}catch(Exception e){
			System.out.println(e);
		}
		setSize(300,400);
		setLayout(new FlowLayout());
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setVisible(true);
		revalidate();
		repaint();
		start();
	}
	public void start(){
		try{
			System.out.println("Waiting to connect ...");
			while(true){
				s=ss.accept();
				System.out.println("Connected");
				inFromClient=new BufferedReader(new InputStreamReader(s.getInputStream()));
				String rec=inFromClient.readLine();
				out.setText(rec);
			}
		}catch(Exception e){
			System.out.println(e);
		}
	}
	public void actionPerformed(ActionEvent ae){
		if(ae.getSource()==submit){
			try{
				if(s!=null){
					String rec,snd;
					snd=in.getText();
					DataOutputStream outToClient = new DataOutputStream(s.getOutputStream());
					outToClient.writeBytes(snd+'\n');
					rec=inFromClient.readLine();
					out.setText(rec);
					String msg="bye";
					if(rec.equals(msg) || snd.equals(msg)){
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

public class ChatServer{
	public static void main(String args[]){
		GUIServer obj=new GUIServer();
	}
}
