package com.devin.dev.controller.post;

import com.devin.dev.model.DefaultResponse;
import com.devin.dev.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.data.domain.Pageable;

@RestController
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @GetMapping("/postlist/posts")
    public DefaultResponse<?> getPostList(@RequestParam(defaultValue = "-1")Long id,
          @PageableDefault(page = 0, sort = "createdDate", direction = Sort.Direction.DESC) Pageable pageable) {
        return postService.getPostInfoList(id, pageable);
    }

}
