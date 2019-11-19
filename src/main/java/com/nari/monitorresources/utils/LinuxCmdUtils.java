package com.nari.monitorresources.utils;

import com.nari.monitorresources.CodeConstant;
import com.nari.monitorresources.entity.FromSeeProcMsg;
import com.nari.monitorresources.entity.FromServerIOMsg;
import com.nari.monitorresources.entity.FromServerMsg;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;


public class LinuxCmdUtils {

    /**
     * 日志句柄
     */
    //private static Logger logger = Logger.getLogger(LinuxCmdUtils.class);

    private final static Logger logger = LoggerFactory.getLogger(LinuxCmdUtils.class);

    public static boolean executeLinuxCmd(String cmd) {

        boolean result = false;

        System.out.println("got cmd : " + cmd);
        Runtime run = Runtime.getRuntime();
        //InputStream in=null;
        try {
            System.out.println("===================start");
            Process process = run.exec(cmd);
            //执行结果 0 表示正常退出
            int exeResult = process.waitFor();
            System.out.println("===================start" + exeResult);
            if (exeResult == 0 || exeResult == 2) {
                System.out.println("执行成功");
                result = true;
            }

        } catch (Exception e) {
            logger.error("LinuxCmdUtils.executeLinuxCmd error",e);
        }
        return result;
    }

    /**
     * 获取linux命令执行的结果,cat 之类
     *
     * @param cmd
     * @return
     */
    public static String getCmdResult(String cmd) {

        String result = "";
        try {

            Process process = Runtime.getRuntime().exec(cmd);
            InputStreamReader ir = new InputStreamReader(process.getInputStream());
            LineNumberReader input = new LineNumberReader(ir);
            String line;
            while ((line = input.readLine()) != null) {
                result = line;
            }

        } catch (IOException e) {

            logger.error("IOException " + e.getMessage());

        }
        return result;
    }

