package com.bank.document.project.payload;

import com.bank.document.project.model.CommentModel;
import com.bank.document.project.model.DocumentModel;
import com.bank.document.project.model.PostModel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;
@Getter@Setter@ToString
public class GetDocResponse {
    private List<PostModel> postModelList;
    private List<CommentModel> commentModelList;
    private DocumentModel documentModel;

    public GetDocResponse(DocumentModel documentModel, List<PostModel> postModelList, List<CommentModel> commentModelList){
        this.documentModel=documentModel;
        this.postModelList=postModelList;
        this.commentModelList=commentModelList;
    }
}
