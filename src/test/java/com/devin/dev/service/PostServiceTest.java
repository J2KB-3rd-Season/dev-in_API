package com.devin.dev.service;

import com.devin.dev.entity.post.Post;
import com.devin.dev.entity.post.PostImage;
import com.devin.dev.entity.post.PostTag;
import com.devin.dev.entity.post.Subject;
import com.devin.dev.entity.user.User;
import com.devin.dev.entity.user.UserStatus;
import com.devin.dev.repository.post.PostRepository;
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

    @Test
    void postTest() {
        Subject subject1 = new Subject("s1");
        Subject subject2 = new Subject("s2");
        Subject subject3 = new Subject("s3");
        em.persist(subject1);
        em.persist(subject2);
        em.persist(subject3);

        User postUser = new User("D", "d@b.com", "passD", "0004", UserStatus.ACTIVE);
        em.persist(postUser);

        postService.post(postUser.getId(), "titleA1", "postA1", List.of("s1", "s2") ,List.of("p1", "p2", "p3"));

        List<Post> posts = postRepository.findByTitle("titleA1");

        assertThat(posts).extracting("title").containsExactly("titleA1");
        assertThat(posts.get(0).getTags()).extracting("tag").containsExactly(subject1, subject2);
        assertThat(posts.get(0).getImages()).extracting("path").containsExactly("p1", "p2", "p3");
        assertThat(posts.get(0).getUser().getExp()).isEqualTo(1);
    }

    @Test
    void editPostTest() {
        Subject subject1 = new Subject("s1");
        Subject subject2 = new Subject("s2");
        Subject subject3 = new Subject("s3");
        em.persist(subject1);
        em.persist(subject2);
        em.persist(subject3);

        User postUser = new User("D", "d@b.com", "passD", "0004", UserStatus.ACTIVE);
        em.persist(postUser);

        Post post1 = new Post(postUser, "PostC1", "ContentC1");
        List<PostTag> postTags = PostTag.createPostTags(List.of(subject1, subject2));
        post1.setPostTags(postTags);
        List<PostImage> postImages = PostImage.createPostImages(List.of("p1", "p2", "p3"));
        post1.setImages(postImages);
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
}