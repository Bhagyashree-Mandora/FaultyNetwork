import java.nio.ByteBuffer;
import java.util.Arrays;

public class MessageEncoder {

    private static final int ENCODE_SIZE = 129;
    private static final int DATA_SIZE = 127;

    public Message decode(byte[] data) {
//        if (data.length == 258) {
            int id = data[0];
            int len = data[1];
            byte[] payloadData = Arrays.copyOfRange(data, 2, 2+len);
            return new Message(id, payloadData);
//        } else {
//            throw new Exception("Wrong UDP response size");
//        }
//        return null;
    }

    public byte[] encode(int id, byte[] data) {
        ByteBuffer buffer = ByteBuffer.allocate(ENCODE_SIZE);

        buffer.put((byte) id);

        int dataLen = data.length;
        buffer.put((byte) dataLen);

        byte[] nullPadding = new byte[DATA_SIZE - dataLen];
        buffer.put(data);
        buffer.put(nullPadding);

        return buffer.array();
    }
}
