package com.po.general;

import com.po.Parameter;

import java.util.Random;

public abstract class OA {
    //种群大小
    protected int popSize;
    //单条染色体长度
    protected int cLength;
    //迭代次数
    protected int iterNum;
    //进行迭代
    public void go(){
        prego();
        for(int i=0;i<iterNum;++i){
            System.out.print(String.format("%d th Iteration: ",i));
            OneIteration();
        }
    }
    //一次迭代
    public abstract void OneIteration();
    //第一次迭代之前做的工作
    public abstract void prego();
    //进行多次算法过程
//    public abstract void statisticalGo();
    
   

}
