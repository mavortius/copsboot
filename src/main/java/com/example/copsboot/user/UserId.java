package com.example.copsboot.user;

import com.example.copsboot.orm.jpa.AbstractEntityId;

import java.util.UUID;

public class UserId extends AbstractEntityId<UUID> {

    protected UserId() {}

    public UserId(UUID id) {
        super(id);
    }
}
