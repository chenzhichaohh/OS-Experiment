#include <stdio.h>
#include <math.h>
#include <stdlib.h>
#include<time.h>

// ���������10���ŵ� 
int a[10];
// ƽ���ƶ��ŵ����� 
float avarageMove[4];
int n, init;

// �����ȷ���
void FCFS(int a[], int n, int init) {
    int i, s, sum, temp = 0;
    int b[20];

    for (i = 0; i < n; i++)
        b[i] = a[i];
    s = init;
    sum = 0;
    printf("\nFCFS���Ƚ��:\n");
    for (i = 0; i < n; i++) {
        // �����ʵĴŵ���
        printf("%3d  ", b[i]);
        // �����ƶ�����
        temp = abs(s - b[i]);
        sum += temp;
        s = b[i];
    }
    avarageMove[0] = sum * 1.0 / n;
    printf("\n");
}

// ���Ѱ����
void SSTF(int a[], int n, int init) {
    int i, j, s, sum = 0, p, temp = 0;
    int b[20];

    for (i = 0; i < n; i++)
        b[i] = a[i];
    printf("\nSSTF���Ƚ��:\n");
    for (i = n - 1; i >= 0; i--) {
        s = b[0];
        p = 0;
        for (j = 0; j <= i; j++)
            if (abs(b[j] - init) < abs(s - init)) {
                s = b[j];
                p = j;
            }
        b[p] = b[i];
        // �����ʵĴŵ���
        printf("%3d  ", s);
        // �����ƶ�����
        temp = abs(s - init);
        sum += temp;
        init = s;
    }
    avarageMove[1] = sum * 1.0 / n;
    printf("\n");
}

// ɨ���㷨
void SCAN(int a[], int n, int init) {
    int i, j, s, sum = 0, p, biaoji, temp = 0;
    int b[20];
    for (i = 0; i < n; i++)
        b[i] = a[i];

    printf("\nSCAN���Ƚ��(�ŵ������ӵķ���):\n");
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
            // �����ʵĴŵ���
            printf("%3d  ", s);
            // �����ƶ�����
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
            // �����ʵĴŵ���
            printf("%3d  ", s);
            // �����ƶ�����
            temp = abs(init - s);
            sum += temp;
            init = s;
        }
    }
    avarageMove[2] = sum * 1.0 / n;
    printf("\n");
}

// ѭ��ɨ���㷨
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
            if (b[i] > b[j])// �Դŵ��Ž��д�С��������
            {
                temp = b[i];
                b[i] = b[j];
                b[j] = temp;
            }
        }
    }
    printf("\nCSCAN���Ƚ��(�ŵ������ӵķ���):\n");

    if (b[n - 1] <= init) {// �ж���������������Ƿ�С�ڵ�ǰ�ŵ���
        for (i = 0; i < n; i++) {
            // �����ʵĴŵ���
            printf("%3d  ", b[i]);
        }
        sum = init - b[0] + b[n - 1];// �����ƶ�����
    } else if (b[0] >= init) {// �ж���������������Ƿ񶼴��ڵ�ǰ�ŵ���
        for (i = 0; i < n; i++) {
            // �����ʵĴŵ���
            printf("%3d  ", b[i]);
            // �����ƶ�����
            //printf("%3d\n",abs(init_temp - b[i]));
            //init_temp = b[i];
        }
        sum = b[n - 1] - init;// �����ƶ�����
    } else {
        while (b[k] < init) {
            // ��һ�Ƚ���ȷ��Kֵ
            k++;
        }
        r = k;

        for (j = r; j < n; j++) {
            // �����ʵĴŵ���
            printf("%3d  ", b[j]);
        }
        for (j = 0; j < r; j++) {
            // �����ʵĴŵ���
            printf("%3d  ", b[j]);
        }
        sum = 2 * (b[n - 1] - b[0]) + b[r - 1] - init;// �����ƶ�����
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

    // ��ʾ��Ҫ���ʵĴŵ�
    printf("�������һ���ŵ��������У�\n");
    for (int i = 0; i < n; i++) {
        printf("%d  ", a[i]);
    }
    printf("\n");
}


int main() {
    DataInit();
    printf("\n**************************************************\n");
    printf("                  ���̵����㷨                     \n");
    printf("     ���繤��18(4)��       ���ǳ�                    \n");
    printf("**************************************************\n");

    FCFS(a, n, init);
    SSTF(a, n, init);
    SCAN(a, n, init);
    CSCAN(a, n, init);

    printf("\nƽ���ƶ��ŵ����Ա�ͳ��: \n");
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
