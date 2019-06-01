package com.po.general;


import com.po.Parameter;

import java.util.*;

public class Operator {
    protected static int []alreadyAddNum;
    protected int cLength;
    protected static Random random=new Random();
    protected static Func func= Parameter.func;
    public Operator(int cLength,Func func){
        this.cLength=cLength;
        this.func=func;
    }
    public static LinkedList<Integer> array2LinkedList(Integer []c){
        List<Integer> list= Arrays.asList(c);
        return new LinkedList<>(list);
    }

    public static int [] insertByTimes(int []c,int times){
        Integer [] copyc=int2Integer(c);
        LinkedList<Integer>linkedList=array2LinkedList(copyc);
        for(int i=0;i<times;++i){
            insertByOne(linkedList);
        }
        Integer [] res=linkedList.toArray(copyc);
        return Integer2int(res);
    }
    protected static void insertByOne(LinkedList<Integer>c) {
        int [] pos=generateTwoDifferentInteger(c.size());
        insertByOne(c,pos[0],pos[1],false);
    }
    protected static LinkedList<Integer>  insertByOne(LinkedList<Integer>c,int originalPos,int newPos,boolean returnWithNewOne){
        /**
         * originalPos是原index
         * newPos是插入的index
         * returnWithNewOne 如果为true就返回一个复制的LinkedList，通常在调用时需要赋给新的变量
         *                  如果为false就返回原LinkedList,通常在调用时就不用赋值给新的变量
         */

        if(returnWithNewOne){
            c=new LinkedList<Integer>(c);
        }
        Integer itemBeSelected=c.remove(originalPos);
        c.add(newPos,itemBeSelected);
        return c;
    }
    public static int [] generateTwoDifferentInteger(int max){
        int []res=new int[2];
        res[0]=random.nextInt(max);
        res[1]=random.nextInt(max);
        while(res[1]==res[0])
            res[1]=random.nextInt(max);
        return res;
    }
    protected static Integer[] int2Integer(int [] array){
        Integer [] res=new Integer[array.length];
        for(int i=0;i<array.length;++i){
            res[i]=array[i];
        }
        return res;
    }
    public static int [] Integer2int(Integer [] array){
        int [] res=new int[array.length];
        for(int i=0;i< array.length;++i){
            res[i]=array[i];
        }
        return res;
    }
    public static int [] Ls1(int [] c,Func func){
        int [] ref=generateReferenceLoc(c.length);
        int k=0;
        int counter=0;
        LinkedList<Integer> curr=array2LinkedList(int2Integer(c));
        while(counter<c.length){
            int [] best=copyArray(Integer2int(curr.toArray(new Integer[c.length])));
            for(int index=0;index<c.length;++index){
                if(index!=ref[k]){
                    insertByOne(curr,ref[k],index,false);
                    int [] currArray=Integer2int(curr.toArray(new Integer[c.length]));
                    if(func.function(currArray)<func.function(best))
                        best=currArray;
                    //将插入之后的数组还原
                    insertByOne(curr,index,ref[k],false);
                }
            }
            if(func.function(best)<func.function(copyArray(Integer2int(curr.toArray(new Integer[c.length]))))){
                curr=array2LinkedList(int2Integer(best));
                counter=0;
            }
            else
                counter+=1;
            k=(k+1)%c.length;
        }
        return Integer2int(curr.toArray(new Integer[c.length]));
    }
    public static int[] Ls2(int []c,Func func){
        int []copyc=copyArray(c);
        int []ref=generateReferenceLoc(c.length);
        int k=0;
        int counter=0;
        while(counter<c.length){
            int [] best=copyArray(copyc);
            for(int index=ref[k]+1;index<copyc.length;++index){
                swap(copyc,ref[k],index,false);
                if(func.function(copyc)<func.function(best)){
                    best=copyArray(copyc);
                }
                swap(copyc,index,ref[k],false);
            }
            if(func.function(best)<func.function(copyc)){
                copyc=best;
                counter=0;
            }
            else counter+=1;
            k=(k+1)%c.length;
        }
        return copyc;
    }
    public static int[] swap(int []c){
        int [] pos=generateTwoDifferentInteger(c.length);
        //返回一个新的数组
        return swap(c,pos[0],pos[1],true);
    }
    public static int [] swap(int []c,int pos1,int pos2,boolean returnWithNewOne){
        /**
         * c是带执行操作的数组
         * pos1和pos2是交换的位置
         * returnWithNewOne为true时，在新数组上操作，为false时，在原数组上操作
         */
        if(returnWithNewOne)c=copyArray(c);
        int temp=c[pos1];
        c[pos1]=c[pos2];
        c[pos2]=temp;
        return c;
    }
    //通过染色体的长度来确定交换的次数
    public static int [] swapByLength(int [] c)
    {
        c=copyArray(c);
        for(int i=0;i<c.length;++i){
            int [] pos=generateTwoDifferentInteger(c.length);
            swap(c,pos[0],pos[1],false);
        }
        return c;
    }
    public static int[] PathRelinking(int[] c1,int[] opt) {
        int [] copyc1=copyArray(c1);
        Vector<Integer> diff=getDiff(copyc1,opt);
        int [] curr=copyArray(copyc1);
        int [] best=copyArray(copyc1);
        int  bestValue=func.function(best);
        for(int i=0;i<diff.size()-2;++i){
            int k=i+1;
            while(k<diff.size()&&curr[diff.get(k)]!=opt[diff.get(i)])++k;
            int temp=curr[diff.get(i)];
            curr[diff.get(i)]=opt[diff.get(i)];
            curr[diff.get(k)]=temp;
            if(curr[diff.get(k)]==opt[diff.get(k)])
                diff.remove(k);
            if(func.function(curr)<bestValue){
                best=copyArray(curr);
            }
//            for(int j=0;j<curr.length;++j){
//                System.out.print(curr[j]+" ");
//            }
//            System.out.println("\n");

        }
        return best;
    }
    public static int [] copyArray(int [] c){
        int [] copy=new int[c.length];
        for(int i=0;i<copy.length;++i){
            copy[i]=c[i];
        }
        return copy;
    }
    private  static Vector<Integer> getDiff(int []c1,int []opt){
        Vector<Integer>diff=new Vector<Integer>();
        for(int i=0;i<c1.length;++i){
            if(c1[i]!=opt[i]){
                diff.add(i);
            }
        }
        if(diff.size()==2){
            int [] temp=insertByTimes(c1,20);
            for(int i=0;i<temp.length;++i){
                c1[i]=temp[i];
            }
//            int a=random.nextInt(c1.length);
//            int b=random.nextInt(c1.length);
//            while(b==a)b=random.nextInt(c1.length);
//            int temp=c1[b];
//            c1[b]=c1[a];
//            c1[a]=temp;
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
    protected static void clearAlreadyAddNum(){
        for(int i=0;i<alreadyAddNum.length;++i){
            alreadyAddNum[i]=0;
        }
    }
    public static int getDistance(int []c1,int[] c2){
        int res=0;
        for(int i=0;i<c1.length;++i){
            if(c1[i]!=c2[i])res+=1;
        }
        return res;
    }
    protected static int [] generateReferenceLoc(int clength){
        /**
         * 生成0到clength-1的随机排列
         */
        int [] ref=new int[clength];
        for(int i=0;i<clength;++i){
            ref[i]=i;
        }
        for(int i=0;i<clength;++i)
            ref=swap(ref);
        return ref;
    }
    protected static int [] findOptByInsertAll(int [] c,Func func,Compare compare){
        int []ref=generateReferenceLoc(c.length);
        int originalValue=func.function(c);
        int bestValue=originalValue;
        int [] bestC=c;
        Integer []copyc=int2Integer(c);
        LinkedList<Integer>linkedList=array2LinkedList(copyc);
        for(int item:ref){
            for(int i=0;i<linkedList.size();++i){
                if(i!=item){
                    linkedList=insertByOne(linkedList,item,i,true);
                    int [] newC=Integer2int(linkedList.toArray(new Integer[linkedList.size()]));
                    int newValue=func.function(newC);
                    if(compare.function(newValue,bestValue)) {
                        bestC=newC;
                        bestValue=newValue;
                    }
                }
            }
        }
        return bestC;
    }
    public static int[] vns(int []c,Func func){
        int l=1;
        int [] newc;
        while(l<=2){
            if(l==1)
                newc=Ls2(c,func);
            else
                newc=Ls1(c,func);
            if(func.function(newc)<func.function(c)){
                l=1;
                c=newc;
            }else
                l+=1;
        }
        return c;
    }
    public static void testLs1(){
        /**
         * 测试Ls1函数
         */
        Func func=(x)->{
            int res=0;
            for(int i=0;i<x.length-1;++i){
                if(x[i]>x[i+1])res+=1;
            }
            return res;
        };
        int []c={1,5,6,7,3,4,2};
        int [] res=Operator.Ls1(c,func);

        for(int i=0;i<res.length;++i){
            System.out.print(res[i]+" ");
        }
        System.out.println(" ");
        System.out.println(func.function(c));
        System.out.println(func.function(res));
    }

    public static void testLs2(){
        /**
         * 测试Ls2函数
         */
        Func func=(x)->{
            int res=0;
            for(int i=0;i<x.length-1;++i){
                if(x[i]<x[i+1])res+=1;
            }
            return res;
        };
        int []c={1,5,6,7,3,4,2};
        int [] res=Operator.vns(c,func);

        for(int i=0;i<res.length;++i){
            System.out.print(res[i]+" ");
        }
        System.out.println(" ");
        System.out.println(func.function(c));
        System.out.println(func.function(res));
    }


    protected static int [] generateAllNegativeOffspring(int len){
        int [] offspring=new int[len];
        for(int i=0;i<len;++i){
            offspring[i]=-1;
        }
        return offspring;
    }
    public static void fillRest(int [] offspring,int [] c1,int [] c2,int [] alreadyAddNum,int [] sumSo){
        int k=0;
        for(int i=0;i<c1.length;++i){
            if(offspring[i]==c1[i])
                continue;
            else{
                while(alreadyAddNum[c2[k]]>=sumSo[c2[k]])k++;
                offspring[i]=c2[k];
                ++alreadyAddNum[c2[k]];
                ++k;
            }
        }
    }
    public static void fillRest(int [] offspring,int [] c1,int [] c2,int [] alreadyAddNum,int sumSo){
        int k=0;
        for(int i=0;i<c1.length;++i){
            if(offspring[i]==c1[i])
                continue;
            else{
                while(alreadyAddNum[c2[k]]>=sumSo)k++;
                offspring[i]=c2[k];
                ++alreadyAddNum[c2[k]];
                ++k;
            }
        }
    }
    public static void main(String [] args){
        testLs2();
    }
}

