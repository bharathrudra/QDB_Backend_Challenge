package com.bank.document.project.payload;

import com.bank.document.project.model.CommentModel;
import com.bank.document.project.model.DocumentModel;
import com.bank.document.project.model.PostModel;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter@Setter
public class CreateDocResponse {

    private String fileId;
    private String fileType;
    private long size;

    public CreateDocResponse(String fileId, String fileType, long size){
        this.fileId=fileId;
        this.fileType=fileType;
        this.size=size;
    }

}
