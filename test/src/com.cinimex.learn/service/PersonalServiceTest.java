package com.cinimex.learn.service;

import com.cinimex.learn.model.AccountEntity;
import com.cinimex.learn.repository.AccountRepositoryImpl;
import org.junit.Test;

import static org.junit.Assert.*;

public class PersonalServiceTest {

    @Test
    public void testRegisterRequest() throws Exception {

    }

    @Test
    public void testGetPersonal() throws Exception {
        AccountRepositoryImpl acc = new AccountRepositoryImpl();
        AccountEntity ae = new AccountEntity();
        ae.setName("FF1");
        acc.save(ae);
    }


}