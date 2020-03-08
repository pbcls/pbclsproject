package com.zucc.pbcls.service.Case;

import com.zucc.pbcls.dao.Case.Case_TaskDao;
import com.zucc.pbcls.dao.Case.Case_TaskToTaskDao;
import com.zucc.pbcls.pojo.Case.Case_Task;
import com.zucc.pbcls.pojo.Case.Case_TaskToTask;
import com.zucc.pbcls.pojo.Case.Case_Task_pk;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Iterator;
import java.util.List;

@Service
public class Case_TaskToTaskService {
    @Autowired
    Case_TaskToTaskDao case_taskToTaskDao;
    @Autowired
    Case_TaskDao case_taskDao;

    //该任务为前驱,找它的后继任务
    public List<Case_TaskToTask> findAllTasksSuccessor(int caseid,int taskid){
        List<Case_TaskToTask> TasksSuccessor = case_taskToTaskDao.findAllByCaseidAndPredecessorid(caseid,taskid);
//        for (int i = 0; i < TasksSuccessor.size(); i++) {
//            if (TasksSuccessor.get(i).getSuccessorid() != 0)
//                TasksSuccessor.get(i).setTaskname(case_taskDao.findAllByCaseTaskpk(new Case_Task_pk(caseid, TasksSuccessor.get(i).getSuccessorid())).getTaskname());
//            else
//                TasksSuccessor.remove(i);
//        }
        Iterator<Case_TaskToTask> iterator_successor = TasksSuccessor.iterator();
        while (iterator_successor.hasNext()) {
            Case_TaskToTask case_taskToTask = iterator_successor.next();
            if (case_taskToTask.getSuccessorid()==0)
                iterator_successor.remove();
            else
                case_taskToTask.setTaskname(case_taskDao.findAllByCaseTaskpk(new Case_Task_pk(caseid,case_taskToTask.getSuccessorid())).getTaskname());
        }
        return TasksSuccessor;
    }
    //该任务为后继,找它的前驱任务
    public List<Case_TaskToTask> findAllTasksPredecessor(int caseid,int taskid){
        List<Case_TaskToTask> TasksPredecessor = case_taskToTaskDao.findAllByCaseidAndSuccessorid(caseid,taskid);
        System.out.println(TasksPredecessor);
//        for (int i = 0; i < TasksPredecessor.size(); i++) {
//            if (TasksPredecessor.get(i).getPredecessorid() != 0)
//                TasksPredecessor.get(i).setTaskname(case_taskDao.findAllByCaseTaskpk(new Case_Task_pk(caseid, TasksPredecessor.get(i).getPredecessorid())).getTaskname());
//            else
//                TasksPredecessor.remove(i);
//        }

        Iterator<Case_TaskToTask> iterator_predecessor = TasksPredecessor.iterator();
        while (iterator_predecessor.hasNext()) {
            Case_TaskToTask case_taskToTask = iterator_predecessor.next();
            if (case_taskToTask.getPredecessorid()==0)
                iterator_predecessor.remove();
            else
                case_taskToTask.setTaskname(case_taskDao.findAllByCaseTaskpk(new Case_Task_pk(caseid,case_taskToTask.getPredecessorid())).getTaskname());
        }



        return TasksPredecessor;
    }




}
