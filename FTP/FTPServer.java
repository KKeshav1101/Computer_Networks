import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import javax.swing.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;

public class FTPServer {
    public static JFrame jf;
    public static JTextArea jta;
    public static JPanel p;
    public static JLabel jl1, jl2, jl3;
    // jl1 for connection status
    // jl2 for file name
    // jl3 for successful file reception

    private static DataOutputStream dos = null;
    private static DataInputStream dis = null;

    public static void main(String args[]) {
        jf = new JFrame("Server side file reception");
        jf.setSize(400, 400);
        jf.setLayout(new FlowLayout());
        jf.setVisible(true);

        jl1 = new JLabel("..waiting to connect..");
        jl2 = new JLabel("...");
        jl3 = new JLabel();
        jf.add(jl1);

        jta = new JTextArea("..waiting to read..");
        jf.add(jta);

        jf.show();

        try (ServerSocket ss = new ServerSocket(900)) {
            System.out.println("Server is starting in port 900");
            Socket clientSocket = ss.accept();
            System.out.println("Connected");
            jl1.setText("Connected");
            jf.add(jl1);
            dis = new DataInputStream(clientSocket.getInputStream());
            dos = new DataOutputStream(clientSocket.getOutputStream());
            receiveFile("NewFile1.txt");
            File file = new File("NewFile1.txt");

            BufferedReader br = new BufferedReader(new FileReader(file));
            String st;
            String print = new String();
            while ((st = br.readLine()) != null) {
                System.out.println(st);
                print = print + st + "\n";
            }

            jta.setText(print);
            jl2.setText("NewFile1.txt");
            jf.add(jl2);
            dis.close();
            dos.close();
            clientSocket.close();
            jf.repaint();
        } catch (Exception e) {
            e.printStackTrace();
        }

        jf.repaint();
    }

    private static void receiveFile(String fileName) throws Exception {
        int bytes = 0;
        FileOutputStream fos = new FileOutputStream(fileName);
        long size = dis.readLong();
        byte[] buffer = new byte[4 * 1024];
        while (size > 0 && (bytes = dis.read(buffer, 0, (int) Math.min(buffer.length, size))) != -1) {
            for (int i = 0; i < bytes; i++) {
                buffer[i] -= 3; // Decryption by shifting ASCII values back by 3
            }
            fos.write(buffer, 0, bytes);
            size -= bytes;
        }
        System.out.println("File is received");
        jl3.setText("File received successfully");
        jf.add(jl3);
        fos.close();
        jf.repaint();
    }
}
