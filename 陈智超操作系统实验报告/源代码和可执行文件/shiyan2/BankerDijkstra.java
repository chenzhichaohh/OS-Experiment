package com.chenzhichao.www.shiyan2;

import java.util.ArrayList;
import java.util.Scanner;

/**
 * @author ChenZhichao
 * @mail chenzhichaohh@163.com
 * @create 2020-06-17
 */
public class BankerDijkstra {
    //资源最大需求
    private int[][] max;
    //已经分配的资源
    private int[][] allocation;
    //需要的资源
    private int[][] need;
    //剩余的资源
    private int[][] available;
    //执行资源
    private int[][] work;
    //目前剩余的资源
    private int[][] workAllocation;
    //是否安全
    private boolean[] finish;
    //存入满足当前需求的资源序号
    private ArrayList processNumList;

    private Scanner scanner;

    //进程
    private String[] processes;
    //资源
    private Resource[] resources;
    //最大的资源以及进程数
    private int MAX_VALUE = 20;
    //真实进程个数
    private int processesSize;
    //真实资源个数
    private int resourcesSize;      


    //副本数据, 用于检测资源是否符合要求
    //已经分配的资源
    private int[][] allocationTmp;
    //需要的资源
    private int[][] needTmp;
    //剩余的资源
    private int[][] availableTmp;
    //进程
    private String[] processesTmp;  


    /**
     * 初始化
     */
    public BankerDijkstra() {
        this.max = new int[MAX_VALUE][MAX_VALUE];
        this.allocation = new int[MAX_VALUE][MAX_VALUE];
        this.need = new int[MAX_VALUE][MAX_VALUE];
        this.available = new int[1][MAX_VALUE];
        this.work = new int[MAX_VALUE][MAX_VALUE];
        this.workAllocation = new int[MAX_VALUE][MAX_VALUE];
        this.finish = new boolean[MAX_VALUE];
        this.processes = new String[MAX_VALUE];
        this.resources = new Resource[MAX_VALUE];
        this.scanner = new Scanner(System.in);
        this.processNumList = new ArrayList();

        //副本
        this.allocationTmp = new int[MAX_VALUE][MAX_VALUE];
        this.needTmp = new int[MAX_VALUE][MAX_VALUE];
        this.availableTmp = new int[MAX_VALUE][MAX_VALUE];
        this.processesTmp = new String[MAX_VALUE];
    }

    public static void main(String[] args) {

        BankerDijkstra dijkstra = new BankerDijkstra();
        dijkstra.menu();
    }


    /**
     * 菜单
     */
    private void menu() {
        System.out.println("银行家算法");
        System.out.println("网络工程18（4）班    陈智超");
        while (true) {
            System.out.println("---------------------------");
            System.out.println("请选择：");
            System.out.println("0.初始化测试数据");
            System.out.println("1.请求资源分配");
            System.out.println("2.退出程序");
            System.out.println("---------------------------");
            int i = scanner.nextInt();
            switch (i) {
                case 0:
                    initData();
                    break;
                case 1:
                    requset();
                    break;
                case 2:
                    return;
                default:
                    System.out.println("无效输入！");
                    break;

            }
        }
    }

    /**
     * 初始化数据
     */
    private void initData() {
        int proceNum = 5;   //进程个数为为5
        int k = 0;
        while (processesSize < proceNum) {
            String[] proceName = {"P0", "P1", "P2", "P3", "P4"};
            this.processes[processesSize++] = proceName[k++];
        }
        int resouNum = 3;   //资源种类为3，A B C
        int m = 0;
        while (resourcesSize < resouNum) {
            String[] resouName = {"A", "B", "C"};
            // A B C资源个数分别为 10 15 12
            int[] size = {10, 15, 12};
            this.resources[resourcesSize++] = new Resource(resouName[m], size[m]);
            m++;
        }


        for (int i = 0; i < this.processesSize; i++) {
            for (int j = 0; j < this.resourcesSize; j++) {
                // 随机产生已分配的资源
                allocation[i][j] = (int) (Math.random() * 4);
            }
        }
        // 随机产生需要的资源
        for (int i = 0; i < this.processesSize; i++) {
            for (int j = 0; j < this.resourcesSize; j++) {
                need[i][j] = (int) (Math.random() * 4);
            }
        }
        // 获取资源分配表
        getResourceTable();
    }


