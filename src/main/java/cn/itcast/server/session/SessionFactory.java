package cn.itcast.server.session;

/**
 * @author tiancn
 * @date 2023/3/16 19:28
 */
public abstract class SessionFactory {

    private static Session session = new SessionMemoryImpl();

    public static Session getSession() {
        return session;
    }
}
