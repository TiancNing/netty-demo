package cn.itcast.protocol;

import cn.itcast.message.Message;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageCodec;
import lombok.extern.slf4j.Slf4j;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.List;

/**
 * @author tiancn
 * @date 2023/3/16 19:42
 */
@Slf4j
public class MessageCodec extends ByteToMessageCodec<Message> {
    @Override
    public void encode(ChannelHandlerContext channelHandlerContext, Message msg, ByteBuf out) throws Exception {
        //1. 4字节的魔数
        out.writeBytes(new byte[]{1,2,3,4});
        //2. 1字节的版本
        out.writeByte(1);
        //3. 1字节的序列化方式 jdk 0 json 1
        out.writeByte(0);
        //4. 1字节的指令类型
        out.writeByte(msg.getMessageType());
        //5. 请求序号 4个字节
        out.writeInt(msg.getSequenceId());
        //无意义 对其填充 一个字节 为了让协议长度为2^n
        out.writeByte(0xff);
        // 6. 获取内容的字节数组 jdk序列化
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(bos);
        oos.writeObject(msg);
        byte[] bytes = bos.toByteArray();
        //7. 长度
        out.writeInt(bytes.length);
        //8. 写入内容
        out.writeBytes(bytes);
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        int magicNum = in.readInt();//魔术
        byte version = in.readByte();//版本号
        byte serializerType = in.readByte();//序列化算法
        byte messageType = in.readByte();//指令类型
        int sequenceId = in.readInt();//请求序号
        in.readByte();
        int length = in.readInt();//正文长度
        byte[] bytes = new byte[length];
        in.readBytes(bytes, 0, length);//消息正文
        ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(bytes));//反序列化
        Message message = (Message) ois.readObject();
        log.debug("{}, {}, {}, {}, {}, {}", magicNum, version, serializerType, messageType, sequenceId, length);
        log.debug("{}", message);
        out.add(message);//往下传
    }
}
