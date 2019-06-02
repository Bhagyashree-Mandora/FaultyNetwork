import java.io.IOException;
import java.net.*;

public class UdpClientSender {

    private static final int PORT = 1983;
//    private static final int LISTEN_AT_PORT = 1983;
    private DatagramSocket sendToSocket, ListenAtSocket;
    private InetAddress host;
    private static UdpClientSender client;
    private int senderPort;

    public UdpClientSender(int senderPort) throws SocketException, UnknownHostException {
//        ListenAtSocket = new DatagramSocket(LISTEN_AT_PORT);
        this.senderPort = senderPort;
        host = InetAddress.getByName("localhost");
    }

//    public synchronized static UdpClientSender getInstance() throws SocketException, UnknownHostException {
//        if (client == null) {
//            client = new UdpClientSender();
//        }
//        return client;
//    }

//    public synchronized byte[] sendData(byte[] data) throws IOException {
    public synchronized void sendData(byte[] data, int portNum) throws IOException {
        sendToSocket = new DatagramSocket(senderPort);
        data = new MessageEncoder().encode(1, data);
        DatagramPacket datagramPacket = new DatagramPacket(data, data.length, host, portNum);
        sendToSocket.send(datagramPacket);
        sendToSocket.close();


//        byte[] buffer = new byte[49];
//        DatagramPacket reply = new DatagramPacket(buffer, buffer.length);
//        try {
//            ListenAtSocket.receive(reply);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        return reply.getData();
    }
}
