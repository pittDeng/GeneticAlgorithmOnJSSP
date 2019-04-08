package com.po.wwo;

import com.po.Code;
import com.po.ReadData;
import com.po.general.Chromosome;
import com.po.general.Func;

public final class JSPChromosome extends Chromosome {
    public static Code read=new Code("data.txt");
    private int fitnessValue=0;
    private int height=5;
    private Func func;
    private static int heightMax=5;
    public JSPChromosome(Func func){
        this.func=func;
    }
    @Override
    protected void postClone() {

    }

    @Override
    protected void postGenerate() {
        this.fitnessValue=this.func.function(this.code);
        this.setHeight(heightMax);
    }

    @Override
    protected void init() {
        this.code=this.read.generateOneSolution();
        postGenerate();
    }

    public int getFitnessValue() {
        return fitnessValue;
    }

    public void setFitnessValue(int fitnessValue) {
        this.fitnessValue = fitnessValue;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }
    public void decreaseHeight(){
        this.height-=1;
    }
}
