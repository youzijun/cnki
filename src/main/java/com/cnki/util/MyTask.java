package com.cnki.util;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by ZJX-BJ-01-00057 on 2018/7/31.
 */
public class MyTask implements Runnable{

    private static final String fileDir = "D:\\dev\\20180731\\pdf\\";

    private static List<String> list = Collections.synchronizedList(new ArrayList<>());

    private String fileName;

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public MyTask(String fileName) {
        this.fileName = fileName;
    }


    @Override
    public void run() {

        doHandle();

    }


    public synchronized void doHandle(){
        String excelPath = "";

        int hash = this.fileName.hashCode();
        String hashFlag = String.valueOf(hash % 10);

        switch (hashFlag){
            case "0" : excelPath = "D:\\dev\\20180731\\txt\\txt1.txt";break;
            case "1" : excelPath = "D:\\dev\\20180731\\txt\\txt2.txt";break;
            case "2" : excelPath = "D:\\dev\\20180731\\txt\\txt3.txt";break;
            case "3" : excelPath = "D:\\dev\\20180731\\txt\\txt4.txt";break;
            case "4" : excelPath = "D:\\dev\\20180731\\txt\\txt5.txt";break;
            case "5" : excelPath = "D:\\dev\\20180731\\txt\\txt6.txt";break;
            case "6" : excelPath = "D:\\dev\\20180731\\txt\\txt7.txt";break;
            case "7" : excelPath = "D:\\dev\\20180731\\txt\\txt8.txt";break;
            case "8" : excelPath = "D:\\dev\\20180731\\txt\\txt9.txt";break;
            case "9" : excelPath = "D:\\dev\\20180731\\txt\\txt10.txt";break;
        }


        File file = new File(fileDir + this.fileName);

        PDDocument document = null;
        try {
            document = PDDocument.load(file);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // 获取页码
        int pages = document.getNumberOfPages();

        // 读文本内容
        PDFTextStripper stripper= null;
        try {
            stripper = new PDFTextStripper();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // 设置按顺序输出
        stripper.setSortByPosition(true);
        stripper.setStartPage(1);
        stripper.setEndPage(pages);
        String content = null;
        try {
            content = stripper.getText(document);
        } catch (IOException e) {
            e.printStackTrace();
        }
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

            list.add(email);
            System.out.println("----------------------" + list.size());

            //存文件

            File f = new File(excelPath);

            try {
                if (f.exists()) {
                    System.out.print("文件存在");
                } else {
                    System.out.print("文件不存在=========================================");
                    return;
                }

                BufferedWriter output = new BufferedWriter(new FileWriter(f,true));

                output.write(email);
                output.write("\r\n");//换行

                output.flush();
                output.close();


            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }



}
