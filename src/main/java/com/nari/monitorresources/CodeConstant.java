package com.nari.monitorresources;

public interface CodeConstant {

    interface CODE_SLOGTY{
        String ONE = "1";
        String TWO = "2";
        String THREE = "3";
        String FOUR = "4";
    }

    interface CODE_OTHERS{
        String LEVEL = "5";
        String SYSTYPE = "D5000";
        String LOGTY = "5";
        String PERCENT = "%";
    }

    interface CODE_DOMAIN{
        String ZERO = "0";
        String ONE = "1";
        String TWO = "2";
    }

    interface CODE_SECTION{
        String ONE = "1";
        String TWO = "2";
        String THREE = "3";
    }

    interface  CODE_TIME_FORMAT{
        String ONE = "yyyy-MM-dd HH:mm:ss";
    }

    interface CODE_STATUS {
        String  STATUS = "Online";
    }

    interface CODE_CRITICAL {
        String GENERAL = "General";
        String CRUCIAL = "Crucial";
        String CRUCIAL_SPECIAL = "Crucial*";
    }
}
