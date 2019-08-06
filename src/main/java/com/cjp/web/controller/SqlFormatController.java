package com.cjp.web.controller;


import com.cjp.web.utils.SqlFormatUtils;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;


/**
 * @author admin
 * @Date: 2019/5/23 20:49
 * @Description:
 */
@Api
@Controller
public class SqlFormatController {


    @Autowired
    private SqlFormatUtils sqlFormatUtils;


    @RequestMapping("/sqlFormat/formatSql")
    public String formatSql(String sqlInputStr, Model model) {
        System.out.println("sqlInput:\n" + sqlInputStr);
        String sqlFormated = sqlFormatUtils.formatSelectColumns(sqlInputStr);
        System.out.println("sqlOutput:\n" + sqlFormated);
        model.addAttribute("sqlInputStr", sqlInputStr);
        model.addAttribute("sqlFormated", sqlFormated);
        return "/sqlFormat/sqlInput";
    }

    public static void main(String[] args) {
        String ss = "\\n";
        String sss = "\"";
        String ssss = "+";
        System.out.println(ss);
        System.out.println(sss);
        System.out.println(ssss);
    }

}
