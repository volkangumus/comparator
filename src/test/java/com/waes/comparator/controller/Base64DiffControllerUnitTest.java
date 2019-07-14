package com.waes.comparator.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.waes.comparator.controller.request.AspectEnum;
import com.waes.comparator.controller.request.EncodedDataRequest;
import com.waes.comparator.service.Base64DiffService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Random;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Created by volkangumus on 14.07.2019
 */
public class Base64DiffControllerUnitTest {

    @InjectMocks
    private Base64DiffController controller;

    @Mock
    private Base64DiffService diffService;

    private MockMvc mockMvc;
    private long randomId;
    private static final String BASE_URL = "/v1/diff/{id}";

    @Before
    public void onSetup() {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .build();

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

        verify(diffService).save(randomId, request.getData(), AspectEnum.LEFT);
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

        verify(diffService).save(randomId, request.getData(), AspectEnum.RIGHT);
        assertThat(content, is("{\"message\":\"Right side of entry registered\"}"));
    }

    @Test
    public void testDiff() throws Exception {
        when(diffService.getDiff(randomId)).thenReturn("Left and Right base64 datas are in same size and here is offset diff: 9");

        String content = mockMvc.perform(
                get(BASE_URL, this.randomId)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        verify(diffService).getDiff(randomId);
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
