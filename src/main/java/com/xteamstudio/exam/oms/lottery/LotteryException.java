package com.xteamstudio.exam.oms.lottery;

public class LotteryException extends RuntimeException {

    static int error_3 = 3;
    static int error_2 = 2;
    static int error_1 = 1;

    private final int errCode;

    LotteryException(String s, int errCode) {
        super(s);
        this.errCode = errCode;
    }

    public int getErrCode() {
        return errCode;
    }
}
