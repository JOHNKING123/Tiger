package com.zhengcq.srv.client1.srv.client1;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;

import java.util.List;
import java.util.Random;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * @ClassName: TuiliwSpiderProcessor
 * @Description: todo
 * @Company: 广州市两棵树网络科技有限公司
 * @Author: zhengcq
 * @Date: 2020/7/29
 */
public class Ebook23SpiderProcessor implements PageProcessor {

    /**
     * userAgentArray
     */
    private String[] userAgentArray = {"Mozilla/5.0 (Windows NT 6.1; WOW64; rv:6.0) Gecko/20100101 Firefox/6.0",
            "Mozilla/5.0 (Windows; U; MSIE 9.0; Windows NT 9.0; en-US)", "Opera/9.80 (X11; Linux i686; U; ru) Presto/2.8.131 Version/11.11",
            "Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.2 (KHTML, like Gecko) Chrome/22.0.1216.0 Safari/537.2",
            "Mozilla/5.0 (Windows NT 6.2; Win64; x64; rv:16.0.1) Gecko/20121011 Firefox/16.0.1",
            "Mozilla/5.0 (iPad; CPU OS 6_0 like Mac OS X) AppleWebKit/536.26 (KHTML, like Gecko) Version/6.0 Mobile/10A5355d Safari/8536.25",
            "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.1 (KHTML, like Gecko) Chrome/22.0.1207.1 Safari/537.1",
            "Mozilla/5.0 (X11; CrOS i686 2268.111.0) AppleWebKit/536.11 (KHTML, like Gecko) Chrome/20.0.1132.57 Safari/536.11",
            "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/536.6 (KHTML, like Gecko) Chrome/20.0.1092.0 Safari/536.6",
            "Mozilla/5.0 (Windows NT 6.2) AppleWebKit/536.6 (KHTML, like Gecko) Chrome/20.0.1090.0 Safari/536.6",
            "Mozilla/5.0 (Windows NT 6.2; WOW64) AppleWebKit/537.1 (KHTML, like Gecko) Chrome/19.77.34.5 Safari/537.1",
            "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/536.5 (KHTML, like Gecko) Chrome/19.0.1084.9 Safari/536.5",
            "Mozilla/5.0 (Windows NT 6.0) AppleWebKit/536.5 (KHTML, like Gecko) Chrome/19.0.1084.36 Safari/536.5",
            "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/536.3 (KHTML, like Gecko) Chrome/19.0.1063.0 Safari/536.3",
            "Mozilla/5.0 (Windows NT 5.1) AppleWebKit/536.3 (KHTML, like Gecko) Chrome/19.0.1063.0 Safari/536.3",
            "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_8_0) AppleWebKit/536.3 (KHTML, like Gecko) Chrome/19.0.1063.0 Safari/536.3",
            "Mozilla/5.0 (Windows NT 6.2) AppleWebKit/536.3 (KHTML, like Gecko) Chrome/19.0.1062.0 Safari/536.3",
            "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/536.3 (KHTML, like Gecko) Chrome/19.0.1062.0 Safari/536.3",
            "Mozilla/5.0 (Windows NT 6.2) AppleWebKit/536.3 (KHTML, like Gecko) Chrome/19.0.1061.1 Safari/536.3",
            "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/536.3 (KHTML, like Gecko) Chrome/19.0.1061.1 Safari/536.3",
            "Mozilla/5.0 (Windows NT 6.1) AppleWebKit/536.3 (KHTML, like Gecko) Chrome/19.0.1061.1 Safari/536.3",
            "Mozilla/5.0 (Windows NT 6.2) AppleWebKit/536.3 (KHTML, like Gecko) Chrome/19.0.1061.0 Safari/536.3"
    };

