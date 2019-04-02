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

}
