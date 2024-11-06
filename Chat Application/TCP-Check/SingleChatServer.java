import javax.swing.*;
import java.io.DataInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class SingleChatServer {
    private JFrame frame;
    private JLabel messageLabel;
    private JLabel resultLabel;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            SingleChatServer server = new SingleChatServer();
            server.createAndShowGUI();
            new Thread(server::startServer).start();
        });
    }

    private void createAndShowGUI() {
        frame = new JFrame("Palindrome Checker Server");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 200);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        messageLabel = new JLabel("Waiting for message...");
        messageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        panel.add(messageLabel);

        resultLabel = new JLabel("");
        resultLabel.setHorizontalAlignment(SwingConstants.CENTER);
        panel.add(resultLabel);

        frame.add(panel);
        frame.setVisible(true);
    }

    private void startServer() {
        int port = 6789;
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            while (true) {
                Socket server = serverSocket.accept();
                DataInputStream in = new DataInputStream(server.getInputStream());
                String message = in.readUTF();
                SwingUtilities.invokeLater(() -> displayResult(message));
                server.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void displayResult(String message) {
        messageLabel.setText("Received: " + message);
        String result = isPalindrome(message) ? "It is a palindrome" : "It is not a palindrome";
        resultLabel.setText(result);
    }

    private boolean isPalindrome(String str) {
        String cleanStr = str.replaceAll("\\s+", "").toLowerCase();
        int length = cleanStr.length();
        for (int i = 0; i < length / 2; i++) {
            if (cleanStr.charAt(i) != cleanStr.charAt(length - i - 1)) {
                return false;
            }
        }
        return true;
    }
}
