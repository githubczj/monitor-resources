package com.nari.monitorresources.task;

import com.nari.monitorresources.impl.MonitorProcImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.UnknownHostException;
import java.util.Properties;

@Component
public class TaskSchedule implements SchedulingConfigurer {

    private final static Logger logger = LoggerFactory.getLogger(TaskSchedule.class);


    private String cron = "* 0/1 * * * ?"; //调用set方法可动态设置时间规则   0 */1 * * * ?

    public String getCron() {
        return cron;
    }

    public void setCron(String cron) {
        this.cron = cron;
    }

    @Override
    public void configureTasks(ScheduledTaskRegistrar scheduledTaskRegistrar) {

        //String cronstr = ConfigMangerUtil.getInstance().getString("monitor.resources.cronstr");
        String cronstr = null;
        Properties properties = new Properties();
        //读外部的配置文件实现了
        File file = new File("param.properties");
        FileInputStream fis = null;
        try {
             fis = new FileInputStream(file);
             properties.load(fis);
             cronstr = properties.getProperty("monitor.resources.cronstr");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                if (fis != null) {
                    fis.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        logger.info("TaskSchedule cronstr>>>>" + cronstr);
        if (null != cronstr) {
            cron = cronstr;
        }
        scheduledTaskRegistrar.addCronTask(new Runnable() {

            @Override
            public void run() {
                logger.info(scheduledTaskRegistrar+" --- > 开始");
                //业务逻辑
                try {
                    new MonitorProcImpl().moniProcCpuUsage();
                    new MonitorProcImpl().moniProcMemoryUsage();
                    new MonitorProcImpl().moniProcFileDescriptor();
                    new MonitorProcImpl().moniProcDiskIO();
                } catch (UnknownHostException e) {
                    e.printStackTrace();
                }
                logger.info(scheduledTaskRegistrar+" --- > 结束");
            }
        }, cron);  //加入时间
    }
}

