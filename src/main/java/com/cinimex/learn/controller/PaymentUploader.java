package com.cinimex.learn.controller;

import com.cinimex.learn.service.PaymentService;
import com.cinimex.learn.service.XmlService;
import org.apache.log4j.Logger;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.servlet.http.Part;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;

/**
 * Bean for working with 'file_load.jsf'
 */
@ManagedBean
@SessionScoped
public class PaymentUploader implements Serializable  {

    private static final Logger log = Logger.getLogger(PaymentUploader.class);

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
                inputStream = file.getInputStream();
                paymentService.upload(inputStream, fileName);
                statusMessage = "File upload successfully!!!";
            }
            else {
                statusMessage = "File not validate! " + xmlService.getLastMessage() ;
                log.warn("XML file not validate");
            }
        }
        catch (IOException e) {
            log.error("Upload file failed", e);
            statusMessage = "File upload failed!!!";
        }
        finally {
            log.debug("End upload");
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
        log.debug("***** partHeader: " + partHeader);

        for (String content : part.getHeader("content-disposition").split(";")) {
            if (content.trim().startsWith("filename")) {
                return content.substring(content.indexOf('=') + 1).trim()
                        .replace("\"", "");
            }
        }
        return null;
    }

}
