package cn.itcast.nio.c2;

import java.nio.ByteBuffer;

import static cn.itcast.nio.c2.ByteBufferUtil.debugAll;

/**
 * @author tiancn
 * @date 2023/3/10 21:07
 */

/**
 * 黏包半包
 */
public class TestByteBufferExam {
    public static void main(String[] args) {
        ByteBuffer source = ByteBuffer.allocate(32);

        source.put("Hello,world\nI'm zhangsan\nHo".getBytes());
        split(source);

        source.put("w are you?\nhaha!\n".getBytes());
        split(source);

    }
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
        source.compact();
    }
}
