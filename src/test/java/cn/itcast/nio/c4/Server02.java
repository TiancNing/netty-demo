package cn.itcast.nio.c4;

/**
 * @author tiancn
 * @date 2023/3/11 11:58
 */

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;

import static cn.itcast.nio.c2.ByteBufferUtil.debugRead;

/**
 * 非阻塞模式
 */
@Slf4j
public class Server02 {
    public static void main(String[] args) throws IOException {
        //0.创建ByteBuffer
        ByteBuffer buffer = ByteBuffer.allocate(16);
        //1.创建服务器
        ServerSocketChannel ssc = ServerSocketChannel.open();
        ssc.configureBlocking(false);//非阻塞模式

        //2.绑定监控端口
        ssc.bind(new InetSocketAddress(8080));

        //3.连接集合
        ArrayList<SocketChannel> channels = new ArrayList<>();

        while (true){
            //4.accept建立与客户端连接，SocketChannel用来与客户端之间通信
            //log.debug("connecting...");
            SocketChannel sc = ssc.accept(); //非阻塞，线程还会继续运行，如果没有连接建立，sc为null
            if (sc != null){
                log.debug("connected... {}", sc);
                sc.configureBlocking(false);//非阻塞模式
                channels.add(sc);
            }


            for (SocketChannel channel : channels) {
                //5接收客户端发送的数据
                //log.debug("before read... {}", channel);
                int read = channel.read(buffer);//非阻塞 ，线程仍然会运行，如果没有读到数据，read返回0
                if (read > 0){
                    buffer.flip();//读模式
                    debugRead(buffer);
                    buffer.clear();//写模式
                    log.debug("after read...{}", channel);
                }
            }
        }
    }
}
