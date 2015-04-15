package com.cinimex.learn.service;

import org.apache.log4j.Logger;
import org.xml.sax.SAXException;

import javax.ejb.ConcurrencyManagement;
import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import java.io.*;

import static javax.ejb.ConcurrencyManagementType.BEAN;

/**
 * Created by nXs on 06.04.2015.
 */
@Startup
@Singleton
@ConcurrencyManagement(BEAN)
public class PaymentService {

    private static final Logger log = Logger.getLogger(PaymentService.class);

    @EJB
    XmlService xmlService;

    private final static String LOCATION_SCHEMA = "http://localhost:8080/learn-ee-1.0/xsd/";
    private final static String SCHEMA_MESSAGE_NAME = "message.xsd";
    public final static String BASE_PATH = "C:" + File.separator + "temp" + File.separator;

    public Boolean validate(InputStream xmlFile) throws Exception {
        log.debug("Start validate xml");
        Boolean result = false;
        try {
            xmlService.setSchema(LOCATION_SCHEMA + SCHEMA_MESSAGE_NAME);
            result = xmlService.validateBySchema(xmlFile);
        }
        catch(SAXException e) {
            log.error("Validate error", e);
            result = false;
        }
        return result;
    }

    public void upload(InputStream inputStream, String fileName) throws IOException {
        log.debug("Start upload file");
        File outputFile = new File(BASE_PATH + fileName);

//        Creating file if not exist
        if(!outputFile.exists()) {
            outputFile.createNewFile();

       }

        OutputStream outputStream = new FileOutputStream(outputFile, false);
        try {
            byte[] buffer = new byte[inputStream.available()];
            int read = 0;
            String xml = xmlService.streamToString(inputStream);
            inputStream.read(buffer);

            while ((read = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, read);
            }
        }
        finally {
            if (inputStream != null) {
                inputStream.close();
            }
            if (outputStream != null) {
                outputStream.close();
            }
        }
    }






}
