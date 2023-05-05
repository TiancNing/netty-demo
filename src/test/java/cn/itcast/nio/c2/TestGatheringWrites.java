package cn.itcast.nio.c2;

/**
 * @author tiancn
 * @date 2023/3/10 20:50
 */

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;

/**
 * 多个 buffer 的数据填充至 channel
 */
public class TestGatheringWrites {
    public static void main(String[] args) {
        ByteBuffer b1 = StandardCharsets.UTF_8.encode("hello");
        ByteBuffer b2 = StandardCharsets.UTF_8.encode("world");
        ByteBuffer b3 = StandardCharsets.UTF_8.encode("你好");

        try(FileChannel channel = new RandomAccessFile("word2.txt", "rw").getChannel()){
            channel.write(new ByteBuffer[]{b1,b2,b3});

        }catch (IOException e){

        }

    }
}
