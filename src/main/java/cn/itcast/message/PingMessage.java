package cn.itcast.message;

/**
 * @author tiancn
 * @date 2023/3/17 16:07
 */
public class PingMessage extends Message{
    @Override
    public int getMessageType() {
        return PingMessage;
    }
}
