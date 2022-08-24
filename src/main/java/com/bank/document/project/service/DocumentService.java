package com.bank.document.project.service;


import com.bank.document.project.exception.DocumentStoreException;
import com.bank.document.project.model.DocumentModel;
import com.bank.document.project.repository.DocumentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class DocumentService {
    @Autowired
    private DocumentRepository documentRepository;

    public DocumentModel createDocument(MultipartFile file) {
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        try {
            DocumentModel document = new DocumentModel(fileName, file.getContentType(), file.getBytes());
            return documentRepository.save(document);
        } catch (IOException ie) {
            throw new DocumentStoreException(" Error in creating Document " + fileName + " Please try again ", ie);
        }
    }

    public List<DocumentModel> getAllDocuments(){
        List<DocumentModel> list= new ArrayList<DocumentModel>();
            list= (List<DocumentModel>) documentRepository.findAll();
            return list;
    }

    public DocumentModel getDocument(String fileId) throws FileNotFoundException {
        return documentRepository.findById(fileId)
                .orElseThrow(()-> new FileNotFoundException("File not found with given Id "+ fileId));
    }

    public void deleteDocument(String docId){
        documentRepository.deleteById(docId);
    }
}
