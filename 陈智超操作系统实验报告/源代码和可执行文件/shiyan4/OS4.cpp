#include <stdio.h>
#include <math.h>
#include <stdlib.h>
#include<time.h>

// 随机产生的10个磁道 
int a[10];
// 平均移动磁道数组 
float avarageMove[4];
int n, init;

// 先来先服务
void FCFS(int a[], int n, int init) {
    int i, s, sum, temp = 0;
    int b[20];

    for (i = 0; i < n; i++)
        b[i] = a[i];
    s = init;
    sum = 0;
    printf("\nFCFS调度结果:\n");
    for (i = 0; i < n; i++) {
        // 被访问的磁道号
        printf("%3d  ", b[i]);
        // 计算移动距离
        temp = abs(s - b[i]);
        sum += temp;
        s = b[i];
    }
    avarageMove[0] = sum * 1.0 / n;
    printf("\n");
}

// 最短寻道法
void SSTF(int a[], int n, int init) {
    int i, j, s, sum = 0, p, temp = 0;
    int b[20];

    for (i = 0; i < n; i++)
        b[i] = a[i];
    printf("\nSSTF调度结果:\n");
    for (i = n - 1; i >= 0; i--) {
        s = b[0];
        p = 0;
        for (j = 0; j <= i; j++)
            if (abs(b[j] - init) < abs(s - init)) {
                s = b[j];
                p = j;
            }
        b[p] = b[i];
        // 被访问的磁道号
        printf("%3d  ", s);
        // 计算移动距离
        temp = abs(s - init);
        sum += temp;
        init = s;
    }
    avarageMove[1] = sum * 1.0 / n;
    printf("\n");
}

// 扫描算法
void SCAN(int a[], int n, int init) {
    int i, j, s, sum = 0, p, biaoji, temp = 0;
    int b[20];
    for (i = 0; i < n; i++)
        b[i] = a[i];

    printf("\nSCAN调度结果(磁道号增加的方向):\n");
    for (i = n - 1; i >= 0; i--) {
        biaoji = 0;
        for (j = 0; j <= i; j++)
            if (b[j] - init > 0) {
                biaoji = 1;
                p = j;
                break;
            }
        if (biaoji == 1) {
            s = b[p];
            for (j = 0; j <= i; j++)
                if (b[j] > init && b[j] - init < s - init) {
                    s = b[j];
                    p = j;
                }
            b[p] = b[i];
            // 被访问的磁道号
            printf("%3d  ", s);
            // 计算移动距离
            temp = s - init;
            sum += s - init;
            init = s;
        } else {
            s = b[0];
            for (j = 0; j <= i; j++)
                if (init - b[j] <= init - s) {
                    s = b[j];
                    p = j;
                }
            b[p] = b[i];
            // 被访问的磁道号
            printf("%3d  ", s);
            // 计算移动距离
            temp = abs(init - s);
            sum += temp;
            init = s;
        }
    }
    avarageMove[2] = sum * 1.0 / n;
    printf("\n");
}

// 循环扫描算法
void CSCAN(int a[], int n, int init) {
    int temp;
    int k = 1;
    int r;
    int b[20];
    int i, j, sum = 0;

    for (i = 0; i < n; i++)
        b[i] = a[i];
    for (i = 0; i < n; i++) {
        for (j = i + 1; j < n; j++) {
            if (b[i] > b[j])// 对磁道号进行从小到大排列
            {
                temp = b[i];
                b[i] = b[j];
                b[j] = temp;
            }
        }
    }
    printf("\nCSCAN调度结果(磁道号增加的方向):\n");

    if (b[n - 1] <= init) {// 判断整个数组里的数是否都小于当前磁道号
        for (i = 0; i < n; i++) {
            // 被访问的磁道号
            printf("%3d  ", b[i]);
        }
        sum = init - b[0] + b[n - 1];// 计算移动距离
    } else if (b[0] >= init) {// 判断整个数组里的数是否都大于当前磁道号
        for (i = 0; i < n; i++) {
            // 被访问的磁道号
            printf("%3d  ", b[i]);
            // 计算移动距离
            //printf("%3d\n",abs(init_temp - b[i]));
            //init_temp = b[i];
        }
        sum = b[n - 1] - init;// 计算移动距离
    } else {
        while (b[k] < init) {
            // 逐一比较以确定K值
            k++;
        }
        r = k;

        for (j = r; j < n; j++) {
            // 被访问的磁道号
            printf("%3d  ", b[j]);
        }
        for (j = 0; j < r; j++) {
            // 被访问的磁道号
            printf("%3d  ", b[j]);
        }
        sum = 2 * (b[n - 1] - b[0]) + b[r - 1] - init;// 计算移动距离
        avarageMove[3] = sum * 1.0 / n;
    }
    printf("\n");
}

void DataInit() {

    n = 10;
    init = 100;
    srand((unsigned int) (time(NULL)));
    for (int i = 0; i < n; i++) {
        a[i] = (rand() % 171) + 30;
    }

    // 显示需要访问的磁道
    printf("随机产生一个磁道访问序列：\n");
    for (int i = 0; i < n; i++) {
        printf("%d  ", a[i]);
    }
    printf("\n");
}


int main() {
    DataInit();
    printf("\n**************************************************\n");
    printf("                  磁盘调度算法                     \n");
    printf("     网络工程18(4)班       陈智超                    \n");
    printf("**************************************************\n");

    FCFS(a, n, init);
    SSTF(a, n, init);
    SCAN(a, n, init);
    CSCAN(a, n, init);

    printf("\n平均移动磁道数对比统计: \n");
    printf("----------------------------------------------\n");
    printf("|   algorithm  | average moving track number |\n");
    printf("----------------------------------------------\n");
    printf("|   FCFS       |    %.1f                     |\n", avarageMove[0]);
    printf("|   SSTF       |    %.1f                     |\n", avarageMove[1]);
    printf("|   SCAN       |    %.1f                     |\n", avarageMove[2]);
    printf("|   CSCAN      |    %.1f                     |\n", avarageMove[3]);
    printf("----------------------------------------------\n");
    getchar();

    return 0;
}
