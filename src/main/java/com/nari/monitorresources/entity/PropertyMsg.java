package com.nari.monitorresources.entity;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix="monitor.resources")
@PropertySource(value= {"classpath:param.properties"})
public class PropertyMsg {

    private  String ip;

    private int port;

    private String cronstr;


    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getCronstr() {
        return cronstr;
    }

    public void setCronstr(String cronstr) {
        this.cronstr = cronstr;
    }
}
