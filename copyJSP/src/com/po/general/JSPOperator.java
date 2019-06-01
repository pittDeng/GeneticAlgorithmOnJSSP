package com.po.general;

import com.po.Code;
import java.util.*;

public class JSPOperator extends Operator{
    private int OperationForEachJob;
    private int JobNum;
    public JSPOperator (int cLength,int JobNum,int OperationForEachJob,Func func){
        super(cLength,func);
        this.OperationForEachJob=OperationForEachJob;
        this.JobNum=JobNum;
        alreadyAddNum=new int[this.JobNum];
        if(cLength<5){
            System.out.println("染色体长度必须大于4");
            System.exit(0);
        }
    }
    public int [] pbx(int [] c1,int [] c2,double pbxPossibility){
        int [] offspring=generateAllNegativeOffspring(c1.length);
        clearAlreadyAddNum();
        for(int i=0;i<c1.length;++i){
            if(random.nextDouble()< pbxPossibility){
                offspring[i]=c1[i];
                ++alreadyAddNum[c1[i]];
            }
        }
        Operator.fillRest(offspring,c1,c2,alreadyAddNum,OperationForEachJob);
        return offspring;
    }



//    protected int [] findLocationBetween(int []c,int[] rsequence){
//        int [] loc=new int[c.length];
//        clearAlreadyAddNum();
//        for(int i=0;i<rsequence.length;++i){
//            int index=1;
//            for(int j=0;j<c.length;++j){
//                if(c[j]==rsequence[i]){
//                    if(index>alreadyAddNum[c[j]]){
//                        alreadyAddNum[c[j]]++;
//                        loc[i]=j;
//                        break;
//                    }
//                    index++;
//                }
//            }
//        }
//        return loc;
//    }



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
       int[] ref= JSPOperator.generateReferenceLoc(10);
        for(int item:ref)System.out.print(item+" ");
    }
}
