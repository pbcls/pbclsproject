package com.zucc.pbcls.controller;

import com.zucc.pbcls.pojo.Case.CaseInfo;
import com.zucc.pbcls.pojo.MyUser;
import com.zucc.pbcls.service.Admin_CaseService;
import com.zucc.pbcls.service.Case.CaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class Admin_CaseController {
    @Autowired
    Admin_CaseService admin_caseService;

    @RequestMapping("/findcases")
    @ResponseBody
    public Page<CaseInfo> findCases(@RequestParam(value = "statuson")boolean statuson,@RequestParam(value = "statusoff") boolean statusoff,
                                    @RequestParam(value = "findstr") String findstr, @RequestParam(value = "pagenum") int  pagenum,
                                    @RequestParam(value = "pagesize") int pagesize){
        //注意,后台的pagenum是从0开始的,前端显示是从1开始的
        Pageable pageable = PageRequest.of(pagenum-1, pagesize, Sort.Direction.ASC, "caseid");
        Page<CaseInfo> page = admin_caseService.findCases(statuson,statusoff,findstr, pageable);
        return page;

    }


    @RequestMapping("/updatestatus")
    @ResponseBody
    public boolean updateStatus(@RequestParam(value = "caseid") int caseid,@RequestParam(value = "status") boolean status){
        return admin_caseService.updateStatus(caseid, status);
    }




    /**
     * 案例删除
     * 项目查找
     * 项目删除?
     * 项目列表
     * 等等功能等待项目模块结束后补充
     */
}
