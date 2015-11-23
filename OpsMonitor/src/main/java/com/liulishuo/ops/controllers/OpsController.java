package com.liulishuo.ops.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.management.ManagementFactory;

/**
 * Created by Administrator on 2015/11/20 0020.
 */
@Controller
public class OpsController {

    private static String[] jstackCmd = null;

    @RequestMapping(value = "/")
    public String welcome(ModelAndView modelAndView) {
        return "index";
    }

    @RequestMapping(method = {RequestMethod.GET, RequestMethod.POST},
            value = "/ops/jstack")
    public @ResponseBody String printJStack()
            throws IOException, InterruptedException {
        if (jstackCmd == null) {
            setJStackCmd();
        }
        StringBuffer jstackOut = new StringBuffer();

        Process jstackProcess = Runtime.getRuntime().exec(jstackCmd);
        BufferedReader stdinReader = new BufferedReader(
                new InputStreamReader((jstackProcess.getInputStream()))
        );

        String line = "";
        while((line = stdinReader.readLine()) != null) {
            jstackOut.append(line + "\n");
        }

        return jstackOut.toString();
    }

    private static String setJStackCmd() {
        final String url = "http://localhost:8080/getpid";
        RestTemplate restTemplate = new RestTemplate();
        String pid = restTemplate.getForObject(url, String.class);

        jstackCmd = new String[3];
        jstackCmd[0] = "jstack";
        jstackCmd[1] = "-l";
        jstackCmd[2] = pid;
        return pid;
    }
}

