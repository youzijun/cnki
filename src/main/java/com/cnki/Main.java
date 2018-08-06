
package com.cnki;

import com.cnki.run.SingleThreadMain;
import com.cnki.util.*;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;

/**
 * Created by ZJX-BJ-01-00057 on 2018/7/31.
 */
public class Main {

    private static final Logger logger = LoggerFactory.getLogger(Main.class);
    private static final String filePath = "D:\\dev\\pdf\\%s";

    public static void main(String[] args) throws SessionException, IOException, InterruptedException {
        String journalource = "临床生物化学与检验学分册";
        String yearStart = "2004";
        String yearEnd = "2018";
        doRun(journalource, yearStart, yearEnd);


//        sysInput();


    }

    private static void sysInput() throws InterruptedException, IOException, SessionException {
        String journalource = "@@";
        String yearStart = "@@";
        String yearEnd = "@@";
        String errorPage = "1";

        //接收键盘输入
        Scanner sc = new Scanner(System.in);
        System.out.println("请输入期刊名称：");
        journalource = sc.next();
        System.out.println("请输入开始年限：");
        yearStart = sc.next();
        System.out.println("请输入结束年限：");
        yearEnd = sc.next();
        System.out.println("请输入开始下载的页数：");
        errorPage = sc.next();
        System.out.println("你的信息如下：");
        System.out.println("期刊名称:[" + journalource + "],开始年限:[" + yearStart + "],结束年限:[" + yearEnd + "]"
                            +"开始下载的页数为：" + errorPage);
        Thread.sleep(1000);

        System.out.println("确认执行请输入 y ，信息有误请关闭此窗口。");
        String y = sc.next();
        if("y".equals(y)){
            System.out.println("5s后开始执行");
            Thread.sleep(1000);
            System.out.println("4s");
            Thread.sleep(1000);
            System.out.println("3s");
            Thread.sleep(1000);
            System.out.println("2s");
            Thread.sleep(1000);
            System.out.println("1s");
            Thread.sleep(1000);
        }else{
            return;
        }

        doRun(journalource, yearStart, yearEnd);
    }

    private static void  doRun(String journalource, String yearStart, String yearEnd) throws SessionException, IOException, InterruptedException {
        Session session = new SessionBuilder().build();

        String str = ResultUrl.login(session);

        String[] arr = str.split("\\^");

        String uid = arr[1];
        String loginCallback = arr[0];

        ResultUrl.doQuery(session, journalource, yearStart, yearEnd);   //20条

        for(int i = 22; i < ResultUrl.pageTotal; i++){

            logger.info("============================正在解析第{}页", i);

            ResultUrl.doQueryOther(session, i); //20条

            for(int j = 0; j < ResultUrl.urlList.size(); j++){

                ParseRes res = ResultUrl.urlList.get(j);
                String fileName = String.format(filePath, i + res.name + ".caj");
                try {
                    String download = session.download(res.url, fileName);
                    if (null == download) {
                        System.out.println("dowload error! url: " + ResultUrl.urlList.get(j));
                        continue;
                    }
//                    String email = SingleThreadMain.parseEmailFrompdf(download);
//
//                    SingleThreadMain.apeendEmailToFile(email);
                    Thread.sleep(8000);

                }catch (Exception e) {
                    logger.error("error, :{}", e);
                    logger.error("文件：{}", fileName);

                    Thread.sleep(50000);
                    ResultUrl.loginOut(session, loginCallback);
                    ResultUrl.login(session);
                    if (j >= 2) {
                        j--;
                    }
                }

//                try{
//
//                    //请求html阅读页
//                    String email = SingleThreadMain.parseEmailFromHtml(session, j, uid);
//
//                    logger.info("===============解析html返回的邮箱为：{}", email);
//
//                    //存邮箱
//                    SingleThreadMain.apeendEmailToFile(email);
//
//                    if(j % 10 == 1){
//                        ResultUrl.loginOut(session, loginCallback);
//                        ResultUrl.login(session);
//                    }
//
//                }catch (Exception e){
//                    Thread.sleep(15000);
//                    ResultUrl.loginOut(session, loginCallback);
//                    ResultUrl.login(session);
//                    if (j >= 2) {
//                        j--;
//                    }
//                }

            }
            ResultUrl.urlList.clear();
            Thread.sleep(10000);

            //重新登录
//            Thread.sleep(15000);
            if(i%3==0){ //每5页重新登录一次
                ResultUrl.loginOut(session, loginCallback);
                ResultUrl.login(session);
            }

        }
    }
}