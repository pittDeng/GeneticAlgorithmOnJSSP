package com.po.mgra;

import com.po.general.Operator;

import java.util.ArrayList;

public class MLDecoder {
    /**
     * 这个类是为了解决基于规则的柔性JSSP问题的解码
     */
    private ArrayList<ArrayList<ArrayList<Integer>>>machineAvailable;
    private ArrayList<ArrayList<ArrayList<Integer>>>machineTime;
    private  int numOfJob;
    private  int numOfMachine;
    private  int [] numOfOperation;
    private  int[] alreadyAdded;
    private int[] occupiedTime;
    private int [] flexibility;
    private int[] order;
    private int[] orderTime;
    private int[] sumOrder;
    private int totalDelay;
    private int maxFinished;
    private int totalTime;
    public MLDecoder(String txt){
        ArrayList<Integer> arr=new ArrayList<>();
        for(int i=0;i<txt.length()-1;++i){
            if(txt.charAt(i)<='Z'&&txt.charAt(i)>='A'&&txt.charAt(i+1)=='\n'){
                arr.add(i+1);
            }
        }
        String []jobmachine=getSubString(txt,arr.get(0),arr.get(1));
        String []npdw=getSubString(txt,arr.get(1),arr.get(2));
        String []ma=getSubString(txt,arr.get(2),arr.get(3));
        String []t=getSubString(txt,arr.get(3),arr.get(4));
        numOfJob=Integer.parseInt(jobmachine[1]);
        numOfMachine=Integer.parseInt(jobmachine[2]);
        numOfOperation=new int[numOfJob];
        for(int i=1;i<=numOfJob;++i) {
            numOfOperation[i - 1] = Integer.parseInt(npdw[i]);
        }
        machineAvailable=new ArrayList<>();
        machineTime=new ArrayList<>();
        flexibility=new int[numOfMachine];
        int ima=1;
        for(int index=0;index<numOfJob;++index){
            machineAvailable.add(new ArrayList<>());
            machineTime.add(new ArrayList<>());
            for(int j=0;j<numOfOperation[index];++j,++ima){
                String [] temp=ma[ima].split(",");
                String [] ttemp=t[ima].split(",");
                if(temp.length!=ttemp.length)
                {
                    System.out.println("时间矩阵和可选矩阵不匹配");
                    System.exit(-1);
                }
                machineAvailable.get(index).add(new ArrayList<>());
                machineTime.get(index).add(new ArrayList<>());
                for(int k=0;k<temp.length;++k){
                    machineAvailable.get(index).get(j).add(Integer.parseInt(temp[k])-1);
                    flexibility[Integer.parseInt(temp[k])-1]+=1;
                    machineTime.get(index).get(j).add(Integer.parseInt(ttemp[k]));
                }
            }
        }
        this.occupiedTime=new int[numOfMachine];
        this.alreadyAdded=new int[numOfJob];
        String [] or=getSubString(txt,arr.get(4),arr.get(5));
        String [] ot=getSubString(txt,arr.get(5),arr.get(6));
        this.order=new int[or.length-1];
        this.orderTime=new int[or.length-1];
        for(int i=1;i<or.length;++i){
            this.order[i-1]=Integer.parseInt(or[i]);
            this.orderTime[i-1]=Integer.parseInt(ot[i]);
        }
        this.sumOrder=new int[numOfJob];
        for(int i=0;i<this.order.length;++i){
            this.sumOrder[this.order[i]]+=1;
        }
    }

