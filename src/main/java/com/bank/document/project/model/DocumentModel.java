package com.bank.document.project.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity
@Table(name="files")
@Getter
@Setter
@ToString
public class DocumentModel {
    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private String docId;
    private String fileName;
    private String fileType;
    @Lob
    private byte[] fileData;

    public DocumentModel(){}

    public DocumentModel(String fileName, String fileType, byte[] fileData){
        this.fileName=fileName;
        this.fileType=fileType;
        this.fileData=fileData;
    }
}
