package com.t5eng.miaosha.exception;

import com.t5eng.miaosha.result.CodeMsg;
import com.t5eng.miaosha.result.Result;
import org.springframework.validation.BindException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@ControllerAdvice //切面
@ResponseBody
public class GlobalExceptionHandler { //用于拦截运行产生的异常信息

    @ExceptionHandler(value = Exception.class)// 拦截指定的异常类型
    public Result<String> exceptionHandler(HttpServletRequest request, Exception e){
        if(e instanceof GlobalException){
            GlobalException ex = (GlobalException) e;
            System.out.println("------------GlobalException------------");
            return Result.error(ex.getCm());
        }
        else if(e instanceof BindException){ //假如是绑定异常
            BindException ex = (BindException) e; //获取绑定异常
            List<ObjectError> errors = ex.getAllErrors(); //绑定异常可能有多个
            ObjectError error = errors.get(0); // 只查看第一个异常
            String msg = error.getDefaultMessage(); // 获取异常的报告
            System.out.println("------------BindException------------");
            return Result.error(CodeMsg.BIND_ERROR.fillArgs(msg));
        }else{
            System.out.println("------------Other Exception------------");
            return Result.error(CodeMsg.SERVER_ERROR);//不是绑定异常的话返回服务错误.
        }
    }
}
