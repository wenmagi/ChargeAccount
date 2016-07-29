package com.wen.magi.baseframe.utils;

import java.util.LinkedList;

import static com.wen.magi.baseframe.base.AppConfig.DEV_BUILD;


public class BytePool {
    private LinkedList<byte[]> pool = new LinkedList<byte[]>();

    private static int creation_count = 0;


    public synchronized byte[] acquire(int minCapacity) {
        int size = pool.size();
        int index = -1;
        for (int i = 0; i < size; i++) {
            if (pool.get(i).length >= minCapacity) {
                index = i;
                break;
            }
        }

        byte[] ret = null;
        if (index != -1)
            ret = pool.remove(index);
        else if (pool.isEmpty()) {
            ret = new byte[minCapacity];
            if (DEV_BUILD) {
                creation_count++;
                LogUtils.d("bytes creation: " + creation_count);
            }
        } else
            ret = pool.remove(0);
        return ret;
    }

    public synchronized void release(byte[] buf) {
        if (!pool.contains(buf))
            pool.addLast(buf);
    }

    public synchronized void clear() {
        pool.clear();
    }
}
