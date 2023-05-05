package cn.itcast.server.service;

/**
 * @author tiancn
 * @date 2023/3/16 19:21
 */
public abstract class UserServiceFactory {

    private static UserService userService = new UserServiceMemoryImpl();

    public static UserService getUserService() {
        return userService;
    }
}
