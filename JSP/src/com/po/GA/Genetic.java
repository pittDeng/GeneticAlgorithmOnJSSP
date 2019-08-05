package com.po.GA;

import com.po.Code;
import com.po.Parameter;

import java.util.Random;

public class Genetic {
    private Solution[] pop;
    private static Random random;
    private static int codeLength=0;
    private static int[] alreadyAddNum;
    private static int numSolutions;
    private static int numOfCrossOverMethod=2;
    private static int numOfMutationMethod=3;
    private static int jobForEachWorkPiece=0;
    //存储每一个解与其他解的距离之和
    private static double []sumDistance;
    private static int[] timeForAllSolution;
    private static double[] values;
    private double [] possibility;
    //private boolean [] isLived;
    private int minimumFinishedTime;
    private int  optIndex;
    private int [] twoPosition;
    //交叉得来的子代
    private Solution [] csonPop;
    //变异得来的子代
    private Solution [] msonPop;
    //存储一个generation中所有的父代和子代
    private Solution [] allPop;
    //private static int numOfInsert=0;
    public Genetic(Code code){
        timeForAllSolution=Code.getTimeForAllSolution();
        initializePop(code.getPop());
        jobForEachWorkPiece=code.getJobForEachWorkPiece();
        numSolutions=pop.length;
        sumDistance=new double[numSolutions];
        possibility=new double[numSolutions];
        random=new Random();
        codeLength=code.getLength();
        alreadyAddNum=new int[Code.getWorkpieceNum()];
        setMinimumFinishedTime();
        calculateJudge();
        values=new double[timeForAllSolution.length];
        twoPosition=new int[2];
        csonPop=new Solution[(int)(Parameter.csonRatio*code.getPopsize())];
        initializecsonPop();
        msonPop=new Solution[(int)(Parameter.msonRatio*code.getPopsize())];
        initializemsonPop();
        allPop=new Solution[pop.length+csonPop.length+msonPop.length];
    }

    //接种操作
    private static void insert(int [] opt,int [] solution)
    {
//        testso(opt);
//        testso(solution);
//        a,b是接种点，且a<b
        int a=random.nextInt(codeLength);
        int b=random.nextInt(codeLength);
        while(a==b){
            b=random.nextInt(codeLength);
        }
        if(a>b){
            int temp=a;
            a=b;
            b=temp;
        }
        clearAlreadyAddNum();
        //----test----
        for(int i=0;i<alreadyAddNum.length;++i){
            if(alreadyAddNum[i]!=0)System.out.print("ErrorLine71");
        }
        for(int i=0;i<=a;++i){
            ++alreadyAddNum[solution[i]];
        }
        for(int i=b;i<codeLength;++i){
            ++alreadyAddNum[solution[i]];
        }
        int index=0;

        if(!testso(opt))System.out.println("index==0");
        for(int i=a+1;i<b;++i){
            while(alreadyAddNum[opt[index]]>=jobForEachWorkPiece)
                index++;
            solution[i]=opt[index];
            ++alreadyAddNum[solution[i]];
            ++index;
        }
        //if((++numOfInsert)%100==0)System.out.println("numOfInsert:"+numOfInsert);
        //System.out.print(alreadyAddNum);
    }
    private void insertAll(Solution[]pop){
        for(int i=0;i<pop.length;++i){
            if(i!=optIndex){
                //System.out.println("i: "+i+" optIndex: "+optIndex);
                if(pop[i]==pop[optIndex])System.out.println("两个Solution相同");
                insert(pop[optIndex].solution,pop[i].solution);
            }
        }
    }
    private double calculateAffinity(int time){
        return ((double)minimumFinishedTime)/((double)time);
    }
    //计算每一个solution的浓度值
    //不能在calculateJudge()之外的函数调用
    private void calculateDensity(){
        clearSumDistance();
        //在循环中作为存储距离的中间变量
        double temp=0;
        for(int i=0;i<numSolutions;++i)
            for(int j=i+1;j<numSolutions;++j){
                temp=distance(pop[i].solution,pop[j].solution);
                sumDistance[i]+=temp;
                sumDistance[j]+=temp;
            }
        for(int i=0;i<sumDistance.length;++i){
            pop[i].density=sumDistance[i]/numSolutions;
        }
    }
    //计算每一个Solution的目标函数，这个函数的调用必须在
    //函数里面调用了calculateDensity(),不能再在其他地方再调用
    private void calculateJudge(){
        calculateDensity();
        for(int i=0;i<numSolutions;++i){
            pop[i].judge=Parameter.alpha*calculateAffinity(pop[i].time)+(1-Parameter.alpha)*pop[i].density;
        }
    }
    //在调用这个函数之前必须要将Solution[] pop进行从大到小的顺序排列
    private void calPossibility(){
        clearPossibility();
        double sumOfJudge= Function.sumOfJudge(pop);
        double sum=0;
        for(int i=0;i<pop.length;++i){
            sum+=pop[i].judge;
            possibility[i]=sum/sumOfJudge;
        }
    }
    //计算所有解的时间
    private static void calTime(Solution[] pop){
        for(int i=0;i<pop.length;++i){
            timeForAllSolution[i]=Code.decode(pop[i].solution);
            pop[i].time=timeForAllSolution[i];
        }
    }