    public void decode(int [] so){
        if(!verify(so)){
            System.out.println("solution does not meet the requirement");
            System.exit(-1);
        }
        clearArray(alreadyAdded);
        int [] tempOrder=copyArray(order);
        int [] curTime=new int[order.length];
        for(int i=0;i<so.length;++i){
            int product=so[i];
            int indexOp=alreadyAdded[product]%numOfOperation[product];
            ArrayList<Integer> machineChoice=machineAvailable.get(product).get(indexOp);
            int machineindex=chooseMachine(machineChoice);
            int machine=machineChoice.get(machineindex);
            int time=machineTime.get(product).get(indexOp).get(machineindex);
            totalTime+=time;
            int orderIndex=findItemIndex(order,so[i]);
            int max=Math.max(occupiedTime[machine],curTime[orderIndex]);
            occupiedTime[machine]=max+time;
            curTime[orderIndex]=max+time;
            alreadyAdded[so[i]]+=1;
            if(alreadyAdded[so[i]]%numOfOperation[so[i]]==0){
                tempOrder[orderIndex]=-1;
            }

        }
        for(int tempi=0;tempi<curTime.length;++tempi){
            if(curTime[tempi]>orderTime[tempi]){
                totalDelay+=curTime[tempi]-orderTime[tempi];
            }
        }
        maxFinished=0;
        for(int item:curTime){
            if(item>maxFinished)maxFinished=item;
        }
        System.out.print("maxFinished:"+maxFinished);
        System.out.print("        totalDelay:"+totalDelay);
        System.out.println("        totalTime:"+totalTime);
    }
    private int chooseMachine(ArrayList<Integer> machineChoice){
        /**
         * 这个函数返回的是当前可选机器列表中的编号，
         * 第0个可选机器返回0，
         * 这样可以更好的得到这个机器的时间
         */
        ArrayList<Integer> early=new ArrayList<>();
        int earlyTime=occupiedTime[machineChoice.get(0)];
        early.add(0);
        int indexearly=0;
        for(int j=1;j<machineChoice.size();++j){
            if(occupiedTime[machineChoice.get(j)]<earlyTime){
                early.clear();
                earlyTime = occupiedTime[machineChoice.get(j)];
                early.add(j);
            }
            else if(occupiedTime[machineChoice.get(j)]==early.get(0))
                early.add(j);
        }

        int minflex=flexibility[early.get(0)];
        for(int tempi=1;tempi<early.size();++tempi){
            if(flexibility[early.get(tempi)]<minflex){
                indexearly=tempi;
                minflex=flexibility[early.get(tempi)];
            }
        }
        return indexearly;
    }
    public boolean verify(int []so){
        clearArray(alreadyAdded);
        for(int i=0;i<so.length;++i){
            alreadyAdded[so[i]]++;
        }
        for(int i=0;i<alreadyAdded.length;++i){
            if(alreadyAdded[i]!=numOfOperation[i]*sumOrder[i]){
                return false;
            }
        }
        return true;
    }
    private static void clearArray(int [] array){
        for(int i=0;i<array.length;++i){
            array[i]=0;
        }
    }
    private static String [] getSubString(String txt,int beginIndex,int endIndex){
        String res=null;
        res=txt.substring(beginIndex,endIndex);
        int index=0;
        for(;index<res.length();++index){
            if(res.charAt(index)>='A'&&res.charAt(index)<='Z'){
                break;
            }
        }
        return res.substring(0,index).split("\t|\n| ");
    }
    public static int findItemIndex(int [] arr,int item){
        int index=0;
        for(;index<arr.length;++index){
            if(arr[index]==item){
                break;
            }
        }
        return index;
    }
    public static int [] copyArray(int [] c){
        int [] res=new int[c.length];
        for(int i=0;i<c.length;++i){
            res[i]=c[i];
        }
        return res;
    }
    public void clearResult(){
        this.totalTime=0;
        this.totalDelay=0;
        this.maxFinished=0;
        this.clearArray(occupiedTime);
    }
    public static void main(String [] args){
        int numOfSolution=1000;
        String txt=new DataReader("example1.txt").read();
        MLDecoder decoder=new MLDecoder(txt);
        int [] so={0,0,0,2,2,2,0,0,3,0,3,3,4,4,4,5,5,5};
        decoder.decode(so);
        int mtotalTime=decoder.totalTime;
        int mfinished=decoder.maxFinished;
        int mdelay=decoder.totalDelay;
        for(int i=0;i<10000;++i){
            for(int j=0;j<so.length;++j){
                so=Operator.swap(so);
                decoder.clearResult();
                decoder.decode(so);
                mdelay=Math.min(decoder.totalDelay,mdelay);
                mfinished=Math.min(decoder.maxFinished,mfinished);
                mtotalTime=Math.min(decoder.totalTime,mtotalTime);
            }
        }
        System.out.print("mdeley:"+mdelay);
        System.out.print("      mfinished:"+mfinished);
        System.out.println("    mtotalTime: "+mtotalTime);
    }
}
