package com.devin.dev.service;

import com.devin.dev.controller.post.PostSearchCondition;
import com.devin.dev.controller.reply.ReplyOrderCondition;
import com.devin.dev.dto.post.PostDetailsDto;
import com.devin.dev.dto.post.PostInfoDto;
import com.devin.dev.entity.post.Post;
import com.devin.dev.entity.post.PostImage;
import com.devin.dev.entity.post.PostTag;
import com.devin.dev.entity.post.Subject;
import com.devin.dev.entity.user.User;
import com.devin.dev.model.DefaultResponse;
import com.devin.dev.repository.post.PostImageRepository;
import com.devin.dev.repository.post.PostRepository;
import com.devin.dev.repository.post.PostTagRepository;
import com.devin.dev.repository.reply.ReplyRepository;
import com.devin.dev.repository.reply.ReplyImageRepository;
import com.devin.dev.repository.reply.ReplyLikeRepository;
import com.devin.dev.repository.subject.SubjectRepository;
import com.devin.dev.repository.user.UserRepository;
import com.devin.dev.utils.ResponseMessage;
import com.devin.dev.utils.StatusCode;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
    public DefaultResponse<PostDetailsDto> post(Long userId, String title, String content, List<String> tags, List<String> imagePaths) {
        // 엔티티 조회. 실패시 에러코드 반환
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isEmpty()) {
            return new DefaultResponse<>(StatusCode.BAD_REQUEST, ResponseMessage.NOT_FOUND_USER);
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
        return new DefaultResponse<>(StatusCode.OK, ResponseMessage.POST_UPLOAD_SUCCESS, postDto);
    }

    // 게시글 수정
    @Transactional
    public DefaultResponse<?> editPost(Long userId, Long postId, String title, String content, List<String> tags, List<String> imagePaths) {
        // 엔티티 조회. 실패시 에러코드 반환
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isEmpty()) {
            return new DefaultResponse<>(StatusCode.BAD_REQUEST, ResponseMessage.NOT_FOUND_USER);
        }
        Optional<Post> postOptional = postRepository.findById(postId);
        if (postOptional.isEmpty()) {
            return new DefaultResponse<>(StatusCode.BAD_REQUEST, ResponseMessage.NOT_FOUND_POST);
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
        return new DefaultResponse<>(StatusCode.OK, ResponseMessage.POST_EDIT_SUCCESS);
    }

    @Transactional
    public DefaultResponse<PostDetailsDto> getPost(Long postId, ReplyOrderCondition replyOrderCondition) {
        Optional<PostDetailsDto> postOptional = postRepository.findPostDetailsById(postId, replyOrderCondition);
        if (postOptional.isEmpty()) {
            return new DefaultResponse<>(StatusCode.BAD_REQUEST, ResponseMessage.NOT_FOUND_POST);
        }
        PostDetailsDto postDetailsDto = postOptional.get();

        return new DefaultResponse<>(StatusCode.OK, ResponseMessage.FOUND_POST, postDetailsDto);
    }

    // 게시글 검색
    @Transactional
    public DefaultResponse<Page<PostInfoDto>> getPostInfoListByCondition(PostSearchCondition condition, Pageable pageable) {
        Page<PostInfoDto> postDtos = postRepository.findPostInfoDtoPageByCondition(condition, pageable);

        PageImpl<PostInfoDto> page = new PageImpl<>(postDtos.toList(), pageable, postDtos.getTotalElements());

        return new DefaultResponse<>(StatusCode.OK, ResponseMessage.FOUND_POST, page);
    }

    @Transactional
    public DefaultResponse<Page<PostInfoDto>> getPostInfoList(Long id, Pageable pageable) {
        Page<Post> posts =  (id > 0)? postRepository.findAllByTagId(id, pageable) : postRepository.findAll(pageable);
        Page<PostInfoDto> postInfoDtos = posts.map(PostInfoDto::new);
        PageImpl<PostInfoDto> postInfoDtosImpl = new PageImpl<>(postInfoDtos.toList(), pageable, postInfoDtos.getTotalElements());
        return new DefaultResponse<>(StatusCode.OK, ResponseMessage.FOUND_POST, postInfoDtosImpl);
    }
}
