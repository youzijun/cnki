package com.cnki;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import java.io.*;
import java.util.Scanner;
import java.io.IOException;



/**
 * Created by ZJX-BJ-01-00057 on 2018/8/7.
 */
public class ParsePdfForEmail {


    public static void main(String[] args) throws IOException {

        String fileDir = "";
        Scanner sc = new Scanner(System.in);
        System.out.println("请粘贴需要解析的pdf文件夹路径:");
        fileDir = sc.next();

        //创建文件
        String filePath = fileDir + "\\Email.txt";
        File textFile = new File(filePath);
        if(!textFile.exists()){
            textFile.createNewFile();
        }


        File file1 = new File(fileDir);
        File[] files = file1.listFiles();// 获取目录下的所有文件或文件夹
        if (files == null) {// 如果目录为空，直接退出
            return;
        }

        for(File f : files){

            if(!f.getPath().contains(".caj") && !f.getPath().contains(".pdf") && !f.getPath().contains(".txt")){
                File file = new File(f.getPath());
                File[] files1 = file.listFiles();
                if(files1.length > 0){
                    f = files1[0];
                }else{
                    continue;
                }
            }


            PDDocument document = null;
            try {
                document = PDDocument.load(f);
            } catch (IOException e) {
                e.printStackTrace();
                continue;
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
            if(content.contains("通讯作者")){
                String email;
                email = content.substring(content.indexOf("通讯作者"), content.indexOf("通讯作者")+100);
                if(email.contains("cn")){
                    email = email.substring(email.indexOf("通讯作者"), email.indexOf("cn")) + "cn";
                }else if(email.contains("net")){
                    email = email.substring(email.indexOf("通讯作者"), email.indexOf("net")) + "net";
                }else if(email.contains("com")){
                    email = email.substring(email.indexOf("通讯作者"), email.indexOf("com")) + "com";
                }

                email = email.replaceAll(" ", "");
                email = email.replaceAll("．", ".");

                //存文件
                File f1 = new File(filePath);

                try {
                    if (f1.exists()) {
                        System.out.print("文件存在");
                    } else {
                        System.out.print("文件不存在=========================================");
                        return;
                    }

                    BufferedWriter output = new BufferedWriter(new FileWriter(f1,true));

                    output.write(email);
                    output.write("\r\n");//换行

                    output.flush();
                    output.close();
                    System.out.print("==============邮箱写入成功：" + email);
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }

    }

}
