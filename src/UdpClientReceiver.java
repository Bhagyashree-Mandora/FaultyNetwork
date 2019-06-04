import java.net.*;

public class UdpClientReceiver extends Thread {

    private DatagramSocket listenAtSocket;
    private Controller controller;

    public UdpClientReceiver(DatagramSocket receiverSocket, Controller controller) {
        listenAtSocket = receiverSocket;
        this.controller = controller;
    }

    @Override
    public void run() {
        try {

            byte[] buffer = new byte[Constants.HAMMING_ENCODED_SIZE];
            DatagramPacket reply = new DatagramPacket(buffer, buffer.length);
            while(true)
            {
                listenAtSocket.receive(reply);
                controller.receive(reply.getData());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
