import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.DatagramSocket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FaultyNetwork {
    public static void main(String[] args) throws IOException {

        DatagramSocket socket1 = new DatagramSocket(1993);
        DatagramSocket socket2 = new DatagramSocket(1992);

        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Please enter test case number: ");
        int testCase = Integer.parseInt(br.readLine());

        Controller client1, client2;
        UdpClientReceiver receiver1, receiver2;

        switch (testCase) {
            case 1:
                ChatWindow window1 = new ChatWindow("Alice");
                ChatWindow window2 = new ChatWindow("Bob");
                client1 = new Controller("Bob", socket1, 1983, window1);
                client2 = new Controller("Alice", socket2, 1982, window2);

                client1.connect("Client1 joined".getBytes());
                client2.connect("Client2 joined".getBytes());

                receiver1 = new UdpClientReceiver(socket1, client1);
                receiver1.start();
                receiver2 = new UdpClientReceiver(socket2, client2);
                receiver2.start();

                window1.setClient(client1);
                window1.display();
                window2.setClient(client2);
                window2.display();
                break;

            default:
                client1 = new Controller("Alice", socket1, 1983, null);
                client2 = new Controller("Bob", socket2, 1982, null);

                client1.connect("Client1 joined".getBytes());
                client2.connect("Client2 joined".getBytes());

                receiver1 = new UdpClientReceiver(socket1, client1);
                receiver1.start();
                receiver2 = new UdpClientReceiver(socket2, client2);
                receiver2.start();
                String fileName = "LargeTextFile.txt";
                Path location = Paths.get(fileName);
                byte[] data = Files.readAllBytes(location);
                client1.send(data);
                break;

        }
    }
}
