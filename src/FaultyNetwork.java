import java.io.IOException;
import java.net.DatagramSocket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static java.lang.Thread.sleep;

public class FaultyNetwork {
    public static void main(String[] args) throws IOException, InterruptedException {

//        UdpClientSender sender1 = new UdpClientSender(1992);
//        UdpClientSender sender2 = new UdpClientSender(1993);
//        byte[] data1 = "Connecting from 1983".getBytes();
//        sender1.sendData(data1, 1983);
////        sleep(5000);
//
//        data1 = "Connecting from 1982".getBytes();
//        sender2.sendData(data1, 1982);
//
//        byte[] data2 = "Hello world from 1983".getBytes();
//        sender1.sendData(data2, 1983);
//        data2 = "Hello world from 1982".getBytes();
//        sender2.sendData(data2, 1982);

        DatagramSocket socket1 = new DatagramSocket(1993);
        DatagramSocket socket2 = new DatagramSocket(1992);
        Controller client1 = new Controller("one", socket1, 1983);
        Controller client2 = new Controller("two", socket2, 1982);


        client1.connect("Client1 joined".getBytes());
        client2.connect("Client2 joined".getBytes());

        UdpClientReceiver receiver1 = new UdpClientReceiver(socket1, client1);
        receiver1.start();
        UdpClientReceiver receiver2 = new UdpClientReceiver(socket2, client2);
        receiver2.start();

        String fileName = "LargeTextFile.txt";
        Path location = Paths.get(fileName);
        byte[] data = Files.readAllBytes(location);
//        byte[] data = "Hello, how are you. this is a test. Sending a message of length more than size of 2 packets. I think this will work. We need more bytes".getBytes();
        client1.send(data);
//        sleep(10000);


//        UdpClientReceiver receiver1 = new UdpClientReceiver(1993);
//        receiver1.start();



    }
}
