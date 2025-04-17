package com.bupt.backend.controller;

import com.bupt.backend.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;

    @GetMapping
    public ResponseEntity<Page<PostDTO>> getPosts(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) String status) {

        return ResponseEntity.ok(postService.getPostsByPage(page, size, status));
    }

    @PostMapping
    public ResponseEntity<Integer> createPost(@Valid @RequestBody PostCreateDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(postService.createPost(dto));
    }
}