    private void chooseChild(){
        int k=-1;
//        System.out.println("csonPop");
//        testPop(csonPop);
//        System.out.println("msonPop");
//        testPop(msonPop);
//        System.out.println("Pop");
//        testPop(pop);
        for(int i=0;i<pop.length;++i)
            allPop[++k]=pop[i];
        for(int i=0;i<csonPop.length;++i)
            allPop[++k]=csonPop[i];
        for(int i=0;i<msonPop.length;++i)
            allPop[++k]=msonPop[i];
//        System.out.println("allPop");
//        testPop(allPop);

        //test
        //if(k<allPop.length-1)System.out.print("出现错误，chooseChild()");
        //--test

        Function.qsortTime(allPop);
//        System.out.println("After quick sort");
//        testPop(allPop);
        //testAllPop();
        for(int i=0;i<pop.length;++i){
            pop[i]=allPop[i];
        }
    }
    private static void clearAlreadyAddNum(){
        for(int i=0;i<alreadyAddNum.length;++i){
            alreadyAddNum[i]=0;
        }
    }
    private static void clearSumDistance(){
        for(int i=0;i<sumDistance.length;++i){
            sumDistance[i]=0;
        }
    }
    //将copy数组拷贝到arr
    private static void copyArray(int [] copy,int [] arr){
        /*test*/
        if(copy.length!=arr.length)System.out.println("出现错误146");
        for(int i=0;i<copy.length;++i){
            arr[i]=copy[i];
        }
    }
    //只允许calPossibility调用
    private void clearPossibility(){
        for(int i=0;i<possibility.length;++i){
            possibility[i]=0;
        }
    }

    private void crossOver(){
        for(int i=0;i<csonPop.length;++i){
            csonPop[i]=new Solution(new int[codeLength],0);
        }
        for(int i=0;i<csonPop.length;++i){
            int a=random.nextInt(pop.length);
            int b=random.nextInt(pop.length);
            while(b==a)b=random.nextInt(pop.length);
            int method=random.nextInt(numOfCrossOverMethod);
            switch (method){
                case 0: csonPop[i].solution=pbxCrossOver(pop[a].solution,pop[b].solution);
                        break;
                case 1: csonPop[i].solution=opxCrossOver(pop[a].solution,pop[b].solution);
                        break;
            }
            testso(csonPop[i].solution);
            csonPop[i].time=Code.decode(csonPop[i].solution);
        }

    }
    //字符交换变异算子
    private void exchangeMutation(int [] so){
        generateTwoPositon();
        int temp=so[twoPosition[0]];
        so[twoPosition[0]]=so[twoPosition[1]];
        so[twoPosition[1]]=temp;

    }


