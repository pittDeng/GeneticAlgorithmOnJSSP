package com.po.general;

import com.po.Code;
import java.util.*;

public class JSPOperator extends Operator{
    protected int []alreadyAddNum;
    private int OperationForEachJob;

    public JSPOperator (int cLength,int OperationForEachJob,Func func){
        super(cLength,func);
        this.OperationForEachJob=OperationForEachJob;
        if(cLength<5){
            System.out.println("染色体长度必须大于4");
            System.exit(0);
        }
    }
    protected int [] pbx(int [] c1,int [] c2,double pbxPossibility){
        int [] offspring=generateAllNegativeOffspring(cLength);
        clearAlreadyAddNum();
        for(int i=0;i<cLength;++i){
            if(random.nextDouble()< pbxPossibility){
                offspring[i]=c1[i];
                ++alreadyAddNum[c1[i]];
            }
        }
        int k=0;
        for(int i=0;i<cLength;++i){
            if(offspring[i]==c1[i])
                continue;
            else{
                while(alreadyAddNum[c2[k]]>=OperationForEachJob)k++;
                offspring[i]=c2[k];
                ++alreadyAddNum[c2[k]];
                ++k;
            }
        }
        return offspring;
    }



    protected int [] generateAllNegativeOffspring(int len){
        int [] offspring=new int[len];
        for(int i=0;i<len;++i){
            offspring[i]=-1;
        }
        return offspring;
    }
    protected void clearAlreadyAddNum(){
        for(int i=0;i<alreadyAddNum.length;++i){
            alreadyAddNum[i]=0;
        }
    }

//    public static void testInsert(){
//        int [] a={1,2,3,4,5,6,7,8,9};
//        int [] res=new JSPOperator(9,1).insert(a,10);
//        for(int i=0;i<res.length;++i){
//            System.out.print(res[i]+" ");
//        }
//    }
//    public static void testPathRelinking(){
//        int []a={1,2,3,3,2,4,1,2,3,1};
//        int []b={3,1,2,4,2,3,1,1,2,3};
//        new JSPOperator(10,9).PathRelinking(a,b);
//    }

    public static void main(String [] args) {
//        int [] c1={2,2,3,2,3,3,3,2,2,3};
//        int [] c2={3,2,3,3,2,2,2,3,3,2};
//        new JSPOperator(10,10).PathRelinking(c1,c2);
//        Integer[] c = {2, 3, 2, 2, 1, 2, 1};
//        List list = Arrays.asList(c);
//        LinkedList<Integer> linkedList = new LinkedList<Integer>(list);
//        Integer[] a = linkedList.toArray(new Integer[linkedList.size()+2]);
//        a[0] = 3;
//        for (Integer item : c) {
//            System.out.print(item + " ");
//        }
//        System.out.println("");
//        for (Integer item : a) {
//            System.out.print(item + " ");
//        }
//        testInsert();
//        testPathRelinking();
    }
}
