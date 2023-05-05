package cn.itcast.server.handler;

import cn.itcast.message.GroupJoinRequestMessage;
import cn.itcast.message.GroupJoinResponseMessage;
import cn.itcast.server.session.Group;
import cn.itcast.server.session.GroupSessionFactory;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * @author tiancn
 * @date 2023/3/16 21:51
 */
@ChannelHandler.Sharable
public class GroupJoinRequestMessageHandler extends SimpleChannelInboundHandler<GroupJoinRequestMessage> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, GroupJoinRequestMessage msg) throws Exception {
        String username = msg.getUsername();
        String groupName = msg.getGroupName();
        Group group = GroupSessionFactory.getGroupSession().joinMember(groupName, username);
        if (group != null){
            ctx.writeAndFlush(new GroupJoinResponseMessage(true,msg.getGroupName() + "群加入成功"));
        }else {
            ctx.writeAndFlush(new GroupJoinResponseMessage(false,msg.getGroupName() + "群不存在"));
        }

    }
}
