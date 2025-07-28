package org.example.recapprojectspring;

import org.example.recapprojectspring.ToDo.EntryRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

@SpringBootTest
@AutoConfigureMockMvc
public class GlobalExceptionHandlerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    EntryRepository entryRepository;

    @Test
    public void test_Invalid_DTO_provided() throws Exception {
        String json = """
                {
                    "di":"1",
                    "deskripshon":23,
                    "sts":true
                }
                """;

        String response_string = "TodoDTO description is empty\nNo status is provided\n";

        String json_wrong_status = """
                {
                    "di":"1",
                    "description":"hi",
                    "status":"Idk"
                }
                """;
        String response_string_wrong_status = "Invalid entry status provided\nValid entry status values are: OPEN, IN_PROGRESS, DONE";

        MvcResult response = mockMvc.perform(post("/api/todo")
                .contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andReturn();
        assert(response.getResponse().getContentAsString().equals(response_string));

        response = mockMvc.perform(post("/api/todo")
                        .contentType(MediaType.APPLICATION_JSON).content(json_wrong_status))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andReturn();
        assert(response.getResponse().getContentAsString().equals(response_string_wrong_status));
    }

    @Test
    public void test_Invalid_Id_provided() throws Exception {
        String json = """
                {
                    "id":"1",
                    "description":"hi",
                    "status":"DONE"
                }
                """;

        String response_string = "Entry not found";

        MvcResult response = mockMvc.perform(put("/api/todo/17")
                        .contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andReturn();
        assert(response.getResponse().getContentAsString().equals(response_string));

    }
}
