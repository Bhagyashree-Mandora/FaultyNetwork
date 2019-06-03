import java.nio.ByteBuffer;
import java.util.Arrays;

public class MessageBuilder {

    public Message extract(byte[] data) {
//        if (data.length == MESSAGE_SIZE) {
            int id = data[0];
            int len = data[1];
            byte[] payloadData = Arrays.copyOfRange(data, 2, 2+len);
            return new Message(id, payloadData);
//        } else {
//            throw new Exception("Wrong UDP response size");
//        }
//        return null;
    }

    public byte[] build(int id, byte[] data) {
        ByteBuffer buffer = ByteBuffer.allocate(Constants.MESSAGE_SIZE);

        buffer.put((byte) id);

        int dataLen = data.length;
        buffer.put((byte) dataLen);

        byte[] nullPadding = new byte[Constants.DATA_BYTES_PER_MESSAGE - dataLen];
        buffer.put(data);
        buffer.put(nullPadding);

        return buffer.array();
    }
}
