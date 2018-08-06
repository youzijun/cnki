package com.cnki.util;

import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Created by ZJX-BJ-01-00057 on 2018/7/31.
 */
public class ResultUrl {
    private static final Logger logger = LoggerFactory.getLogger(Map.class);

    public static List<ParseRes> urlList = Collections.synchronizedList(new ArrayList<>());

    public static int pageTotal = 0;

    public static void main(String[] args) throws SessionException, IOException {

        Session session = new SessionBuilder().build();

        String journalource = "中国药师";
        String yearStart = "2014";
        String yearEnd = "2018";

        new ResultUrl().doQuery(session, journalource, yearStart, yearEnd);
    }

    public static String login(Session session) throws IOException, SessionException {
        //第一次登录
        final String url10 = "http://login.cnki.net/TopLogin/api/loginapi/IpLogin?callback=jQuery11130630452901371479_1533116955765&isAutoLogin=false&checkCode=&isForceLogin=true&_=1533116955772";
        HttpResponse response1 = session.next(url10).get(5);
        String str = EntityUtils.toString(response1.getEntity());

        String loginRes = "";
        if (str.contains("Msg")) {
            String msg = str.substring(str.indexOf("Msg") + 3);
            loginRes = msg.substring(0, msg.indexOf(","));
        }
        logger.info("登录结果: {}", loginRes);

        logger.info("第一次登陆打印" + str);

        String uid = str.substring(str.indexOf("Uid")+6, str.indexOf("UserName")-3);

        logger.info("第一次登陆打印uid" + uid);

        System.out.println(uid);

        //第二次登录
        final String url11 = "http://kns.cnki.net/kns/Loginid.aspx";
        HttpResponse response2 = session.next(url11)
                .addFormParam("uid", uid)
                .post(5);
        String s = EntityUtils.toString(response2.getEntity());

        if (!str.contains("{")) {
            logger.error("登录失败！");
            return "";
        }
        String callBack = str.substring(0, str.indexOf("{") - 1);
        return callBack + "^" + uid;
    }

    public static String loginOut(Session session, String callback) throws IOException, SessionException {

        String url = "http://login.cnki.net/TopLogin/api/loginapi/Logout" +
                "?callback=" + callback +
                "&_=" + System.currentTimeMillis();
        HttpResponse httpResponse = session.next(url).get(5);

        String str = EntityUtils.toString(httpResponse.getEntity());
        String loginOutRes = "";
        if (str.contains("Msg")) {
            String msg = str.substring(str.indexOf("Msg") + 3);
            loginOutRes = msg.substring(0, msg.indexOf(","));
        }

        logger.info("登出结果: {}", loginOutRes);

//        HttpResponse httpResponse1 = session.next("http://kns.cnki.net/kns/Logout.aspx?q=-1")
//                .post(1);

//        session.next("http://kns.cnki.net/kns/")
//                .get(1);

        return loginOutRes;
    }


    public static void doQuery(Session session, String magazineValue, String yaerFrom, String yearTo) throws SessionException, IOException {

        //请求查询参数
        final String url1 = "http://kns.cnki.net/kns/request/SearchHandler.ashx?action=&NaviCode=*&ua=1.21&PageName=ASP.brief_result_aspx&DbPrefix=CJFQ&DbCatalog=%e4%b8%ad%e5%9b%bd%e5%ad%a6%e6%9c%af%e6%9c%9f%e5%88%8a%e7%bd%91%e7%bb%9c%e5%87%ba%e7%89%88%e6%80%bb%e5%ba%93&ConfigFile=CJFQ.xml&db_opt=CJFQ&db_value=%E4%B8%AD%E5%9B%BD%E5%AD%A6%E6%9C%AF%E6%9C%9F%E5%88%8A%E7%BD%91%E7%BB%9C%E5%87%BA%E7%89%88%E6%80%BB%E5%BA%93&magazine_value1="+magazineValue+"&magazine_special1=%25&year_from="+yaerFrom+"&year_to="+yearTo+"&year_type=echar&his=0&__=Wed%20Aug%2001%202018%2017%3A07%3A34%20GMT%2B0800%20(%E4%B8%AD%E5%9B%BD%E6%A0%87%E5%87%86%E6%97%B6%E9%97%B4)";

        HttpResponse response = session.next(url1).get(5);
        EntityUtils.toString(response.getEntity());


        //请求第一页数据
        final String url2 = "http://kns.cnki.net/kns/brief/brief.aspx?pagename=ASP.brief_result_aspx&dbPrefix=CJFQ&dbCatalog=%e4%b8%ad%e5%9b%bd%e5%ad%a6%e6%9c%af%e6%9c%9f%e5%88%8a%e7%bd%91%e7%bb%9c%e5%87%ba%e7%89%88%e6%80%bb%e5%ba%93&ConfigFile=CJFQ.xml&research=off&t=1533114454349&keyValue=&S=1";

        response = session.next(url2).post(5);

        String res = EntityUtils.toString(response.getEntity());

        Document document = Jsoup.parse(res);

        //第一页下载链接
        logger.info("---------解析第一页下载链接...");
//        parseDownlodUrl(res);


        //总页数
        String pageNumText = document.select("#J_ORDER > tbody > tr:nth-child(2) > td > table > tbody > tr > td:nth-child(2) > div > span.countPageMark").text();

        if (!pageNumText.contains("/")) {
            logger.error("解析总页数异常, text: {}", pageNumText);
            return;
        }

        pageTotal = Integer.valueOf(pageNumText.substring(pageNumText.indexOf("/") + 1));
        logger.info("总页数:{}", pageTotal);

    }


    public static void doQueryOther(Session session, int i) throws IOException, SessionException {
        logger.info("---------解析第{}页下载链接...", i);
        String url3 = "http://kns.cnki.net/kns/brief/brief.aspx?" +
                "curpage=" + i + "&RecordsPerPage=20&QueryID=4&ID=&turnpage=1&tpagemode=L&dbPrefix=CJFQ&Fields=&DisplayMode=listmode&PageName=ASP.brief_result_aspx";

        HttpResponse httpResponse = session.next(url3).get(5);

        String res = EntityUtils.toString(httpResponse.getEntity());
        parseDownlodUrl(res);
    }


    private static void parseDownlodUrl(String html) {

        Document document = Jsoup.parse(html);

        Elements trs = document.select("#ctl00 > table > tbody > tr:nth-child(2) > td > table > tbody > tr");

        trs.stream()
                .filter(tr -> !tr.attr("bgcolor").equals(""))
                .map(tr -> {

                    String text = tr.select(" td:nth-child(2) > a").text();
                    String href = tr.select(" td:nth-child(7) > a").attr("href");
                    String htmlUrl = tr.select(" td:nth-child(8) > a").attr("href");    //html阅读 链接
                    href = "http://nvsm.cnki.net/KNS" +href.substring(2, href.length());
                    htmlUrl = "http:/" + htmlUrl;
                    return new ParseRes(text, href, htmlUrl);

                }).forEach(res -> {
            urlList.add(res);   //拼接完整下载路径
        });
    }
}

