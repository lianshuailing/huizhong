package com.huizhong.common.exception;

import com.huizhong.common.enums.ExceptionEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * @author shkstart
 * @create 2019-07-19 21:06
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class HzException extends RuntimeException {
    private ExceptionEnum exceptionEnum;

}
