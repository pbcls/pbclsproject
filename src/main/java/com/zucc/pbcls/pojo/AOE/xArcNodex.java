package com.zucc.pbcls.pojo.AOE;

import com.zucc.pbcls.pojo.Project.Project_Task_pk;

/**
 * 表结点结构
 * @author Administrator
 *
 */
public class xArcNodex {
//    private int adjvex;//表头顶点的邻接顶点编号(pk)
    private Project_Task_pk project_task_pk;
    private int data;//边的信息
    private int edge;
    private xArcNodex nextArc;//指向下一个邻接顶点的指针

    public xArcNodex(Project_Task_pk project_task_pk, int data, int edge, xArcNodex nextArc) {
        this.project_task_pk = project_task_pk;
        this.data = data;
        this.edge = edge;
        this.nextArc = nextArc;
    }

    public Project_Task_pk getProject_task_pk() {
        return project_task_pk;
    }

    public void setProject_task_pk(Project_Task_pk project_task_pk) {
        this.project_task_pk = project_task_pk;
    }

    public int getData() {
        return data;
    }

    public void setData(int data) {
        this.data = data;
    }

    public xArcNodex getNextArc() {
        return nextArc;
    }

    public void setNextArc(xArcNodex nextArc) {
        this.nextArc = nextArc;
    }

    public int getEdge() {
        return edge;
    }

    public void setEdge(int edge) {
        this.edge = edge;
    }
}

