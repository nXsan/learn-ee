package com.cinimex.learn.controller;

import com.cinimex.learn.service.PaymentService;
import com.cinimex.learn.service.XmlService;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.servlet.http.Part;
import java.io.*;

/**
 * Bean for working with 'file_load.jsf'
 */
@ManagedBean
@SessionScoped
public class PaymentUploader implements Serializable  {

    private static final long serialVersionUID = 9040359120893077422L;

    private Part file;
    private String statusMessage;

    @EJB
    private PaymentService paymentService;

    @EJB
    private XmlService xmlService;

    public void upload() throws Exception{

        String fileName = getFileName(file);
        InputStream inputStream = file.getInputStream();

        try {
            if (paymentService.validate(inputStream)) {
                paymentService.upload(inputStream, fileName);
                statusMessage = "File upload successfull!!!";
            }
            else {
                statusMessage = "File not validate! " + xmlService.getLastMessage() ;
            }
        }
        catch (IOException e) {
            e.printStackTrace();
            statusMessage = "File upload failed!!!";
        }
        finally {
            if (inputStream != null) {
                inputStream.close();
            }
        }
    }

    public Part getFile() {
        return file;
    }

    public void setFile(Part file) {
        this.file = file;
    }

    public String getStatusMessage() {
        return statusMessage;
    }

    public void setStatusMessage(String statusMessage) {
        this.statusMessage = statusMessage;
     }

    private String getFileName(Part part) {
        final String partHeader = part.getHeader("content-disposition");
        System.out.println("***** partHeader: " + partHeader);
        for (String content : part.getHeader("content-disposition").split(";")) {
            if (content.trim().startsWith("filename")) {
                return content.substring(content.indexOf('=') + 1).trim()
                        .replace("\"", "");
            }
        }
        return null;
    }

}
