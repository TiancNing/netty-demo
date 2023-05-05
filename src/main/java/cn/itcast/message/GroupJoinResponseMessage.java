package cn.itcast.message;

import lombok.Data;
import lombok.ToString;

/**
 * @author tiancn
 * @date 2023/3/16 19:38
 */
@Data
@ToString(callSuper = true)
public class GroupJoinResponseMessage extends AbstractResponseMessage {

    public GroupJoinResponseMessage(boolean success, String reason) {
        super(success, reason);
    }

    @Override
    public int getMessageType() {
        return GroupJoinResponseMessage;
    }
}
