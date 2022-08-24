package com.bank.document.project.controller;

import com.bank.document.project.model.DocumentModel;
import com.bank.document.project.repository.DocumentRepository;
import com.bank.document.project.service.DocumentService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMultipartHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.util.Assert;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class DocumentControllerTest {
    @InjectMocks
    DocumentController documentController;
    @MockBean
    private DocumentService documentService;
    @Autowired
    MockMvc mockMvc;
    @MockBean
    private DocumentModel documentModel;
    @MockBean
    private DocumentRepository documentRepository;

    @Test
    public void test_fileUpload() throws Exception{
        String fileName="TestPDFFile.pdf";
        MockMultipartFile sampleFile = new MockMultipartFile(
                "file",
                fileName,
                "application/pdf",
                "This is a test PDF file".getBytes());
        ;
        Mockito.when(documentService.createDocument(sampleFile)).thenReturn(documentModel);

        MvcResult result= mockMvc.perform(MockMvcRequestBuilders.
                multipart("/documents/uploadDocument").file(sampleFile)
        ).andExpect(status().isOk()).andReturn();
        Assert.state((result.getResponse().getStatus()==200),"test");
    }

    @Test
    public void test_FileUpload_NoFileProvided() throws Exception {
        MockMultipartHttpServletRequestBuilder multipartRequest =
                MockMvcRequestBuilders.multipart("/documents/uploadDocument");

        mockMvc.perform(multipartRequest)
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testCheckDocument() throws Exception {
        String uri = "/documents/checkDocumentId/448df40a-7ca6-4baf-abe2-5a394d441c58";
        Mockito.when(documentService.
                getDocument("448df40a-7ca6-4baf-abe2-5a394d441c58")).thenReturn(documentModel);
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.
                get(uri)).andReturn();
        Assert.state((result.getResponse().getStatus()==200),"test");
    }

    @Test
    public void testCheckDocumentNoId() throws Exception {
        String uri = "/documents/checkDocumentId/";
        Mockito.when(documentService.
                getDocument("448df40a-7ca6-4baf-abe2-5a394d441c58")).thenReturn(documentModel);
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.
                get(uri)).andReturn();
        Assert.state((result.getResponse().getStatus()==404),"test");
    }

    @Test
    public void deleteDocument() throws Exception {
        String uri = "/documents/deleteDocument/448df40a-7ca6-4baf-abe2-5a394d441c58";
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.delete(uri)).andReturn();
        Assert.state((mvcResult.getResponse().getStatus()==200),"test");
    }

}

