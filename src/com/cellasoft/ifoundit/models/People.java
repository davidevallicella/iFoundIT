package com.cellasoft.ifoundit.models;

import com.pipl.api.data.Source;
import com.pipl.api.data.containers.Record;
import com.pipl.api.data.fields.*;
import com.pipl.api.thumbnail.ThumbnailAPI;

import java.io.Serializable;
import java.util.List;

public class People implements Serializable {

    private Source source;
    private List<Name> names;
    private List<Username> usernames;
    private List<Image> images;
    private List<Address> addresses;
    private List<DOB> dobs;
    private List<Phone> phones;
    private List<Job> jobs;

    public People(Record record) {
        this.source = record.getSource();
        this.names = record.getNames();
        this.usernames = record.getUsernames();
        this.images = record.getImages();
        this.addresses = record.getAddresses();
        this.dobs = record.getDobs();
        this.phones = record.getPhones();
        this.jobs = record.getJobs();
    }

    public String getFirst() {
        return (names == null || names.isEmpty()) ? "" : names.get(0).getFirst();
    }

    public String getMiddle() {
        return (names == null || names.isEmpty()) ? "" : names.get(0).getMiddle();
    }

    public String getLast() {
        return (names == null || names.isEmpty()) ? "" : names.get(0).getLast();
    }

    public String getFullName() {
        return (names == null || names.isEmpty()) ? "" : names.get(0).toString();
    }

    public String getUsername() {
        return (usernames == null || usernames.isEmpty()) ? "" : usernames.get(0).getContent();
    }

    public String getAddress() {
        return (addresses == null || addresses.isEmpty()) ? "" : addresses.get(0).toString();
    }

    public String getAge() {
        return (dobs == null || dobs.isEmpty()) ? "" : dobs.get(0).toString();
    }

    public String getPhone() {
        return (phones == null || phones.isEmpty()) ? "" : phones.get(0).getDisplayInternational();
    }


    public String getDomain() {
        return source.getDomain();
    }

    public String getUrl() {
        return source == null ? "" : source.getUrl();
    }

    public String getImage() {
        return (images == null || images.isEmpty()) ? "" : images.get(0).getUrl();
    }

    public String getJob() {
        return (jobs == null || jobs.isEmpty()) ? "" : jobs.get(0).display();
    }

    public String getThumbnail() {
        try {
            return new ThumbnailAPI.Generator()
                    .imageUrl(getImage())
                    .height(250)
                    .width(250)
                    .generate();
        } catch (Exception e) {
            return "";
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
  
        return super.toString();
    }
}
