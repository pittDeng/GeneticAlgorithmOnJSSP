package com.po.data;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;

import static org.apache.poi.hssf.record.aggregates.RowRecordsAggregate.createRow;

public class ToExcel {
    HSSFWorkbook workbook;
    HSSFSheet sheet;
    String path;
    public ToExcel(String path,String sheetName){
        this.path=path;
        InputStream is=null;
        workbook=null;
        try{
            is=new FileInputStream(path);
            workbook=new HSSFWorkbook(is);
        }catch (Exception e){
            FileOutputStream fos=null;
            workbook=new HSSFWorkbook();
            try{
                fos=new FileOutputStream(path);
                workbook.write(fos);
                fos.flush();
            }catch (Exception exc){
                exc.printStackTrace();
            }finally {
                try{
                    fos.close();
                }catch (Exception exc){
                    e.printStackTrace();
                }

            }
        }
        sheet=workbook.getSheet(sheetName);
        if(sheet==null){
            sheet=workbook.createSheet(sheetName);
        }
    }
    public void insertData(int rowIndex,int columnIndex,int data){
        HSSFRow row=sheet.getRow(rowIndex);
        if(row==null){
            row=sheet.createRow(rowIndex);
        }
        HSSFCell cell=row.createCell(columnIndex);
        cell.setCellValue(data);
    }
    public void insertDouble(int rowIndex,int columnIndex,double data){
        HSSFRow row=sheet.getRow(rowIndex);
        if(row==null){
            row=sheet.createRow(rowIndex);
        }
        HSSFCell cell=row.createCell(columnIndex);
        cell.setCellValue(data);
    }
    public void insertString(int rowIndex,int columnIndex,String data){
        HSSFRow row=sheet.getRow(rowIndex);
        if(row==null){
            row=sheet.createRow(rowIndex);
        }
        HSSFCell cell=row.createCell(columnIndex);
        cell.setCellValue(data);
    }
    public void save(){
        FileOutputStream fos=null;
        try{
            fos=new FileOutputStream(path);
            workbook.write(fos);
            fos.flush();
        }catch (Exception e){

        }finally {
            try{
                fos.close();
            }catch (Exception e){
                e.printStackTrace();
            }

        }
    }
    public static void main(String [] args){
        ToExcel toExcel=new ToExcel("God1.xls","expData");
        for(int i=0;i<100;++i)
            for(int j=i;j<100;++j){
                toExcel.insertData(i,j,1);
            }
        toExcel.save();
    }
}
