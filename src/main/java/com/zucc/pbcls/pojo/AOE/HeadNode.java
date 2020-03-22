package com.zucc.pbcls.pojo.AOE;

/**
 * 头结点结构
 * @author Administrator
 *
 */
public class HeadNode {
    private String data;//节点的信息
    private xArcNodex firstArc;//指向第一个邻接顶点的指针
    public HeadNode() {

    }
    public HeadNode(String data) {
        this.data = data;
    }
    public HeadNode(String data, xArcNodex firstArc) {
        this.data = data;
        this.firstArc = firstArc;
    }
    public String getData() {
        return data;
    }
    public void setData(String data) {
        this.data = data;
    }
    public xArcNodex getFirstArc() {
        return firstArc;
    }
    public void setFirstArc(xArcNodex firstArc) {
        this.firstArc = firstArc;
    }
}
