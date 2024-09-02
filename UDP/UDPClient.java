import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.nio.ByteBuffer;

public class UDPClient extends JFrame implements ActionListener {
    private JTextField inputField;
    private JTextArea resultArea;
    private JButton sendButton;
    
    public UDPClient() {
        setTitle("UDP Client");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new FlowLayout());
        
        inputField = new JTextField(20);
        resultArea = new JTextArea(10, 30);
        sendButton = new JButton("Send Array");
        sendButton.addActionListener(this);
        
        add(new JLabel("Enter comma-separated integers:"));
        add(inputField);
        add(sendButton);
        add(new JScrollPane(resultArea));
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            String[] strNumbers = inputField.getText().split(",");
            int[] numbers = new int[strNumbers.length];
            for (int i = 0; i < strNumbers.length; i++) {
                numbers[i] = Integer.parseInt(strNumbers[i].trim());
            }
            
            ByteBuffer buffer = ByteBuffer.allocate(4 + numbers.length * 4);
            buffer.putInt(numbers.length);
            for (int number : numbers) {
                buffer.putInt(number);
            }
            
            DatagramSocket socket = new DatagramSocket();
            InetAddress address = InetAddress.getByName("localhost");
            DatagramPacket packet = new DatagramPacket(buffer.array(), buffer.position(), address, 9876);
            socket.send(packet);
            
            byte[] responseBuffer = new byte[4];
            DatagramPacket responsePacket = new DatagramPacket(responseBuffer, responseBuffer.length);
            socket.receive(responsePacket);
            
            int sum = ByteBuffer.wrap(responsePacket.getData()).getInt();
            resultArea.append("Sum of elements: " + sum + "\n");
            
            socket.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new UDPClient().setVisible(true);
        });
    }
}
