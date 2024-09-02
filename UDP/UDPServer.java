import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.nio.ByteBuffer;

public class UDPServer {
    public static void main(String[] args) {
        try {
            DatagramSocket socket = new DatagramSocket(9876);
            byte[] buffer = new byte[1024];
            
            while (true) {
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                socket.receive(packet);
                
                ByteBuffer byteBuffer = ByteBuffer.wrap(packet.getData(), 0, packet.getLength());
                int length = byteBuffer.getInt();
                int[] numbers = new int[length];
                for (int i = 0; i < length; i++) {
                    numbers[i] = byteBuffer.getInt();
                }
                
                System.out.println("Received array:");
                int sum = 0;
                for (int number : numbers) {
                    System.out.print(number + " ");
                    sum += number;
                }
                System.out.println("\nSum of elements: " + sum);
                
                byte[] responseBuffer = ByteBuffer.allocate(4).putInt(sum).array();
                DatagramPacket responsePacket = new DatagramPacket(responseBuffer, responseBuffer.length, packet.getAddress(), packet.getPort());
                socket.send(responsePacket);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
