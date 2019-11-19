package com.nari.monitorresources.entity;

/**
 * 执行seeproc  all 放回的字段
 */
public class FromSeeProcMsg {

    private String appName; //态

    private String procName; //进程名

    private String status; //状态

    private String procId; //进程id

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getProcName() {
        return procName;
    }

    public void setProcName(String procName) {
        this.procName = procName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getProcId() {
        return procId;
    }

    public void setProcId(String procId) {
        this.procId = procId;
    }
}
