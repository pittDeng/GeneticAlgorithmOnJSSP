package com.po.wwo;

import com.po.Code;
import com.po.Parameter;
import com.po.data.ToExcel;
import com.po.general.*;

import java.util.Arrays;
import java.util.Comparator;

public class WWO extends OA {
    private JSPChromosome[] pop;
    private JSPChromosome best=null;
    private int insertTimes=(int)(0.2*cLength);
    //每次propagation时insert得到的个体
    private int omega=3;
    private Func func=(x)->{return Code.decode(x);};
    private static int fromIndex=0;
    private static Comparator<JSPChromosome> comparator=new Comparator<JSPChromosome>() {
        @Override
        public int compare(JSPChromosome o1, JSPChromosome o2) {
            return o1.getFitnessValue()-o2.getFitnessValue();
        }
    };
    public WWO(int iterNum,int popSize){
        this.iterNum=iterNum;
        this.popSize=popSize;
        this.pop=new JSPChromosome[popSize];
        initPop();
    }
    private void initPop(){
        for(int i=0;i<popSize;++i){
            this.pop[i]=new JSPChromosome(func);
            this.pop[i].init();
        }
    }

    @Override
    public void prego() {
        Arrays.sort(pop,comparator);
        best=new JSPChromosome(func);
        best.setCode(Operator.copyArray(pop[0].getCode()));
    }

    @Override
    public void OneIteration() {
        //本身这里没有fromIndex-------test-------
        for(int i=fromIndex;i<popSize;++i){
            JSPChromosome jspChromosome=twoStagePropagation(pop[i]);
//            int [] afterpbx= new JSPOperator(pop[i].getCode().length,JSPChromosome.read.getWorkpieceNum(),JSPChromosome.read.getJobForEachWorkPiece(),func).pbx(pop[i].getCode(),best.getCode(), Parameter.pbxPossibility);
//            if(func.function(afterpbx)<jspChromosome.getFitnessValue()){
//                jspChromosome.setCode(afterpbx);
//            }
            if(jspChromosome.getFitnessValue()<pop[i].getFitnessValue()){
                if(jspChromosome.getFitnessValue()<best.getFitnessValue()){
                    int []bestcode=Operator.vns(jspChromosome.getCode(),func);
                    best=new JSPChromosome(func);
                    best.setCode(bestcode);
                }
                pop[i]=jspChromosome;
            }
            else{
                pop[i].decreaseHeight();
                if(pop[i].getHeight()==0){
//                    System.out.print("pathLinking");
                    int [] temp=Operator.PathRelinking(pop[i].getCode(),best.getCode());
                    pop[i]=new JSPChromosome(func);
                    pop[i].setCode(temp);
                    if(Operator.getDistance(pop[i].getCode(),best.getCode())<=5&&pop[i].getFitnessValue()>=best.getFitnessValue()){
                        pop[i].init();
                    }
                }
            }
        }
        //后面还需要排序等等操作
        Arrays.sort(pop,comparator);
        if(comparator.compare(pop[0],best)<0){
            JSPChromosome temp=pop[0];
            pop[0]=best;
            best=temp;
        }
        System.out.print("best "+best.getFitnessValue());
        for(int i=0;i<5;++i){
            System.out.print(String.format(" %d th:%d ",i*10,pop[i*10].getFitnessValue()));
        }
        System.out.println("");
    }

    private JSPChromosome twoStagePropagation(Chromosome chromosome){
            int [] solution=Operator.insertByTimes(chromosome.getCode(),insertTimes);
            int fitness=func.function(solution);
            int [] bestso=solution;
            int bestFitness=fitness;
            for(int j=1;j<omega;++j){
                solution=Operator.insertByTimes(chromosome.getCode(),insertTimes);
                fitness=func.function(solution);
                if(fitness<bestFitness){
                    bestso=solution;
                    bestFitness=fitness;
                }
            }
            //这里原本是Ls1
            bestso=Operator.Ls2(bestso,func);
            JSPChromosome chromosome1=new JSPChromosome(func);
            chromosome1.setCode(bestso);
            return chromosome1;
    }
    public void saveBest(ToExcel toExcel,int rowIndex,int index){
        int []c=best.getCode();
        toExcel.insertData(rowIndex,0,index);
        for(int i=0;i<c.length;++i){
            toExcel.insertData(rowIndex,i+1,c[i]);
        }
        toExcel.save();
    }

    public static void statisticalGo(int times,int iterNum,int popSize) {
        ToExcel toExcel=new ToExcel("JSPdata.xls","JSP");
        ToExcel bestSaver=new ToExcel("JSPdata.xls","JSPbest");
        toExcel.insertString(0,0,"ft10");
        toExcel.save();
        for(int i=0;i<times;++i){
            System.out.println(String.format("--------------------------------------%d-------------------------",i));
            WWO wwo=new WWO(iterNum,popSize);
            wwo.go();
            wwo.saveBest(bestSaver,i,i);
            toExcel.insertData(0,i+1,wwo.best.getFitnessValue());
            toExcel.save();
        }
    }

    public static void main(String [] args){
        WWO.statisticalGo(50,1000,50);
    }

}
