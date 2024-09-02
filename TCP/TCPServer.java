import java.io.*;
import java.net.*;

public class TCPServer {
    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(5555)) {
            System.out.println("Server started and listening on port 5555...");
            while (true) {
                try (Socket connectionSocket = serverSocket.accept();
                     BufferedReader inFromClient = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));
                     DataOutputStream outToClient = new DataOutputStream(connectionSocket.getOutputStream())) {

                    String clientSentence = inFromClient.readLine();
                    String serverResponse;
                    if (isPalindrome(clientSentence)) {
                        serverResponse = clientSentence + " is a palindrome.";
                    } else {
                        serverResponse = clientSentence + " is not a palindrome.";
                    }
                    outToClient.writeBytes(serverResponse + '\n');
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static boolean isPalindrome(String str) {
        String cleanedStr = str.replaceAll("\\s+", "").toLowerCase();
        int len = cleanedStr.length();
        for (int i = 0; i < len / 2; i++) {
            if (cleanedStr.charAt(i) != cleanedStr.charAt(len - i - 1)) {
                return false;
            }
        }
        return true;
    }
}
