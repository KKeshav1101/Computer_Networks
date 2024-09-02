import java.io.*;
import java.net.Socket;
import javax.swing.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.filechooser.*;

public class FTPClient {
    static JFrame jf;
    static JFileChooser jfc;
    static JButton send;
    static JLabel l;

    private static DataOutputStream dos = null;
    private static DataInputStream dis = null;
    private static String filepath;

    public static void main(String args[]) {
        filepath = "E:";// default file path

        jf = new JFrame("Select file - client");
        jf.setSize(400, 400);
        jf.setVisible(true);
        jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        send = new JButton("Choose File");
        FTPClient clientGUI = new FTPClient();

        send.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                JFileChooser j = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
                int r = j.showSaveDialog(null);
                if (r == JFileChooser.APPROVE_OPTION) {
                    l.setText(j.getSelectedFile().getAbsolutePath());
                    filepath = j.getSelectedFile().getAbsolutePath().toString();
                    System.out.println(filepath);
                    // port 900 is for ftp
                    try (Socket socket = new Socket("localhost", 900 )) {
                        dis = new DataInputStream(socket.getInputStream());
                        dos = new DataOutputStream(socket.getOutputStream());
                        System.out.println("Sending the file to the server");
                        sendFile(filepath);
                        dis.close();
                        dos.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else
                    l.setText("The user didn't choose a file");
            }
        });
        JPanel p = new JPanel();
        p.add(send);

        l = new JLabel("no file selected");

        p.add(l);
        jf.add(p);
        jf.show();

    }

    private static void sendFile(String path) throws Exception {
        int bytes = 0;
        File file = new File(path);
        FileInputStream fis = new FileInputStream(file);
        dos.writeLong(file.length());
        byte[] buffer = new byte[4 * 1024];
        while ((bytes = fis.read(buffer)) != -1) {
            for (int i = 0; i < bytes; i++) {
                buffer[i] += 3; // Encryption by shifting ASCII values by 3
            }
            dos.write(buffer, 0, bytes);
            dos.flush();
        }
        fis.close();
    }
}
