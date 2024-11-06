//ftpclient.java

import java.io.*;
import java.net.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class FTPClient{
  JFrame jf;
  JLabel jl;
  JTextArea jta;
  JButton submit;
  
  static DataOutputStream dos=null;
  static DataInputStream dis=null;
  static String filepath;
  
  public static void main(String args[]){
    jf=new JFrame("Client");
    jf.setSize(500,500);
    jf.setVisible(true);
    jf.setLayout(new FlowLayout);
    jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    jl=new JLabel("Enter Absolute Path:");
    jf.add(jl);
    
    jta=new JTextArea();
    jf.add(jta);
    
    submit=new JButton("Send");
    jf.add(submit);
    submit.addActionListener(new ActionListener(){
      public void actionPerformed(ActionEvent ae){
        filepath=jta.getText();
        jl.setText(filepath);
        try(Socket s=new Socket("localhost",1234)){
          dis=new DataInputStream(s.getInputStream());
          dos=new DataOutputStream(s.getOutputStream());
          sendFile(filepath);
          dis.close();
          dos.close();
        }catch(Exception e){
          System.out.println(e);
        }
        jf.repaint();
        jf.revalidate();
      }
    });
  }
  public static void sendFile(String path) throws Exception{
    int bytes=0;
    File file=new File(path);
    dos.writeLong(file.length());
    FileInputStream fis=new FileInputStream(file);
    byte[] buffer = new byte[4*1024];
    while((bytes=fis.read(buffer))!=-1){
      dos.write(buffer,0,bytes);
      dos.flush();
    }
    fis.close();
  }
}
