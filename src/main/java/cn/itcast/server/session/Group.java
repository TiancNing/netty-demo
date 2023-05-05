package cn.itcast.server.session;

import lombok.Data;

import java.util.Collections;
import java.util.Set;

/**
 * @author tiancn
 * @date 2023/3/16 19:28
 */
@Data
/**
 * 聊天组，即聊天室
 */
public class Group {
    // 聊天室名称
    private String name;
    // 聊天室成员
    private Set<String> members;

    public static final Group EMPTY_GROUP = new Group("empty", Collections.emptySet());

    public Group(String name, Set<String> members) {
        this.name = name;
        this.members = members;
    }
}
