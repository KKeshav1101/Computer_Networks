public class Server {
    public static void main(String[] args) {
        try {
            TCPServer.main(args);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}