package com.nari.monitorresources.entity;

import java.sql.Timestamp;

public class SecurityEventMsg {
	
	//事件级别

	private String LEVEL;
	
	//事件发生时间
	private String STIME;
	
	//业务系统类型
	private String SYSTYPE;
	
	//业务系统应用类型
	private String APPTYPE;
	
	//安全区
	private String SAFEAREA;
	
	//节点名称
	private String DEVNA;
	
	//节点IP
	private String DEVIP;
	
	//事件类型
	private String LOGTY;
	
	//事件子类型
	private String SLOGTY;
	
	//内容描述
	private String[] DESC;

	public String getLEVEL() {
		return LEVEL;
	}

	public void setLEVEL(String LEVEL) {
		this.LEVEL = LEVEL;
	}

	public String getSTIME() {
		return STIME;
	}

	public void setSTIME(String STIME) {
		this.STIME = STIME;
	}

	public String getSYSTYPE() {
		return SYSTYPE;
	}

	public void setSYSTYPE(String SYSTYPE) {
		this.SYSTYPE = SYSTYPE;
	}

	public String getAPPTYPE() {
		return APPTYPE;
	}

	public void setAPPTYPE(String APPTYPE) {
		this.APPTYPE = APPTYPE;
	}

	public String getSAFEAREA() {
		return SAFEAREA;
	}

	public void setSAFEAREA(String SAFEAREA) {
		this.SAFEAREA = SAFEAREA;
	}

	public String getDEVNA() {
		return DEVNA;
	}

	public void setDEVNA(String DEVNA) {
		this.DEVNA = DEVNA;
	}

	public String getDEVIP() {
		return DEVIP;
	}

	public void setDEVIP(String DEVIP) {
		this.DEVIP = DEVIP;
	}

	public String getLOGTY() {
		return LOGTY;
	}

	public void setLOGTY(String LOGTY) {
		this.LOGTY = LOGTY;
	}

	public String getSLOGTY() {
		return SLOGTY;
	}

	public void setSLOGTY(String SLOGTY) {
		this.SLOGTY = SLOGTY;
	}

	public String[] getDESC() {
		return DESC;
	}

	public void setDESC(String[] DESC) {
		this.DESC = DESC;
	}
}
