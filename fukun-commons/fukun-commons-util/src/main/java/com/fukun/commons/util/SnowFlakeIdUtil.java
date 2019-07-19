package com.fukun.commons.util;

/**
 * 全局分布式id生成器（SnowFlake算法）
 * 分享地址：https://segmentfault.com/a/1190000011282426
 *
 * @author tangyifei
 */
public class SnowFlakeIdUtil {

    private long workerId;
    private long dataCenterId;
    private long sequence;

    private static SnowFlakeIdUtil single = new SnowFlakeIdUtil(1L, 1L, 0L);

    public static SnowFlakeIdUtil getInstance() {
        return single;
    }

    /**
     * 构造函数
     *
     * @param workerId     工作ID (0~31)
     * @param dataCenterId 数据中心ID (0~31)
     */
    private SnowFlakeIdUtil(long workerId, long dataCenterId, long sequence) {
        // sanity check for workerId
        if (workerId > maxWorkerId || workerId < 0) {
            throw new IllegalArgumentException(String.format("worker Id can't be greater than %d or less than 0", maxWorkerId));
        }
        if (dataCenterId > maxdataCenterId || dataCenterId < 0) {
            throw new IllegalArgumentException(String.format("datacenter Id can't be greater than %d or less than 0", maxdataCenterId));
        }
        System.out.printf("worker starting. timestamp left shift %d, datacenter id bits %d, worker id bits %d, sequence bits %d, workerid %d",
                timestampLeftShift, dataCenterIdBits, workerIdBits, sequenceBits, workerId);

        this.workerId = workerId;
        this.dataCenterId = dataCenterId;
        this.sequence = sequence;
    }

    private long twepoch = 1288834974657L;

    private long workerIdBits = 5L;
    private long dataCenterIdBits = 5L;
    private long maxWorkerId = -1L ^ (-1L << workerIdBits);
    private long maxdataCenterId = -1L ^ (-1L << dataCenterIdBits);
    private long sequenceBits = 12L;

    private long workerIdShift = sequenceBits;
    private long dataCenterIdShift = sequenceBits + workerIdBits;
    private long timestampLeftShift = sequenceBits + workerIdBits + dataCenterIdBits;
    private long sequenceMask = -1L ^ (-1L << sequenceBits);

    private long lastTimestamp = -1L;

//    public long getWorkerId() {
//        return workerId;
//    }
//
//    public long getdataCenterId() {
//        return dataCenterId;
//    }
//
//    public long getTimestamp() {
//        return System.currentTimeMillis();
//    }

    /**
     * 保证线程安全性
     *
     * @return
     */
//    public synchronized long nextId() {
//        long timestamp = timeGen();
//
//        if (timestamp < lastTimestamp) {
//            System.err.printf("clock is moving backwards.  Rejecting requests until %d.", lastTimestamp);
//            throw new RuntimeException(String.format("Clock moved backwards.  Refusing to generate id for %d milliseconds",
//                    lastTimestamp - timestamp));
//        }
//
//        if (lastTimestamp == timestamp) {
//            sequence = (sequence + 1) & sequenceMask;
//            if (sequence == 0) {
//                timestamp = tilNextMillis(lastTimestamp);
//            }
//        } else {
//            sequence = 0;
//        }
//
//        lastTimestamp = timestamp;
//        return ((timestamp - twepoch) << timestampLeftShift) |
//                (dataCenterId << dataCenterIdShift) |
//                (workerId << workerIdShift) |
//                sequence;
//    }
    public long nextId() {
        long timestamp = timeGen();

        if (timestamp < lastTimestamp) {
            System.err.printf("clock is moving backwards.  Rejecting requests until %d.", lastTimestamp);
            throw new RuntimeException(String.format("Clock moved backwards.  Refusing to generate id for %d milliseconds",
                    lastTimestamp - timestamp));
        }

        if (lastTimestamp == timestamp) {
            sequence = (sequence + 1) & sequenceMask;
            if (sequence == 0) {
                timestamp = tilNextMillis(lastTimestamp);
            }
        } else {
            sequence = 0;
        }

        lastTimestamp = timestamp;
        return ((timestamp - twepoch) << timestampLeftShift) |
                (dataCenterId << dataCenterIdShift) |
                (workerId << workerIdShift) |
                sequence;
    }

    private long tilNextMillis(long lastTimestamp) {
        long timestamp = timeGen();
        while (timestamp <= lastTimestamp) {
            timestamp = timeGen();
        }
        return timestamp;
    }

    private long timeGen() {
        return System.currentTimeMillis();
    }

    public static String defaultId() {
        return String.valueOf(getInstance().nextId());
    }

}
