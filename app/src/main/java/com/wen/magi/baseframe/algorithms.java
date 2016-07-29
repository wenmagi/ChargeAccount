package com.wen.magi.baseframe;

import com.wen.magi.baseframe.utils.LangUtils;

import java.util.Stack;

/**
 * Created by MVEN on 16/7/1.
 * <p/>
 * email: magiwen@126.com.
 */


public class algorithms {

    private static algorithms instance = null;

    public static algorithms getInstance() {
        if (instance == null) {
            synchronized (algorithms.class) {
                if (instance == null)
                    instance = new algorithms();
            }
        }
        return instance;
    }

    private algorithms() {
        testAlgorithms();
    }

    private void testAlgorithms() {
        int a[] = {1, 2, 0, 0, 0, 14, 9, 5, 10, 5, 6, 7, 8};
        int b[] = {2, 3, 4, 5, 6, 6};
        NODE longer = create(a);
        NODE shorter = create(b);
        sum(longer, shorter, longer, a.length, b.length);
        printNODE(reverse(longer));
        quickSort(a, 0, a.length - 1);
    }


    private void testTreeNode() {
        algorithms.TREENODE a = new algorithms.TREENODE();
        a.data = 3;
        a.left = a.right = null;
        algorithms.TREENODE b = new algorithms.TREENODE();
        b.data = -1;
        b.left = a;
        b.right = null;
        algorithms.TREENODE c = new algorithms.TREENODE();
        c.data = -1;
        c.left = a;
        c.right = null;
        algorithms.TREENODE d = new algorithms.TREENODE();
    }


    //展示可被2或者3整除的第index个数
    public static int show2Or3(int index) {
        int i = 0;
        index = 2222;
        for (int j = 1; i < index; j++) {
            if (j % 2 == 0 || j % 3 == 0) {
                i++;
            }
            if (i == 2333) {
                return j;
            }
        }
        return -1;
    }

    public static class NODE {
        public int data;
        public int index;
        public NODE next;
    }

    public static class TREENODE {
        public int data;
        public TREENODE left;
        public TREENODE right;
    }

    /**
     * 链表算法题
     * </p>
     * start
     *
     * @param array
     * @return
     */
    public static NODE create(int[] array) {
        if (array == null || array.length <= 0)
            return null;
        NODE head = null, pre = null, newNODE = null;
        int len = array.length;
        for (int i = 0; i < len; i++) {
            newNODE = new NODE();
            newNODE.data = array[i];
            newNODE.index = len - i - 1;
            if (i == 0) {
                head = newNODE;
                pre = newNODE;
            } else {
                pre.next = newNODE;
                pre = newNODE;
            }
        }
        pre.next = null;
        printNODE(head);
        return head;
    }

    //链表实现加法
    public static int sum(NODE longer, NODE shorter, NODE newNODE, int longerLen, int shorterLen) {
        if (newNODE == null)
            return 0;
        int temp = 0;
        int result = 0;
        if (longerLen > shorterLen) {
            result = sum(longer.next, shorter, newNODE.next, longerLen - 1, shorterLen);
            temp = longer.data + result;
            newNODE.data = temp % 10;
            return temp >= 10 ? 1 : 0;
        } else {//(longerLen == shorterLen)
            result = sum(longer.next, shorter.next, newNODE.next, longerLen - 1, shorterLen - 1);
            temp = result + longer.data + shorter.data;
            newNODE.data = temp % 10;
            return temp >= 10 ? 1 : 0;
        }
    }

    //链表实现减法
    public static int subtraction(NODE longer, NODE shorter, NODE newNODE, int longerLen, int shortLen) {
        if (newNODE == null)
            return 0;
        int temp = 0;
        int result = 0;
        if (longerLen > shortLen) {
            result = subtraction(longer.next, shorter, newNODE.next, longerLen - 1, shortLen);
            temp = longer.data - result;
            newNODE.data = temp < 0 ? 10 + temp : temp;
            return temp < 0 ? 1 : 0;
        } else {
            result = subtraction(longer.next, shorter.next, newNODE.next, longerLen - 1, shortLen - 1);
            temp = longer.data - shorter.data - result;
            newNODE.data = temp < 0 ? 10 + temp : temp;
            return temp < 0 ? 1 : 0;
        }
    }

    //翻转链表
    public static NODE reverse(NODE target) {
        NODE head, p, q;
        head = q = target;
        p = head.next;
        head.next = null;
        while (p != null) {
            q = p.next;
            p.next = head;
            head = p;
            p = q;
        }
        return head;
    }

