package com.kuang.utils;

import com.kuang.pojo.ListNode;

/**
 * @author wbw
 * @date 2021/5/23 17:37
 */
public class ListUtil {
    public ListNode headA;

    public ListNode init(int[] array){
        headA = new ListNode(array[0]);
        ListNode cur = headA;
        for(int i=1;i<array.length;i++){
            ListNode next = new ListNode(array[i]);
            cur.next = next;
            cur = cur.next;
        }
        return headA;
    }

    public ListNode findNodeByIndex(int index){
        int i=0;
        ListNode cur = headA;
        while(i!=index){
            cur = cur.next;
            i++;
        }
        return cur;
    }

    public ListNode getIntersectionNode(ListNode headA, ListNode headB) {
        //第一种： 结尾法
        ListNode pa = headA;
        ListNode pb = headB;
        int lenA = 0, lenB=0, count=0, tA=0, tB=0;

        ListNode curA = headA;
        while(curA != null){
            lenA++;
            curA = curA.next;
        }
        System.out.println(lenA);

        ListNode curB = headB;
        while(curB != null){
            lenB++;
            curB = curB.next;
        }
        System.out.println(lenB);
        int d = Math.abs(lenA - lenB);
        if(lenA > lenB){
            int i=0;
            while(i!=d){
                pa = pa.next;
                i++;
            }
        }else{
            int i=0;
            while(i!=d){
                pb = pb.next;
                i++;
            }
        }
        while(pa != pb){
            if(pa == null || pb == null){
                return null;
            }
            pa = pa.next;
            pb = pb.next;
        }
        //System.out.println("pb:" + pb.val);

        return pa;
    }

}
