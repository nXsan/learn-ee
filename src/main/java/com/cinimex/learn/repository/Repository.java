package com.cinimex.learn.repository;

import java.util.List;

/**
 * Created by nXs on 09.04.2015.
 */
public interface Repository<T> {

    T findById(Long id);
    List findAll();
    void remove(T e);
    void add(T e);


}