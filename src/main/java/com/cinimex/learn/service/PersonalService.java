package com.cinimex.learn.service;

import javax.annotation.Resource;
import javax.ejb.ConcurrencyManagement;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.jms.Queue;

import static javax.ejb.ConcurrencyManagementType.BEAN;

/**
 * Created by nXs on 18.03.2015.
 */
@Startup
@Singleton
@ConcurrencyManagement(BEAN)
public class PersonalService {
    

    public void registerRequest(String req) {



    }

    public void getPersonal() {

    }



}
