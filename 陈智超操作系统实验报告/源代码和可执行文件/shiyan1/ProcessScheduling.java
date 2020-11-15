package com.chenzhichao.www.shiyan1;

/**
 * 进程调度主要实现类
 * @author ChenZhichao
 * @mail chenzhichaohh@163.com
 * @create 2020-06-08
 */

import java.util.*;

public class ProcessScheduling {
    // 当前正在执行的进程
    PCB nowProess;

    // 存放所有进程的集合
    ArrayList<PCB> pcb;

    // 由于LinkedList集合类是按进入顺序排序的
    // 因此可以当做队列来存放已经进入就绪队列的进程的集合
    LinkedList<PCB> waitingQueue;

    // 存放已经完成的进程，用于打印输出
    ArrayList<PCB> newPCBList;

    /**
     * 封装产生随机数 1 - 6的函数
     */
    private int getRandom() {
        Random random = new Random();
        int randomNumber  = random.nextInt(5) + 1;
        return randomNumber;
    }

    // 构造初始化的数据
    public void initData() {
        pcb = new ArrayList<PCB>();
        waitingQueue = new LinkedList<PCB>();
        newPCBList = new ArrayList<PCB>();

        // 四个参数分别是进程名字、到达时间、服务时间、最高响应比的优先级
        PCB p1 = new PCB("1",         0  ,  getRandom() ,  getRandom());
        PCB p2 = new PCB("2",          getRandom()  ,   getRandom(),  getRandom());
        PCB p3 = new PCB("3",          getRandom(),   getRandom(),  getRandom());
        PCB p4 = new PCB("4",          getRandom(),   getRandom() ,  getRandom());
        PCB p5 = new PCB("5",          getRandom() ,   getRandom() ,  getRandom());
        pcb.add(p1);pcb.add(p2);pcb.add(p3);pcb.add(p4);pcb.add(p5);
        //先将jcb排序，便于下面的算法实现，就不需要再定义一个标识进程是否已到达的boolean,即无需每次都从头开始扫描jcb容器，
        //而是用一个K记录下当前已经扫描到的位置，一次遍历即可，提高了算法效率。
        Collections.sort(pcb, new EqArriveTimeEerverTimeComparator());

    }

    /**
     * 短作业优先算法
     */
    public void SJF() {
        // 清空newPCBList容器内的内容，方便存储各种算法的结果并展示
        newPCBList.clear();
        // 创建进程操作类，
        ProcessQueueControl processQueueControl = new ProcessQueueControl();
        // 将到达的进程从队尾依次放入就绪队列waitingQueue
        processQueueControl.EnqueueLast();
        System.out.println("==================短作业优先算法=====================");
        while (!waitingQueue.isEmpty()) {
            //打印就绪队列中的进程
            processQueueControl.printfWaitingProcess();
            //出队
            processQueueControl.Dequeue();
            //已到达的进程入队
            processQueueControl.EnqueueLast();
            //可能有新的进程到达，重新使用外部比较器按短作业优先的顺序对就绪队列进行排序
            Collections.sort(waitingQueue, new ServerTimeComparator());
        }
        printPingJunZhouZhuan();
    }

    /**
     * 时间片轮转算法，每个时间片为1
     */
    public void RR() {
        newPCBList.clear();//清空newPCBList容器内的内容，方便存储各种算法的结果并展示
        ProcessQueueControl processQueueControl = new ProcessQueueControl();
        processQueueControl.EnqueueLast();
        System.out.println("==================时间片轮转调度算法=====================");
        while (!waitingQueue.isEmpty()) {
            //打印当前队列中的进程
            processQueueControl.printfWaitingProcessRR();
            //进程出队，一次一个，也就是调度改进程，出队调度完成后要是进程还未结束则继续从队尾入队
            processQueueControl.Dequeue(1);
        }
        // 打印平均周转时间
        printPingJunZhouZhuan();
    }

    /**
     * 高响应比优先算法
     */
    public void HRN() {
        //清空newPCBList容器内的内容，方便存储各种算法的结果并展示
        newPCBList.clear();
        ProcessQueueControl pq = new ProcessQueueControl();
        pq.EnqueueLast();
        System.out.println("==================高响应比优先调度算法=====================");
        while (!waitingQueue.isEmpty()) {
            //打印当前就绪队列中的进程
            pq.printfWaitingProcess();
            //出队，一次一个
            pq.Dequeue();
            //已到达的进程入队
            pq.EnqueueLast();
            //使用外部比较比对就绪队列中的进程按响应比进行排序
            Collections.sort(waitingQueue, new PriorityComparator());
        }
        // 打印输出平均周转时间
        printPingJunZhouZhuan();
    }

    class ProcessQueueControl {
        int k = 0;// PCB中的进程遍历时的下标
        int nowTime = 0;// 当前时间
        double sliceTime;//轮转调度时间片
        int i = 0;//记录当前出入队列的次数
        //进程首次入队，可一次进多个,从队尾进入
        public void EnqueueLast() {
            while (k < pcb.size()) {//当遍历完PCB中的所有进程时结束
                if (pcb.get(k).arriveTime <= nowTime) {//已经到达的进程按到达时间先后进入队列
                    waitingQueue.addLast(pcb.get(k));
                    k++;
                } else {
                    break;//如果该进程还未入队，即先结束遍历，保留当前下标k值，注意：此处不要k--；
                }
            }
        }

