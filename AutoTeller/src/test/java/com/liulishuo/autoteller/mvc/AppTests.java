package com.liulishuo.autoteller.mvc;

import com.liulishuo.autoteller.mvc.models.ApiResult;
import org.hamcrest.core.Is;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import static org.hamcrest.core.Is.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration("file:src/test/resources/mvc-dispatcher-servlet.xml")
public class AppTests {
    private MockMvc mockMvc;

    @SuppressWarnings("SpringJavaAutowiringInspection")
    @Autowired
    protected WebApplicationContext wac;

    @Before
    public void setup() {
        this.mockMvc = webAppContextSetup(this.wac).build();
    }


    @Transactional
    @Test
    public void addUserCoinsTest() throws Exception {
        mockMvc.perform(post("/user/add?user_id=4&coins=10"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$.status", is(ApiResult.SUCCESS.getStatus())))
                .andExpect(jsonPath("$.message", is("Success:add 10 coins for 4")));
    }

    @Test
    public void getCoinsOfTest() throws Exception  {
        mockMvc.perform(get("/coins/user/1"))
                .andExpect(status().isOk());
    }

    @Test
    public void getCoinsOfMinusUserTest() throws Exception {
        mockMvc.perform(get("/coins/user/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status", is(ApiResult.ILLEGAL_ARGS.getStatus())));
    }
}
