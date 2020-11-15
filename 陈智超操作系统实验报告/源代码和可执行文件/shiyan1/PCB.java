package com.chenzhichao.www.shiyan1;

/**
 *
 * @author ChenZhichao
 * @mail chenzhichaohh@163.com
 * @create 2020-06-08
 */

public class PCB {
    // 进程名
    String pcbName;
    // 到达时间
    int arriveTime;
    // 服务时间
    int serveTime;
    // 开始时间
    int beginTime;
    // 结束时间
    int finshTime;
    // 周转时间
    int roundTime;
    // 带权周转时间
    double aveRoundTime;
    // 在时间轮转调度算法中，记录该进程真实服务时间已经用时的时长
    double clock=0;
    // 只用于最高响应比优先调度算法中，记录每个进程到达后的等待时间
    int waitTime;
    // 在轮转算法中标识开始时间是否第一次计算
    boolean firstTimeTag=false;

    public PCB() {
    }


    public PCB(String pcbName, int arriveTime, int serveTime, double priority) {
        super();
        this.pcbName = pcbName;
        this.arriveTime = arriveTime;
        this.serveTime = serveTime;
        this.waitTime=0;
    }

    @Override
    public String toString() {
        String info=new String("进程名："+"P" + this.pcbName);
        return info;
    }

}