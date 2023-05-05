package cn.itcast.nio.c4;

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

/**
 * @author tiancn
 * @date 2023/3/11 13:48
 */

/**
 *  处理消息的边界，半包黏包问题
 */
@Slf4j
public class Server04 {
    //处理半包黏包问题按照分隔符及逆行分割拆分
    //如果一个buffer存不下一个信息，那么将信息扩充
    private static void split(ByteBuffer source){
        //开启写模式
        source.flip();
        for (int i = 0; i < source.limit(); i++) {
            //找到一条完整消息 get(i) 不会移动移动读指针 get()会移动
            if (source.get(i) == '\n'){
                int length = i + 1 - source.position();
                //把这条完整消息存入新的ByteBuffer
                ByteBuffer target = ByteBuffer.allocate(length);
                //从source读，向target写 get()会移动移动读指针 ,都指针向前走
                for (int j = 0; j < length; j++) {
                    target.put(source.get());
                }
                debugAll(target);
            }
        }
        //开启写模式，compact会将buffer中还没有读的往前移动，写指针再这些未读内容后面。
        source.compact(); //如果source中的内容没有满足要求的 ，那么在执行这句话后 position =  limit 此时需要扩容
    }
    public static void main(String[] args) throws IOException {
        //1.创建selector ， 管理多个channel
        Selector selector = Selector.open();
        //创建服务器
        ServerSocketChannel ssc = ServerSocketChannel.open();
        ssc.configureBlocking(false);//非阻塞模式
        //2.建立selector和channel的联系（注册）
        //SelectionKey 就是将来事件发生后，通过它可以知道事件是哪个channel的事件
        SelectionKey sscKey = ssc.register(selector, 0, null);
        //sscKey只有一个
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

                    ByteBuffer buffer = ByteBuffer.allocate(16); // attachment
                    // 将一个 byteBuffer 作为附件关联到 selectionKey 上
                    //scKey有多个
                    SelectionKey scKey = sc.register(selector, 0, buffer);
                    scKey.interestOps(SelectionKey.OP_READ);
                    log.debug("{}", sc);
                    log.debug("scKey:{}", scKey);
                }
                else if (key.isReadable()){//如果是read
                    try {
                        SocketChannel channel = (SocketChannel) key.channel();// 拿到触发事件的channel

                        // 获取 selectionKey 上关联的附件
                        ByteBuffer buffer = (ByteBuffer) key.attachment();

                        int read = channel.read(buffer);//如果是正常断开，read的方法的返回值是 -1
                        if (read == -1){
                            key.cancel(); //取消处理
                        }else {
                            split(buffer);
                            // 一个buffer放不下一个消息时就需要扩容
                            if (buffer.position() == buffer.limit()){
                                //变为原来容量的2被
                                ByteBuffer newBuffer = ByteBuffer.allocate(buffer.capacity() * 2);
                                //开启读模式，将旧buffer的内容copy进新buffer中
                                buffer.flip();
                                newBuffer.put(buffer); // 0123456789abcdef3333\n
                                //并将新的buffer关联到selectionKey 上
                                key.attach(newBuffer);
                            }
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
