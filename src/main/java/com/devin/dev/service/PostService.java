package com.devin.dev.service;

import com.devin.dev.entity.post.Post;
import com.devin.dev.entity.post.PostImage;
import com.devin.dev.entity.post.PostTag;
import com.devin.dev.entity.post.Subject;
import com.devin.dev.entity.user.User;
import com.devin.dev.repository.post.PostImageRepository;
import com.devin.dev.repository.post.PostRepository;
import com.devin.dev.repository.post.PostTagRepository;
import com.devin.dev.repository.reply.ReplyRepository;
import com.devin.dev.repository.reply.ReplyImageRepository;
import com.devin.dev.repository.reply.ReplyLikeRepository;
import com.devin.dev.repository.subject.SubjectRepository;
import com.devin.dev.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostService {

    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final PostImageRepository postImageRepository;
    private final PostTagRepository postTagRepository;
    private final SubjectRepository subjectRepository;
    private final ReplyRepository replyRepository;
    private final ReplyImageRepository replyImageRepository;
    private final ReplyLikeRepository replyLikeRepository;

    @Transactional
    public Long post(Long userId, String title, String content, List<String> tags, List<String> imagePaths) {
        // 엔티티 조회
        User user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("유저 조회 실패"));
        List<Subject> postSubjects = subjectRepository.findByNameIn(tags);

        // 엔티티 생성
        List<PostImage> postImages = PostImage.createPostImages(imagePaths);
        List<PostTag> postTags = PostTag.createPostTags(postSubjects);
        Post post = Post.createPostWithImages(user, title, content, postTags, postImages);

        // 게시글 작성자 경험치증가
        user.changeExp(User.ExpChangeType.CREATE_POST);

        // 저장
        postRepository.save(post);
        postImageRepository.saveAll(postImages);
        postTagRepository.saveAll(postTags);

        return post.getId();
    }

    // 게시글 수정
    @Transactional
    public Long editPost(Long userId, Long postId, String title, String content, List<String> tags, List<String> imagePaths) {
        // 엔티티 조회. 실패시 IllegalArgumentException
        User user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("유저 조회 실패"));
        Post post = postRepository.findById(postId).orElseThrow(() -> new IllegalArgumentException("게시글 조회 실패"));
        List<Subject> postSubjects = subjectRepository.findByNameIn(tags);
        List<PostTag> existTags = postTagRepository.findByTagIn(postSubjects);

        System.out.println("postSubjects = " + postSubjects);
        System.out.println("existTags = " + existTags);
        boolean b = postSubjects.containsAll(existTags.stream().map(PostTag::getTag).collect(Collectors.toList()));
        System.out.println("booleanValue = " + b);


        // 기존 태그 및 이미지 경로 삭제
        List<PostImage> postImages = postImageRepository.findByPost(post);
        postImageRepository.deleteInBatch(postImages);

        // 수정된 내용 반영
        List<PostImage> newPostImages = PostImage.createPostImages(imagePaths);
        post.setTitle(title);
        post.setContent(content);
        List<PostTag> deleteList = new ArrayList<>();

        for (PostTag tag : post.getTags()) { // 1 2
            if (!postSubjects.contains(tag.getTag())) { // 2 3
                deleteList.add(tag);
            } else {
                postSubjects.remove(tag.getTag());
            }
        }
        List<PostTag> newPostTags = PostTag.createPostTags(postSubjects);
        postTagRepository.deleteInBatch(deleteList);

        System.out.println("deleteList = " + deleteList);
        System.out.println("newPostTags = " + newPostTags);
        post.setPostTags(newPostTags);
        post.setPostImages(newPostImages);

        // 저장
        postImageRepository.saveAll(newPostImages);
        postRepository.save(post);
        postTagRepository.saveAll(newPostTags);

        return post.getId();
    }
}
