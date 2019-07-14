package com.waes.comparator;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.waes.comparator.controller.request.EncodedDataRequest;
import com.waes.comparator.entity.Base64Entry;
import com.waes.comparator.repository.Base64DiffRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import java.util.Optional;
import java.util.Random;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;


/**
 * Created by volkangumus on 14.07.2019
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class Base64DiffControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    public Base64DiffRepository diffRepository;

    @Autowired
    private WebApplicationContext webApplicationContext;

    private static final String BASE_URL = "/v1/diff/{id}";
    private long randomId;

    @Before
    public void setup() {
        this.mockMvc = webAppContextSetup(webApplicationContext).build();
        this.diffRepository.deleteAll();
        this.randomId = createRandomId();
    }

    @Test
    public void testSaveLeft() throws Exception {
        EncodedDataRequest request = new EncodedDataRequest()
                .setData("SGVsbG8gV2Flcw==");

        String content = mockMvc.perform(
                post(BASE_URL + "/left", randomId)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectToJsonString(request)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        Optional<Base64Entry> optionalBase64Entry = diffRepository.findById(randomId);
        assertThat(optionalBase64Entry, notNullValue());

        Base64Entry entry = optionalBase64Entry.get();
        assertThat(entry.getId(), is(randomId));
        assertThat(entry.getLeft(), is("SGVsbG8gV2Flcw=="));
        assertThat(entry.getRight(), nullValue());

        assertThat(content, is("{\"message\":\"Left side of entry registered\"}"));
    }

    @Test
    public void testSaveRight() throws Exception {
        EncodedDataRequest request = new EncodedDataRequest()
                .setData("SGVsbG8gV2Flcw==");

        String content = mockMvc.perform(
                post(BASE_URL + "/right", randomId)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectToJsonString(request)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        Optional<Base64Entry> optionalBase64Entry = diffRepository.findById(randomId);
        assertThat(optionalBase64Entry, notNullValue());

        Base64Entry entry = optionalBase64Entry.get();
        assertThat(entry.getId(), is(randomId));
        assertThat(entry.getRight(), is("SGVsbG8gV2Flcw=="));
        assertThat(entry.getLeft(), nullValue());

        assertThat(content, is("{\"message\":\"Right side of entry registered\"}"));
    }

    @Test
    public void testDiffWithEqual() throws Exception {
        Base64Entry base64Entry = new Base64Entry()
                .setId(randomId)
                .setLeft("aGVsbG8gV2FlcyA=")
                .setRight("aGVsbG8gV2FlcyA=");

        diffRepository.save(base64Entry);

        String content = mockMvc.perform(
                get(BASE_URL, randomId)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertThat(content, is("{\"message\":\"Left and Right base64 datas are equal\"}"));
    }

    @Test
    public void testDiffWithDifferentLengths() throws Exception {
        Base64Entry base64Entry = new Base64Entry()
                .setId(randomId)
                .setLeft("aGVsbG8gV2FlcyA=")
                .setRight("aGVsbG8gV2=");

        diffRepository.save(base64Entry);

        String content = mockMvc.perform(
                get(BASE_URL, randomId)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertThat(content, is("{\"message\":\"Left and Right base64 datas do not have same size\"}"));
    }

    @Test
    public void testDiff() throws Exception {
        Base64Entry base64Entry = new Base64Entry()
                .setId(randomId)
                .setLeft("SGVsbG8gV2Flcw==")
                .setRight("SGVsbG8gV0Flcw==");

        diffRepository.save(base64Entry);

        String content = mockMvc.perform(
                get(BASE_URL, randomId)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertThat(content, is("{\"message\":\"Left and Right base64 datas are in same size and here is offset diff: 9\"}"));
    }

    private long createRandomId() {
        Random random = new Random();
        return random.nextLong();
    }

    private String objectToJsonString(EncodedDataRequest request) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(request);
    }
}
