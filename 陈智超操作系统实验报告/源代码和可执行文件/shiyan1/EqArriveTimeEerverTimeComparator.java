package com.chenzhichao.www.shiyan1;

import java.util.Comparator;

/**
 * @author ChenZhichao
 * @mail chenzhichaohh@163.com
 * @create 2020-06-18
 */
public class EqArriveTimeEerverTimeComparator implements Comparator<PCB> {
    /**
     * 外部比较器，按照服务时间排序
     * 按到达时间升序，若到达时间相同，按服务时间升序
     */
    @Override
    public int compare(PCB o1, PCB o2) {
        int a = o1.arriveTime - o2.arriveTime;
        if (a > 0) {
            return 1;
        }
        else if (a == 0) {
            return o1.serveTime > o2.serveTime ? 1 : -1;
        } else {
            return -1;
        }
    }
}
