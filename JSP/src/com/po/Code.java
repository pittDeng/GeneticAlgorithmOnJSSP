package com.po;

import java.util.Random;

public class Code {
    public static String filePath="data.txt";
    private static int length=0;
    private static int workpieceNum=0;
    private static int jobForEachWorkPiece=0;
    private static int popsize=50;
    private static int tempForSeed=0;
    private static int [] alreadyAddNum=null;
    private int [][] pop=null;
    private static int [] machineTime=null;
    private static int [] lastFinishedTime=null;
    private static int [][]assignMatrix=null;
    private static int [][]timeMatrix=null;
    private static int[]timeForAllSolution=null;

    public Code(String filePath){
        ReadData readData=new ReadData();
        readData.read(filePath);
        assignMatrix=readData.getAssignMatrix();
        timeMatrix=readData.getTimeMatrix();
        this.workpieceNum=readData.getRows();
        this.jobForEachWorkPiece=readData.getCols();
        length=workpieceNum*jobForEachWorkPiece;
        alreadyAddNum=new int[workpieceNum];
        machineTime=new int[jobForEachWorkPiece];//存储机器的加工时间
        lastFinishedTime=new int[workpieceNum];
        timeForAllSolution=new int[popsize];
        pop=new int[popsize][length];
        generatePopulation();
        decode();
    }

    public static int[] getTimeForAllSolution() {
        return timeForAllSolution;
    }

    public int getLength() {
        return length;
    }

    public static int getWorkpieceNum() {
        return workpieceNum;
    }

    public static int getJobForEachWorkPiece() {
        return jobForEachWorkPiece;
    }

    public static int getPopsize() {
        return popsize;
    }

    public int[][] getPop() {
        return pop;
    }

    public static int[][] getAssignMatrix() {
        return assignMatrix;
    }

    public static void setAssignMatrix(int[][] assignMatrix) {
        Code.assignMatrix = assignMatrix;
    }

    public static int[][] getTimeMatrix() {
        return timeMatrix;
    }

    public static void setTimeMatrix(int[][] timeMatrix) {
        Code.timeMatrix = timeMatrix;
    }

    private void generatePopulation(){

        for(int i=0;i<pop.length;++i){
            pop[i]=generateOneSolution();
        }

    }
    public static int[] generateOneSolution(){
        clearAlreadyAddNum();
        int i=-1;
        int rand=0;//记录循环中的随机数
        Random random=new Random(System.currentTimeMillis()+(tempForSeed++));
        int [] solution=new int[length];
        while(i<length-1){
            rand=random.nextInt(workpieceNum);
            if(++alreadyAddNum[rand]<=jobForEachWorkPiece){
                solution[++i]=rand;
            }
        }
        return solution;
    }

    public int[] decode(){
        for(int i=0;i<pop.length;++i){
            timeForAllSolution[i]=decode(pop[i]);
        }
        return timeForAllSolution;
    }
    public static int decode(int [] solution){
        clearAlreadyAddNum();
        clearMachineTime();
        clearLastFinishedTime();
        //对于遍历的每一个所用的机器的编号
        int machineNum=0;
        //对于遍历的每一个所用时间为itemTime;
        int itemTime=0;
        for(int i=0;i<solution.length;++i){
            machineNum=assignMatrix[solution[i]][alreadyAddNum[solution[i]]];
            itemTime=timeMatrix[solution[i]][alreadyAddNum[solution[i]]];
            if(lastFinishedTime[solution[i]]>machineTime[machineNum]){
                machineTime[machineNum]=lastFinishedTime[solution[i]]+itemTime;
            }else{
                machineTime[machineNum]+=itemTime;
            }
            lastFinishedTime[solution[i]]=machineTime[machineNum];
            ++alreadyAddNum[solution[i]];
        }
        return maxOfArray(machineTime);

    }
    private static void clearAlreadyAddNum(){
        for(int i=0;i<alreadyAddNum.length;++i){
            alreadyAddNum[i]=0;
        }
    }
    private static void clearMachineTime(){
        for(int i=0;i<machineTime.length;++i){
            machineTime[i]=0;
        }
    }
    private static void clearLastFinishedTime(){
        for (int i=0;i<lastFinishedTime.length;++i){
            lastFinishedTime[i]=0;
        }
    }
    //找到一个数组的最大值
    private static int maxOfArray(int[] array){
        int max=array[0];
        for(int i=0;i<array.length;++i){
            if(array[i]>max)max=array[i];
        }
        return max;
    }
    public static void test(){
        Code code=new Code(filePath);
        int [] solution=code.generateOneSolution();
        System.out.println("length:"+solution.length);
        for(int i=0;i<solution.length;++i){
            System.out.print(solution[i]+" ");
        }
        int [] tempAlreadyAdd=new int[code.workpieceNum];
        for(int i=0;i<tempAlreadyAdd.length;++i){
            tempAlreadyAdd[i]=0;
        }
        for(int i=0;i<solution.length;++i){
            ++tempAlreadyAdd[solution[i]];
        }
        System.out.println("AlreadyAdd");
        for(int i=0;i<tempAlreadyAdd.length;++i){
            System.out.print(tempAlreadyAdd[i]+" ");
        }
    }
    public static void test1(){
        Code code=new Code(filePath);
        code.generatePopulation();
        for(int i=0;i<code.pop.length;++i){
            for(int j=0;j<code.pop[i].length;++j){
                System.out.print(code.pop[i][j]+" ");
            }
            System.out.print("\n");
        }
        for(int i=0;i<code.pop.length;++i){
            System.out.println(code.decode(code.pop[i]));
        }
    }
    public static void main(String [] args){
        test1();
    }
}
