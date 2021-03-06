package com.devin.dev.service;

import com.devin.dev.controller.reply.ReplyOrderCondition;
import com.devin.dev.dto.post.PostDetailsDto;
import com.devin.dev.entity.post.*;
import com.devin.dev.entity.user.User;
import com.devin.dev.entity.user.UserStatus;
import com.devin.dev.model.DefaultResponse;
import com.devin.dev.repository.post.PostRepository;
import com.devin.dev.utils.ResponseMessage;
import com.devin.dev.utils.StatusCode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Transactional
@SpringBootTest
class PostServiceTest {

    @Autowired
    EntityManager em;

    @Autowired
    PostService postService;

    @Autowired
    PostRepository postRepository;

    Subject subject1;
    Subject subject2;
    Subject subject3;
    User postUser;

    @BeforeEach
    void createSampleData() {
        subject1 = new Subject("s1");
        subject2 = new Subject("s2");
        subject3 = new Subject("s3");
        em.persist(subject1);
        em.persist(subject2);
        em.persist(subject3);

        postUser = new User("D", "d@b.com", "passD", "0004", UserStatus.ACTIVE);
        em.persist(postUser);
    }

    @Test
    void postSucceeded() {
        DefaultResponse<?> response = postService.post(postUser.getId(), "titleA1", "postA1", List.of("s1", "s2"), List.of("p1", "p2", "p3"));
        assertThat(response.getStatusCode()).isEqualTo(StatusCode.SUCCESS.getCode());
        assertThat(response.getResponseMessage()).isEqualTo(ResponseMessage.POST_UPLOAD_SUCCESS);

        List<Post> posts = postRepository.findByTitle("titleA1");

        assertThat(posts).extracting("title").containsExactly("titleA1");
        assertThat(posts.get(0).getTags()).extracting("tag").containsExactly(subject1, subject2);
        assertThat(posts.get(0).getImages()).extracting("path").containsExactly("p1", "p2", "p3");
        assertThat(posts.get(0).getUser().getExp()).isEqualTo(1);
    }

    @Test
    void postFailed() {
        DefaultResponse<?> response = postService.post(9999L, "titleA1", "postA1", List.of("s1", "s2"), List.of("p1", "p2", "p3"));
        assertThat(response.getStatusCode()).isEqualTo(StatusCode.NOT_EXIST.getCode());
        assertThat(response.getResponseMessage()).isEqualTo(ResponseMessage.NOT_FOUND_USER);
    }

    @Test
    void editPostTest() {
        Post post1 = new Post(postUser, "PostC1", "ContentC1");
        List<PostTag> postTags = PostTag.createPostTags(List.of(subject1, subject2));
        post1.setPostTags(postTags);
        List<PostImage> postImages = PostImage.createPostImages(List.of("p1", "p2", "p3"));
        post1.setPostImages(postImages);
        postTags.forEach(em::persist);
        postImages.forEach(em::persist);
        em.persist(post1);

        Post post = postRepository.findById(post1.getId()).get();
        assertThat(post.getTitle()).isEqualTo("PostC1");
        assertThat(post.getTags()).extracting("tag").extracting("name").containsExactly("s1", "s2");
        assertThat(post.getImages()).extracting("path").containsExactly("p1", "p2", "p3");
        assertThat(post.getUser().getExp()).isEqualTo(0);


        postService.editPost(postUser.getId(), post1.getId(), "PostC2", "EditedC1", List.of("s2", "s3"), List.of("p2", "p3", "p4", "p1"));
        em.flush();
        em.clear();
        post = postRepository.findById(post1.getId()).get();
        assertThat(post.getTitle()).isEqualTo("PostC2");
        assertThat(post.getTags()).extracting("tag").extracting("name").containsExactly("s2", "s3");
        assertThat(post.getImages()).extracting("path").containsExactly("p2", "p3", "p4", "p1");
        assertThat(post.getUser().getExp()).isEqualTo(0);


        postService.editPost(postUser.getId(), post1.getId(), "PostC3","EditedC2", List.of("s3", "s2"), List.of("p4", "p3", "p1", "p2"));
        em.flush();
        post = postRepository.findById(post1.getId()).get();
        assertThat(post.getTitle()).isEqualTo("PostC3");
        assertThat(post.getTags()).extracting("tag").extracting("name").containsExactly("s2", "s3");
        assertThat(post.getImages()).extracting("path").containsExactly("p4", "p3", "p1", "p2");
        assertThat(post.getUser().getExp()).isEqualTo(0);
    }

    @Test
    void getPostSucceeded() {
        Post post1 = new Post(postUser, "PostC1", "ContentC1");
        List<PostTag> postTags = PostTag.createPostTags(List.of(subject1, subject2));
        post1.setPostTags(postTags);
        List<PostImage> postImages = PostImage.createPostImages(List.of("p1", "p2", "p3"));
        post1.setPostImages(postImages);
        post1.setStatus(PostStatus.NOT_SELECTED);
        postTags.forEach(em::persist);
        postImages.forEach(em::persist);
        em.persist(post1);
        em.flush();
        em.clear();

        ReplyOrderCondition replyOrderCondition = new ReplyOrderCondition();
        replyOrderCondition.setLatestDate(true);

        DefaultResponse<PostDetailsDto> response = postService.getPost(post1.getId(), replyOrderCondition);
        assertThat(response.getStatusCode()).isEqualTo(StatusCode.SUCCESS.getCode());
        assertThat(response.getResponseMessage()).isEqualTo(ResponseMessage.FOUND_POST);

        PostDetailsDto postDto = response.getData();
        assertThat(postDto.getTitle()).isEqualTo("PostC1");
        assertThat(postDto.getPublisher_name()).isEqualTo("D");
        assertThat(postDto.getPost_images()).containsExactly("p1","p2","p3");
    }

    @Test
    void deleteSucceeded() {
        Post post1 = new Post(postUser, "PostC1", "ContentC1");
        List<PostTag> postTags = PostTag.createPostTags(List.of(subject1, subject2));
        post1.setPostTags(postTags);
        List<PostImage> postImages = PostImage.createPostImages(List.of("p1", "p2", "p3"));
        post1.setPostImages(postImages);
        postTags.forEach(em::persist);
        postImages.forEach(em::persist);
        em.persist(post1);

        postService.deletePost(post1.getId());

        Post post = em.find(Post.class, post1.getId());
        assertThat(post).isNull();
    }

}