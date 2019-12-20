package com.t5eng.miaosha.exception;

import com.sun.org.apache.bcel.internal.classfile.Code;
import com.t5eng.miaosha.result.CodeMsg;

public class GlobalException extends RuntimeException {
    private  static final long serialVersionUID = 1L;

    private CodeMsg cm;

    public  GlobalException(CodeMsg cm){
        this.cm = cm;
    }
    public CodeMsg getCm(){
        return this.cm;
    }
}
