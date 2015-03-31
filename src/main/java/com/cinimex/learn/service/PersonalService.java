package com.cinimex.learn.service;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.*;
import javax.jms.Queue;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static javax.ejb.ConcurrencyManagementType.BEAN;

/**
 * Created by nXs on 18.03.2015.
 */
@Startup
@Singleton
@ConcurrencyManagement(BEAN)
public class PersonalService {

    @Resource(lookup = "java:/jms/queue/Chat")
    private Queue chatQueue;

    public void registerRequest(String req) {



    }

    public void getPersonal() {

    }



}