    //找到最小完成时间的解，并且将optSo赋值为这个解
    private  void findMinimumTime(){
        minimumFinishedTime=timeForAllSolution[0];
        optIndex=0;
        for(int i=0;i<timeForAllSolution.length;++i){
            if(timeForAllSolution[i]<minimumFinishedTime){
                minimumFinishedTime=timeForAllSolution[i];
                optIndex=i;
            }
        }
    }
    //求解两个解之间的距离
    private static double distance(int[] so1,int[]so2){
        int res=0;
        for(int i=0;i<codeLength;++i){
            if(so1[i]!=so2[i])res+=1;
        }
        return ((double)res)/codeLength;
    }
    //初始化offspring要全部为-1，这样才可以判断是否是parent1的赋值
    //还是原本就是0
    private int [] generateAllNegativeOffspring(int len){
        int [] offspring=new int[len];
        for(int i=0;i<len;++i){
            offspring[i]=-1;
        }
        return offspring;
    }
    private void generateTwoPositon(){
        twoPosition[0]=random.nextInt(codeLength);
        twoPosition[1]=twoPosition[0];
        while(twoPosition[1]==twoPosition[0])
        twoPosition[1]=random.nextInt(codeLength);
        if(twoPosition[1]<twoPosition[0]){
            int temp=twoPosition[0];
            twoPosition[0]=twoPosition[1];
            twoPosition[1]=temp;
        }
    }
    private void killAndGenerate(){
        for(int i=Parameter.numOfStay;i<possibility.length;++i){
            if(random.nextDouble()>possibility[i]){
                pop[i].solution=Code.generateOneSolution();
                pop[i].time=Code.decode(pop[i].solution);
            }
        }
    }

    private void moveMutation(int [] so){
        generateTwoPositon();
        int a=twoPosition[0];
        int b=twoPosition[1];
        int temp=so[a];
        for(int i=a;i<b;++i){
            so[i]=so[i+1];
        }
        so[b]=temp;
    }

    private void mutation(){
        for(int i=0;i<msonPop.length;++i){
            msonPop[i]=new Solution(new int[codeLength],0);
        }
        for(int i=0;i<msonPop.length;++i){
            int a=random.nextInt(pop.length);
            copyArray(pop[a].solution,msonPop[i].solution);
            int method=random.nextInt(numOfMutationMethod);
            switch (method){
                case 0: exchangeMutation(msonPop[i].solution);
                        msonPop[i].time=Code.decode(msonPop[i].solution);
                        break;
                case 1: reverseMutation(msonPop[i].solution);
                        msonPop[i].time=Code.decode(msonPop[i].solution);
                        break;
                case 2: moveMutation(msonPop[i].solution);
                        msonPop[i].time=Code.decode(msonPop[i].solution);
                        break;
                        //test
                default:System.out.println("出现错误mutation");
            }
        }
    }
    private void oneGeneration(int iterindex){
        random.setSeed(System.currentTimeMillis());
        calTime(pop);
        findMinimumTime();
        insertAll(pop);
        calculateJudge();
//        //Function.qsort(pop);
        calPossibility();
        killAndGenerate();
        crossOver();
        mutation();
        chooseChild();
        //if(iterindex==1)testPop();
    }
    //进行1px交叉操作
    private int [] opxCrossOver(int [] so1,int [] so2){
        int [] offspring=generateAllNegativeOffspring(codeLength);
        clearAlreadyAddNum();
        int i=0;
        for(;i<Parameter.opxPossibility*codeLength;++i){
            offspring[i]=so1[i];
            ++alreadyAddNum[so1[i]];
        }
        int k=0;
        for(;i<codeLength;++i){
            //循环内的这一段代码在pbx中也出现了
            while(alreadyAddNum[so2[k]]>=jobForEachWorkPiece)k++;
            offspring[i]=so2[k];
            ++alreadyAddNum[so2[k]];
            ++k;
        }
        return offspring;
    }
    //进行pbx交叉操作
    private int [] pbxCrossOver(int [] so1,int [] so2){
        //这里编码的长度可以在类里面设一个全局变量
        int [] offspring=generateAllNegativeOffspring(codeLength);
        clearAlreadyAddNum();
        for(int i=0;i<codeLength;++i){
            if(random.nextDouble()<Parameter.pbxPossibility){
                offspring[i]=so1[i];
                ++alreadyAddNum[so1[i]];
            }

        }
        int k=0;
        for(int i=0;i<offspring.length;++i){
            if(offspring[i]==so1[i])continue;
            else{
                while(alreadyAddNum[so2[k]]>=jobForEachWorkPiece)k++;
                offspring[i]=so2[k];
                ++alreadyAddNum[so2[k]];
                ++k;
            }
        }
        return offspring;
    }
    private void setMinimumFinishedTime(){
        minimumFinishedTime=Function.minOfArray(timeForAllSolution);
    }
    //字符反转变异算子
    private void reverseMutation(int [] so){
        generateTwoPositon();
        int a=twoPosition[0];
        int b=twoPosition[1];
        int temp=0;
        int i=a;
        int j=b;
        for(;i<j;){
            temp=so[i];
            so[i]=so[j];
            so[j]=temp;
            ++i;
            --j;
        }
    }
    public void runAlgorithm(){
        for(int i=0;i<Parameter.numOfIteration;++i){
            oneGeneration(i);
            if(i%1000==0)System.out.println("最小时间："+pop[0].time+"(第"+i+"次迭代)");
        }
    }
    private void initializePop(int [][]tempPop){
        pop=new Solution[tempPop.length];
        for(int i=0;i<tempPop.length;++i){
            pop[i]=new Solution(tempPop[i],timeForAllSolution[i]);
        }
    }
    private void initializecsonPop(){
        for(int i=0;i<csonPop.length;++i){
            csonPop[i]=new Solution(new int [codeLength],0);
        }
    }

