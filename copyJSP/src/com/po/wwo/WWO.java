package com.po.wwo;

import com.po.Code;
import com.po.Parameter;
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
        for(int i=0;i<popSize;++i){
            JSPChromosome jspChromosome=twoStagePropagation(pop[i]);
            int [] afterpbx= new JSPOperator(pop[i].getCode().length,JSPChromosome.read.getWorkpieceNum(),JSPChromosome.read.getJobForEachWorkPiece(),func).pbx(pop[i].getCode(),best.getCode(), Parameter.pbxPossibility);
            if(func.function(afterpbx)<jspChromosome.getFitnessValue()){
                jspChromosome.setCode(afterpbx);
            }
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
                    if(Operator.getDistance(pop[i].getCode(),best.getCode())<=10&&pop[i].getFitnessValue()>=best.getFitnessValue()){
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
            int [] best=solution;
            int bestFitness=fitness;
            for(int j=1;j<omega;++j){
                solution=Operator.insertByTimes(chromosome.getCode(),insertTimes);
                fitness=func.function(solution);
                if(fitness<bestFitness){
                    best=solution;
                    bestFitness=fitness;
                }
            }
            best=Operator.Ls1(best,func);
            JSPChromosome chromosome1=new JSPChromosome(func);
            chromosome1.setCode(best);
            return chromosome1;
    }
    public static void main(String [] args){
        WWO wwo=new WWO(1000,50);
        wwo.go();
    }

}
