package com.po.mgra;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;

public class DataReader {
    private  String txt;
    private String filePath;
    public DataReader(String filePath){
        this.filePath=filePath;
    }
    public  String read(){
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
                    txt+="\n";
                }
            }catch (Exception e){
                e.printStackTrace();
            }finally {
                try{
                    br.close();
                    isr.close();
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }
        return txt;
    }

}
