package cn.itcast.message;

import lombok.Data;
import lombok.ToString;

/**
 * @author tiancn
 * @date 2023/3/16 19:38
 */
@Data
@ToString(callSuper = true)
public class GroupJoinRequestMessage extends Message {
    private String groupName;

    private String username;

    public GroupJoinRequestMessage(String username, String groupName) {
        this.groupName = groupName;
        this.username = username;
    }

    @Override
    public int getMessageType() {
        return GroupJoinRequestMessage;
    }
}
