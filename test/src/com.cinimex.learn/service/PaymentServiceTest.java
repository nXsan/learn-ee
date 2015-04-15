package com.cinimex.learn.service;

import org.junit.Before;
import org.junit.Test;

import javax.naming.NamingException;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import static org.junit.Assert.*;

public class PaymentServiceTest {

    private final static String RESOURCE_PATH = "src/main/resources/";
    private final static String TEMP_FILE_NAME = "test.xml";
    private final static String CORRECT_PAYMENT_FILE = RESOURCE_PATH+ "payment.xml";
    private final static String INCORRECT_PAYMENT_FILE = RESOURCE_PATH+ "payment_wrong.xml";
    private final static String CORRECT_RECEIPT_FILE = RESOURCE_PATH+ "receiptnew.xml";
    private final static String INCORRECT_RECEIPT_FILE = RESOURCE_PATH+ "receiptnew_wrong.xml";

    private PaymentService paymentService;


    @Before
    public void setUpData() throws NamingException {
        paymentService = new PaymentService();
        paymentService.xmlService = new XmlService();
    }

    @Test
    public void testValidatePayment() throws Exception {
        InputStream inputStream;

        inputStream = new FileInputStream(CORRECT_PAYMENT_FILE);
        assertTrue(paymentService.validate(inputStream));

        inputStream = new FileInputStream(INCORRECT_PAYMENT_FILE);
        assertFalse(paymentService.validate(inputStream));

    }

    @Test
    public void testValidateReciept() throws Exception {
        InputStream inputStream;

        inputStream = new FileInputStream(CORRECT_RECEIPT_FILE);
        assertTrue(paymentService.validate(inputStream));

        inputStream = new FileInputStream(INCORRECT_RECEIPT_FILE);
        assertFalse(paymentService.validate(inputStream));

    }

    @Test
    public void testUpload() throws Exception {
        InputStream inputStream = new FileInputStream(CORRECT_PAYMENT_FILE);
        paymentService.upload(inputStream, TEMP_FILE_NAME);

        File file = new File (paymentService.BASE_PATH + TEMP_FILE_NAME);

        assertTrue(file.exists());
        assertEquals(TEMP_FILE_NAME, file.getName());
    }
}