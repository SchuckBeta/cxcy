package com.oseasy.util.common.utils;

/**
 * 雪花算法.
 *      1位，不用。二进制中最高位为1的都是负数，但是我们生成的id一般都使用整数，所以这个最高位固定是0
 *      41位，用来记录时间戳（毫秒）。
 *      41位可以表示个数字，
 *      如果只用来表示正整数（计算机中正数包含0），可以表示的数值范围是：0 至 ，减1是因为可表示的数值范围是从0开始算的，而不是1。
 *      也就是说41位可以表示个毫秒的值，转化成单位年则是年
 *
 *      10位，用来记录工作机器id。
 *      可以部署在个节点，包括5位dcenterid和5位wkid
 *      5位（bit）可以表示的最大正整数是，即可以用0、1、2、3、....31这32个数字，来表示不同的datecenterId或wkid
 *
 *      12位，序列号，用来记录同毫秒内产生的不同id。
 *      12位（bit）可以表示的最大正整数是，即可以用0、1、2、3、....4094这4095个数字，来表示同一机器同一时间截（毫秒)内产生的4095个ID序号
 *
 *      由于在Java中64bit的整数是long类型，所以在Java中SnowFlake算法生成的id就是long来存储的。
 *
 *      SnowFlake可以保证：
 *
 *      所有生成的id按时间趋势递增
 *      整个分布式系统内不会产生重复id（因为有dcenterid和wkid来做区分）
 * Created by Administrator on 2019/7/10 0010.
 */
public class IdSnow {
    private long twepoch = 1288834974657L;
    private long wkidBits = 5L;
    private long dcenteridBits = 5L;
    private long maxwkid = -1L ^ (-1L << wkidBits);
    private long maxdcenterid = -1L ^ (-1L << dcenteridBits);
    private long sequenceBits = 12L;
    private long wkidShift = sequenceBits;
    private long dcenteridShift = sequenceBits + wkidBits;
    private long timestampLeftShift = sequenceBits + wkidBits + dcenteridBits;
    private long sequenceMask = -1L ^ (-1L << sequenceBits);
    private long lastTimestamp = -1L;


    private long wkid;//工作机器ID
    private long dcenterid;//
    private long sequence;//序列

    public IdSnow(long sequence){
        // sanity check for wkid
        if (wkid > maxwkid || wkid < 0) {
            throw new IllegalArgumentException(String.format("worker Id can't be greater than %d or less than 0",maxwkid));
        }
        if (dcenterid > maxdcenterid || dcenterid < 0) {
            throw new IllegalArgumentException(String.format("datacenter Id can't be greater than %d or less than 0",maxdcenterid));
        }
        System.out.printf("worker starting. timestamp left shift %d, datacenter id bits %d, worker id bits %d, sequence bits %d, wkid %d",
                timestampLeftShift, dcenteridBits, wkidBits, sequenceBits, wkid);

        this.wkid = 1;
        this.dcenterid = 1;
        this.sequence = sequence;
    }

    public IdSnow(long wkid, long dcenterid, long sequence){
        // sanity check for wkid
        if (wkid > maxwkid || wkid < 0) {
            throw new IllegalArgumentException(String.format("worker Id can't be greater than %d or less than 0",maxwkid));
        }
        if (dcenterid > maxdcenterid || dcenterid < 0) {
            throw new IllegalArgumentException(String.format("datacenter Id can't be greater than %d or less than 0",maxdcenterid));
        }
        System.out.printf("worker starting. timestamp left shift %d, datacenter id bits %d, worker id bits %d, sequence bits %d, wkid %d",
                timestampLeftShift, dcenteridBits, wkidBits, sequenceBits, wkid);

        this.wkid = wkid;
        this.dcenterid = dcenterid;
        this.sequence = sequence;
    }

    public long getWkid(){
        return wkid;
    }

    public long getDcenterid(){
        return dcenterid;
    }

    public long getTimestamp(){
        return System.currentTimeMillis();
    }

    public synchronized long nextId() {
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
                (dcenterid << dcenteridShift) |
                (wkid << wkidShift) |
                sequence;
    }

    private long tilNextMillis(long lastTimestamp) {
        long timestamp = timeGen();
        while (timestamp <= lastTimestamp) {
            timestamp = timeGen();
        }
        return timestamp;
    }

    private long timeGen(){
        return System.currentTimeMillis();
    }

    //---------------测试---------------
    public static void main(String[] args) {
        IdSnow worker = new IdSnow(1);
//        IdSnow worker = new IdSnow(1,1,1);
        for (int i = 0; i < 30; i++) {
            System.out.println(worker.nextId());
        }
    }
}
