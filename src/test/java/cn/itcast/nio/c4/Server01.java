package cn.itcast.nio.c4;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;

import static cn.itcast.nio.c2.ByteBufferUtil.debugRead;

/**
 * @author tiancn
 * @date 2023/3/11 11:42
 */

/**
 * 阻塞模式
 */
@Slf4j
public class Server01 {
    public static void main(String[] args) throws IOException {
        //0.创建ByteBuffer
        ByteBuffer buffer = ByteBuffer.allocate(16);
        //1.创建服务器
        ServerSocketChannel ssc = ServerSocketChannel.open();

        //2.绑定监控端口
        ssc.bind(new InetSocketAddress(8080));

        //3.连接集合
        ArrayList<SocketChannel> channels = new ArrayList<>();

        while (true){
            //4.accept建立与客户端连接，SocketChannel用来与客户端之间通信
            log.debug("connecting...");
            SocketChannel sc = ssc.accept(); //阻塞方法 线程停止运行
            log.debug("connected... {}", sc);
            channels.add(sc);

            for (SocketChannel channel : channels) {
                //5.接收客户端发送的数据
                log.debug("before read... {}", channel);
                channel.read(buffer);//阻塞方法 ，线程停止运行
                buffer.flip();//读模式
                debugRead(buffer);
                buffer.clear();//写模式
                log.debug("after read...{}", channel);
            }
        }
    }
}
