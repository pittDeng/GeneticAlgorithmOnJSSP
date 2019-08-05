package com.po;

import java.io.*;

public class ReadData {
    private String txt="";
    private int rows=0;
    private int cols=0;
    private int [][] assignMatrix;
    private int [][] timeMatrix;

    public int[][] getAssignMatrix() {
        return assignMatrix;
    }

    public int[][] getTimeMatrix() {
        return timeMatrix;
    }

    public void read(String filePath){
        File file=new File(filePath);
        InputStreamReader isr=null;
        BufferedReader br=null;
        if(file.isFile()&&file.exists()){
            try{
                isr=new InputStreamReader(new FileInputStream(file));
                br=new BufferedReader(isr);
                String temp=null;
                while((temp=br.readLine())!=null){
                    txt+=temp;
                }
            }catch(Exception e){
                e.printStackTrace();
            }finally {
                try{
                    br.close();
                    isr.close();
                }catch(IOException e)
                {
                    e.printStackTrace();
                }

            }

        }
        parseTxt();//将数据转化为二维数组的情况
    }
    private void parseTxt(){
        String [] numStr=txt.split(" ");
        //System.out.println("temp");
        int index=0;
        int i=0;
        for(;i<numStr.length;++i){
            if(index==0&&!numStr[i].equals("")){
                rows=Integer.parseInt(numStr[i]);
                ++index;
            }else if(index==1&&!numStr[i].equals("")){
                cols=Integer.parseInt(numStr[i]);
                break;
            }
        }
        if(rows!=0&&cols!=0){
            timeMatrix=new int[rows][cols];
            assignMatrix=new int[rows][cols];
            try{
                for(int j=0;j<rows;++j){
                    for(int k=0;k<cols;++k){
                        while(numStr[++i].equals(""));
                        assignMatrix[j][k]=Integer.parseInt(numStr[i]);
                        while(numStr[++i].equals(""));
                        timeMatrix[j][k]=Integer.parseInt(numStr[i]);
                    }
                }
            }catch (IndexOutOfBoundsException e){
                System.out.println("数据不足,Line76");
            }

        }
    }

    public int getRows() {
        return rows;
    }

    public int getCols() {
        return cols;
    }
    //    public static void main(String [] args){
//        ReadData readData=new ReadData();
//        System.out.println(readData.read("data.txt"));
//        readData.parseTxt();
//        //System.out.println("temp");
//    }
}
