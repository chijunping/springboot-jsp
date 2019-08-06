package com.cjp.web.controller;

import io.swagger.annotations.Api;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author admin
 * @Date: 2019/5/23 20:49
 * @Description:
 */
@Api
@Controller
public class PageController {

    /**
     * sqlInput 页面
     *
     * @return
     */
    @RequestMapping("/page/sqlInput")
    public String sqlInput() {
        //跳转页面的时候，不需要加项目名
        return "/sqlFormat/sqlInput";
    }
}
