package com.nari.monitorresources.entity;

public class FromServerIOMsg {

    private String readRate;
    private String writeRate;

    public String getReadRate() {
        return readRate;
    }

    public void setReadRate(String readRate) {
        this.readRate = readRate;
    }

    public String getWriteRate() {
        return writeRate;
    }

    public void setWriteRate(String writeRate) {
        this.writeRate = writeRate;
    }
}
