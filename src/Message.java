public class Message {
    public int id;
//    public int dataLen;
    public byte[] data;

    public Message(int id, byte[] data) {
        this.id = id;
//        this.dataLen = dataLen;
        this.data = data;
    }
}
