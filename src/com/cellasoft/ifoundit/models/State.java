package com.cellasoft.ifoundit.models;

/**
 * Created by netsysco on 29/07/2014.
 */
public class State {

    public String name;
    public String key;

    public State(String key, String name) {
        this.key = key.toLowerCase();
        this.name = name;
    }

    @Override
    public String toString() {
        return this.key.toUpperCase();
    }
}
