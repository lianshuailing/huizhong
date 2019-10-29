package com.huizhong.common.vo;

import com.huizhong.common.enums.ExceptionEnum;
import lombok.Data;

/**
 * @author shkstart
 * @create 2019-07-19 21:14
 */
@Data
public class ExceptionResult {
    private int status;
    private String msg;
    private long timestamp;

    public ExceptionResult(ExceptionEnum em) {
        this.status = em.getCode();
        this.msg = em.getMsg();
        this.timestamp = System.currentTimeMillis();
    }
}
