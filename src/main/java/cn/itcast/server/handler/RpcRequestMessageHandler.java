package cn.itcast.server.handler;

import cn.itcast.message.RpcRequestMessage;
import cn.itcast.message.RpcResponseMessage;
import cn.itcast.server.service.HelloService;
import cn.itcast.server.service.ServicesFactory;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @author tiancn
 * @date 2023/3/18 15:46
 */
@Slf4j
@ChannelHandler.Sharable
public class RpcRequestMessageHandler extends SimpleChannelInboundHandler<RpcRequestMessage> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RpcRequestMessage msg) throws Exception {
        System.out.println("test...............................");
        RpcResponseMessage response = new RpcResponseMessage();
        response.setSequenceId(msg.getSequenceId());
        try {
            HelloService service = (HelloService)ServicesFactory.getService(Class.forName(msg.getInterfaceName()));
            Method method = service.getClass().getMethod(msg.getMethodName(), msg.getParameterTypes());
            Object invoke = method.invoke(service, msg.getParameterValue());
            response.setReturnValue(invoke);
        }catch (Exception e){
            e.printStackTrace();
            String message = e.getCause().getMessage();
            response.setExceptionValue(new Exception("远程调用出错：" + message));
        }
        ctx.writeAndFlush(response);

    }

    public static void main(String[] args) throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        RpcRequestMessage message = new RpcRequestMessage(
                1,
                "cn.itcast.server.service.HelloService",
                "sayHello",
                String.class,
                new Class[]{String.class},
                new Object[]{"涨三"}
        );
        HelloService service = (HelloService)ServicesFactory.getService(Class.forName(message.getInterfaceName()));
        System.out.println(service);
        Method method = service.getClass().getMethod(message.getMethodName(), message.getParameterTypes());//反射获取方法
        Object invoke = method.invoke(service, message.getParameterValue());//执行方法
        System.out.println(invoke);
    }
}
