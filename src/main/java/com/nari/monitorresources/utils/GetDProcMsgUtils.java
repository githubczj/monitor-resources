package com.nari.monitorresources.utils;


import com.google.gson.Gson;
import com.nari.monitorresources.CodeConstant;
import com.nari.monitorresources.entity.FromSeeProcMsg;
import com.nari.monitorresources.entity.FromServerMsg;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * 获取d5000 下面的进程信息
 *
 */
public class GetDProcMsgUtils {


    private final static Logger logger = LoggerFactory.getLogger(GetDProcMsgUtils.class);

    public static List<FromServerMsg> getFromServerMsg() {

        List<FromServerMsg> fromServerMsgs = new ArrayList<>();

        //大体捞一遍
        List<FromServerMsg> resultstmp = LinuxCmdUtils.getGrepCmdReturn("ps -aux | grep d5000");

        if (null != resultstmp) {
            Gson gson = new Gson();
            logger.debug("GetDProcMsgUtils getFromServerMsg ps -aux | grep d5000>>>>>" + gson.toJson(resultstmp));
        }


        //seeproc d5000系统  d5000用户下才行
        List<FromSeeProcMsg> fromSeeProcResults = LinuxCmdUtils.getSeeprocCmdReturn("seeproc all");

        if (null != fromSeeProcResults) {
            Gson gson = new Gson();
            logger.debug("GetDProcMsgUtils getFromServerMsg seeproc all>>>>>" + gson.toJson(fromSeeProcResults));
        }

        //获取 ps -aux | grep  与 seeproc all 匹配后的数据
        for (FromServerMsg fromServerMsg : resultstmp) {

            for (FromSeeProcMsg fromSeeProcMsg : fromSeeProcResults) {
                // ==  equal 的用法
                if (fromServerMsg.getPid().equals(fromSeeProcMsg.getProcId()) && CodeConstant.CODE_STATUS.STATUS.equals(fromSeeProcMsg.getStatus())) {
                    fromServerMsg.setAppName(fromSeeProcMsg.getAppName());
                    fromServerMsg.setProcName(fromSeeProcMsg.getProcName());
                    fromServerMsgs.add(fromServerMsg);
                }
            }

        }
        return fromServerMsgs;
    }
}