        public void EnqueueFirst() {//进程首次入队，可一次进多个,从队首进入
            while (k < pcb.size()) {//当遍历完PCB中的所有进程时结束
                if (pcb.get(k).arriveTime <= nowTime) {//已经到达的进程按到达时间先后进入队列
                    waitingQueue.addFirst(pcb.get(k));
                    k++;
                } else {
                    break;//如果该进程还未入队，即先结束遍历，保留当前下标k值，注意：此处不要k--；
                }
            }
        }

        public void Dequeue() {//进程出队，一次只出一个
            nowProess = waitingQueue.removeFirst();//移除队列的队首元素并且返回该对象元素
            nowProess.beginTime = nowTime;//计算开始时间，即为上一个进程的结束时间
            nowProess.finshTime = nowProess.beginTime + nowProess.serveTime;//计算结束时间，该进程开始时间+服务时间
            nowProess.roundTime = nowProess.finshTime - nowProess.arriveTime;//计算周转时间
            nowProess.aveRoundTime = (double) nowProess.roundTime / nowProess.serveTime;//计算平均周转时间
            nowTime = nowProess.finshTime;//获得结束时间，即当前时间，方便判断剩下的进程是否已到达
            newPCBList.add(nowProess);//经处理过数据后加入newPCBList容器

            // 打印已经运行完的进程
            printALLProcess();
            for (int i = 0; i < waitingQueue.size(); ++i) {
                waitingQueue.get(i).waitTime++;//所有进入等待队列的进程等待时间+1,此处只为最高响应比算法所用
            }
            System.out.println();
        }

        // 轮转调度算法的出队，sliceTime为时间片，每个时间片为1
        public void Dequeue(double sliceTime) {
            //移除队列的队首元素并且返回该对象元素
            nowProess = waitingQueue.removeFirst();
            if (nowProess.firstTimeTag == false) {
                /*轮转调度进程可能会多次反复进出队列，不像FCFS和SJF的进程只会进出一次，所以计算开始时间可以设个标志位，让每个进程在
                 * 第一次执行时记录一遍即可*/
                nowProess.beginTime = nowTime;//进程开始执行的时间
                nowProess.firstTimeTag = true;//计算第一次即可，下次无需更新计算
            }
            //每次出队，用时一个时间片，更新当前时间
            nowTime += sliceTime;
            //更新当前出队列的进程已服务时间
            nowProess.clock += sliceTime;
            if (nowProess.clock >= nowProess.serveTime) {
                //计算该进程完成时间
                nowProess.finshTime = nowTime;
                //计算周转时间
                nowProess.roundTime = nowProess.finshTime - nowProess.arriveTime;
                //计算平均周转时间
                nowProess.aveRoundTime = (double) nowProess.roundTime / nowProess.serveTime;
                //经处理过数据后加入newPCBList容器
                newPCBList.add(nowProess);
                //打印已经调度完成的进程
                printALLProcess();
                //已到达的进程先入队
                EnqueueFirst();
            } else {
                //已到达的进程先入队
                EnqueueFirst();
                //上一轮出的再紧接着进入队尾
                waitingQueue.addLast(nowProess);
            }
        }

        //打印就绪对队列中的进程
        public void printfWaitingProcess() {
            i++;
            System.out.println("第" + i + "个时刻就绪队列中的进程：" + waitingQueue);
            System.out.println();
            // 调度队头的进程
            System.out.println("调度进程：P" + waitingQueue.getFirst().pcbName);
            System.out.println("正在运行中：P" +waitingQueue.getFirst().pcbName);
            System.out.println("P" +waitingQueue.getFirst().pcbName + "   调度完成");
            System.out.println();

        }
        //打印就绪对队列中的进程，专用于轮转算法
        public void printfWaitingProcessRR() {
            i++;
            System.out.println("第" + i + "个时间片就绪队列中的进程：" + waitingQueue);
            System.out.println();
            // 调度队头的进程
            System.out.println("调度进程：P" + waitingQueue.getFirst().pcbName);
            System.out.println("正在运行中：P" +waitingQueue.getFirst().pcbName);
            System.out.println("P" +waitingQueue.getFirst().pcbName + "已经用完第 " + i +  " 个时间片");
            System.out.println();

        }
    }


    /**
     * 打印已经完成的调度
     */
    public void printALLProcess() {
        System.out.println("输出全部运行完的进程：");
        System.out.println("进程名   到达时间    服务时间     开始时间    完成时间    周转时间    带权周转时间");
        for (int i = 0; i < newPCBList.size(); ++i) {
            System.out.println("P" + newPCBList.get(i).pcbName + "       " + newPCBList.get(i).arriveTime + "          " +
                    newPCBList.get(i).serveTime + "         " + newPCBList.get(i).beginTime + "         " + newPCBList.get(i).finshTime +
                    "         " + newPCBList.get(i).roundTime + "        " + newPCBList.get(i).aveRoundTime);
        }
    }

    /**
     * 打印平均周转时间
     */
    public void printPingJunZhouZhuan() {
        double allTime = 0;
        for (PCB pcb1 : newPCBList) {
            allTime += pcb1.roundTime;
        }
        double avageTime = allTime / newPCBList.size();
        System.out.println("平均周转时间:" + avageTime);
    }
}