    /**
     * grep 类的shell命令
     *
     * @param cmdStr
     * @return List<FromServerMsg> 即 ps -aux | d5000 的进程信息
     */
    public static List<FromServerMsg> getGrepCmdReturn(String cmdStr) {

        String[] cmd = new String[3];
        cmd[0] = "/bin/sh";
        cmd[1] = "-c";
        cmd[2] = cmdStr;

        //得到Java进程的相关Runtime运行对象
        Runtime runtime = Runtime.getRuntime();
        List<FromServerMsg> fsms = null;
        try {
            Process process = runtime.exec(cmd);
            BufferedReader bufferReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String temp = null;
            fsms = new ArrayList<>();
            while ((temp = bufferReader.readLine()) != null) {
                temp = temp.replaceAll(" {2,}", " ");
                logger.info("List<FromServerMsg getGrepCmdReturn>>>>" + temp);
                String tempArray[] = temp.trim().split(" ");
                logger.info("List<FromServerMsg getGrepCmdReturn>>>>" + tempArray.length);
                FromServerMsg fsm = new FromServerMsg();
                fsm.setUser(tempArray[0]);
                fsm.setPid(tempArray[1]);
                fsm.setCpu(tempArray[2]);
                fsm.setMem(tempArray[3]);
                fsm.setVsz(tempArray[4]);
                fsm.setRss(tempArray[5]);
                fsm.setTty(tempArray[6]);
                fsm.setStat(tempArray[7]);
                fsm.setStart(tempArray[8]);
                fsm.setTime(tempArray[9]);
                fsm.setCommand(null); //进程名这一块不做处理  放在 后面seeproc 里处理
                fsms.add(fsm);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return fsms;
    }

    /**
     * 获取d5000下面的进程信息
     *
     * @param cmdStr
     * @return List<FromSeeProcMsg>  及 seeproc 返回的结果
     */
    public static List<FromSeeProcMsg> getSeeprocCmdReturn(String cmdStr) {

        logger.info("List<FromSeeProcMsg> getSeeprocCmdReturn>>>>start");
        String[] cmd = new String[3];
        cmd[0] = "/bin/sh";
        cmd[1] = "-c";
        cmd[2] = cmdStr;

        //得到Java进程的相关Runtime运行对象
        Runtime runtime = Runtime.getRuntime();
        List<FromSeeProcMsg> fspms = null;
        try {
            Process process = runtime.exec(cmd);
            BufferedReader bufferReader = new BufferedReader(
                    new InputStreamReader(process.getInputStream()));
            String temp = null;
            fspms = new ArrayList<>();
            //int flag = 0;
            ////这一块逻辑的优化
            while ((temp = bufferReader.readLine()) != null) {

                temp = temp.replaceAll(" {2,}", " ");
                logger.info("List<FromSeeProcMsg> getSeeprocCmdReturn>>>>" + temp);
                String tempArray[] = temp.trim().split(" ");
                logger.info("List<FromSeeProcMsg> getSeeprocCmdReturn length>>>>" + tempArray.length);
                if (tempArray.length > 1 && isInteger(tempArray[0])) {

                    FromSeeProcMsg fspm = new FromSeeProcMsg();

                    //modify 2019/11/15  防止数组越界 && 进程名中出现下面的关键字
                    if (tempArray.length > 4) {

                        String stat = null;
                        String pId = null;
                        for (int i = 0; i < tempArray.length; i++) {

                            //这几种状态还有待确认
                            if (tempArray[i].equals(CodeConstant.CODE_CRITICAL.GENERAL)
                                    || tempArray[i].equals(CodeConstant.CODE_CRITICAL.CRUCIAL_SPECIAL)
                                    || tempArray[i].equals(CodeConstant.CODE_CRITICAL.CRUCIAL)) {
                                stat = tempArray[i-1];
                                pId = tempArray[i+1];
                            }
                        }
                        fspm.setProcId(pId);
                        fspm.setStatus(stat);
                        fspm.setAppName(tempArray[2]);
                        fspm.setProcName(tempArray[3]);
                    }

                    fspms.add(fspm);

                    logger.info("List<FromSeeProcMsg> getSeeprocCmdReturn>>>>end");
                }
            }

        } catch (IOException e) {

            e.printStackTrace();
        }

        return fspms;
    }


    /**
     * grep 类的shell命令
     *
     * @param cmdStr
     * @return
     */
    public static String getCmdReturn(String cmdStr) {

        String[] cmd = new String[3];
        //String[] res=null;
        cmd[0] = "/bin/sh";
        cmd[1] = "-c";
        cmd[2] = cmdStr;

        //得到Java进程的相关Runtime运行对象
        Runtime runtime = Runtime.getRuntime();
        StringBuffer stringBuffer = null;

        try {
            Process process = runtime.exec(cmd);

            BufferedReader bufferReader = new BufferedReader(
                    new InputStreamReader(process.getInputStream()));

            stringBuffer = new StringBuffer();

            String temp = null;
            while ((temp = bufferReader.readLine()) != null) {
                stringBuffer.append(temp);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return stringBuffer.toString();

    }

    /**
     *
     * @param cmdStr
     * @return
     */
    public static FromServerIOMsg getCmdIOReturn(String cmdStr) {

        String[] cmd = new String[3];
        cmd[0] = "/bin/sh";
        cmd[1] = "-c";
        cmd[2] = cmdStr;

        //得到Java进程的相关Runtime运行对象
        Runtime runtime = Runtime.getRuntime();
        FromServerIOMsg fromServerIOMsg = null;

        try {
            Process process = runtime.exec(cmd);
            BufferedReader bufferReader = new BufferedReader(
                    new InputStreamReader(process.getInputStream()));

            String temp = null;
            int flag = 0;
            fromServerIOMsg = new FromServerIOMsg();
            while ((temp = bufferReader.readLine()) != null) {
                flag++;
                if (flag > 2) {
                    temp = temp.replaceAll(" {2,}", " ");
                    String arr[] = temp.trim().split(" ");
                    logger.debug("FromServerIOMsg getCmdIOReturn>>>>" + arr.length);
                    for (int i = 0; i < arr.length; i++) {
                        logger.debug("FromServerIOMsg getCmdIOReturn>>>>" + arr[i]);
                    }
                    fromServerIOMsg.setReadRate(arr[3]);
                    fromServerIOMsg.setWriteRate(arr[4]);
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return fromServerIOMsg;

    }


    /**
     * 执行长的命令
     *
     * @param cmdStr
     * @return boolean  true false
     */
    public static boolean getGrepCmd(String cmdStr) {
        System.out.println("getGrepCmd cmdStr is......." + cmdStr);
        String[] cmd = new String[3];
        boolean result = false;

        cmd[0] = "/bin/sh";
        cmd[1] = "-c";
        cmd[2] = cmdStr;

        //得到Java进程的相关Runtime运行对象
        Runtime runtime = Runtime.getRuntime();
        try {
            Process process = runtime.exec(cmd);
            int exeResult = process.waitFor();
            System.out.println("===================start" + exeResult);
            if (exeResult == 0) {
                System.out.println("执行成功");
                result = true;
            }
        } catch (Exception e) {
            // logger.error("LinuxCmdUtils.getGrepCmd error",e);
        }
        return result;
    }

    /**
     * 判断有效数据的起始位置
     * @param str
     * @return
     */
    public static boolean isInteger(String str) {

        Pattern pattern = Pattern.compile("[0-9]*");
        return pattern.matcher(str).matches();
    }

}
