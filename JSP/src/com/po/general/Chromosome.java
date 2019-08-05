package com.po.general;

import java.util.Random;

public abstract class Chromosome implements Cloneable{
    protected int [] code;
    @Override
    protected Object clone() throws CloneNotSupportedException {
        Chromosome res=null;
        res=(Chromosome) super.clone();
        int [] newcode=new int[this.code.length];
        for(int i=0;i<this.code.length;++i){
            newcode[i]=this.code[i];
        }
        res.code=newcode;
        res.postClone();
        return res;
    }
    protected  abstract  void postClone();
    protected abstract void postGenerate();
    protected abstract void init();

    public int[] getCode() {
        return code;
    }

    public void setCode(int[] code) {
        //当code改变时，自动改变其他fitness值
        this.code = code;
        postGenerate();
    }

}
