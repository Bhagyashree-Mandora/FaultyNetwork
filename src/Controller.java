import java.io.IOException;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.util.Arrays;

public class Controller {
    private static final int MAX_RETRIES = 100;

//    private static final int SEND_TO_PORT = 1983;
//    private static final int SENDER_PORT = 1993;

    private String name;
    private MessageBuilder messageBuilder;
    private Hamming hamming;
    private UdpClientSender udpSender;
    private int sendToPort;
    private int lastReceivedId;
    private int lastReceivedAckId;

    public Controller(String name, DatagramSocket senderSocket, int sendToPort) throws SocketException, UnknownHostException {
        this.name = name;
        messageBuilder = new MessageBuilder();
        hamming = new Hamming();
        udpSender = new UdpClientSender(senderSocket);
        this.sendToPort = sendToPort;
    }

    public void connect(byte[] data) {
        try {
                byte[] finalMessage = wrap(data, 0);
                udpSender.sendData(finalMessage, sendToPort);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void send(byte[] data) {
        int idCount = 1;
        try {
            for (int i = 0; i < data.length; i = i + Constants.DATA_BYTES_PER_MESSAGE) {
                int end = i + Constants.DATA_BYTES_PER_MESSAGE;
                if (end > data.length) {
                    end = data.length;
                }
                byte[] chunk = Arrays.copyOfRange(data, i, end);

                byte[] messageChunk = messageBuilder.build(idCount, chunk);
                ByteBuffer encodedChunk = ByteBuffer.allocate(Constants.HAMMING_ENCODED_SIZE);

                for (byte aByte : messageChunk) {
                    byte[] encoded = hamming.encode(aByte);
                    encodedChunk.put(encoded);
                }

                byte[] finalMessage = encodedChunk.array();

                int retryCount = 0;
                while (retryCount < MAX_RETRIES) {
                    udpSender.sendData(finalMessage, sendToPort);
                    //System.out.println(name + ": data sent");
                    //byte[] response = udpSender.receiveData();
                    //Message reply = unWrap(response);
                    Thread.sleep(200);
                    //if(reply.id == idCount){
                    if (lastReceivedAckId == idCount) {
                        break;
                    }
                    retryCount++;
//                    System.out.println("XXX retrying " + retryCount);
                }
                if(retryCount == MAX_RETRIES){
                    System.out.println("Message exceeded max send retries");
                }
                idCount = (idCount + 1) % 128;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private byte[] wrap(byte[] chunk, int idCount) {
        byte[] messageChunk = messageBuilder.build(idCount, chunk);
        ByteBuffer encodedChunk = ByteBuffer.allocate(Constants.HAMMING_ENCODED_SIZE);

        for (byte aByte : messageChunk) {
            byte[] encoded = hamming.encode(aByte);
            encodedChunk.put(encoded);
        }

        return encodedChunk.array();
    }

    public void receive(byte[] data) throws IOException {
        Message response = unWrap(data);
        if (response.data.length == 0) {
            // This is ack message
            lastReceivedAckId = response.id;
            return;
        }
        // This is normal message receive
        byte[] ack = wrap(new byte[0], response.id);
        udpSender.sendData(ack, sendToPort);
        if (((lastReceivedId + 1) % 128) <= response.id) {
            System.out.print(new String(response.data));
            lastReceivedId = response.id;
        }
    }

    private Message unWrap(byte[] data){
        ByteBuffer buffer = ByteBuffer.allocate(Constants.MESSAGE_SIZE);

        for(int i=0; i<data.length; i=i+2){
            byte[] toDecode = Arrays.copyOfRange(data, i, i+2);
            buffer.put(hamming.decode(toDecode));
        }

        return messageBuilder.extract(buffer.array());
    }
}
