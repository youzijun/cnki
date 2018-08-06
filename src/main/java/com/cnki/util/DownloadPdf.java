package com.cnki.util;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

public class DownloadPdf {


    /**
     * 从网络Url中下载文件
     * @param urlStr
     * @param fileName
     * @param savePath
     * @throws IOException
     */
    public static void  downLoadFromUrl(String urlStr,String fileName,String savePath) throws IOException {
        URL url = new URL(urlStr);
        HttpURLConnection conn = (HttpURLConnection)url.openConnection();
        //设置超时间为3秒
        conn.setConnectTimeout(3*1000);
        //防止屏蔽程序抓取而返回403错误
        conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_12_3) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/56.0.2924.87 Safari/537.36");

        //得到输入流
        InputStream inputStream = conn.getInputStream();
        //获取自己数组
        byte[] getData = readInputStream(inputStream);

        //文件保存位置
        File saveDir = new File(savePath);
        if(!saveDir.exists()){
            saveDir.mkdir();
        }
        File file = new File(saveDir+File.separator+fileName);
        FileOutputStream fos = new FileOutputStream(file);
        fos.write(getData);
        if(fos!=null){
            fos.close();
        }
        if(inputStream!=null){
            inputStream.close();
        }

        System.out.println("info:"+url+" download success");

    }


    /**
     * 从输入流中获取字节数组
     * @param inputStream
     * @return
     * @throws IOException
     */
    public static  byte[] readInputStream(InputStream inputStream) throws IOException {
        byte[] buffer = new byte[1024];
        int len = 0;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        while((len = inputStream.read(buffer)) != -1) {
            bos.write(buffer, 0, len);
        }
        bos.close();
        return bos.toByteArray();
    }


    public static void main(String[] args) {
        try{
            downLoadFromUrl("http://nvsm.cnki.net/KNS/download.aspx?filename=XJjZvIXZ19yN6NlQzYzMFRHRCVEdxokbmpGZJZmTLVlW2olNJJDZhN2NxlmaqtiUvczKxk2ckhUVipVSjhkYFdTV6hDe0UjYxUET08Scn5GMwR2NYhEb65ESpRmTBpHahhXcK9GdJNTN6R0Sjh3bx8yb5tWYwEUU&tablename=CJFD2004",
                    "百度2.pdf","D:\\dev\\下载pdf");
        }catch (Exception e) {
            // TODO: handle exception
        }
    }



}
