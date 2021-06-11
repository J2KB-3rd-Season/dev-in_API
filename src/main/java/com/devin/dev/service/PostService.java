package com.devin.dev.service;

import com.devin.dev.controller.post.PostForm;
import com.devin.dev.controller.post.PostSearchCondition;
import com.devin.dev.controller.reply.ReplyOrderCondition;
import com.devin.dev.dto.post.PostDetailsDto;
import com.devin.dev.dto.post.PostInfoDto;
import com.devin.dev.dto.reply.ReplyLikeDto;
import com.devin.dev.entity.post.*;
import com.devin.dev.entity.reply.ReplyLike;
import com.devin.dev.entity.user.User;
import com.devin.dev.model.DefaultResponse;
import com.devin.dev.repository.post.PostImageRepository;
import com.devin.dev.repository.post.PostLikeRepository;
import com.devin.dev.repository.post.PostRepository;
import com.devin.dev.repository.post.PostTagRepository;
import com.devin.dev.repository.reply.ReplyRepository;
import com.devin.dev.repository.reply.ReplyImageRepository;
import com.devin.dev.repository.reply.ReplyLikeRepository;
import com.devin.dev.repository.subject.SubjectRepository;
import com.devin.dev.repository.user.UserRepository;
import com.devin.dev.security.JwtAuthTokenProvider;
import com.devin.dev.utils.ResponseMessage;
import com.devin.dev.utils.StatusCode;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostService {

    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final PostImageRepository postImageRepository;
    private final PostLikeRepository postLikeRepository;
    private final PostTagRepository postTagRepository;
    private final SubjectRepository subjectRepository;
    private final ReplyRepository replyRepository;
    private final ReplyImageRepository replyImageRepository;
    private final ReplyLikeRepository replyLikeRepository;
    private final JwtAuthTokenProvider tokenProvider;

    @Transactional
    public DefaultResponse<?> post(PostForm form, HttpServletRequest request) {
        String token = tokenProvider.parseToken(request);
        Long userId;
        if (tokenProvider.validateToken(token)) {
            userId = tokenProvider.getUserId(token);
        } else {
            return new DefaultResponse<>(StatusCode.FAIL_AUTH, ResponseMessage.NOT_FOUND_USER);
        }

        // 엔티티 조회. 실패시 에러코드 반환
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isEmpty()) {
            return new DefaultResponse<>(StatusCode.NOT_EXIST, ResponseMessage.NOT_FOUND_USER);
        }
        User user = userOptional.get();
        List<Subject> postSubjects = subjectRepository.findByNameIn(form.getPost_tags());

        // 엔티티 생성
        List<PostImage> postImages = PostImage.createPostImages(form.getPost_images());
        List<PostTag> postTags = PostTag.createPostTags(postSubjects);
        Post post = Post.createPostWithImages(user, form.getTitle(), form.getContent(), postTags, postImages);

        // 게시글 작성자 경험치증가
        user.changeExp(User.ExpChangeType.CREATE_POST);

        // 저장
        postRepository.save(post);
        postImageRepository.saveAll(postImages);
        postTagRepository.saveAll(postTags);

        PostDetailsDto postDetailsDto = new PostDetailsDto(post);

        // 성공 메시지 및 코드 반환
        return new DefaultResponse<>(StatusCode.SUCCESS, ResponseMessage.POST_UPLOAD_SUCCESS, postDetailsDto);
    }

    @Transactional
    public DefaultResponse<PostDetailsDto> post(Long userId, String title, String content, List<String> tags, List<String> imagePaths) {
        // 엔티티 조회. 실패시 에러코드 반환
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isEmpty()) {
            return new DefaultResponse<>(StatusCode.NOT_EXIST, ResponseMessage.NOT_FOUND_USER);
        }
        User user = userOptional.get();
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

        PostDetailsDto postDto = new PostDetailsDto(post);

        // 성공 메시지 및 코드 반환
        return new DefaultResponse<>(StatusCode.SUCCESS, ResponseMessage.POST_UPLOAD_SUCCESS, postDto);
    }

    // 게시글 수정
    @Transactional
    public DefaultResponse<?> editPost(Long userId, Long postId, String title, String content, List<String> tags, List<String> imagePaths) {
        // 엔티티 조회. 실패시 에러코드 반환
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isEmpty()) {
            return new DefaultResponse<>(StatusCode.NOT_EXIST, ResponseMessage.NOT_FOUND_USER);
        }
        Optional<Post> postOptional = postRepository.findById(postId);
        if (postOptional.isEmpty()) {
            return new DefaultResponse<>(StatusCode.NOT_EXIST, ResponseMessage.NOT_FOUND_POST);
        }
        Post post = postOptional.get();
        List<Subject> postSubjects = subjectRepository.findByNameIn(tags);

        // 기존 태그 및 이미지 경로 삭제
        List<PostImage> postImages = postImageRepository.findByPost(post);
        postImageRepository.deleteInBatch(postImages);

        // 수정된 내용 반영
        List<PostImage> newPostImages = PostImage.createPostImages(imagePaths);
        post.setTitle(title);
        post.setContent(content);
        List<PostTag> deleteList = new ArrayList<>();

        for (PostTag tag : post.getTags()) {
            if (!postSubjects.contains(tag.getTag())) {
                deleteList.add(tag);
            } else {
                postSubjects.remove(tag.getTag());
            }
        }
        List<PostTag> newPostTags = PostTag.createPostTags(postSubjects);
        postTagRepository.deleteInBatch(deleteList);

        post.setPostTags(newPostTags);
        post.setPostImages(newPostImages);

        // 저장
        postImageRepository.saveAll(newPostImages);
        postRepository.save(post);
        postTagRepository.saveAll(newPostTags);

        // 성공 메시지 및 코드 반환
        return new DefaultResponse<>(StatusCode.SUCCESS, ResponseMessage.POST_EDIT_SUCCESS);
    }

    @Transactional(readOnly = true)
    public DefaultResponse<PostDetailsDto> getPost(Long postId, ReplyOrderCondition replyOrderCondition) {
        Optional<PostDetailsDto> postOptional = postRepository.findPostDetailsById(postId, replyOrderCondition);
        if (postOptional.isEmpty()) {
            return new DefaultResponse<>(StatusCode.NOT_EXIST, ResponseMessage.NOT_FOUND_POST);
        }
        PostDetailsDto postDetailsDto = postOptional.get();

        return new DefaultResponse<>(StatusCode.SUCCESS, ResponseMessage.FOUND_POST, postDetailsDto);
    }

    // 게시글 검색
    @Transactional(readOnly = true)
    public DefaultResponse<Page<PostInfoDto>> getPostInfoListByCondition(PostSearchCondition condition, Pageable pageable) {
        Page<PostInfoDto> postDtos = postRepository.findPostInfoDtoPageByCondition(condition, pageable);

        PageImpl<PostInfoDto> page = new PageImpl<>(postDtos.toList(), pageable, postDtos.getTotalElements());

        return new DefaultResponse<>(StatusCode.SUCCESS, ResponseMessage.FOUND_POST, page);
    }

    @Transactional(readOnly = true)
    public DefaultResponse<Page<PostInfoDto>> getPostInfoList(Long id, Pageable pageable) {
        Page<Post> posts =  (id > 0)? postRepository.findAllByTagId(id, pageable) : postRepository.findAll(pageable);
        Page<PostInfoDto> postInfoDtos = posts.map(PostInfoDto::new);
        PageImpl<PostInfoDto> postInfoDtosImpl = new PageImpl<>(postInfoDtos.toList(), pageable, postInfoDtos.getTotalElements());
        return new DefaultResponse<>(StatusCode.SUCCESS, ResponseMessage.FOUND_POST, postInfoDtosImpl);
    }

    @Transactional
    public DefaultResponse<PostDetailsDto> deletePost(Long postId) {
        Optional<Post> postOptional = postRepository.findById(postId);
        if (postOptional.isEmpty()) {
            return new DefaultResponse<>(StatusCode.NOT_EXIST, ResponseMessage.NOT_FOUND_POST);
        }
        Post post = postOptional.get();

        postImageRepository.deleteAll(post.getImages());
        postTagRepository.deleteAll(post.getTags());
        postLikeRepository.deleteAll(post.getLikes());
        replyImageRepository.deleteAll(post.getReplies().stream().flatMap(reply -> reply.getImages().stream()).collect(Collectors.toList()));
        replyLikeRepository.deleteAll(post.getReplies().stream().flatMap(reply -> reply.getLikes().stream()).collect(Collectors.toList()));
        replyRepository.deleteAll(post.getReplies());
        postRepository.delete(post);

        return new DefaultResponse<>(StatusCode.SUCCESS, ResponseMessage.DELETED_POST);
    }
}


