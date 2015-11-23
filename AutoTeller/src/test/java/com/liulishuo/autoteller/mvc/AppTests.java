package com.liulishuo.autoteller.mvc;

import com.liulishuo.autoteller.mvc.models.ApiResult;
import com.liulishuo.autoteller.mvc.services.CoinService;
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

import java.math.BigInteger;

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

    @Autowired
    private CoinService coinService;

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
                .andExpect(jsonPath("$.status", is(ApiResult.SUCCESS)))
                .andExpect(jsonPath("$.message", is("Success:add 10 coins for 4")));
    }

    @Test
    public void addUserCoinsIllegalArgTest() throws Exception {
        mockMvc.perform(post("/user/add?user_id=-4&coins=10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status", is(ApiResult.ILLEGAL_ARGS)))
                .andExpect(jsonPath("$.message",
                        is("Illegal Argument:user_id and coins both " +
                                "should be greater than 0, receive -4 10")));
    }

    @Test
    public void getCoinsOfTest() throws Exception  {
        mockMvc.perform(get("/coins/user/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data", is(10)));
    }

    @Test
    public void getCoinsOfMinusUserTest() throws Exception {
        mockMvc.perform(get("/coins/user/-1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status", is(ApiResult.ILLEGAL_ARGS)))
                .andExpect(jsonPath("$.message",
                        is("Illegal Argument:user_id should be greater than 0, receive -1")));
    }

    @Test
    public void getCoinsOfNotExistUserTest() throws Exception {
        mockMvc.perform(get("/coins/user/1024"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status", is(ApiResult.ILLEGAL_ARGS)))
                .andExpect(jsonPath("$.message",
                        is("Illegal Argument:The user 1024 is not exist")));
    }


    @Transactional
    @Test
    public void transferCoinsTest() throws Exception{
        coinService.createUser(new BigInteger("4"));
        mockMvc.perform(post("/transaction/transfer?from_user_id=1&" +
                "to_user_id=4&coins=5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status", is(ApiResult.SUCCESS)))
                .andExpect(jsonPath("$.message",
                        is("Success:transfer 5 coins from 1 to 4")));
    }

}
