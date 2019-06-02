import java.util.Arrays;

public class Message {
    public int id;
//    public int dataLen;
    public byte[] data;

    public Message(int id, byte[] data) {
        this.id = id;
//        this.dataLen = dataLen;
        this.data = data;
    }

    @Override
    public String toString() {
        return "Message{" +
                "id=" + id +
                ", data=" + new String(data) +
                '}';
    }
}
