package com.example.copsboot.orm.jpa;

public interface UniqueIdGenerator<T> {

    T getNextUniqueId();
}
