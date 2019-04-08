package com.po.wwo;

import com.po.Code;
import com.po.general.Chromosome;
import com.po.general.Func;
import com.po.general.OA;
import com.po.general.Operator;

public class WWO extends OA {
    private JSPChromosome[] pop;
    private JSPChromosome best=null;
    private int insertTimes=(int)(0.2*cLength);
    //每次propagation时insert得到的个体
    private int omega=3;
    private Func func=(x)->{return Code.decode(x);};
    public WWO(){
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
    public void OneIteration() {
        for(int i=0;i<popSize;++i){
            JSPChromosome jspChromosome=twoStagePropagation(pop[i]);
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
                    int [] temp=Operator.PathRelinking(pop[i].getCode(),best.getCode());
                    pop[i]=new JSPChromosome(func);
                    pop[i].setCode(temp);
                }
            }
        }
        //后面还需要排序等等操作
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

}
