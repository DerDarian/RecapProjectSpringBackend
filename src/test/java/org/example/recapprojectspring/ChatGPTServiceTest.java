package org.example.recapprojectspring;

import org.example.recapprojectspring.Spellcheck.ChatGPTService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.AutoConfigureMockRestServiceServer;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureMockRestServiceServer
public class ChatGPTServiceTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    MockRestServiceServer mockRestServiceServer;

    @Test
    void test_correct_spelling_german() throws Exception {
        mockRestServiceServer.expect(requestTo("https://api.openai.com/v1/responses"))
                .andExpect(method(HttpMethod.POST))
                .andRespond(withSuccess("{\"text\":\"German\"}", MediaType.APPLICATION_JSON));
        mockRestServiceServer.expect(requestTo("https://api.openai.com/v1/responses"))
                .andExpect(method(HttpMethod.POST))
                .andRespond(withSuccess("{\"text\":\"Dieser Text ist fehlerfrei\"}", MediaType.APPLICATION_JSON));

        MvcResult result =  mockMvc.perform(MockMvcRequestBuilders.post("/api/todo")).andExpect(MockMvcResultMatchers.status().is2xxSuccessful()).andReturn();
        assert (result.getResponse().getContentAsString().equals("Dieser Text ist fehlerfrei"));
    }
}
