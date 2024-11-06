import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

public class SingleChatClient {
    private JFrame frame;
    private JTextField textField;
    private JButton sendButton;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new SingleChatClient().createAndShowGUI());
    }

    private void createAndShowGUI() {
        frame = new JFrame("TCP Client");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 200);

        JPanel panel = new JPanel();
        frame.add(panel);
        placeComponents(panel);

        frame.setVisible(true);
    }

    private void placeComponents(JPanel panel) {
        panel.setLayout(null);

        JLabel label = new JLabel("Enter a string:");
        label.setBounds(10, 20, 80, 25);
        panel.add(label);

        textField = new JTextField(20);
        textField.setBounds(100, 20, 165, 25);
        panel.add(textField);

        sendButton = new JButton("Send");
        sendButton.setBounds(10, 80, 80, 25);
        panel.add(sendButton);

        sendButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sendMessage();
            }
        });
    }

    private void sendMessage() {
        String serverName = "localhost";
        int port = 6789;
        try {
            Socket client = new Socket(serverName, port);
            OutputStream outToServer = client.getOutputStream();
            DataOutputStream out = new DataOutputStream(outToServer);
            String message = textField.getText();
            out.writeUTF(message);
            client.close();
            JOptionPane.showMessageDialog(frame, "Message sent: " + message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
