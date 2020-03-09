package com.zucc.pbcls.service;

import com.zucc.pbcls.dao.Case.CaseDao;
import com.zucc.pbcls.pojo.Case.CaseInfo;
import com.zucc.pbcls.pojo.MyUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

@Service
public class Admin_CaseService {
    @Autowired
    CaseDao caseDao;

    //分页查询
    public Page<CaseInfo> findCases(boolean statuson,boolean statusoff,String findstr,Pageable pageable) {
        Page<CaseInfo> pageList = caseDao.findAll(new Specification<CaseInfo>(){
            @Override
            public Predicate toPredicate(Root<CaseInfo> root, CriteriaQuery<?> query, CriteriaBuilder cb) {

                List<Predicate> listAndstatus = new ArrayList<Predicate>();
                if (statuson&&!statusoff){
                    listAndstatus.add(cb.equal(root.get("status"),true));
                }else if (!statuson&&statusoff){
                    listAndstatus.add(cb.equal(root.get("status"),false));
                }else if (!statuson&&!statusoff){
                    listAndstatus.add(cb.equal(root.get("status"),3));
                }
                Predicate[] arrayStatus = new Predicate[listAndstatus.size()];
                Predicate Status = cb.and(listAndstatus.toArray(arrayStatus));

                List<Predicate> listName = new ArrayList<Predicate>();
                listName.add(cb.like(root.get("casename"), "%" + findstr + "%"));
                Predicate[] arrayName = new Predicate[listName.size()];
                Predicate Name = cb.and(listName.toArray(arrayName));

                return query.where(Status,Name).getRestriction();
            }
        }, pageable);
        return pageList;
    }


    public boolean updateStatus(int caseid , boolean status){
        CaseInfo caseInfo = caseDao.findAllByCaseid(caseid);
        if (caseInfo!=null) {
            caseInfo.setStatus(status);
            caseDao.save(caseInfo);
            return true;
        }else
            return false;//已经被删除
    }

    /**
     * 0  已经被删除
     * 1  下面还有运行的项目？
     * 2  成功删除？
     */
    public int deleteCase(int caseid){
        CaseInfo caseInfo = caseDao.findAllByCaseid(caseid);
        if (caseInfo!=null){
            //删除逻辑等待项目模块完工
            //1.下面有运行的项目的时候无法删除
            //2.下面已经结束的项目是否需要删除
            //3.需要删除相关文件
            return 1;
        }else
            return 0;
    }


/**
 * 项目查找
 * 项目删除?
 * 项目列表
 * 等等功能等待项目模块结束后补充
 */
}
