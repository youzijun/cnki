package com.cnki.run;

import com.cnki.util.ResultUrl;
import com.cnki.util.Session;
import com.cnki.util.SessionException;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.util.StringUtils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URLDecoder;

/**
 * Created by ZJX-BJ-01-00057 on 2018/7/31.
 */
public class SingleThreadMain {


//    private static final String fileDir = "D:\\dev\\20180731\\pdf\\";
//    private static final String fileDir = "D:\\dev\\pdf\\";

    //    private static final String excelPath = "D:\\dev\\20180731\\txt\\txt.txt";
    private static final String excelPath = "D:\\dev\\email.txt";


    public static String parseEmailFromHtml(Session session, int j, String uid) throws IOException {
        String htmlUrl = ResultUrl.urlList.get(j).htmlUrl;
        htmlUrl = URLDecoder.decode(htmlUrl);

        String dbcode = htmlUrl.substring(htmlUrl.indexOf("dbcode=")+7, htmlUrl.indexOf("&filename"));
        String fileName = htmlUrl.substring(htmlUrl.indexOf("filename=")+9, htmlUrl.length());

        String url = "http://kns.cnki.net/KXReader/Detail?" +
                "dbcode=" + dbcode +
                "&filename=" + fileName + "&uid=" + uid;

        HttpResponse httpResponse = null;
        String content = "";
        try {
            httpResponse = session.next(url).get(5);
            content = EntityUtils.toString(httpResponse.getEntity());
        } catch (Exception e) {
            System.out.println("异常暂不处理");
            e.printStackTrace();
        }


        String email = handlerEmail(StringUtils.isEmpty(content)?"请求失败，邮箱":content);

        return email;
    }


    public static String parseEmailFrompdf(String filePath) throws IOException {

        if (StringUtils.isEmpty(filePath)) {
            return "";
        }
        File f = new File(filePath);

        PDDocument document = null;
        document = PDDocument.load(f);

        // 获取页码
        int pages = document.getNumberOfPages();

        // 读文本内容
        PDFTextStripper stripper = null;
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
        String email = handlerEmail(content);

        return email;
    }

    public static void apeendEmailToFile(String email) {

        //存文件
        File f1 = new File(excelPath);

        try {
            if (f1.exists()) {
                System.out.print("文件存在");
            } else {
                System.out.print("文件不存在=========================================");
                boolean newFile = f1.createNewFile();
                String message = newFile ? "创建成功" : "创建失败";
                System.out.print(message + "=========================================");
                return;
            }

            BufferedWriter output = new BufferedWriter(new FileWriter(f1, true));

            output.write(email);
            output.write("\r\n");//换行

            output.flush();
            output.close();


        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private static String handlerEmail(String content){
        String email = "";
        if(StringUtils.isEmpty(content)){
            return "邮箱为空";
        }
        if (content.contains("mail")) {
            email = content.substring(content.indexOf("mail"), content.indexOf("mail") + 40);
            if (email.contains("com")) {
                email = email.substring(email.indexOf("mail"), email.indexOf("com")) + "com";
            } else if (email.contains("cn")) {
                email = email.substring(email.indexOf("mail"), email.indexOf("cn")) + "cn";
            }

            email = email.replaceAll(" ", "");
            email = email.replaceAll("．", ".");

            if (email.indexOf("mail:") != -1) {
                email = email.substring(email.indexOf("mail:") + 5);
            }
        }
        return email;
    }


//    public static void  (String[] args) throws IOException {
//
//        //创建文件
//        File textFile = new File(excelPath);
//        if(!textFile.exists()){
//            textFile.getParentFile().mkdirs();
//        }
//        textFile.createNewFile();
//
//
//        File file1 = new File(fileDir);
//        File[] files = file1.listFiles();// 获取目录下的所有文件或文件夹
//        if (files == null) {// 如果目录为空，直接退出
//            return;
//        }
//
//        for(File f : files){
//
//            PDDocument document = null;
//            try {
//                document = PDDocument.load(f);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//
//            // 获取页码
//            int pages = document.getNumberOfPages();
//
//            // 读文本内容
//            PDFTextStripper stripper= null;
//            try {
//                stripper = new PDFTextStripper();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//
//            // 设置按顺序输出
//            stripper.setSortByPosition(true);
//            stripper.setStartPage(1);
//            stripper.setEndPage(pages);
//            String content = null;
//            try {
//                content = stripper.getText(document);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//            if(content.contains("mail")){
//                String email;
//                email = content.substring(content.indexOf("mail"), content.indexOf("mail")+40);
//                if(email.contains("com")){
//                    email = email.substring(email.indexOf("mail"), email.indexOf("com")) + "com";
//                }else if(email.contains("cn")){
//                    email = email.substring(email.indexOf("mail"), email.indexOf("cn")) + "cn";
//                }
//
//                email = email.replaceAll(" ", "");
//                email = email.replaceAll("．", ".");
//
//                //存文件
//                File f1 = new File(excelPath);
//
//                try {
//                    if (f1.exists()) {
//                        System.out.print("文件存在");
//                    } else {
//                        System.out.print("文件不存在=========================================");
//                        return;
//                    }
//
//                    BufferedWriter output = new BufferedWriter(new FileWriter(f1,true));
//
//                    output.write(email);
//                    output.write("\r\n");//换行
//
//                    output.flush();
//                    output.close();
//
//
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//
//            }
//        }

//    }

}
