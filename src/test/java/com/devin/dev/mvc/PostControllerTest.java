package com.devin.dev.mvc;

import com.devin.dev.entity.post.Post;
import com.devin.dev.repository.post.PostRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import javax.servlet.http.HttpServletRequestWrapper;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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
}