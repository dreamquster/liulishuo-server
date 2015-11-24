package com.liulishuo.autoteller.mvc.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.lang.management.ManagementFactory;
import java.util.Map;
import java.util.Objects;

/**
 * Created by Administrator on 2015/11/23 0023.
 */
@Controller
public class OpsControllers {

    private static String CURRENT_PID = getPid();

    @RequestMapping(value = "/")
    public String welcome(Map<String,Object> model) {
        return "index";
    }

    @RequestMapping(value = "/getpid")
    public @ResponseBody String echoPid() {
        return CURRENT_PID;
    }

    @RequestMapping(value = "/ok")
    public @ResponseBody String echoOk() {
        return "Ok";
    }

    private static String getPid() {
        return ManagementFactory.getRuntimeMXBean().getName().split("@")[0];
    }
}
