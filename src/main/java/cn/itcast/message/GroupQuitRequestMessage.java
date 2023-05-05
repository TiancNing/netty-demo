package cn.itcast.message;

import lombok.Data;
import lombok.ToString;

/**
 * @author tiancn
 * @date 2023/3/16 19:38
 */
@Data
@ToString(callSuper = true)
public class GroupQuitRequestMessage extends Message {
    private String groupName;

    private String username;

    public GroupQuitRequestMessage(String username, String groupName) {
        this.groupName = groupName;
        this.username = username;
    }

    @Override
    public int getMessageType() {
        return GroupQuitRequestMessage;
    }
}
