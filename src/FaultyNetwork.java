import java.io.IOException;
import java.net.DatagramSocket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FaultyNetwork {
    public static void main(String[] args) throws IOException, InterruptedException {

        DatagramSocket socket1 = new DatagramSocket(1993);
        DatagramSocket socket2 = new DatagramSocket(1992);
        ChatWindow window1 = new ChatWindow("Alice");
        ChatWindow window2 = new ChatWindow("Bob");
        Controller client1 = new Controller("Alice", socket1, 1983, window1);
        Controller client2 = new Controller("Bob", socket2, 1982, window2);

        client1.connect("Client1 joined".getBytes());
        client2.connect("Client2 joined".getBytes());

        UdpClientReceiver receiver1 = new UdpClientReceiver(socket1, client1);
        receiver1.start();
        UdpClientReceiver receiver2 = new UdpClientReceiver(socket2, client2);
        receiver2.start();

        window1.setClient(client1);
        window1.display();
        window2.setClient(client2);
        window2.display();

//        String fileName = "LargeTextFile.txt";
//        Path location = Paths.get(fileName);
//        byte[] data = Files.readAllBytes(location);
//        byte[] data = "Hello, how are you. this is a test. Sending a message of length more than size of 2 packets. I think this will work. We need more bytes".getBytes();
//        client1.send(data);
    }
}