    private void initializemsonPop(){
        for(int i=0;i<msonPop.length;++i){
            msonPop[i]=new Solution(new int[codeLength],0);
        }
    }
    private static boolean testso(int [] so){
        int [] already=new int[alreadyAddNum.length];
        for(int i=0;i<already.length;++i){
            already[i]=0;
        }
        for(int i=0;i<so.length;++i){
            ++already[so[i]];
        }
        for(int i=0;i<alreadyAddNum.length;++i){
            if(already[i]!=jobForEachWorkPiece) {
                System.out.println("出现错误Line407 " + already[i]);
                return false;
            }
        }
        return true;
    }
    private void testPop(Solution [] pop){
        for(int i=0;i<pop.length;++i)
            for(int j=i+1;j<pop.length;++j)
            {
                if(pop[i]==pop[j]){
                    System.out.println("testPop出现地址相同的solution");
                }
            }
    }
    private void testAllPop(){
        Solution [] tempPop=new Solution[pop.length+msonPop.length+csonPop.length];
        int k=-1;
        for(int i=0;i<pop.length;++i)
            tempPop[++k]=pop[i];
        for(int i=0;i<msonPop.length;++i)
            tempPop[++k]=msonPop[i];
        for(int i=0;i<csonPop.length;++i)
            tempPop[++k]=csonPop[i];
        for(int i=0;i<tempPop.length;++i) {
            int j=i+1;
            for (; j < tempPop.length; ++j) {
                if (tempPop[i] == tempPop[j]) {
                    System.out.println("AllPop出现相同Solution");
                    System.out.println(String.format("tempPop[%d]==tempPop[%d]",i,j));
                    break;
                }

            }
            if(j<tempPop.length)break;
        }
    }
    //测试Insert函数是否正确
    private static void testInsert()
    {
        Code code=new Code("data.txt");
        Genetic genetic=new Genetic(code);
        int [] so1=code.getPop()[1];
        int [] so2=code.getPop()[2];
//        int [] offspring=genetic.insert(so1,so2);
//        System.out.println(Genetic.distance(so1,so2));
//        System.out.println(genetic.minimumFinishedTime);

    }
    private static void testSort(){
        Code code=new Code("data.txt");
        Genetic genetic=new Genetic(code);
        Function.qsort(genetic.pop);
        for(int i=0;i<genetic.pop.length;++i){
            System.out.print(genetic.pop[i].judge+" ");
        }
    }
    public static void testOneGeneration(){
        Code code=new Code("ft10.txt");
        Genetic genetic=new Genetic(code);
        genetic.oneGeneration(0);
    }
    public static void testAlgorithm(){
        Code code=new Code("ft20.txt");
        Genetic genetic=new Genetic(code);
        genetic.runAlgorithm();
    }
    public static void main(String [] args) {
        testAlgorithm();

    }
}
