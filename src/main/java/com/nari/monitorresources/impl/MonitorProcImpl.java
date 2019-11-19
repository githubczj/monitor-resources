package com.nari.monitorresources.impl;

import com.google.gson.Gson;
import com.nari.monitorresources.CodeConstant;
import com.nari.monitorresources.IMonitorProc;
import com.nari.monitorresources.entity.*;
import com.nari.monitorresources.utils.ConfigMangerUtil;
import com.nari.monitorresources.utils.GetDProcMsgUtils;
import com.nari.monitorresources.utils.LinuxCmdUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 定时任务处理的类
 */
@Component
public class MonitorProcImpl implements IMonitorProc {


    private static int port = 514;//定义端口

    private static String IP = "127.0.0.1";//定义IP地址

    private String hostAddrss = null;

    private String hostName = null;

    private String section = null;



    private final static Logger logger = LoggerFactory.getLogger(MonitorProcImpl.class);

    public MonitorProcImpl() throws UnknownHostException {

        InetAddress intMst = InetAddress.getLocalHost();
        section = ConfigMangerUtil.getSection();
        hostAddrss = intMst.getHostAddress();
        hostName = intMst.getHostName();
    }


    /**
     * 监视进程cpu 利用率
     */

    @Override
    public void moniProcCpuUsage() {

        logger.info("moniProcCpuUsage start>>>>>>");
        List<FromServerMsg> results = GetDProcMsgUtils.getFromServerMsg();
        if (null != results) {
            Gson gson = new Gson();
            logger.debug("moniProcCpuUsage s4j results>>>>>" +  gson.toJson(results));
        }

        //组装监视进程信息
        List<SecurityEventMsg> sendMsgs = assembleCPUSecurityEventMsg(results);

        if (null != sendMsgs) {
            Gson gson = new Gson();
            logger.debug("moniProcCpuUsage s4j sendMsgs>>>>>" +  gson.toJson(sendMsgs));
        }
        //发送传输信息
        dataSend(sendMsgs);
        logger.info("moniProcCpuUsage end >>>>>>");

    }


    /**
     * 监视进程内存利用率
     */

    @Override
    public void moniProcMemoryUsage() {

        logger.info("moniProcMemoryUsage start >>>>>>");
        List<FromServerMsg> results = GetDProcMsgUtils.getFromServerMsg();
        if (null != results) {
            Gson gson = new Gson();
            logger.debug("moniProcMemoryUsage s4j results>>>>>" +  gson.toJson(results));
        }

        //组装传输信息
        List<SecurityEventMsg> sendMsgs = assembleMEMSecurityEventMsg(results);
        if (null != sendMsgs) {
            Gson gson = new Gson();
            logger.debug("moniProcMemoryUsage s4j sendMsgs>>>>>" +  gson.toJson(sendMsgs));
        }
        //发送传输信息
        dataSend(sendMsgs);
        logger.info("moniProcMemoryUsage end >>>>>>");

    }

    /**
     * 监视进程文件描述符
     */

    @Override
    public void moniProcFileDescriptor() {

        logger.info("moniProcFileDescriptor start >>>>>>");
        List<FromServerMsg> results = GetDProcMsgUtils.getFromServerMsg();
        if (null != results) {
            Gson gson = new Gson();
            logger.debug("moniProcFileDescriptor s4j results>>>>>" +  gson.toJson(results));
        }

        //组装传输信息
        List<SecurityEventMsg> sendMsgs = assemProcFileDescriptorMsg(results);

        if (null != sendMsgs) {
            Gson gson = new Gson();
            logger.debug("moniProcFileDescriptor s4j sendMsgs>>>>>" +  gson.toJson(sendMsgs));
        }

        //发送传输信息
        dataSend(sendMsgs);
        logger.info("moniProcFileDescriptor end >>>>>>");

    }


    /**
     * 监视进程磁盘io
     */

    @Override
    public void moniProcDiskIO() {

        logger.info("moniProcDiskIO start >>>>>>");
        List<FromServerMsg> results = GetDProcMsgUtils.getFromServerMsg();
        if (null != results) {
            Gson gson = new Gson();
            logger.debug("moniProcDiskIO s4j results>>>>>" +  gson.toJson(results));
        }

        //组装传输信息
        List<SecurityEventMsg> sendMsgs = assemProcDiskIODescriptorMsg(results);

        if (null != sendMsgs) {
            Gson gson = new Gson();
            logger.debug("moniProcDiskIO s4j sendMsgs>>>>>" +  gson.toJson(sendMsgs));
        }

        //发送传输信息
        dataSend(sendMsgs);
        logger.info("moniProcDiskIO end >>>>>>");

    }


