package com.cnki.test;

import com.cnki.util.MyTask;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by ZJX-BJ-01-00057 on 2018/7/31.
 */
public class ThreadMain {


    public static LinkedBlockingDeque blockingDeque = new LinkedBlockingDeque<>(); //无界队列

    public static void main(String[] args) throws InterruptedException, IOException {

//        //创建线程池，用于解析pdf
//        ThreadPoolExecutor pool = new ThreadPoolExecutor(
//                5,
//                10,
//                120,
//                TimeUnit.SECONDS,
//                blockingDeque //无界队列
//        );
//
//
//        String fileDir = "D:\\dev\\20180731\\pdf\\";
//
//        File file = new File(fileDir);
//        File[] files = file.listFiles();// 获取目录下的所有文件或文件夹
//        if (files == null) {// 如果目录为空，直接退出
//            return;
//        }
//
//        for(File f : files){
//            String fileName = f.getName();
////            fileName = fileName.substring(0, fileName.lastIndexOf("."));
//            blockingDeque.add(new MyTask(fileName));
//        }
//
//        while(blockingDeque.size() > 0){
//            MyTask task = (MyTask) blockingDeque.removeFirst();
//            pool.execute(task);
//        }


//        String journalource = "@@";
//        String yearStart = "@@";
//        String yearEnd = "@@";
//
//        //接收键盘输入
//        Scanner sc = new Scanner(System.in);
//        System.out.println("请输入期刊名称：");
//        journalource = sc.next();
//        System.out.println("请输入开始年限：");
//        yearStart = sc.next();
//        System.out.println("请输入结束年限：");
//        yearEnd = sc.next();
//        System.out.println("你的信息如下：");
//        System.out.println("期刊名称:[" + journalource + "],开始年限:[" + yearStart + "],结束年限:[" + yearEnd + "]");
//        Thread.sleep(1000);
//
//        System.out.println("确认执行请输入 y ，信息有误请关闭此窗口。");
//        String y = sc.next();
//        if("y".equals(y)){
//            System.out.println("5s后开始执行");
//            Thread.sleep(1000);
//            System.out.println("4s");
//            Thread.sleep(1000);
//            System.out.println("3s");
//            Thread.sleep(1000);
//            System.out.println("2s");
//            Thread.sleep(1000);
//            System.out.println("1s");
//            Thread.sleep(1000);
//        }else{
//            return;
//        }

//        File file = new File("D:\\1234567\\email.txt");
//        if(!file.exists()){
//            System.out.println("1");
//            file.getParentFile().mkdirs();
//        }
//
//        file.createNewFile();


        String str = "jQuery11130630452901371479_1533116955765({\"IsSuccess\":true,\"Msg\":\"登录成功\",\"ErrorCode\":1,\"ErrorMsg\":null,\"Uid\":\"WEEvREcwSlJHSldRa1Fhb09jSnZnU3o5dFI0bk02NWg3VDhUSWh2a0Jodz0=$9A4hF_YAuvQ5obgVAqNKPCYcEjKensW4IQMovwHtwkF4VYPoHbKxJw!!\",\"UserName\":\"sh0266\",\"ShowName\":\"厦门大学图书馆\",\"UserType\":\"bk\",\"IsAutoLogin\":false,\"IsShowCheck\":false,\"r\":\"ZmDdjH\"})";

        System.out.println(str.substring(str.indexOf("Uid")+6, str.indexOf("UserName")-3));



    }


}
