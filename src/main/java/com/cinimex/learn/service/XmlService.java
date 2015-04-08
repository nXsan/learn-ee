package com.cinimex.learn.service;

import com.cinimex.learn.handler.SchemaHandler;
import com.cinimex.learn.jaxb.MessageType;
import org.apache.commons.io.IOUtils;
import org.xml.sax.SAXException;

import javax.ejb.ConcurrencyManagement;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;

import static javax.ejb.ConcurrencyManagementType.BEAN;

/**
 * Service for working with xml ans xsd
 */
@Startup
@Singleton
@ConcurrencyManagement(BEAN)
public class XmlService {

    final static String encoding = "UTF-8";

    static private Schema schema;

    static private String lastMessage = "";

    public byte [] marshallObject(Object object) throws JAXBException {
        // create marshaller
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        JAXBContext context = JAXBContext.newInstance(MessageType.class);
        Marshaller marshaller = context.createMarshaller();

        // set an enconding if the encoding is specified
        if (encoding != null)
            marshaller.setProperty(Marshaller.JAXB_ENCODING, encoding);

        // set schema
        if (schema != null)
            marshaller.setSchema(schema);

        marshaller.setEventHandler(new SchemaHandler());

        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
        marshaller.marshal(object, os);
        return os.toByteArray();
    }

    public void unmarshallObject(String xmlString) throws JAXBException  {
        JAXBContext jc = JAXBContext.newInstance(MessageType.class);
        Unmarshaller unmarshaller = jc.createUnmarshaller();

        if (schema != null)
            unmarshaller.setSchema(schema);

        unmarshaller.setEventHandler(new SchemaHandler());
        unmarshaller.unmarshal(new StringReader(xmlString));
    }

    public String streamToString(InputStream io) throws IOException{
        StringWriter writer = new StringWriter();
        IOUtils.copy(io, writer, encoding);
        return writer.toString().replaceAll("[^\\x20-\\x7e]", "");
    }

    public static String getEncoding() {
        return encoding;
    }

    public static Schema getSchema() {
        return schema;
    }

    public static void setSchema(Schema schema) {
        XmlService.schema = schema;
    }

    public static void setSchema(String schemaPath) throws SAXException, MalformedURLException {
        SchemaFactory sf = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
        Schema schema = sf.newSchema(new URL(schemaPath));
//        Schema schema = sf.newSchema(new File(schemaPath));
        XmlService.schema = schema;
    }

    public static Boolean validateBySchema(InputStream xml) throws IOException, SAXException {
        try {
            Validator validator = schema.newValidator();
            StreamSource source = new StreamSource(xml);
            //        validator.setErrorHandler(new Error());
            validator.validate(source);
        } catch (IOException | SAXException e) {
            lastMessage = "Exception: "+e.getMessage();
            System.out.println("Exception: "+e.getMessage());
            return false;
        }
        return true;
    }

    public static String getLastMessage() {
        return lastMessage;
    }
}