    public static void printNODE(NODE node) {
        String aaa = "";
        while (node != null) {
            aaa += node.data;
            node = node.next;
        }
//        LogUtils.e("wwwwwwwwww print %s", aaa);
    }

    public static void printTREENODE(TREENODE node) {
        String aaa = "";
        while (node != null) {
            aaa += node.data;
        }
//        LogUtils.e("wwwwwwwwww print %s", aaa);
    }

    /**
     * 链表 end
     */

    /**
     * 快排
     *
     * @param attr
     * @param low
     * @param high
     */
    public static void quickSort(int[] attr, int low, int high) {
        if (low >= high)
            return;
        int start = low;
        int end = high;
        int key = attr[start];
        while (start < end) {

            while (start < end && attr[end] >= key)
                end--;

            attr[start] = attr[end];

            while (start < end && attr[start] <= key)
                start++;

            attr[end] = attr[start];

        }
        attr[start] = key;
        quickSort(attr, low, start - 1);
        quickSort(attr, end + 1, high);
    }

    /**
     * 二分法
     *
     * @param array
     * @param value
     * @return
     */
    public static int binarySearch(int[] array, int value) {
        int low = 0;
        int high = array.length - 1;
        while (low < high) {
            int mid = high + low / 2;
            if (array[mid] > value) {
                high = mid - 1;
            } else if (array[mid] < value) {
                low = mid + 1;
            } else {
                return mid;
            }
        }
        return -1;
    }

    //将有二个有序子数组a[start...mid]和a[mid+1...end]合并。
    private static void mergeSortedArray(int[] array, int start, int mid, int end, int[] temp) {
        int i = start, j = mid + 1;
        int k = 0;
        while (i <= mid && j <= end) {
            if (array[i] < array[j])
                temp[k++] = array[i++];
            else
                temp[k++] = array[j++];
        }
        while (i <= mid) {
            temp[k++] = array[i++];
        }
        while (j <= end) {
            temp[k++] = array[i++];
        }
        for (i = 0; i < k; i++)
            array[start + i] = temp[i];
    }

    //归并排序
    public static void mergeSort(int[] array, int start, int end, int[] temp) {
        if (start >= end)
            return;


        int mid = start + end / 2;
        mergeSort(array, start, mid, temp);
        mergeSort(array, mid + 1, end, temp);
        mergeSortedArray(array, start, mid, end, temp);
    }

    public static int maxValue;

    public static int maxValueBTree(TREENODE root) {

        if (root == null)
            return 0;

        int rootValue = root.data;
        int leftMaxValue = maxValueBTree(root.left);
        int rightMaxValue = maxValueBTree(root.right);

        //经过root的路径的最大和（根结点+以左子树为终点的路径最大和+以右子树为终点的路径最大和）
        if (leftMaxValue > 0)
            rootValue += leftMaxValue;
        if (rightMaxValue > 0)
            rootValue += rightMaxValue;

        maxValue = LangUtils.max(maxValue, rootValue);
        //计算以root为终点的路径最大和
        //如果左子树和右子树的最大单路径和都是负的，那么就返回root->val
        return LangUtils.max(root.data, LangUtils.max(root.data + leftMaxValue, root.data + rightMaxValue));
    }

    private static int max_distance;

    public static int maxDistance(TREENODE root) {
        if (root == null)
            return 0;

        int leftMaxDistance = maxDistance(root.left);
        int rightMaxDistance = maxDistance(root.right);

        int val = leftMaxDistance + rightMaxDistance + 1;
        LangUtils.max(val, max_distance);
        return LangUtils.max(leftMaxDistance, rightMaxDistance) + 1;
    }

    //前序遍历的递归实现
    public static void preLoopTree(TREENODE head) {
        if (head != null) {
            printTREENODE(head);
            preLoopTree(head.left);
            preLoopTree(head.right);
        }
    }

    //前序遍历，非递归实现
    public static void preLoopTreeStack(Stack<TREENODE> stack, TREENODE head) {
        if (head == null)
            return;

        while (head != null && stack.size() > 0) {

            while (head != null) {
                printTREENODE(head);
                stack.add(head);
                head = head.left;
            }
            head = stack.pop().right;
        }
    }

    private void test() {
        int[][] array = new int[26][];
        int[] zimu = new int[26];
        zimu['a'] = 10;
    }
}
