import java.io.IOException;
import java.net.*;

public class UdpClientSender {

    private InetAddress host;
    private DatagramSocket socket;

    public UdpClientSender(DatagramSocket socket) throws UnknownHostException {
        this.socket = socket;
        host = InetAddress.getByName("localhost");
    }

    public synchronized void sendData(byte[] data, int portNum) throws IOException {
        DatagramPacket datagramPacket = new DatagramPacket(data, data.length, host, portNum);
        socket.send(datagramPacket);
    }
}
