package com.nari.monitorresources.enums;

import com.nari.monitorresources.ErrorCode;

public enum ErrorCodeEnum implements ErrorCode {

    UN_PATH("500", "没有获取到路径"),
    ERROR_USER("501", "执行的用户的不正确"),
    UNSPECIFIED("502", "未知错误"),
    FAIL_EXECUTORCMD("503","linux命令没有执行成功");


    private final String code;

    private final String description;


    private ErrorCodeEnum(String code, String description) {
        this.code = code;
        this.description =  description;
    }

    /**
     * 根据编码获取枚举值
     * @param code
     * @return
     */
    public static ErrorCodeEnum getByCode(String code) {
        for (ErrorCodeEnum value : ErrorCodeEnum.values()) {
            if (code.equals(value.getCode())) {
                return value;
            }
        }
        return UNSPECIFIED;
    }

    /**
     * 枚举包含此code
     * @param code
     * @return
     */
    public static Boolean contains(String code){
        for (ErrorCodeEnum value : ErrorCodeEnum.values()) {
            if (code.equals(value.getCode())) {
                return true;
            }
        }
        return  false;
    }

    @Override
    public String getCode() {
        return null;
    }

    @Override
    public String getDescription() {
        return null;
    }

}
