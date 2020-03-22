package com.zucc.pbcls.pojo.AOE;

import java.util.Arrays;

public class Stack {
    private Object[] elementData;
    private int currentCapacity;
    private int base;
    private int top;
    private int capacityIncrements;
    private int initCapacity;

    public Stack(){
        base = 0;
        top = 0;
        initCapacity = 10;
        capacityIncrements = 10;
        currentCapacity = initCapacity;
        elementData = new Object[initCapacity];
    }

    public void push(Object obj) {
        if (top < currentCapacity) {
            elementData[top++] = obj;
        }
        else {
            //扩容
            currentCapacity += capacityIncrements;
            ensureCapacityHelper();
            elementData[top++] = obj;
        }
    }

    private void ensureCapacityHelper() {
        elementData = Arrays.copyOf(elementData, currentCapacity);
    }
    public int getSize() {
        return top;
    }

    public Object pop() throws Exception {
        if (top > base) {
            Object obj = elementData[top - 1];
            elementData[top--] = null;
            return obj;
        } else {
            throw new ArrayIndexOutOfBoundsException("stack is null");
        }
    }


    public String toString() {
        String str = "";
        for (int i = 0; i < top; i ++) {
            str += elementData[i].toString() + " ";
        }
        return str;
    }


    public Object getTop() {
        return elementData[top - 1];
    }

    public boolean isEmpty() {
        if (base == top) {
            return true;
        } else {
            return false;
        }

    }

}