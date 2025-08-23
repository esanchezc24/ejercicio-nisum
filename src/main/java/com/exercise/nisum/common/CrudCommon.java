package com.exercise.nisum.common;

public interface CrudCommon<T, ID> {

    T save(T entity);

    T update(ID id, T entity);

    T findById(ID id);

    void delete(ID id);
}