    /**
     * 求出当前的资源分配表
     */
    private void getResourceTable() {
        for (int i = 0; i < this.processesSize; i++) {
            for (int j = 0; j < this.resourcesSize; j++) {
                max[i][j] = allocation[i][j] + need[i][j];
            }
        }

        //求每个进程分出去的总和
        int[] resour_sun = new int[this.resourcesSize]; //每个进程分出去的总和数组
        int tmp = 0;
        while (tmp < this.resourcesSize) {
            for (int i = 0; i < this.processesSize; i++) {
                resour_sun[tmp] += allocation[i][tmp];
            }
            tmp++;
        }
        //求出avaiable
        for (int i = 0; i < this.resourcesSize; i++) {
            available[0][i] = resources[i].resourceSize - resour_sun[i];
        }
        copy();
        allocation();
    }

    /**
     * 给副本赋值
     */
    private void copy() {
        //给副本赋值
        for (int i = 0; i < this.processesSize; i++) {
            for (int j = 0; j < this.resourcesSize; j++) {
                allocationTmp[i][j] = allocation[i][j];
                needTmp[i][j] = need[i][j];
            }
        }
        for (int i = 0; i < this.resourcesSize; i++) {
            availableTmp[0][i] = available[0][i];
        }
        for (int i = 0; i < this.processesSize; i++) {
            processesTmp[i] = processes[i];
        }
    }

    /**
     * 展示结果
     */
    private void display() {
        resReturn();
        System.out.println("当前资源情况：");
        System.out.println("进程名" + "       Max    " + "      allocation    " + "     need    " + "     available     ");
        // index为resources数组的下标
        int index = 0;
        while (index < 4) {
            System.out.print("          ");
            for (int i = 0; i < this.resourcesSize; i++) {
                System.out.print(resources[i].resourceName + " ");
            }
            index++;
        }
        System.out.println();
        for (int i = 0; i < this.processesSize; i++) {
            System.out.print(processes[i] + "        ");
            for (int j = 0; j < this.resourcesSize; j++) {
                System.out.print(max[i][j] + " ");
            }
            System.out.print("          ");
            for (int j = 0; j < this.resourcesSize; j++) {
                System.out.print(allocation[i][j] + " ");
            }
            System.out.print("          ");
            for (int j = 0; j < this.resourcesSize; j++) {
                System.out.print(need[i][j] + " ");
            }
            System.out.print("        ");
            if (i == 0) {
                for (int j = 0; j < this.resourcesSize; j++) {
                    System.out.print(available[0][j] + " ");
                }
            }
            System.out.println();
        }
        System.out.println();

    }

    /**
     * 分配函数，安全性检测
     */
    public void allocation() {
        // 先清空集合
        processNumList.clear();
        for (int i = 0; i < this.processesSize; i++) {
            finish[i] = false;
        }
        for (int i = 0; i < this.resourcesSize; i++) {
            work[0][i] = availableTmp[0][i];
        }
        int count = 0;
        // processNumList中的个数
        int flag = 0; 
        while (count < this.processesSize) {
            for (int i = 0; i < this.processesSize; i++) {
                if (processNumList.contains(i)) {
                    continue;
                } else {
                    int k = 0;
                    while (k < this.resourcesSize) {
                        if (needTmp[i][k] <= work[count][k] && (this.finish[count] == false)) {
                            k++;
                        } else {
                            k = this.resourcesSize + 1;
                        }
                    }
                    if (k == this.resourcesSize) {
                        processNumList.add(i);
                        flag++;
                        // 每次添加一个
                        break;  
                    }
                }
            }

            
            // 每走一遍for循环应该找到一个，才能往下执行
            if ((count + 1) == flag) {
                for (int i = 0; i < this.resourcesSize; i++) {
                    workAllocation[count][i] = work[count][i] + allocationTmp[(int) processNumList.get(count)][i];
                }
                this.finish[count] = true;
                count++;
                // 赋值
                for (int i = 0; i < this.resourcesSize; i++) {
                    work[count][i] = workAllocation[count - 1][i];
                }
            } else {
                count++;
                System.out.println("第" + count + "次安全性检测失败！");
                break;
            }
            if (finish[count - 1] == false) {
                break;
            }
        }
        //跳出循环如果有进程数个值，说明每个进程都能满足，将试分配的值真实分配
        if (processNumList.size() == this.processesSize) {
            for (int i = 0; i < this.processesSize; i++) {
                for (int j = 0; j < this.resourcesSize; j++) {
                    allocation[i][j] = allocationTmp[i][j];
                    need[i][j] = needTmp[i][j];
                }
            }
            for (int i = 0; i < this.resourcesSize; i++) {
                available[0][i] = availableTmp[0][i];
            }
            printfResult();
            System.out.println("*********************************");
            display();
        } else {
            if (processNumList.size() == 0) {
                System.out.println("不满足任何进程的需求！");
                System.out.println("无安全序列！");
                System.out.println("******************************");
                display();
            } else {
                System.out.println("无安全序列！");
                System.out.println("*****************************");
                display();
            }
        }

    }

