package com.zucc.pbcls.pojo.AOE;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ArcNode {
    private int adjvex;                        //该弧指向的顶点位置
    ArcNode nextarc;        //指向下一个表结点
    int duration;
}