    /**
     * 发送消息
     *
     * @param sendMsgs
     */
    private void dataSend(List<SecurityEventMsg> sendMsgs) {

            logger.info("dataSend start>>>>>>");
        try {
            //这一块逻辑还得完善2019/11/18
            DatagramSocket socket = new DatagramSocket();
            Gson gson = new Gson();
            String resultjson = gson.toJson(sendMsgs);
            logger.debug("发送传输信息>>>>>>" +  resultjson);
            byte[] buf = resultjson.getBytes();
            DatagramPacket packet = new DatagramPacket(buf, buf.length, InetAddress.getByName(IP), port);
            socket.send(packet);
            socket.close();
            logger.info("dataSend end>>>>>>");
        } catch (SocketException e) {
            e.printStackTrace();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 组装cpu的信息
     * @param results
     * @return
     */
    private List<SecurityEventMsg> assembleCPUSecurityEventMsg(List<FromServerMsg> results) {

        logger.info("assembleCPUSecurityEventMsg start>>>>>>");
        List<SecurityEventMsg> sendMsgs = new ArrayList<>();

        for (FromServerMsg fromServerMsg : results) {

            SecurityEventMsg securityEventMsg = new SecurityEventMsg();
            comInfoAssem(fromServerMsg, securityEventMsg);
            securityEventMsg.setSLOGTY(CodeConstant.CODE_SLOGTY.ONE);
            String[] desc = new String[2];
            desc[0] = fromServerMsg.getProcName();
            desc[1] = fromServerMsg.getCpu() + CodeConstant.CODE_OTHERS.PERCENT;
            securityEventMsg.setDESC(desc);
            sendMsgs.add(securityEventMsg);
            logger.info("assembleCPUSecurityEventMsg edn>>>>>>");

        }

        return sendMsgs;
    }


    /**
     * 组装内存的信息
     * @param results
     * @return
     */
    private List<SecurityEventMsg> assembleMEMSecurityEventMsg(List<FromServerMsg> results) {

        logger.info("assembleMEMSecurityEventMsg start>>>>>>");
        List<SecurityEventMsg> sendMsgs = new ArrayList<>();

        for (FromServerMsg fromServerMsg : results) {

            SecurityEventMsg securityEventMsg = new SecurityEventMsg();
            comInfoAssem(fromServerMsg, securityEventMsg);
            securityEventMsg.setSLOGTY(CodeConstant.CODE_SLOGTY.TWO);
            String[] desc = new String[2];
            desc[0] = fromServerMsg.getProcName();
            desc[1] = fromServerMsg.getMem() + CodeConstant.CODE_OTHERS.PERCENT;
            securityEventMsg.setDESC(desc);
            sendMsgs.add(securityEventMsg);
            logger.info("assembleMEMSecurityEventMsg end>>>>>>");
        }

        return sendMsgs;
    }

    /**
     * 组装文件描述符信息
     * @param results
     * @return
     */
    private List<SecurityEventMsg> assemProcFileDescriptorMsg(List<FromServerMsg> results) {

        logger.info("assemProcFileDescriptorMsg start>>>>>>");
        List<SecurityEventMsg> sendMsgs = new ArrayList<>();
        for (FromServerMsg fromServerMsg : results) {

            SecurityEventMsg securityEventMsg = new SecurityEventMsg();
            comInfoAssem(fromServerMsg, securityEventMsg);
            securityEventMsg.setSLOGTY(CodeConstant.CODE_SLOGTY.THREE);
            String fileDsc = LinuxCmdUtils.getCmdReturn("lsof -p " + fromServerMsg.getPid() + " | wc -l");
            String[] desc = new String[2];
            desc[0] = fromServerMsg.getProcName();
            desc[1] = fileDsc;
            securityEventMsg.setDESC(desc);
            sendMsgs.add(securityEventMsg);
            logger.info("assemProcFileDescriptorMsg end>>>>>>");
        }

        return sendMsgs;
    }

    /**
     * 组装进程io信息
     * @param results
     * @return
     */
    private List<SecurityEventMsg> assemProcDiskIODescriptorMsg(List<FromServerMsg> results) {

        logger.info("assemProcDiskIODescriptorMsg start>>>>>>");
        List<SecurityEventMsg> sendMsgs = new ArrayList<>();

        //组装传输信息
        for (FromServerMsg fromServerMsg : results) {

            SecurityEventMsg securityEventMsg = new SecurityEventMsg();
            comInfoAssem(fromServerMsg, securityEventMsg);
            securityEventMsg.setSLOGTY(CodeConstant.CODE_SLOGTY.FOUR);
            FromServerIOMsg fromServerIOMsg = LinuxCmdUtils.getCmdIOReturn("pidstat -d -p " + fromServerMsg.getPid());
            String[] desc = new String[3];
            desc[0] = fromServerMsg.getProcName();
            desc[1] = fromServerIOMsg.getReadRate() + CodeConstant.CODE_OTHERS.PERCENT;
            desc[2] = fromServerIOMsg.getWriteRate() + CodeConstant.CODE_OTHERS.PERCENT;
            securityEventMsg.setDESC(desc);
            sendMsgs.add(securityEventMsg);
            logger.info("assemProcDiskIODescriptorMsg end>>>>>>");

        }

        return sendMsgs;
    }

    /**
     * 组装共同的信息
     * @param fromServerMsg
     * @param securityEventMsg
     * @return
     */
    private SecurityEventMsg comInfoAssem(FromServerMsg fromServerMsg, SecurityEventMsg securityEventMsg) {

        logger.info("comInfoAssem start>>>>>>");
        securityEventMsg.setLEVEL(CodeConstant.CODE_OTHERS.LEVEL);
        SimpleDateFormat sdf = new SimpleDateFormat(CodeConstant.CODE_TIME_FORMAT.ONE);
        Date date = new Date();
        String time = sdf.format(date);
        securityEventMsg.setSTIME(time);
        securityEventMsg.setSYSTYPE(CodeConstant.CODE_OTHERS.SYSTYPE);
        securityEventMsg.setAPPTYPE(fromServerMsg.getAppName());
        securityEventMsg.setSAFEAREA(section);//modify 2019/11/18
        securityEventMsg.setDEVIP(hostAddrss);
        securityEventMsg.setDEVNA(hostName);
        securityEventMsg.setLOGTY(CodeConstant.CODE_OTHERS.LOGTY);
        logger.info("comInfoAssem end>>>>>>");
        return securityEventMsg;
    }
}
