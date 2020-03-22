package com.zucc.pbcls.pojo.AOE;


import com.zucc.pbcls.pojo.Project.Project_Task_pk;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VNode {
    private Project_Task_pk project_task_pk;
    private int indegree;
    private ArcNode firstarc;
}
