package com.po.mgra;

import com.po.data.ToExcel;
import com.po.general.Operator;
import com.po.general.Solution;
import java.util.Random;


public class GraGenetic {
    private int popSize;
    private int iterNum;
    private double mutationPoss;
    private double pbxPoss;
    public static MLDecoder decoder=null;
    public GraSolution [] solutions=null;
    public Random random=new Random();
    public GraOperator graOperator=null;
    public static String filePath="";
    public GraGenetic(int popSize,int iterNum,double mutationPoss,double pbxPoss){
        this.popSize=popSize;
        this.iterNum=iterNum;
        this.mutationPoss=mutationPoss;
        this.pbxPoss=pbxPoss;
        String txt=new DataReader(filePath).read();
        MLDecoder decoder=new MLDecoder(txt);
        this.decoder=decoder;
        int [] fso=decoder.generateSo(decoder.florder);
        int [] sso=decoder.generateSo(decoder.slorder);
        this.solutions=new GraSolution[popSize];
        for(int i=0;i<popSize;++i){
            fso= Operator.swapByLength(fso);
            sso=Operator.swapByLength(sso);
            solutions[i]=new GraSolution(fso,sso);
        }
        Solution.Qsort(solutions);
        graOperator=new GraOperator(1,(x)->{return x[0];},decoder.numOfJob);
    }
    private void OneIteration(){
        calculatePoss();
        int num=choose();
        crossOverAndMutation(num+1);
        Solution.Qsort(solutions);
    }
    private void go(){
        for(int i=0;i<iterNum;++i){
            OneIteration();
            System.out.print(i+"th:    ");
            System.out.println(solutions[0].fitnessValue);
        }
    }
    private int choose(){
        for(int i=GraParameter.alwaysSave;i<solutions.length;++i){
            if(random.nextDouble()>solutions[i].poss)
                solutions[i].isLived=false;
        }
        GraSolution [] newsolutions=new GraSolution[popSize];
        int k=-1;
        for(GraSolution so :solutions){
            if(so.isLived){
                newsolutions[++k]=so;
            }
        }
        solutions=newsolutions;
        return k;
    }
    private void crossOverAndMutation(int range){
       for(int i=range;i<popSize;++i){
           int [] indexs=Operator.generateTwoDifferentInteger(range);
           int []fso=GraOperator.pbx(solutions[indexs[0]].fso,solutions[indexs[1]].fso,decoder.sumSo,pbxPoss);
           int []sso=GraOperator.pbx(solutions[indexs[0]].sso,solutions[indexs[1]].sso,decoder.sumSo,pbxPoss);
            solutions[i]=new GraSolution(fso,sso);
            if(random.nextDouble()<mutationPoss){
                indexs=Operator.generateTwoDifferentInteger(fso.length);
                fso=Operator.swap(solutions[i].fso,indexs[0],indexs[1],true);
                indexs=Operator.generateTwoDifferentInteger(sso.length);
                sso=Operator.swap(solutions[i].sso,indexs[0],indexs[1],true);
                GraSolution tempSo=new GraSolution(fso,sso);
                if(tempSo.betterThan(tempSo,solutions[i]))solutions[i]=tempSo;
            }
       }
    }
    private void calculatePoss(){
        /**
         * 必须在排序函数之后做计算
         */
        for(int i=0;i<popSize;++i){
            solutions[i].poss=1-((double)(i+1))/((double)popSize);
        }
    }
    private class GraSolution extends Solution {
        public int [] fso;
        public int [] sso;
        public boolean isLived=true;
        public double poss=0;
        public GraSolution(int [] fso,int []sso) {
            this.fso = fso;
            this.sso = sso;
            this.fitnessValue=decoder.getDecodeValue(fso,sso);
            decoder.clearResult();
        }
        @Override
        public boolean betterThan(Solution so1, Solution so2) {
            return so1.fitnessValue<=so2.fitnessValue;
        }
    }
    public static void test(int i,ToExcel toExcel){
        GraGenetic graGenetic=new GraGenetic(50,2000,0.5,0.8);
        graGenetic.go();
        toExcel.insertDouble(i,0,graGenetic.solutions[0].fitnessValue);
    }
    public static void main(String [] args){
        for(int index=1;index<=4;++index){
            filePath=String.format("example%d.txt",index);
            ToExcel toExcel=new ToExcel(GraParameter.excelName,"index"+index);
            int times=20;
            for(int i=0;i<times;++i){
                test(i,toExcel);
                toExcel.save();
            }
        }
    }
}
