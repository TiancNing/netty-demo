package cn.itcast.nio.c4;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;

/**
 * @author tiancn
 * @date 2023/3/11 11:51
 */
public class Client {
    public static void main(String[] args) throws IOException {
        SocketChannel sc = SocketChannel.open();
        sc.connect(new InetSocketAddress("localhost",8080));
        SocketAddress address = sc.getLocalAddress();
        //sc.write(Charset.defaultCharset().encode("0123456789abcdefghijklmn\n321421\n"));
        System.out.println("waiting");
    }
}
