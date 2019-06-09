package com.po.mgra;

import com.po.data.ToExcel;
import com.po.general.Operator;

import java.util.ArrayList;

public class MLDecoder {
    /**
     * 这个类是为了解决基于规则的柔性JSSP问题的解码
     */
    private ArrayList<ArrayList<ArrayList<Integer>>>machineAvailable;
    private ArrayList<ArrayList<ArrayList<Integer>>>machineTime;
    public  int numOfJob;
    private  int numOfMachine;
    private  int [] numOfOperation;
    private  int[] alreadyAdded;
    private int[] occupiedTime;
    private int [] flexibility;
    private int[] order;
    private int[] orderTime;
    public int [] florder;
    private int [] flot;
    ArrayList<SLProduct>slOrder;
    public int [] slorder;
    private int [] slot;
    private int[] sumOrder;
    SLProduct [] slProduct;
    private int totalDelay;
    private int maxFinished;
    private int totalTime;
    public int []sumSo;
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
        String [] secondlayer=getSubString(txt,arr.get(6),arr.get(7));
        slProduct=new SLProduct[secondlayer.length-1];
        for(int i=1;i<secondlayer.length;++i){
            String [] temp=secondlayer[i].split(",");
            int [] flProduct=new int [temp.length-1];
            for(int j=0;j<flProduct.length;++j){
                flProduct[j]=Integer.parseInt(temp[j]);
            }
            slProduct[i-1]=new SLProduct(Integer.parseInt(temp[temp.length-1]),flProduct);
        }
        ArrayList<Integer>temporder=new ArrayList<>();
        ArrayList<Integer>tempot=new ArrayList<>();
        ArrayList<SLProduct>slOrder=new ArrayList<>();
        ArrayList<Integer>slOt=new ArrayList<>();
        for(int i=0;i<order.length;++i){
            boolean flag=true;
            for(int j=0;j<slProduct.length;++j){
                if(order[i]==slProduct[j].index){
                    slOrder.add(new SLProduct(order[i],slProduct[j].flProduct));
                    //for(int item:slProduct[j].flProduct)
                    int len=slProduct[j].flProduct.length;
                    slOrder.get(slOrder.size()-1).flindex=new int[len];
                    for(int k=0;k<len;++k){
                        int item=slProduct[j].flProduct[k];
                        temporder.add(item);
                        tempot.add(Integer.MAX_VALUE);
                        slOrder.get(slOrder.size()-1).flindex[k]=temporder.size()-1;
                    }
                    slOt.add(orderTime[i]);
                    flag=false;
                }
            }
            if(flag){
                temporder.add(order[i]);
                tempot.add(orderTime[i]);
            }
        }
        this.florder=Operator.Integer2int(temporder.toArray(new Integer[temporder.size()]));
        this.flot=Operator.Integer2int(tempot.toArray(new Integer[tempot.size()]));
        this.slOrder=slOrder;
        this.slorder=new int[this.slOrder.size()];
        for(int i=0;i<slorder.length;++i){
            slorder[i]=slOrder.get(i).index;
        }
        this.slot=Operator.Integer2int(slOt.toArray(new Integer[slOt.size()]));
        this.sumOrder=new int[numOfJob];
        for(int i=0;i<this.florder.length;++i){
            this.sumOrder[this.florder[i]]+=1;
        }
        for(int i=0;i<this.slOrder.size();++i){
            this.sumOrder[this.slOrder.get(i).index]+=1;
        }
        sumSo=new int[sumOrder.length];
        for(int i=0;i<sumOrder.length;++i){
            sumSo[i]=sumOrder[i]*numOfOperation[i];
        }

    }
    private class SLProduct{
        public int index;
        public int [] flProduct;
        public int [] flindex;
        public SLProduct(int index,int [] flProduct){
            this.index=index;
            this.flProduct=flProduct;
        }
    }
    public void decode(int [] fso,int []sso){
        if(!verify(fso,sso)){
            System.out.println("solution does not meet the requirement");
            System.exit(-1);
        }
        clearArray(alreadyAdded);
        int [] curTime=new int[florder.length];
        decodeOneLayer(fso,curTime,florder);
        int [] slCurTime=new int[slOrder.size()];
        for(int i=0;i<slOrder.size();++i){
            SLProduct tempslProduct=slOrder.get(i);
            for(int j=0;j<tempslProduct.flindex.length;++j){
                slCurTime[i]=Math.max(slCurTime[i],curTime[tempslProduct.flindex[j]]);
            }
        }
//        int tempmax=0;
//        for(int i=0;i<curTime.length;++i){
//            tempmax=Math.max(tempmax,curTime[i]);
//        }
//        for(int i=0;i<slCurTime.length;++i){
//            slCurTime[i]=tempmax;
//        }
        decodeOneLayer(sso,slCurTime,slorder);
        for(int tempi=0;tempi<curTime.length;++tempi){
            if(curTime[tempi]>flot[tempi]){
                totalDelay+=curTime[tempi]-flot[tempi];
            }
        }
        for(int tempi=0;tempi<slCurTime.length;++tempi){
            if(slCurTime[tempi]>slot[tempi]){
                totalDelay+=slCurTime[tempi]-slot[tempi];
            }
        }
        maxFinished=0;
        for(int item:curTime){
            if(item>maxFinished)maxFinished=item;
        }
        for(int item:slCurTime){
            if(item>maxFinished)maxFinished=item;
        }
//        System.out.print("maxFinished:"+maxFinished);
//        System.out.print("        totalDelay:"+totalDelay);
//        System.out.println("        totalTime:"+totalTime);
    }
    public double getDecodeValue(int [] fso,int [] sso){
        decode(fso,sso);
        return 0.7*this.totalDelay+0.2*this.totalTime+0.1*this.maxFinished;
    }
    private void decodeOneLayer(int [] so,int []curTime,int []curorder){
        int [] tempOrder=copyArray(curorder);
        for(int i=0;i<so.length;++i){
            int product=so[i];
            int indexOp=alreadyAdded[product]%numOfOperation[product];
            ArrayList<Integer> machineChoice=machineAvailable.get(product).get(indexOp);
            int machineindex=chooseMachine(machineChoice);
            int machine=machineChoice.get(machineindex);
            int time=machineTime.get(product).get(indexOp).get(machineindex);
            totalTime+=time;
            int orderIndex=findItemIndex(tempOrder,so[i]);
            //try{
                int max=Math.max(occupiedTime[machine],curTime[orderIndex]);
            //}catch (Exception e){
//                System.out.println("occupiedLen:"+occupiedTime.length+"   machine:"+machine);
//                System.out.println("curTimeLen:"+curTime.length+"   orderIndex:"+machine);
//            }
            occupiedTime[machine]=max+time;
            curTime[orderIndex]=max+time;
            alreadyAdded[so[i]]+=1;
            if(alreadyAdded[so[i]]%numOfOperation[so[i]]==0){
                tempOrder[orderIndex]=-1;
            }
        }
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

        for(int j=1;j<machineChoice.size();++j){
            if(occupiedTime[machineChoice.get(j)]<earlyTime){
                early.clear();
                earlyTime = occupiedTime[machineChoice.get(j)];
                early.add(j);
            }
            else if(occupiedTime[machineChoice.get(j)]==early.get(0))
                early.add(j);
        }
        int indexearly=early.get(0);
        int minflex=flexibility[machineChoice.get(early.get(0))];
        for(int tempi=1;tempi<early.size();++tempi){
            if(flexibility[machineChoice.get(early.get(tempi))]<minflex){
                indexearly=early.get(tempi);
                minflex=flexibility[machineChoice.get(early.get(tempi))];
            }
        }
        return indexearly;
    }
    public boolean verify(int []fso,int []sso){
        clearArray(alreadyAdded);
        for(int i=0;i<sso.length;++i){
            alreadyAdded[sso[i]]++;
        }
        for(int i=0;i<fso.length;++i){
            alreadyAdded[fso[i]]++;
        }
        for(int i=0;i<alreadyAdded.length;++i){
            if(alreadyAdded[i]!=numOfOperation[i]*sumOrder[i]){
                return false;
            }
        }
        return true;
    }
    public int [] generateSo(int [] funcorder){
        ArrayList<Integer>fso=new ArrayList<>();
        for(int i=0;i<funcorder.length;++i){
            for(int j=0;j<numOfOperation[funcorder[i]];++j){
                fso.add(funcorder[i]);
            }
        }
        return Operator.Integer2int(fso.toArray(new Integer[fso.size()] ));
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
        int [] fso=decoder.generateSo(decoder.florder);
        int [] sso=decoder.generateSo(decoder.slorder);
        decoder.decode(fso,sso);
        ToExcel toExcel=new ToExcel("gra1.xls","result");
        int mtotalTime=decoder.totalTime;
        int mfinished=decoder.maxFinished;
        int mdelay=decoder.totalDelay;
        try{
            for(int i=0;i<1000;++i){
                int len=fso.length;
                for(int j=0;j<fso.length;++j){
                    int row=i*fso.length+j;
                    toExcel.insertData(row,0,decoder.maxFinished);
                    toExcel.insertData(row,1,decoder.totalDelay);
                    toExcel.insertData(row,2,decoder.totalTime);
                    double res=0.7*decoder.totalDelay+0.2*decoder.totalTime+0.1*decoder.maxFinished;
                    toExcel.insertDouble(row,3,res);
                    fso=Operator.swap(fso);
                    sso=Operator.swap(sso);
                    decoder.clearResult();
                    decoder.decode(fso,sso);
                    mdelay=Math.min(decoder.totalDelay,mdelay);
                    mfinished=Math.min(decoder.maxFinished,mfinished);
                    mtotalTime=Math.min(decoder.totalTime,mtotalTime);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            toExcel.save();
        }
        System.out.print("mdeley:"+mdelay);
        System.out.print("      mfinished:"+mfinished);
        System.out.println("    mtotalTime: "+mtotalTime);
    }
}
