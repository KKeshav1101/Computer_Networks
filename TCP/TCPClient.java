import java.io.*;
import java.net.*;
import javax.swing.*;

public class TCPClient extends javax.swing.JFrame {
    private DataOutputStream output;
    private BufferedReader input;
    private String serverIP;
    private Socket connection;
    private int port = 5555;

    public TCPClient(String serverIP) {
        initComponents();
        this.serverIP = serverIP;
        this.setTitle("Chat-Client");
        this.setVisible(true);
        status.setVisible(true);
    }

    private void initComponents() {
        jPanel1 = new javax.swing.JPanel();
        jTextField1 = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        chatArea = new javax.swing.JTextArea();
        jLabel2 = new javax.swing.JLabel();
        status = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);

        jPanel1.setBackground(new java.awt.Color(100, 100, 100));
        jPanel1.setForeground(new java.awt.Color(111, 111, 111));
        jPanel1.setLayout(null);

        jTextField1.setToolTipText("Type your message here...");
        jTextField1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField1ActionPerformed(evt);
            }
        });
        jPanel1.add(jTextField1);
        jTextField1.setBounds(10, 370, 410, 40);

        jButton1.setBackground(new java.awt.Color(222, 22, 222));
        jButton1.setFont(new java.awt.Font("Tahoma", 1, 11));
        jButton1.setText("Send");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        jPanel1.add(jButton1);
        jButton1.setBounds(420, 370, 80, 40);

        chatArea.setColumns(20);
        chatArea.setRows(5);
        jScrollPane1.setViewportView(chatArea);

        jPanel1.add(jScrollPane1);
        jScrollPane1.setBounds(10, 80, 490, 280);

        jLabel2.setFont(new java.awt.Font("Myriad Pro", 1, 48));
        jLabel2.setForeground(new java.awt.Color(51, 0, 51));
        jLabel2.setText("Chatting Client");
        jLabel2.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        jPanel1.add(jLabel2);
        jLabel2.setBounds(40, 2, 390, 62);

        status.setForeground(new java.awt.Color(255, 255, 255));
        status.setText("...");
        jPanel1.add(status);
        status.setBounds(10, 50, 300, 40);
        jPanel1.add(jLabel1);
        jLabel1.setBounds(0, 0, 400, 400);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 508, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 419, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        setSize(new java.awt.Dimension(508, 441));
        setLocationRelativeTo(null);
    }

    private void jTextField1ActionPerformed(java.awt.event.ActionEvent evt) {
        sendMessage(jTextField1.getText());
        jTextField1.setText("");
    }

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {
        sendMessage(jTextField1.getText());
        jTextField1.setText("");
    }

    public void startRunning() {
    	try {
        	status.setText("Attempting Connection...");
        	connection = new Socket(InetAddress.getByName(serverIP), port);
        	status.setText("Connected to: " + connection.getInetAddress().getHostName());

	        output = new DataOutputStream(connection.getOutputStream());
        	input = new BufferedReader(new InputStreamReader(connection.getInputStream()));	

        	whileChatting();
    	} catch (IOException ioException) {
        	ioException.printStackTrace();
        	status.setText("Connection Failed");
    		}
}

private void whileChatting() {
    jTextField1.setEditable(true);
    try {
        String message;
        while ((message = input.readLine()) != null) {
            chatArea.append("\n" + message);
        }
    } catch (IOException e) {
        e.printStackTrace();
        chatArea.append("\n Connection closed.");
    }
}

private void sendMessage(String message) {
    try {
        chatArea.append("\nME(Client) - " + message);
        output.writeBytes(message + '\n');
    } catch (IOException ioException) {
        chatArea.append("\n Unable to Send Message");
    }
}

    private javax.swing.JTextArea chatArea;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JLabel status;
}
