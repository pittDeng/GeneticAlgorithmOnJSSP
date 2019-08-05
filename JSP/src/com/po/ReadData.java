package com.po;

import java.io.*;
import java.util.Arrays;

public class ReadData {
    private String txt="";
    private int rows=0;
    private int cols=0;
    private MachineInfo[][]infos;
    private int []operations;
    public MachineInfo[][] getInfos() {
        return infos;
    }

    public String getTxt() {
        return txt;
    }

    public void read(String filePath) throws Exception{
        File file=new File(filePath);
        InputStreamReader isr=null;
        BufferedReader br=null;
        if(file.isFile()&&file.exists()){
            try{
                isr=new InputStreamReader(new FileInputStream(file));
                br=new BufferedReader(isr);
                String temp=null;
                while((temp=br.readLine())!=null){
                    temp+="\n";
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
    private void parseTxt() throws Exception{
        String [] strs=txt.split("\n");
        String [] rc=strs[0].split("\t| ");
        rows=Integer.parseInt(rc[0]);
        operations=new int[rows];
        cols=Integer.parseInt(rc[1]);
        this.infos=new MachineInfo[rows][cols];
        for(int i=0;i<rows;++i){
            this.infos[i]=new MachineInfo[cols];
        }
        for(int i=1;i<strs.length;++i){
            String []tempstr=strs[i].split(" +");
            int len=tempstr.length;
            int tempi=0;
            if(tempstr[0].equals("")){
                --len;
                tempi=1;
            }
            int []data=new int [len];
            for(int datai=0;datai<data.length;++tempi,++datai){
                data[datai]=Integer.parseInt(tempstr[tempi]);
            }
            operations[i-1]=data[0];
            int from=1;
            int to=from+2*data[from]+1;
            for(int j=0;j<operations[i-1];++j){
                int []argdata=Arrays.copyOfRange(data,from,to);
                this.infos[i-1][j]=new MachineInfo(argdata);
                from=to;
                if(from==data.length)break;
                to=from+2*data[from]+1;
            }
        }

    }

    public int getRows() {
        return rows;
    }

    public int getCols() {
        return cols;
    }
        public static void main(String [] args) throws Exception{
        ReadData readData=new ReadData();
        readData.read("MK01.fjs");
        System.out.println(readData.getTxt());
        //System.out.println("temp");
    }
}
