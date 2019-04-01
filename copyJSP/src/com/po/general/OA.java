package com.po.general;

import com.po.Parameter;

import java.util.Random;

public abstract class OA {
    //种群
    protected Chromosome [] pop;
    //种群大小
    protected int popSize;
    //单条染色体长度
    protected int cLength;
    //迭代次数
    protected int iterNum;
    //一个Job的Operation的数目
    protected int OperationForEachJob;
    //进行迭代
    public abstract void go();
    
   

}
