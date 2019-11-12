package com.st.vo;

/**
 * <pre>
 * </pre>
 * Datas
 * Date: 2016/5/26
 * Time: 21:35
 *
 * @author zhaoyexing@social-touch.com
 */
public class Datas {
    private boolean result;
    private int state_code;

    public boolean isResult() {
        return result;
    }

    public void setResult(boolean result) {
        this.result = result;
    }

    public int getState_code() {
        return state_code;
    }

    public void setState_code(int state_code) {
        this.state_code = state_code;
    }

    @Override
    public String toString() {
        return "Datas{" +
                "result=" + result +
                ", state_code=" + state_code +
                '}';
    }
}
