import java.io.IOException;
import java.net.DatagramSocket;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.util.Arrays;

public class Controller {
    private static final int MAX_RETRIES = 200;
    private static final int ACK_COUNT = 30;
    private static final int MSG_ID_MAX = 128;

    private String name;
    private MessageBuilder messageBuilder;
    private Hamming hamming;
    private UdpClientSender udpSender;
    private int sendToPort;
    private int lastReceivedMsgId;
    private int lastReceivedAckId;
    private ChatWindow window;
    private int idCount = 1;


    public Controller(String name, DatagramSocket senderSocket, int sendToPort, ChatWindow window) throws UnknownHostException {
        this.name = name;
        messageBuilder = new MessageBuilder();
        hamming = new Hamming();
        udpSender = new UdpClientSender(senderSocket);
        this.sendToPort = sendToPort;
        this.window = window;
    }

    public void connect(byte[] data) {
        try {
            byte[] finalMessage = wrap(data, 0, Message.DATA_MSG_OPCODE);
            udpSender.sendData(finalMessage, sendToPort);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void send(byte[] data) {
//        int idCount = 1;
        try {
            for (int i = 0; i < data.length; i = i + Constants.DATA_BYTES_PER_MESSAGE) {
                int end = i + Constants.DATA_BYTES_PER_MESSAGE;
                if (end > data.length) {
                    end = data.length;
                }
                byte[] chunk = Arrays.copyOfRange(data, i, end);
                byte[] finalMessage = wrap(chunk, idCount, Message.DATA_MSG_OPCODE);

                int retryCount = 0;
//                System.out.println("Starting send with idCount " + idCount + "\n");
                while (retryCount < MAX_RETRIES && lastReceivedAckId != idCount) {
                    udpSender.sendData(finalMessage, sendToPort);
//                    System.out.println(name + ": data sent");
                    Thread.sleep(200);
                    if (lastReceivedAckId == idCount) {
                        break;
                    }
//                    System.out.print(retryCount + " ");
                    retryCount++;
                }
//                System.out.println();
                if (retryCount == MAX_RETRIES) {
//                    System.out.println("Message exceeded max send retries");
                }
                idCount = (idCount + 1) % MSG_ID_MAX;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private byte[] wrap(byte[] chunk, int idCount, String opcode) {
        byte[] messageChunk = messageBuilder.build(idCount, chunk, opcode);
        ByteBuffer encodedChunk = ByteBuffer.allocate(Constants.HAMMING_ENCODED_SIZE);

        for (byte aByte : messageChunk) {
            byte[] encoded = hamming.encode(aByte);
            encodedChunk.put(encoded);
        }

        return encodedChunk.array();
    }

    public void receive(byte[] data) throws IOException {
        Message response = unWrap(data);
//        System.out.println(name+": In receive..");
        if (response.opcode.equals(Message.ACK_OPCODE)) {
//            System.out.println("Got ack..");
            // This is ack message
            if(((lastReceivedAckId+1)%128) <= response.id){
                lastReceivedAckId = response.id;
            }
            return;
        }
        // This is normal message receive
        byte[] ack = wrap(new byte[0], response.id, Message.ACK_OPCODE);
        for(int i=0; i<ACK_COUNT; i++){
            udpSender.sendData(ack, sendToPort);
        }
        if (((lastReceivedMsgId + 1) % MSG_ID_MAX) <= response.id) {
            System.out.print(new String(response.data));
            if(window != null){
                window.updateText(new String(response.data), name);
            }
            lastReceivedMsgId = response.id;
        }
    }

    private Message unWrap(byte[] data) {
        ByteBuffer buffer = ByteBuffer.allocate(Constants.MESSAGE_SIZE);

        for (int i = 0; i < data.length; i = i + 2) {
            byte[] toDecode = Arrays.copyOfRange(data, i, i + 2);
            buffer.put(hamming.decode(toDecode));
        }

        return messageBuilder.extract(buffer.array());
    }
}
