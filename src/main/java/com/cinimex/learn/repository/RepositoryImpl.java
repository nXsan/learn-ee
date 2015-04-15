package com.cinimex.learn.repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.lang.reflect.ParameterizedType;
import java.util.List;

/**
 * Created by nXs on 09.04.2015.
 */
public class RepositoryImpl<T> implements Repository<Object> {

    protected Class entityClass;

    @PersistenceContext
    protected EntityManager entityManager;

    public RepositoryImpl() {
        ParameterizedType genericSuperclass = (ParameterizedType) getClass().getGenericSuperclass();
        this.entityClass = (Class) genericSuperclass.getActualTypeArguments()[1];
    }

    @Override
    public T findById(Long id) {
        return null;
//        return entityManager.find(entityClass, id);
    }

    @Override
    public List findAll() {
        return null;
    }

    @Override
    public void remove(Object e) {

    }

    @Override
    public void add(Object e) {

    }
}
