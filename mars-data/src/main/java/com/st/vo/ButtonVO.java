package com.st.vo;

/**
 * Created by zhaoyx on 2016/10/26.
 */
public class ButtonVO {
    private String buttonName;
    private int clickTimes;
    private int clickNumber;

    public ButtonVO(String buttonName, int clickTimes, int clickNumber) {
        this.buttonName = buttonName;
        this.clickTimes = clickTimes;
        this.clickNumber = clickNumber;
    }
    public ButtonVO(){}

    public String getButtonName() {
        return buttonName;
    }

    public void setButtonName(String buttonName) {
        this.buttonName = buttonName;
    }

    public int getClickTimes() {
        return clickTimes;
    }

    public void setClickTimes(int clickTimes) {
        this.clickTimes = clickTimes;
    }

    public int getClickNumber() {
        return clickNumber;
    }

    public void setClickNumber(int clickNumber) {
        this.clickNumber = clickNumber;
    }
}
