package com.chenzhichao.www.shiyan1;

import java.util.Comparator;

/**
 * @author ChenZhichao
 * @mail chenzhichaohh@163.com
 * @create 2020-06-18
 */
public class PriorityComparator implements Comparator<PCB> {
    //外部比较器，按响应比升序排序
    @Override
    public int compare(PCB o1, PCB o2) {
        double r1 = (double) o1.waitTime / o1.serveTime;
        double r2 = (double) o2.waitTime / o2.serveTime;
        return r1 > r2 ? 1 : -1;
    }

}
