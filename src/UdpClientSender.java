import java.io.IOException;
import java.net.*;

public class UdpClientSender {

//    private static final int PORT = 1983;
//    private static final int LISTEN_AT_PORT = 1983;
    private InetAddress host;
    private DatagramSocket socket;

    public UdpClientSender(DatagramSocket socket) throws SocketException, UnknownHostException {
//        listenAtSocket = new DatagramSocket(LISTEN_AT_PORT);
        this.socket = socket;
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
        DatagramPacket datagramPacket = new DatagramPacket(data, data.length, host, portNum);
        socket.send(datagramPacket);
    }

    public synchronized byte[] receiveData() {
        byte[] buffer = new byte[Constants.HAMMING_ENCODED_SIZE];
        DatagramPacket reply = new DatagramPacket(buffer, buffer.length);
        try {
            socket.receive(reply);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return reply.getData();
    }
}