    /**
     * 打印分配后的结果
     */
    private void printfResult() {
        System.out.println("当前时刻的安全性：");
        System.out.println("进程名" + "       work    " + "         allocation    " + "       need    " + "         workAllocatio    " + "         finish    ");
        // index为进程的下标
        int tmp = 0;
        while (tmp < 4) {
            System.out.print("            ");
            for (int i = 0; i < this.resourcesSize; i++) {
                System.out.print(resources[i].resourceName + " ");
            }
            tmp++;
        }
        System.out.println();
        for (int i = 0; i < this.processesSize; i++) {
            System.out.print(processes[(int) processNumList.get(i)] + "         ");
            for (int j = 0; j < this.resourcesSize; j++) {
                System.out.print(work[i][j] + " ");
            }
            System.out.print("             ");
            for (int j = 0; j < this.resourcesSize; j++) {
                System.out.print(allocation[(int) processNumList.get(i)][j] + " ");
            }
            System.out.print("              ");
            for (int j = 0; j < this.resourcesSize; j++) {
                System.out.print(need[(int) processNumList.get(i)][j] + " ");
            }
            System.out.print("              ");
            for (int j = 0; j < this.resourcesSize; j++) {
                System.out.print(workAllocation[i][j] + " ");
            }
            System.out.print("              ");
            System.out.print(" " + finish[i] + " ");
            System.out.println();
        }

        // 遍历安全序列
        System.out.println();
        System.out.println("存在安全序列为：");
        int i = 1;
        for (Object o : processNumList) {
            int num = (int) o;
            if (i != processNumList.size()) {
                System.out.print(processes[num] + "->");
            } else {
                System.out.print(processes[num]);
            }
            i ++ ;
        }

        System.out.println();
    }


    /**
     * 申请资源
     */
    private void requset() {
        copy();
        System.out.println("请输入要请求资源的进程号：");
        String name = scanner.next();
        int putInto = -1;    //判断输入的是几号进程
        for (int i = 0; i < this.processesSize; i++) {
            if (name.equals(processes[i])) {
                putInto = i;
            }
        }
        //如果输入的在进程序列内不存在
        if (putInto == -1) {
            System.out.println("非法输入！");
            return;
        }
        System.out.println();
        System.out.println("再次申请的资源大小（A,B,C）：");
        int[] num = new int[this.resourcesSize];   //输入的request值
        for (int i = 0; i < this.resourcesSize; i++) {
            num[i] = scanner.nextInt();
        }
        int k = 0;
        for (int i = 0; i < this.resourcesSize; i++) {
            if ((num[i] <= need[putInto][i]) && (num[i] <= available[0][i])) {
                k++;
            } else {
                if ((num[i] > need[putInto][i])) {
                    System.out.println("对不起，您所申请的资源数大于需要的资源数！");
                    System.out.println("***************************");
                    display();
                    return;
                }
                if (num[i] > available[0][i]) {
                    System.out.println("资源不足！");
                    System.out.println("***************************");
                    display();
                    return;
                }
            }
        }
        //跳出说明满足要求尝试分配资源
        if (k == this.resourcesSize) {
            for (int j = 0; j < this.resourcesSize; j++) {
                needTmp[putInto][j] = needTmp[putInto][j] - num[j];
                allocationTmp[putInto][j] = allocationTmp[putInto][j] + num[j];
                availableTmp[0][j] = availableTmp[0][j] - num[j];
            }
        }
        //分配函数，以及安全性检测
        allocation();
    }

    /**
     * 删除进程(如果某个资源申请后的need 为0)
     */
    private void resReturn() {
        int count = 0;
        int num = 0;
        while (count < processesSize) {
            for (int i = 0; i < this.resourcesSize; i++) {
                if (need[count][i] == 0) {
                    num++;
                }
            }
            if (num == resourcesSize) {
                for (int i = 0; i < this.resourcesSize; i++) {
                    available[0][i] = allocation[count][i] + available[0][i];
                }
                System.out.println("Running Process:" + processes[count]);
                String str = "";
                for (int i = 0; i < this.processesSize; i++) {
                    if (i != count) {
                        str += processes[i] + " ";
                    }
                }
                System.out.println("Waiting Process:" + str);
                for (int i = 0; i < this.processesSize; i++) {
                    for (int j = 0; j < this.resourcesSize; j++) {
                        if (i >= count) {
                            max[i][j] = max[i + 1][j];
                            need[i][j] = need[i + 1][j];
                            allocation[i][j] = allocation[i + 1][j];
                            processes[i] = processes[i + 1];
                        }
                    }
                }
                processesSize--;
            }
            num = 0;
            count++;
        }
    }
}
