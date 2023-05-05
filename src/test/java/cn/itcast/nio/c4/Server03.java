package cn.itcast.nio.c4;

/**
 * @author tiancn
 * @date 2023/3/11 12:13
 */

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.Iterator;

import static cn.itcast.nio.c2.ByteBufferUtil.debugAll;
import static cn.itcast.nio.c2.ByteBufferUtil.debugRead;

/**
 * 使用Selector 多路复用
 */
@Slf4j
public class Server03 {
    public static void main(String[] args) throws IOException {
        //1.创建selector ， 管理多个channel
        Selector selector = Selector.open();
        //创建服务器
        ServerSocketChannel ssc = ServerSocketChannel.open();
        ssc.configureBlocking(false);//非阻塞模式
        //2.建立selector和channel的联系（注册）
        //SelectionKey 就是将来事件发生后，通过它可以知道事件是哪个channel的事件
        SelectionKey sscKey = ssc.register(selector, 0, null);
        sscKey.interestOps(SelectionKey.OP_ACCEPT); // key 只关注 accept 事件
        log.debug("sscKey:{}", sscKey);
        ssc.bind(new InetSocketAddress(8080));//绑定监控端口
        while (true){
            // 3. select 方法, 没有事件发生，线程阻塞，有事件，线程才会恢复运行
            // select 在事件未处理时，它不会阻塞, 事件发生后要么处理，要么取消，不能置之不理
            selector.select();
            // 4. 处理事件, selectedKeys 内部包含了所有发生的事件
            Iterator<SelectionKey> iter = selector.selectedKeys().iterator(); // accept, read
            while (iter.hasNext()){
                SelectionKey key = iter.next();
                log.debug("key: {}", key);

                //5.区分事件类型
                if (key.isAcceptable()){ //如果是accept
                    ServerSocketChannel channel = (ServerSocketChannel) key.channel();
                    SocketChannel sc = channel.accept();
                    //key.cancel(); //不处理就取消
                    sc.configureBlocking(false);

                    SelectionKey scKey = sc.register(selector, 0, null);
                    scKey.interestOps(SelectionKey.OP_READ);
                    log.debug("{}", sc);
                    log.debug("scKey:{}", scKey);
                }
                else if (key.isReadable()){//如果是read
                    try {
                        SocketChannel channel = (SocketChannel) key.channel();// 拿到触发事件的channel
                        ByteBuffer buffer = ByteBuffer.allocate(4);
                        int read = channel.read(buffer);//如果是正常断开，read的方法的返回值是 -1
                        if (read == -1){
                            key.cancel(); //取消处理
                        }else {
                            buffer.flip();
                            //debugRead(buffer);
                            System.out.println(Charset.defaultCharset().decode(buffer));
                        }
                    }catch (IOException e){
                        e.printStackTrace();
                        key.cancel();// 因为客户端断开了,因此需要将 key 取消
                    }

                }
                // 处理key 时，要从 selectedKeys 集合中删除，否则下次处理就会有问题
                iter.remove();

            }

        }
    }
}
