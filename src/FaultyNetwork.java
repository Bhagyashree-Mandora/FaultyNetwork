import java.io.IOException;

import static java.lang.Thread.sleep;

public class FaultyNetwork {
    public static void main(String[] args) throws IOException, InterruptedException {


        UdpClientSender sender1 = new UdpClientSender(1992);
        UdpClientSender sender2 = new UdpClientSender(1993);
        byte[] data1 = "Connecting from 1983".getBytes();
        sender1.sendData(data1, 1983);
//        sleep(5000);

        data1 = "Connecting from 1982".getBytes();
        sender2.sendData(data1, 1982);

        byte[] data2 = "Hello world from 1983".getBytes();
        sender1.sendData(data2, 1983);
        data2 = "Hello world from 1982".getBytes();
        sender2.sendData(data2, 1982);

//        sleep(10000);

        UdpClientReceiver receiver2 = new UdpClientReceiver(1993);
        UdpClientReceiver receiver1 = new UdpClientReceiver(1992);
        receiver1.start();
        receiver2.start();



    }
}
