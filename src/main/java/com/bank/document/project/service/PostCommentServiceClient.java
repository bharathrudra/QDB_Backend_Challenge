package com.bank.document.project.service;

import com.bank.document.project.model.CommentModel;
import com.bank.document.project.model.PostModel;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name="post-comment-service", url = "http://localhost:8083")
public interface PostCommentServiceClient {
    @GetMapping("/posts/getPostsByDocId/{docId}")
    public List<PostModel> getPostDataByDocId(@PathVariable String docId);
    @GetMapping("/comments/getCommentsByDocId/{docId}")
    public List<CommentModel> getCommentsByDocId(@PathVariable String docId);
}
