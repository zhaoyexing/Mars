package com.st.model;

import com.st.enums.ErrorCodeEnum;
import org.apache.commons.lang.StringUtils;

/**
 * Created by zhaoyx on 2016/9/19.
 */
public class OpResult<E> {
    private E result;

    public E getResult() {
        return result;
    }

    public void setResult(E result) {
        this.result = result;
    }

    public int getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * 当前操作结果是否成功，当code为0时候，表示成功
     *
     * @return
     */
    public boolean isSucceed() {
        return Succeed == this.code;
    }

    /**
     * 返回code.成功默认为0，非0都是错误。
     */
    protected Integer code = -1;

    private final static int Succeed = 0;

    /**
     * 返回文字内容
     */
    protected String message;


    public <E>  OpResult<E> cloneReasonResult(){
        return createResult(this.getCode(),this.getMessage(),null);
    }

    public <E> OpResult<E> cloneReasonResult(E input) {
        return createResult(this.getCode(), this.getMessage(), input);
    }



    public static <E> OpResult<E> createResult(int code, String msg, E input) {


        if(ErrorCodeEnum.class.isInstance(input)){
            throw  new IllegalArgumentException("当前不支持Input是ErrorCodeEnum的实例，因为Code已经可以包含EnumCodeEnum");
        }

        OpResult<E> result = new OpResult();
        result.setCode(code);
        result.setMessage(msg);
        result.setResult(input);
        return result;
    }

    public static <E> OpResult<E> createFailResult(ErrorCodeEnum errorCodeEnum) {
        return createResult(errorCodeEnum.getCode(), errorCodeEnum.getMsg(), null);
    }


    public static <E> OpResult<E> createFailResult(ErrorCodeEnum errorCodeEnum, E input) {
        return createResult(errorCodeEnum.getCode(), errorCodeEnum.getMsg(), input);
    }


    public static <E> OpResult<E> createFailResultSpecialMsg(ErrorCodeEnum errorCodeEnum, String msg) {

        if (errorCodeEnum.getCode() == 0) {
            throw new IllegalArgumentException("OpResult.createFailResultSpecialMsg 不能接受code为0,表示成功值");
        }

        String message = StringUtils.isBlank(msg) ? errorCodeEnum.getMsg() : msg;

        return createResult(errorCodeEnum.getCode(), message, null);
    }

    public static <E> OpResult<E> createSucResult() {
        return createResult(Succeed, "成功", null);
    }

    public static <E> OpResult<E> createSucResult(E input, String msg) {
        return createResult(Succeed, msg, input);
    }

    public static <E> OpResult<E> createSucResult(E input) {
        return createResult(Succeed, "成功", input);
    }

    @Override
    public String toString() {
        return "OpResult{" +
                "result=" + result +
                ", code=" + code +
                ", message='" + message + '\'' +
                '}';
    }

}
