package com.st.vo;

/**
 * Created by zhaoyx on 2016/10/26.
 */
public class UserAnalysisVO{
    private String originator;
    private String nickName;
    private int levelOne;
    private int levelTwo;

    public UserAnalysisVO(String originator, String nickName, int levelOne, int levelTwo) {
        this.originator = originator;
        this.nickName = nickName;
        this.levelOne = levelOne;
        this.levelTwo = levelTwo;
    }

    public UserAnalysisVO() {
    }

    public String getOriginator() {
        return originator;
    }

    public void setOriginator(String originator) {
        this.originator = originator;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public int getLevelOne() {
        return levelOne;
    }

    public void setLevelOne(int levelOne) {
        this.levelOne = levelOne;
    }

    public int getLevelTwo() {
        return levelTwo;
    }

    public void setLevelTwo(int levelTwo) {
        this.levelTwo = levelTwo;
    }



}
