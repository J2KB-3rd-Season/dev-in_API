package com.devin.dev;

import com.devin.dev.dto.post.PostDetailsDto;
import com.devin.dev.dto.post.PostSimpleDto;
import com.devin.dev.dto.reply.ReplyDto;
import com.devin.dev.dto.user.UserDetailsDto;
import com.devin.dev.dto.user.UserSimpleDto;
import com.devin.dev.model.DefaultResponse;
import com.devin.dev.service.PostService;
import com.devin.dev.service.ReplyService;
import com.devin.dev.service.SubjectService;
import com.devin.dev.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class SampleData {

    UserService userService;
    PostService postService;
    ReplyService replyService;
    SubjectService subjectService;

    List<UserDetailsDto> userDtos = new ArrayList<>();
    List<PostDetailsDto> postDtos = new ArrayList<>();
    List<ReplyDto> replyDtos = new ArrayList<>();

    public void createSubjects() {
        for (int i = 0; i < 5; i++) {
            subjectService.addSubject("tag_"+i);
        }
    }

    public void createUsers() {
        for (int i = 0; i < 20; i++) {
            UserSimpleDto userDto = new UserSimpleDto("name_"+i, "email_"+i,
                    "password_"+i, "phone_"+i);
            DefaultResponse<UserDetailsDto> response = userService.signUp(userDto);
            userDtos.add(response.getData());
        }
    }

    public void createPosts() {
        for (UserDetailsDto userDto : userDtos) {
            for (int i = 0; i < 3; i++) {
                DefaultResponse<PostDetailsDto> response = postService.post(userDto.getId(), "title_" + i,
                        "post_content_" + i,
                        List.of("tag_" + i, "tag_" + i, "tag_" + i),
                        List.of("post_img_1", "post_img_2", "post_img_3"));
                postDtos.add(response.getData());
            }
        }
    }

    public void createReplies() {
        for (UserDetailsDto userDto : userDtos) {
            for (PostDetailsDto postDto : postDtos) {
                for (int i = 0; i < 2; i++) {
                    DefaultResponse<ReplyDto> response = replyService.reply(userDto.getId(), postDto.getPostId(),
                            "reply_content_" + i,
                            List.of("reply_img_1", "reply_img_2", "reply_img_3"));
                    replyDtos.add(response.getData());
                }
            }
        }
    }


}
