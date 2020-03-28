package home.yura.pivot_test;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@WebMvcTest
public class MainControllerTest {

    @Autowired
    private MockMvc mvc;

    @Test
    public void getTest_status() throws Exception {
        TestModel model = new TestModel("Yura", 28);
        mvc.perform(get("/test")
                .contentType("application/json"))
                .andExpect(status().isOk())
                .andExpect(content().string("{\"name\":\"Yura\",\"age\":28}"))
                .andExpect(content().json(mapToJson(model)));
    }

    static String mapToJson(Object obj) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(obj);
    }
}