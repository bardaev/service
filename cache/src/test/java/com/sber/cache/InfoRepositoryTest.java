package com.sber.cache;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.sber.cache.entity.Info;
import com.sber.cache.repository.IInfoRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.MOCK,
        classes = CacheApplication.class
)
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application.properties")
public class InfoRepositoryTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private IInfoRepository infoRepository;

    @Test
    public void mainControllerCreateTest() throws Exception {
        int id = 5;
        String value = "test";
        LocalDateTime changeTime = LocalDateTime.now();

        Info info = createTestInfo((long) id, value, changeTime);

        byte[] bodyObject = getObjectWriter().writeValueAsBytes(info);

        mockMvc.perform(post("/cache").contentType(MediaType.APPLICATION_JSON).content(bodyObject))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(id)))
                .andExpect(jsonPath("$.value", is(value)))
                .andExpect(jsonPath("$.changeTime", is(changeTime.toString())));

    }

    @Test
    public void mainControllerUpdateTest() throws Exception {
        int id = 5;
        String value = "test";
        LocalDateTime changeTime = LocalDateTime.now();

        Info info = createTestInfo((long) id, value, changeTime);

        info.setValue("change test");
        info.setChangeTime(LocalDateTime.now());

        byte[] bodyObject = getObjectWriter().writeValueAsBytes(info);

        mockMvc.perform(post("/cache").contentType(MediaType.APPLICATION_JSON).content(bodyObject))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(id)))
                .andExpect(jsonPath("$.value", is(info.getValue())))
                .andExpect(jsonPath("$.changeTime", is(info.getChangeTime().toString())));
    }

    private Info createTestInfo(Long id, String value, LocalDateTime time) {
        return infoRepository.createOrUpdateInfo(new Info(id, value, time));
    }

    private ObjectWriter getObjectWriter() {
        ObjectMapper objectMapper = Jackson2ObjectMapperBuilder.json().createXmlMapper(false).build();
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, true);
        return objectMapper.writer().withDefaultPrettyPrinter();
    }
}
