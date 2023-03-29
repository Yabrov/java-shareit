package ru.practicum.shareit.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.util.MultiValueMap;

import java.nio.charset.StandardCharsets;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

abstract class AbstractControllerTest {

    protected abstract Long getXSharerUserId();

    protected ObjectMapper mapper;

    protected MockMvc mvc;

    protected ResultActions performGetRequests(String url, MultiValueMap<String, String> params) throws Exception {
        return mvc.perform(get(url)
                .header("X-Sharer-User-Id", getXSharerUserId())
                .params(params)
                .characterEncoding(StandardCharsets.UTF_8)
                .accept(MediaType.APPLICATION_JSON));
    }

    protected <T> ResultActions performPostRequests(String url, T dto) throws Exception {
        return mvc.perform(post(url)
                .header("X-Sharer-User-Id", getXSharerUserId())
                .content(mapper.writeValueAsString(dto))
                .characterEncoding(StandardCharsets.UTF_8)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON));
    }

    protected <T> ResultActions performPatchRequests(String url, T dto, MultiValueMap<String, String> params) throws Exception {
        return mvc.perform(patch(url)
                .header("X-Sharer-User-Id", getXSharerUserId())
                .params(params)
                .content(mapper.writeValueAsString(dto))
                .characterEncoding(StandardCharsets.UTF_8)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON));
    }

    protected ResultActions performDeleteRequests(String url) throws Exception {
        return mvc.perform(delete(url)
                .header("X-Sharer-User-Id", getXSharerUserId())
                .characterEncoding(StandardCharsets.UTF_8)
                .accept(MediaType.APPLICATION_JSON));
    }
}
