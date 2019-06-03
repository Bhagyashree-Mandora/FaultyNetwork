import java.net.*;

public class UdpClientReceiver extends Thread {

//    private static final int PORT = 1983;
//    private static final int LISTEN_AT_PORT = 1983;
    private DatagramSocket sendToSocket, listenAtSocket;
    private InetAddress host;
    private static UdpClientReceiver client;
    private Controller controller;

    public UdpClientReceiver(DatagramSocket receiverSocket, Controller controller) throws SocketException, UnknownHostException {
        listenAtSocket = receiverSocket;
        this.controller = controller;
//        sendToSocket = new DatagramSocket();
//        host = InetAddress.getByName("localhost");
    }

//    public synchronized static UdpClientReceiver getInstance() throws SocketException, UnknownHostException {
//        if (client == null) {
//            client = new UdpClientReceiver();
//        }
//        return client;
//    }

    @Override
    public void run() {
        try {

            byte[] buffer = new byte[Constants.HAMMING_ENCODED_SIZE];
            DatagramPacket reply = new DatagramPacket(buffer, buffer.length);
//            System.out.println("receiving");
            while(true)
            {
//                System.out.println("In loop..");
                listenAtSocket.receive(reply);
                controller.receive(reply.getData());
//                System.out.println("RECEIVED: " + new String(reply.getData()));
//                Message msg = new MessageBuilder().extract(reply.getData());
//                if(msg != null){
//                    System.out.println(msg.toString());
//                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

//    public synchronized byte[] sendData(byte[] data) throws IOException {
//
//        byte[] buffer = new byte[49];
//        DatagramPacket reply = new DatagramPacket(buffer, buffer.length);
//        try {
//            listenAtSocket.unWrap(reply);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        return reply.getData();
//    }
}
