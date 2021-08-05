package com.liompei.xposeddemo;

public class StatementInfo {

    public static String getTraceInfo(){
        StringBuffer sb = new StringBuffer();

        StackTraceElement[] stacks = new Throwable().getStackTrace();
        sb.append("class: " ).append(stacks[1].getClassName())
                .append("; method: ").append(stacks[1].getMethodName())
                .append("; number: ").append(stacks[1].getLineNumber())
        .append(";");

        return sb.toString();
    }
}
