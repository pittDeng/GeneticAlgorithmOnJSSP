package com.po.mgra;

import java.util.ArrayList;
import java.util.Map;

public class MLDecoder {
    /**
     * 这个类是为了解决基于规则的柔性JSSP问题的解码
     */
    private ArrayList<ArrayList<ArrayList<Integer>>>machineAvailable=new ArrayList<>();
    private static int numOfJob;
    private static int numOfMachine;
    private static int [] numOfOperation;
    public MLDecoder(int numOfJob,int []numOfOperation){
        for(int i=0;i<numOfJob;++i){
            machineAvailable.add(new ArrayList<ArrayList<Integer>>(numOfOperation[i]));
        }
    }
    public static void parse(String txt){
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
        int [] numOfOperation=new int[numOfJob];
        for(int i=1;i<=numOfJob;++i) {
            numOfOperation[i - 1] = Integer.parseInt(npdw[i]);
        }
        ArrayList<ArrayList<ArrayList<Integer>>>machineAvailable=new ArrayList<>();
        ArrayList<ArrayList<ArrayList<Integer>>>machineTime=new ArrayList<>();
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
                    machineAvailable.get(index).get(j).add(Integer.parseInt(temp[k]));
                    machineTime.get(index).get(j).add(Integer.parseInt(ttemp[k]));
                }

            }
        }

        System.out.println("test");
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
    public static void main(String [] args){
        String txt=new DataReader("example1.txt").read();
        parse(txt);
    }
}
