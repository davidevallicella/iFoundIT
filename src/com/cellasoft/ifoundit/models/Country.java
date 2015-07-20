package com.cellasoft.ifoundit.models;

/**
 * Created by netsysco on 26/07/2014.
 */
public class Country {

    public String name;
    public String key;

    public Country(String key, String name) {
        this.key = key.toLowerCase();
        this.name = name;
    }

    @Override
    public String toString() {
        return this.key.toUpperCase();
    }
}
