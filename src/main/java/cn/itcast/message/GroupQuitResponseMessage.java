package cn.itcast.message;

import lombok.Data;
import lombok.ToString;

/**
 * @author tiancn
 * @date 2023/3/16 19:39
 */
@Data
@ToString(callSuper = true)
public class GroupQuitResponseMessage extends AbstractResponseMessage {
    public GroupQuitResponseMessage(boolean success, String reason) {
        super(success, reason);
    }

    @Override
    public int getMessageType() {
        return GroupQuitResponseMessage;
    }
}
