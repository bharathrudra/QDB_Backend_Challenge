package com.bank.document.project.controller;

import com.bank.document.project.exception.DocumentStoreException;
import com.bank.document.project.model.CommentModel;
import com.bank.document.project.model.DocumentModel;
import com.bank.document.project.model.PostModel;
import com.bank.document.project.payload.CreateDocResponse;
import com.bank.document.project.payload.GetDocResponse;
import com.bank.document.project.service.DocumentService;
import com.bank.document.project.service.PostCommentServiceClient;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 . As a User, I want to be able to upload a PDF Document, so that I can later perform other actions with it (e.g. view it, edit it, remove it etc)
 B. As a User, I want to be able to remove my uploaded Document, so that I exercise my GDPR "Right to be forgotten"
 C. As a User, I want to be able to be able to view all my uploaded documents, so that I can select one and view it
 *
 */
@RestController
@RequestMapping("/documents/")
public class DocumentController {
    @Autowired
    private DocumentService documentService;
    @Autowired
    private PostCommentServiceClient postCommentServiceClient;

    public static final String DOC_SERVICE="docService";

    /*To be able to upload a PDF Document*/
    @PostMapping(value="/uploadDocument")
    public ResponseEntity<?> uploadDocument(@RequestParam("file") MultipartFile pdfFile){
        if(!pdfFile.getContentType().contains("pdf")){
            throw new DocumentStoreException(" please upload the pdf file format");
        }
        DocumentModel createdModel=documentService.createDocument(pdfFile);
        return ResponseEntity.ok().body(new CreateDocResponse(createdModel.getDocId(),pdfFile.getContentType(),pdfFile.getSize()));
    }

    /*To be able to upload a PDF Document*/
    @PostMapping(value="/testUploadDocument")
    public ResponseEntity<?> testUploadDocument(@RequestParam("file") MultipartFile pdfFile){
        if(!pdfFile.getContentType().contains("pdf")){
            throw new DocumentStoreException(" please upload the pdf file format");
        }
        DocumentModel createdModel=documentService.createDocument(pdfFile);
        return ResponseEntity.ok().build();
    }

    /*To be able to view all Documents , posts and comments related to it.
    * During fallback - to view all Documents*/
    @GetMapping("/getAllDocuments")
    @CircuitBreaker(name="docService", fallbackMethod = "getAllDocumentsFallback")
    public ResponseEntity<List<ResponseEntity<GetDocResponse>>> getAllDocuments(){
        List<DocumentModel> allDocuments= documentService.getAllDocuments();
        List<ResponseEntity<GetDocResponse>> responseList = new ArrayList<>();
        for(DocumentModel document: allDocuments){
            try {
                responseList.add(getRawDocumentById(document.getDocId()));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        return ResponseEntity.ok().body(responseList);
    }
    /*To be able to remove the document*/
    @DeleteMapping("/deleteDocument/{docId}")
    public String deleteDocument(@PathVariable String docId){
        documentService.deleteDocument(docId);
        String message = "Document with the fileId "+docId+" deleted successfully  ";
        return message;
    }

    /*To be able to view a pdf format document with the help of Id*/
    @GetMapping("/getPdfDocument/{docId}")
    public ResponseEntity<Resource> getPdfDocument(@PathVariable String docId) throws FileNotFoundException {
        System.out.println("-- inside get method---");
        DocumentModel databaseFile = documentService.getDocument(docId);

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(databaseFile.getFileType()))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + databaseFile.getFileName() + "\"")
                .body(new ByteArrayResource(databaseFile.getFileData()));
    }

    /*To be able to view a raw format document along with
    posts and comments associated with it with the help of Id*/
    @GetMapping("/getRawDocumentById/{docId}")
    public ResponseEntity<GetDocResponse> getRawDocumentById(@PathVariable String docId) throws FileNotFoundException {
        DocumentModel documentModel = documentService.getDocument(docId);
        List<PostModel> postModelList =postCommentServiceClient.getPostDataByDocId(docId);
        List<CommentModel> commentModelList = postCommentServiceClient.getCommentsByDocId(docId);
        return ResponseEntity.ok().body(new GetDocResponse(documentModel,postModelList,commentModelList));
    }

    /*Fallback method to get all documents in case of issue with post and comments services*/
    public ResponseEntity<List<ResponseEntity<GetDocResponse>>> getAllDocumentsFallback(Exception e) {
        List<DocumentModel> documentModelList= new ArrayList<DocumentModel>();
        List<ResponseEntity<GetDocResponse>> responseList = new ArrayList<>();
        try{
            documentModelList= documentService.getAllDocuments();
            for(DocumentModel document: documentModelList){
                responseList.add(ResponseEntity.ok().body(new GetDocResponse(document,null,null)));
            }
        }catch(Exception ex){
            throw new DocumentStoreException("Error in fallback method", ex);
        }
        return ResponseEntity.ok().body(responseList);
    }

    /* To be able to check if the docId is available - used while saving posts
     and comments in posts comments service*/
    @GetMapping("/checkDocumentId/{docId}")
    public String checkDocumentId(@PathVariable String docId) throws FileNotFoundException {
        DocumentModel databaseFile = documentService.getDocument(docId);
        return databaseFile.getDocId();
    }
}