    @Override
    public void process(Page page) {

        String targetUrl = isTargetUrl(page.getUrl().toString());
        if (targetUrl != null && !targetUrl.equals("")) {

            StringBuilder contentBuilder = new StringBuilder();
            contentBuilder.append(targetUrl);
            contentBuilder.append("\n");
            List<String> contents = page.getHtml().xpath("//[@class='article-content']//p/text()")
                    .nodes().stream().map(vo -> vo.toString()).collect(Collectors.toList());
            if (contents != null && contents.size() > 0) {
                for (String tmp : contents) {
                    contentBuilder.append(tmp + "\n");
                }
            }
            contentBuilder.append("\n");
            contents = page.getHtml().xpath("//[@class='article-content']//div/a/text()")
                    .nodes().stream().map(vo -> vo.toString()).collect(Collectors.toList());
            if (contents != null && contents.size() > 0) {
                for (String tmp : contents) {
                    contentBuilder.append(tmp + "\n");
                }
            }
            contentBuilder.append("\n");
            String urlStr = page.getHtml().xpath("//[@class='article-content']//div/a/text()").toString();
            if (urlStr != null  && !urlStr.equals("")) {
                contentBuilder.append(urlStr);
            }
            contents = page.getHtml().xpath("//[@class='article-content']//div/text()")
                    .nodes().stream().map(vo -> vo.toString()).collect(Collectors.toList());
            if (contents != null && contents.size() > 0) {
                for (String tmp : contents) {
                    contentBuilder.append(tmp + "\n");
                }
            }

            contentBuilder.append("\n");

            TestSpider.FileUrl  fileUrlVo = new TestSpider.FileUrl();
            String name = page.getHtml().xpath("//[@class='article-title']/text()").toString();
            fileUrlVo.setName(name);
            fileUrlVo.setContent(contentBuilder.toString());
            TestSpider.fileUrlQueue.add(fileUrlVo);
        } else {
            List<String>  urlList = page.getHtml().xpath("//a/@href").nodes().stream().map(vo -> vo.toString()).collect(Collectors.toList());
            for (String url : urlList) {
                String targetUrlTmp = isTargetUrl(url);
                if (!targetUrlTmp.equals("")) {
                    if (TestSpider.targetUrlSet.contains(targetUrlTmp)) {
                        continue;
                    }
                    TestSpider.targetUrlSet.add(targetUrlTmp);
                    TestSpider.targetUrlQueue.add(targetUrlTmp);
                }
                String dirUrl = dirUrl(url);
                if (!dirUrl.equals("")) {
                    if (TestSpider.dirUrlSet.contains(dirUrl)) {
                        continue;
                    }

                    TestSpider.dirUrlSet.add(dirUrl);
                    synchronized (TestSpider.dirUrlQueue) {
                        TestSpider.dirUrlQueue.add(dirUrl);
                    }

                }
            }
        }

    }

    private String isFileUrl(String url) {
        boolean fileFlag =  Pattern.matches(".*pathid=[0-9]$", url);
        if (fileFlag) {
           return url;
        } else  {
            return  "";
        }
    }

    private String isTargetUrl(String url) {
        // https://www.ebook23.com/thread-2294-1-1.html
        boolean targetFlag =  Pattern.matches(".*/thread(-[\\d]*){3}[.]html$|thread(-[\\d]*){3}[.]html$", url);
        if (targetFlag) {
            if (url.startsWith("http")) {
                return url;
            } else {
                url = "https://www.ebook23.com/" + url;
            }
            return url;
        } else  {
            return  "";
        }
    }

    private String dirUrl(String url) {
        // https://www.ebook23.com/forum/49/1.html
        boolean dirFlag = Pattern.matches("^https://www.ebook23.com/forum/.*$", url);
        if (dirFlag) {
            return url;
        }
        return  "";
    }

    @Override
    public Site getSite() {
        Site site = Site.me();
        site.setCharset("utf8");                           //编码
        site.setRetryTimes(3);                     //重试次数
        site.setSleepTime(1000);                       //休眠时间
        site.setTimeOut(6000);                           //超时时间 600L
        site.setCycleRetryTimes(3);           //设置抓取失败时重试的次数用完后依然未抓取成功时，循环重试

        Random random = new Random();
        Integer index = random.nextInt(userAgentArray.length - 1);
        site.setUserAgent(userAgentArray[index]);

        return site;
    }
}
