package com.abbasi.awaz;


public class acceptsreq {

String username;
    String status;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public acceptsreq(String username, String status) {
        this.username = username;
        this.status = status;
    }

    public acceptsreq() {

    }
}

