package com.huizhong.common.advice;

import com.huizhong.common.exception.HzException;
import com.huizhong.common.vo.ExceptionResult;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @author shkstart
 * @create 2019-07-19 13:57
 */
@RestControllerAdvice
public class CommonExceptionHandler {

    /*
    演示：统一异常处理
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<String> hanlerException(RuntimeException e){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }
    */


    /**
     * 演示：复杂类型的统一异常处理
     * @param e
     * @return
     */
    @ExceptionHandler(HzException.class)
    public ResponseEntity<ExceptionResult> hanlerException(HzException e){
        return ResponseEntity.status(e.getExceptionEnum().getCode()).body(new ExceptionResult(e.getExceptionEnum()));
    }

}
