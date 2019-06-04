public class Message {

    public static final String ACK_OPCODE = "A";
    public static final String DATA_MSG_OPCODE = "D";
    public int id;
    public String opcode;
    public byte[] data;

    public Message(int id, String opcode, byte[] data) {
        this.id = id;
        this.opcode = opcode;
        this.data = data;
    }
}
