package com.po.general;


import java.util.*;

public class Operator {
    protected int cLength;
    protected static Random random=new Random();
    protected Func func;
    public Operator(int cLength,Func func){
        this.cLength=cLength;
        this.func=func;
    }
    protected int [] insert(int []c,int times){
        Integer [] copyc=int2Integer(c);
        List<Integer> list= Arrays.asList(copyc);
        LinkedList<Integer> linkedList=new LinkedList<>(list);
        for(int i=0;i<times;++i){
            insertByOne(linkedList);
        }
        Integer [] res=linkedList.toArray(copyc);
        return Integer2int(res);
    }
    protected void insertByOne(LinkedList<Integer>c) {
        int [] pos=generateTwoDifferentInteger(c.size());
        Integer itemBeSelected=c.remove(pos[0]);
        c.add(pos[1],itemBeSelected);
    }
    protected static int [] generateTwoDifferentInteger(int max){
        int []res=new int[2];
        res[0]=random.nextInt(max);
        res[1]=random.nextInt(max);
        while(res[1]==res[0])
            res[1]=random.nextInt(max);
        return res;
    }
    protected Integer[] int2Integer(int [] array){
        Integer [] res=new Integer[array.length];
        for(int i=0;i<array.length;++i){
            res[i]=array[i];
        }
        return res;
    }
    protected int [] Integer2int(Integer [] array){
        int [] res=new int[array.length];
        for(int i=0;i< array.length;++i){
            res[i]=array[i];
        }
        return res;
    }
    protected static int[] swap(int []c){
        int [] pos=generateTwoDifferentInteger(c.length);
        return swap(c,pos[0],pos[1]);
    }
    protected static int [] swap(int []c,int pos1,int pos2){
        int [] copyc=copyArray(c);
        int temp=copyc[pos1];
        copyc[pos1]=copyc[pos2];
        copyc[pos2]=temp;
        return copyc;
    }
    protected  int[] PathRelinking(int[] c1,int[] opt) {
        int [] copyc1=copyArray(c1);
        Vector<Integer> diff=getDiff(copyc1,opt);
        int [] curr=copyArray(copyc1);
        int [] best=copyArray(copyc1);
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
        return best;

    }
    protected static int [] copyArray(int [] c){
        int [] copy=new int[c.length];
        for(int i=0;i<copy.length;++i){
            copy[i]=c[i];
        }
        return copy;
    }
    private  Vector<Integer> getDiff(int []c1,int []opt){
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
}
