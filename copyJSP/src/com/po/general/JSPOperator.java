package com.po.general;

import com.po.Code;

import java.util.Random;
import java.util.Vector;

public class JSPOperator {
    protected int []alreadyAddNum;
    private int cLength;
    protected Random random=new Random();
    private int OperationForEachJob;
    private Func func;
    public JSPOperator (int cLength,int OperationForEachJob){
        this.OperationForEachJob=OperationForEachJob;
        this.cLength=cLength;
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
    private void PathRelinking(int[] c1,int[] opt) {
        Vector<Integer>diff=getDiff(c1,opt);
        int [] curr=copyArray(c1);
        int [] best=copyArray(c1);
//        int  bestValue=func.function(best);
        for(int i=0;i<diff.size()-2;++i){
            int k=i+1;
            while(k<diff.size()&&curr[diff.get(k)]!=opt[diff.get(i)])++k;
            int temp=curr[diff.get(i)];
            curr[diff.get(i)]=opt[diff.get(i)];
            curr[diff.get(k)]=temp;
            if(curr[diff.get(k)]==opt[diff.get(k)])
                diff.remove(k);
//            if(func.function(curr)<bestValue){
//                best=copyArray(curr);
//            }
            for(int j=0;j<curr.length;++j){
                System.out.print(curr[j]+" ");
            }
            System.out.println("\n");

        }


    }
    private Vector<Integer> getDiff(int []c1,int []opt){
        Vector<Integer>diff=new Vector<Integer>();
        for(int i=0;i<cLength;++i){
            if(c1[i]!=opt[i]){
                diff.add(i);
            }
        }
        if(diff.size()==2){
            int a=random.nextInt(cLength);
            int b=random.nextInt(cLength);
            while(b==a||c1[b]==c1[a])b=random.nextInt(cLength);
            int temp=c1[b];
            c1[b]=c1[a];
            c1[a]=temp;
            return getDiff(c1,opt);
        }
        int k=diff.size();
        for(int i=0;i<diff.size();++i){
            int rand=random.nextInt(k);
            int temp=diff.get(k-1);
            diff.set(k-1,diff.get(rand));
            diff.set(rand,temp);
            --k;
        }

        return diff;
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
    protected int [] copyArray(int [] c){
        int [] copy=new int[c.length];
        for(int i=0;i<copy.length;++i){
            copy[i]=c[i];
        }
        return copy;
    }
    public static void main(String [] args){
        int [] c1={2,2,3,2,3,3,3,2,2,3};
        int [] c2={3,2,3,3,2,2,2,3,3,2};
        new JSPOperator(10,10).PathRelinking(c1,c2);
    }
}
