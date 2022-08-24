package com.bank.document.project.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.stereotype.Component;

@Component
@Getter@Setter@ToString
public class CommentModel {
    private long commentId;
    private String commentDescription;
    private String docId;
    private long postId;
}
