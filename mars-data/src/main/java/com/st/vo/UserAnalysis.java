package com.st.vo;

/**
 * Created by Dingdf on 2016/11/3.
 */
public class UserAnalysis {
    private String originator;

    private String nickName;

    private int levelOne;

    private int levelTwo;

    public void setOriginator(String originator) {
        this.originator = originator;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public void setLevelTwo(int levelTwo) {
        this.levelTwo = levelTwo;
    }

    public void setLevelOne(int levelOne) {
        this.levelOne = levelOne;
    }

    public String getOriginator() {
        return originator;
    }

    public String getNickName() {
        return nickName;
    }

    public int getLevelOne() {
        return levelOne;
    }

    public int getLevelTwo() {
        return levelTwo;
    }
}
