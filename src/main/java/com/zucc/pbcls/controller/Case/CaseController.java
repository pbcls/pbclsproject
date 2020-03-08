package com.zucc.pbcls.controller.Case;

import com.zucc.pbcls.pojo.Case.*;
import com.zucc.pbcls.service.Case.*;
import com.zucc.pbcls.utils.CaseDOCSDownloader;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Controller
//@RequestMapping("/student")
public class CaseController {
    @Autowired
    CaseService caseService;
    @Autowired
    Case_RoleService case_roleService;


    @RequestMapping("/showcaselist")
    @ResponseBody
    public List<CaseInfo> showCaseList() {
        return caseService.findAllCases();
    }

    @RequestMapping("/showcaseinfo")
    @ResponseBody
    public String showCaseInfo(@RequestParam(value = "caseid") int caseid) {
        CaseInfo caseInfo = caseService.findAllByCaseid(caseid);
        List<Case_Role> case_roles = case_roleService.findAllRoleByCaseid(caseid);
        //把获得的数据封装成自定义JSON
        JSONObject json_caseInfo = new JSONObject(caseInfo);
        JSONArray json_case_roles = new JSONArray(case_roles);
        //封装在一个JSON对象中
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("case", json_caseInfo);
        jsonObject.put("roles", json_case_roles);
        return jsonObject.toString();
    }


    //以下是关于任务文档的显示
    @RequestMapping("/getcasefilelist")
    @ResponseBody
    public List<String> getCaseFileList(@RequestParam(value = "caseid") int caseid) {
        CaseInfo caseInfo = caseService.findAllByCaseid(caseid);
        CaseDOCSDownloader caseDOCSDownloader = new CaseDOCSDownloader();
        System.out.println(caseDOCSDownloader.getCaseFileList(caseInfo));
        return caseDOCSDownloader.getCaseFileList(caseInfo);

    }


    @RequestMapping("/downloadcasefile")
    @ResponseBody
    public void DownloadCaseFile(@RequestParam(value = "filename") String filename,@RequestParam(value = "caseid") int caseid,HttpServletResponse response) {
        CaseInfo caseInfo = caseService.findAllByCaseid(caseid);
        CaseDOCSDownloader caseDOCSDownloader = new CaseDOCSDownloader();
        caseDOCSDownloader.DownloadCaseFile(filename,caseInfo,response);
    }
}
