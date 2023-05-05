package cn.itcast.server.handler;

import cn.itcast.message.GroupQuitRequestMessage;
import cn.itcast.message.GroupQuitResponseMessage;
import cn.itcast.server.session.Group;
import cn.itcast.server.session.GroupSessionFactory;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * @author tiancn
 * @date 2023/3/16 21:50
 */
@ChannelHandler.Sharable
public class GroupQuitRequestMessageHandler extends SimpleChannelInboundHandler<GroupQuitRequestMessage> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, GroupQuitRequestMessage msg) throws Exception {
        Group group = GroupSessionFactory.getGroupSession().removeMember(msg.getGroupName(), msg.getUsername());
        if (group != null){
            ctx.writeAndFlush(new GroupQuitResponseMessage(true,"已推出群" + msg.getGroupName()));
        }else {
            ctx.writeAndFlush(new GroupQuitResponseMessage(false,msg.getGroupName() + "群不存在"));
        }
    }
}
