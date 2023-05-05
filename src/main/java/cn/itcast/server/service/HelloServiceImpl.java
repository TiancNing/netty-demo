package cn.itcast.server.service;

/**
 * @author tiancn
 * @date 2023/3/18 15:39
 */
public class HelloServiceImpl implements HelloService{
    @Override
    public String sayHello(String msg) {
        //int i = 1/ 0;
        return "你好, " + msg;
    }
}
