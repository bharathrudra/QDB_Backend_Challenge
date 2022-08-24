package com.bank.document.project.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter@Setter@ToString
public class PostModel {
    private long postId;
    private String docId;
    private String postDescription;
}
