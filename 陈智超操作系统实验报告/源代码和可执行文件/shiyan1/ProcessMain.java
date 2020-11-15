package com.chenzhichao.www.shiyan1;

/**
 * 主函数，启动类
 * @author ChenZhichao
 * @mail chenzhichaohh@163.com
 * @create 2020-06-08
 */
public class ProcessMain {
	public static void main(String[] args) {
		ProcessScheduling  processScheduling=new ProcessScheduling();
		System.out.println("**********************进程调度实验**********************");
		System.out.println("*******************网络工程18（4）班  陈智超**************");
		// 数据初始化
		processScheduling.initData();
		System.out.println();
		// 调用短进程优先算法
		processScheduling.SJF();
		System.out.println();
		// 调用时间片轮转算法
		processScheduling.RR();
		System.out.println();
		// 调用高响应比优先算法
		processScheduling.HRN();
	}


}
