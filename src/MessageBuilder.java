import java.nio.ByteBuffer;
import java.util.Arrays;

public class MessageBuilder {


    public Message extract(byte[] data) {
//        if (data.length == MESSAGE_SIZE) {
            int id = data[0];
            String opcode = String.valueOf((char) data[1]);
            int len = data[2];
            byte[] payloadData = Arrays.copyOfRange(data, 3, 3+len);
            return new Message(id, opcode, payloadData);
//        } else {
//            throw new Exception("Wrong UDP response size");
//        }
    }

    public byte[] build(int id, byte[] data, String opcode) {
        ByteBuffer buffer = ByteBuffer.allocate(Constants.MESSAGE_SIZE);

        buffer.put((byte) id);
        buffer.put(opcode.getBytes()[0]);

        int dataLen = data.length;
        buffer.put((byte) dataLen);

        byte[] nullPadding = new byte[Constants.DATA_BYTES_PER_MESSAGE - dataLen];
        buffer.put(data);
        buffer.put(nullPadding);

        return buffer.array();
    }
}
