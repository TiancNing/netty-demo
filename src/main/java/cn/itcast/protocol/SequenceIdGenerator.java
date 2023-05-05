package cn.itcast.protocol;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author tiancn
 * @date 2023/3/18 20:04
 */
public abstract class SequenceIdGenerator {
    private static final AtomicInteger id = new AtomicInteger();
    public static int nextId(){
        return id.incrementAndGet();
    }
}
