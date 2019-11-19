package com.nari.monitorresources;

/**
 * 监视进程的接口
 */
public interface IMonitorProc {


    public  void moniProcCpuUsage();


    public  void moniProcMemoryUsage();


    public  void moniProcFileDescriptor();

    public void moniProcDiskIO();
}
