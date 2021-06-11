package com.devin.dev.mvc;

import com.devin.dev.controller.post.PostForm;
import com.devin.dev.entity.post.Post;
import com.devin.dev.repository.post.PostRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import javax.servlet.http.HttpServletRequestWrapper;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@SpringBootTest
@AutoConfigureMockMvc
class PostControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private PostRepository postRepository;

    private MockHttpServletRequest servletRequest = new MockHttpServletRequest();

    @Test
    @WithMockUser
    void requestPostSucceeded() throws Exception {
        List<Post> posts = postRepository.findByTitle("title_1");
        mvc.perform(get("/postlist/" + posts.get(0).getId()))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @WithMockUser
    void requestPostFailed() throws Exception {
        mvc.perform(get("/postlist/0"))
                .andDo(print());
    }

    @Test
    @WithMockUser
    void requestDeleteSucceeded() throws Exception {
        List<Post> posts = postRepository.findByTitle("title_2");
        mvc.perform(delete("/post/" + posts.get(0).getId()))
                .andDo(print());
    }

    @Test
    @WithMockUser
    void requestCreateSucceeded() throws Exception {
        PostForm form = new PostForm();
        form.setContent("content_1");
        form.setPost_tags(List.of("tag_0","tag_1"));
        form.setPost_images(List.of("img_1", "img_2"));
        form.setTitle("title_1");
        mvc.perform(
                        post("/post").with(request -> {
                            request.setRemoteUser("USER");
                            return request;
                        })
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(new ObjectMapper().writeValueAsString(form))
                    )
                .andDo(print());
    }
}