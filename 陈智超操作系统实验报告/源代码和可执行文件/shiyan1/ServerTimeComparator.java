package com.chenzhichao.www.shiyan1;

import java.util.Comparator;

/**
 * @author ChenZhichao
 * @mail chenzhichaohh@163.com
 * @create 2020-06-18
 */
public class ServerTimeComparator implements Comparator<PCB> {
    // 短进程优先，按服务时间升序
    @Override
    public int compare(PCB pcb1, PCB pcb2) {
        return pcb1.serveTime - pcb2.serveTime;
    }
}
