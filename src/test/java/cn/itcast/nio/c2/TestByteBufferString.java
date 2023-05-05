package cn.itcast.nio.c2;



import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

/**
 * @author tiancn
 * @date 2023/3/10 20:23
 */
public class TestByteBufferString {
    public static void main(String[] args) {
        //1、字符串转ByteBuffer
        ByteBuffer buffer1 = ByteBuffer.allocate(16);
        buffer1.put("hello".getBytes());
        ByteBufferUtil.debugAll(buffer1);

        //2.Charset //直接切换读模式
        ByteBuffer buffer2 = StandardCharsets.UTF_8.encode("hello");
        ByteBufferUtil.debugAll(buffer2);

        //3.wrap //直接切换读模式
        ByteBuffer buffer = ByteBuffer.wrap("hello".getBytes());

        //转为字符串
        String str1 = StandardCharsets.UTF_8.decode(buffer2).toString();
        System.out.println(str1);

        buffer1.flip();
        String str2 = StandardCharsets.UTF_8.decode(buffer1).toString();
        System.out.println(str2);
    }
}
