package com.fileupdate.fileupdatedemo.controller;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import java.io.FileNotFoundException;

@ControllerAdvice
public class handlerExceptionController {

    @ExceptionHandler(MaxUploadSizeExceededException.class)//捕捉对应的异常
    @ResponseBody
    public String handlerException(Exception e)throws Exception{
        return "文件上传现在7M,请重新选择";
    }

    @ExceptionHandler(FileNotFoundException.class)//捕捉对应的异常
    @ResponseBody
    public String downLoadException(Exception e)throws Exception{
        return "文件不存在";
    }
}
