package com.cnki.util;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;


public class ResolutionPdf {

    public static void main(String[] args) throws IOException {


        /*
            写入Excel
         */

        //1.创建工作簿
        Workbook wb = new HSSFWorkbook();

        //2.创建工作表Sheet
        Sheet sheet = wb.createSheet();



        String fileDir = "D:\\新建文件夹";

        File file = new File(fileDir);
        File[] files = file.listFiles();// 获取目录下的所有文件或文件夹
        if (files == null) {// 如果目录为空，直接退出
            return;
        }

        try {
            // 遍历，目录下的所有文件
            int count = 0;
            for (File f : files) {
                PDDocument document = PDDocument.load(f);

                // 获取页码
                int pages = document.getNumberOfPages();

                // 读文本内容
                PDFTextStripper stripper=new PDFTextStripper();

                // 设置按顺序输出
                stripper.setSortByPosition(true);
                stripper.setStartPage(1);
                stripper.setEndPage(pages);
                String content = stripper.getText(document);
                if(content.contains("mail")){
                    String email;
                    email = content.substring(content.indexOf("mail"), content.indexOf("mail")+40);
                    if(email.contains("com")){
                        email = email.substring(email.indexOf("mail"), email.indexOf("com")) + "com";
                    }else if(email.contains("cn")){
                        email = email.substring(email.indexOf("mail"), email.indexOf("cn")) + "cn";
                    }

                    email = email.replaceAll(" ", "");
                    email = email.replaceAll("．", ".");


                    //3.创建行对象Row
                    Row row = sheet.createRow(count); //下标从0开始

                    //4.创建单元格对象   从0记数
                    Cell cell = row.createCell(0);

                    //5.设置单元格内容
                    cell.setCellValue(email);

                    //6.设置单元格的样式
                    CellStyle cellStyle = wb.createCellStyle();

                    Font font = wb.createFont();//创建字体对象
                    font.setFontName("微软雅黑");//设置字体名称
                    font.setFontHeightInPoints((short)11);//设置字体大小

                    cellStyle.setFont(font);//样式中添加一个字体样式

                    cell.setCellStyle(cellStyle);

                    System.out.println("第" + (count+1) + "个邮箱");
                }

                count++;
            }
        }catch (Exception e){

        }finally {
            //7.保存，关闭流
            System.out.println("开始写入excel");
            OutputStream os = new FileOutputStream("D:\\邮箱\\邮箱.xls");//创建一个输出流
            wb.write(os);
            os.close();
            System.out.println("写入excel完成");
        }

    }

}
