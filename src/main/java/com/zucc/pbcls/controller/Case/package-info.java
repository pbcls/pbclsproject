package com.zucc.pbcls.controller.Case;
//package Topologic;
//
//import java.util.ArrayList;
////其中的导入包请参见页底
//import ch03.LinkStack;
//import cho6.ALGraph;
//import cho6.VNode;
//import cho6.xArcNodex;
//
//public class CriticalPathMethod {
//    private int[] ve; // 顶点（事件）的最早发生时间,默认全为0
//    private int[] vl; // 顶点（事件）的最晚发生时间,默认全为0
//    private LinkStack t; // 用于保存逆拓扑序列的栈
//    private ALGraph al; // 用于保存AOE网的邻接表
//
//    public CriticalPathMethod(ALGraph al) {
//        this.al = al;
//    }
//
//    /**
//     * 顺序拓扑序列
//     * @return 是否能构成拓扑序列
//     * @throws Exception
//     */
//    private boolean topoOrder() throws Exception {
//        int count = 0;
//        LinkStack s = new LinkStack();// 用于保存零入度的顶点的栈
//        int[] inDegree = al.getInDegree();
//        for (int i = 0; i < inDegree.length; i++) {
//            if (inDegree[i] == 0)
//                s.push(i);
//        }
//        while (!s.isEmpty()) {
//            int j = (Integer) s.pop();// 零入度栈出栈
//            t.push(j);
//            count++;
//            for (xArcNodex node = ((VNode) al.getVex(j)).firstArc; node != null; node = node.nextArc) {
//                int k = node.adjVex;// j,k即表示顶点j到顶点k
//                int value = node.value;// value表示<j,k>的距离，即权值
//                if (--inDegree[k] == 0) {
//                    s.push(k);
//                }
//                if (ve[j] + value > ve[k]) {
//                    ve[k] = ve[j] + value;
//                }
//            }
//        }
//
//        if (count == al.getVNum())
//            return true;
//        else
//            return false;
//    }
//
//    /**
//     * 逆序拓扑序列
//     * @return 是否能构成拓扑序列
//     * @throws Exception
//     */
//    private boolean reTopoOrder() throws Exception {
//        if (!topoOrder())
//            return false;
//        for (int i = 0; i < al.getVNum(); i++) {
//            vl[i] = ve[al.getVNum() - 1];// 令所有顶点（事件）的最晚发生时间和汇点的最早发生时间相等
//        }
//        while (!t.isEmpty()) {
//            int j = (Integer) t.pop();// 逆序拓扑栈出栈
//            for (xArcNodex node = ((VNode) al.getVex(j)).firstArc; node != null; node = node.nextArc) {
//                int k = node.adjVex;// j,k即表示顶点j到顶点k
//                int value = node.value;// value表示<j,k>的距离，即权值
//                if (vl[k] - value < vl[j]) {
//                    vl[j] = vl[k] - value;
//                }
//            }
//        }
//
//        return true;
//    }
//
//    public void getCriticalPah() throws Exception {
//        if(!reTopoOrder())
//            throw new Exception("所构建的AOE网不存在拓扑序列，故不存在关键路径！");
//        ArrayList a = new ArrayList();
//        for(int j=0;j<al.getVNum();j++) {
//            for(xArcNodex node = ((VNode) al.getVex(j)).firstArc; node != null; node = node.nextArc) {
//                int k = node.adjVex;
//                int value = node.value;
//                int e = ve[j];//边（活动）的最早开始时间
//                int l = ve[k] - value;//边（活动）的最晚开始时间
//                if(e == l) {
//                    System.out.println(al.getVex(j) + "-->" + al.getVex(k));
//                }
//            }
//        }
//    }
//}
//
