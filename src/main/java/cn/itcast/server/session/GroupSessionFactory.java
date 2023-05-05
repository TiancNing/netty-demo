package cn.itcast.server.session;

/**
 * @author tiancn
 * @date 2023/3/16 19:29
 */
public abstract class GroupSessionFactory {

    private static GroupSession session = new GroupSessionMemoryImpl();

    public static GroupSession getGroupSession() {
        return session;
    }
}
