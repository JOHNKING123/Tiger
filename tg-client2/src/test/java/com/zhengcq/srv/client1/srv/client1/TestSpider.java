package com.zhengcq.srv.client1.srv.client1;

import lombok.Data;
import org.apache.commons.io.FileUtils;
import org.apache.http.Header;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.junit.Test;
import us.codecraft.webmagic.Spider;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.*;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;


/**
 * @ClassName: TestSpider
 * @Description: todo
 * @Company: 广州市两棵树网络科技有限公司
 * @Author: zhengcq
 * @Date: 2020/7/29
 */
public class TestSpider {

    public static Set<String> targetUrlSet = new HashSet<>();

     public static Queue<String> targetUrlQueue = new LinkedList();

    public static Queue<String> dirUrlQueue = new LinkedList();

    public static Set<String> dirUrlSet = new HashSet<>();

    public static Queue<FileUrl> fileUrlQueue = new LinkedList<>();

    @Data
    public static class FileUrl {
        private String url;

        private String name;

        private String content;
    }

    @Test
    public void testSpiderKgBook() {
        String urlTmp = "https://kgbook.com/";
        dirUrlSet.add(urlTmp);
        dirUrlQueue.add(urlTmp);
        dirUrlSet.add("https://kgbook.com/list/");
        dirUrlQueue.add("https://kgbook.com/list/");
        ThreadPoolExecutor poolExecutor = new ThreadPoolExecutor(10, Integer.MAX_VALUE, 10,
                TimeUnit.SECONDS, new SynchronousQueue<Runnable>());
        ThreadPoolExecutor filePoolExecutor =  new ThreadPoolExecutor(10, Integer.MAX_VALUE, 10,
                TimeUnit.SECONDS, new SynchronousQueue<Runnable>());
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                int i = 0;
                while (true) {
                    try {
                        while (!fileUrlQueue.isEmpty()) {
                            FileUrl fileUrl = fileUrlQueue.remove();
                            int j = i++;
                            filePoolExecutor.execute(() -> {
                                try {
                                    System.out.println("deal file " + j);
                                    saveToFile(fileUrl, "E:\\ebook\\");
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            });
                        }
                    } catch (Exception e) {
                       System.out.println("deal file error");
                    }

                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        thread.start();
        while (!dirUrlQueue.isEmpty() || !targetUrlQueue.isEmpty()) {
            if (dirUrlQueue.isEmpty()) {
                System.out.println("dirUrlQueue is empty");
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                continue;
            }
            String url = "";
            try {
                  synchronized (dirUrlQueue) {
                           url = dirUrlQueue.remove();
                  }
            } catch (Exception e) {
                System.out.println("dir remove ex: size:" + dirUrlQueue.size());
                continue;
            }

            Spider.create(new TuiliwSpiderProcessor()).addUrl(url).thread(1).run();

            if (!targetUrlQueue.isEmpty()) {
                while (!targetUrlQueue.isEmpty()) {
                    String tartUrl =  targetUrlQueue.remove();
                    poolExecutor.execute(() ->{
                           System.out.println(tartUrl);
                           Spider.create(new TuiliwSpiderProcessor()).addUrl(tartUrl).thread(1).run();
                    });
                }
            }
        }
        while (!fileUrlQueue.isEmpty() || filePoolExecutor.getTaskCount() != 0) {
            System.out.println("fileUrl dealing fileUrlQueue:" + fileUrlQueue.size() + ",   filePool:" + filePoolExecutor.getTaskCount());
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }

    @Test
    public void testSpiderAiBook() {
        String urlTmp = "https://www.aibooks.cc/";
        dirUrlSet.add(urlTmp);
        dirUrlQueue.add(urlTmp);
        ThreadPoolExecutor poolExecutor = new ThreadPoolExecutor(10, Integer.MAX_VALUE, 10,
                TimeUnit.SECONDS, new SynchronousQueue<Runnable>());
        ThreadPoolExecutor filePoolExecutor =  new ThreadPoolExecutor(10, Integer.MAX_VALUE, 10,
                TimeUnit.SECONDS, new SynchronousQueue<Runnable>());
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                int i = 0;
                while (true) {
                    try {
                        while (!fileUrlQueue.isEmpty()) {
                            FileUrl fileUrl = fileUrlQueue.remove();
                            int j = i++;
                            filePoolExecutor.execute(() -> {
                                try {
                                    System.out.println("deal file " + j);
                                    saveAibookContent(fileUrl, "F:\\ebook\\");
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            });
                        }
                    } catch (Exception e) {
                        System.out.println("deal file error");
                    }

                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        thread.start();
        while (!dirUrlQueue.isEmpty() || !targetUrlQueue.isEmpty()) {
            if (dirUrlQueue.isEmpty()) {
                System.out.println("dirUrlQueue is empty");
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                continue;
            }
            String url = "";
            try {
                synchronized (dirUrlQueue) {
                    url = dirUrlQueue.remove();
                }
            } catch (Exception e) {
                System.out.println("dir remove ex: size:" + dirUrlQueue.size());
                continue;
            }

            Spider.create(new AibooksSpiderProcessor()).addUrl(url).thread(1).run();

            if (!targetUrlQueue.isEmpty()) {
                while (!targetUrlQueue.isEmpty()) {
                    String tartUrl =  targetUrlQueue.remove();
                    poolExecutor.execute(() ->{
                        System.out.println(tartUrl);
                        Spider.create(new AibooksSpiderProcessor()).addUrl(tartUrl).thread(1).run();
                    });
                }
            }
        }
        while (!fileUrlQueue.isEmpty() || filePoolExecutor.getTaskCount() != 0) {
            System.out.println("fileUrl dealing fileUrlQueue:" + fileUrlQueue.size() + ",   filePool:" + filePoolExecutor.getTaskCount());
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }


    @Test
    public void testSpiderQinkanBook() {
        String urlTmp = "https://www.qinkan.net/";
        dirUrlSet.add(urlTmp);
        dirUrlQueue.add(urlTmp);
        ThreadPoolExecutor poolExecutor = new ThreadPoolExecutor(10, Integer.MAX_VALUE, 10,
                TimeUnit.SECONDS, new SynchronousQueue<Runnable>());
        ThreadPoolExecutor filePoolExecutor =  new ThreadPoolExecutor(10, Integer.MAX_VALUE, 10,
                TimeUnit.SECONDS, new SynchronousQueue<Runnable>());
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                int i = 0;
                while (true) {
                    try {
                        while (!fileUrlQueue.isEmpty()) {
                            FileUrl fileUrl = fileUrlQueue.remove();
                            int j = i++;
                            filePoolExecutor.execute(() -> {
                                try {
                                    System.out.println("deal file " + j);
                                    saveToFileV1(fileUrl, "E:\\ebook\\qinkan\\");
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            });
                        }
                    } catch (Exception e) {
                        System.out.println("deal file error");
                    }

                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        thread.start();
        while (!dirUrlQueue.isEmpty() || !targetUrlQueue.isEmpty()) {
            if (dirUrlQueue.isEmpty()) {
                System.out.println("dirUrlQueue is empty");
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                continue;
            }
            String url = "";
            try {
                synchronized (dirUrlQueue) {
                    url = dirUrlQueue.remove();
                }
            } catch (Exception e) {
                System.out.println("dir remove ex: size:" + dirUrlQueue.size());
                continue;
            }

            Spider.create(new QinkanSpiderProcessor()).addUrl(url).thread(1).run();

            if (!targetUrlQueue.isEmpty()) {
                while (!targetUrlQueue.isEmpty()) {
                    String tartUrl =  targetUrlQueue.remove();
                    poolExecutor.execute(() ->{
                        System.out.println(tartUrl);
                        Spider.create(new QinkanSpiderProcessor()).addUrl(tartUrl).thread(1).run();
                    });
                }
            }
        }
        while (!fileUrlQueue.isEmpty() || filePoolExecutor.getTaskCount() != 0) {
            System.out.println("fileUrl dealing fileUrlQueue:" + fileUrlQueue.size() + ",   filePool:" + filePoolExecutor.getTaskCount());
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }


    @Test
    public void testSpiderCaimogeBook() {
        String urlTmp = "https://www.qinkan.net/";
        dirUrlSet.add(urlTmp);
        dirUrlQueue.add(urlTmp);
        ThreadPoolExecutor poolExecutor = new ThreadPoolExecutor(10, Integer.MAX_VALUE, 10,
                TimeUnit.SECONDS, new SynchronousQueue<Runnable>());
        ThreadPoolExecutor filePoolExecutor =  new ThreadPoolExecutor(10, Integer.MAX_VALUE, 10,
                TimeUnit.SECONDS, new SynchronousQueue<Runnable>());
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                int i = 0;
                while (true) {
                    try {
                        while (!fileUrlQueue.isEmpty()) {
                            FileUrl fileUrl = fileUrlQueue.remove();
                            int j = i++;
                            filePoolExecutor.execute(() -> {
                                try {
                                    System.out.println("deal file " + j);
                                    saveToFileV2(fileUrl, "E:\\ebook\\caimoge\\");
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            });
                        }
                    } catch (Exception e) {
                        System.out.println("deal file error");
                    }

                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        for (int i = 16211; i<= 58710;i++) {
            String tartUrl =  "https://www.caimoge.net/down/all/" + i ;

            FileUrl fileUrl = new FileUrl();
            fileUrl.setUrl(tartUrl);
            try {
                saveToFileV2(fileUrl, "F:\\ebook\\caimoge\\");
            } catch (IOException e) {
                e.printStackTrace();
            }
//            fileUrlQueue.add(fileUrl);
        }
//        thread.start();
//        while (!fileUrlQueue.isEmpty() || filePoolExecutor.getTaskCount() != 0) {
//            System.out.println("fileUrl dealing fileUrlQueue:" + fileUrlQueue.size() + ",   filePool:" + filePoolExecutor.getTaskCount());
//            try {
//                Thread.sleep(3000);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        }

    }

    @Test
    public void testDownLoadFile() {
        String url = "https://www.caimoge.net/down/all/55763";
//        String url = "https://kgbook.com/d/file/202001/(NEW)%E7%88%B1%E5%BE%B7%E5%8D%8E%E4%B8%80%E4%B8%96%EF%BC%9A%E4%BC%9F%E5%A4%A7%E8%80%8C%E4%BB%A4%E4%BA%BA%E6%88%98%E6%A0%97%E7%9A%84%E5%9B%BD%E7%8E%8B%E5%92%8C%E4%BB%96%E9%94%BB%E9%80%A0%E7%9A%84%E4%B8%8D%E5%88%97%E9%A2%A0_20200130958.epub";

//        downloadFromUrl(url, "E:\\ebook");
        try {
            FileUrl fileUrl = new FileUrl();
            fileUrl.setUrl(url);
            fileUrl.setName("万界之我是演员");
            saveToFileV2(fileUrl, "F:\\");
//            testUrl(url);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String downloadFromUrl(String url,String dir) {

        try {
            URL httpurl = new URL(url);
            String fileName = getFileNameFromUrl(url);
            System.out.println(fileName);
            File f = new File(dir + fileName);
            FileUtils.copyURLToFile(httpurl, f);
        } catch (Exception e) {
            e.printStackTrace();
            return "Fault!";
        }
        return "Successful!";
    }

    public static String getFileNameFromUrl(String url){
        String name = new Long(System.currentTimeMillis()).toString() + ".X";
        int index = url.lastIndexOf("/");
        if(index > 0){
            name = url.substring(index + 1);
            if(name.trim().length()>0){
                return name;
            }
        }
        return name;
    }

    public static void saveToFile(FileUrl fileUrl, String fileName) throws IOException {
        String destUrl = fileUrl.getUrl();

        FileOutputStream fos = null;
        BufferedInputStream bis = null;
        HttpURLConnection httpUrl = null;
        URL url = null;
        byte[] buf = new byte[1024*1024];
        int size = 0;

        url = new URL(destUrl);
        httpUrl = (HttpURLConnection) url.openConnection();
        httpUrl.setRequestProperty("accept-encoding", "gzip, deflate, br");
        httpUrl.setRequestProperty("accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9");
        httpUrl.setRequestProperty("user-agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/84.0.4147.89 Safari/537.36");
        httpUrl.connect();
        try {
            httpUrl.getInputStream();
        } catch (Exception e) {
            System.out.println("ex");
        }


        String fileUrlStr =  convertUrl(httpUrl.getURL().getPath());
        System.out.println(fileUrlStr);
        String tmpFileName = getFileName(fileUrlStr);
        boolean flag =  Pattern.matches("[a-zA-Z0-9_ ]+.[a-zA-Z0-9 ]+$", tmpFileName);
        if (flag && fileUrl.getName() != null && !fileUrl.getName().equals("")) {
          String[] tmps = tmpFileName.split("[.]");
          tmpFileName = fileUrl.getName() + "." + tmps[1];
        }
        fileName = fileName + getFileName(tmpFileName);
        System.out.println(getFileName(fileUrlStr));
        url = new URL(fileUrlStr);
        httpUrl = (HttpURLConnection) url.openConnection();
        httpUrl.connect();  
        bis = new BufferedInputStream(httpUrl.getInputStream());
        fos = new FileOutputStream(fileName);
        int i = 1;
        while ((size = bis.read(buf)) != -1) {
            fos.write(buf, 0, size);
            System.out.println(i++ + "  size:" + size);
        }

        fos.close();
        bis.close();
        httpUrl.disconnect();
    }

    public static void saveToFileV2(FileUrl fileUrl, String fileName) throws IOException {
        String destUrl = fileUrl.getUrl();

        FileOutputStream fos = null;
        BufferedInputStream bis = null;
        HttpURLConnection httpUrl = null;
        URL url = null;
        byte[] buf = new byte[1024*1024];
        int size = 0;

        url = new URL(destUrl);
        httpUrl = (HttpURLConnection) url.openConnection();
//        httpUrl.setRequestProperty("accept-encoding", "gzip, deflate, br");
//        httpUrl.setRequestProperty("accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9");
//        httpUrl.setRequestProperty("user-agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/84.0.4147.89 Safari/537.36");
        System.out.println("connect start");
        httpUrl.connect();
        System.out.println("connect end");
        InputStream inputStream = null;
        try {
            inputStream =  httpUrl.getInputStream();
        } catch (Exception e) {
            System.out.println("ex");
        }


        String fileUrlStr =  convertUrl(httpUrl.getURL().getPath());
        System.out.println(fileUrlStr);
        String tmpFileName = "";
        String[] tmps = URLDecoder.decode(fileUrlStr).split("/");
        tmpFileName =  tmps[tmps.length-1];
        fileName = fileName + tmpFileName;
//        System.out.println(getFileName(fileUrlStr));
//        url = new URL(fileUrlStr);
//        httpUrl = (HttpURLConnection) url.openConnection();
//        httpUrl.connect();
        bis = new BufferedInputStream(inputStream);
        fos = new FileOutputStream(fileName);
        int i = 1;
        while ((size = bis.read(buf)) != -1) {
            fos.write(buf, 0, size);
            System.out.println(i++ + "  size:" + size);
        }

        fos.close();
        bis.close();
        httpUrl.disconnect();
    }


    public static void saveToFileV1(FileUrl fileUrl, String fileName) throws IOException {
        String destUrl = getQinKanUrl(fileUrl.getUrl());

        FileOutputStream fos = null;
        BufferedInputStream bis = null;
        HttpURLConnection httpUrl = null;
        URL url = null;
        byte[] buf = new byte[1024*1024];
        int size = 0;

        url = new URL(destUrl);
        httpUrl = (HttpURLConnection) url.openConnection();
//        httpUrl.setRequestProperty("accept-encoding", "gzip, deflate, br");
//        httpUrl.setRequestProperty("accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9");
//        httpUrl.setRequestProperty("user-agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/84.0.4147.89 Safari/537.36");
        httpUrl.connect();
//        try {
//            httpUrl.getInputStream();
//        } catch (Exception e) {
//            System.out.println("ex");
//        }


        String fileUrlStr =  fileUrl.getUrl();
        String tmpFileName = "";
        String[] tmps = fileUrlStr.split("[.]");
        tmpFileName = fileUrl.getName() + "." + tmps[tmps.length-1];
        fileName = fileName + tmpFileName;
//        System.out.println(getFileName(fileUrlStr));
//        url = new URL(fileUrlStr);
//        httpUrl = (HttpURLConnection) url.openConnection();
//        httpUrl.connect();
        bis = new BufferedInputStream(httpUrl.getInputStream());
        fos = new FileOutputStream(fileName);
        int i = 1;
        while ((size = bis.read(buf)) != -1) {
            fos.write(buf, 0, size);
            System.out.println(i++ + "  size:" + size);
        }

        fos.close();
        bis.close();
        httpUrl.disconnect();
    }

    private RequestConfig createConfig(int timeout, boolean redirectsEnabled)
    {
        return RequestConfig.custom()
            .setSocketTimeout(timeout)
            .setConnectTimeout(timeout)
            .setConnectionRequestTimeout(timeout)
            .setRedirectsEnabled(redirectsEnabled)
            .build();
    }
    public void testUrl(String url)
    {
        CloseableHttpClient client = HttpClients.createDefault();
        try {
            HttpGet httpGet = new HttpGet(url);
            httpGet.setConfig(createConfig(5000, false));    
            CloseableHttpResponse response = client.execute(httpGet);
            try    
            {      
                Header h = response.getFirstHeader("Location");
                if(h!=null)      
                {         
                    System.out.println("重定向地址："+h.getValue());
                    testUrl(h.getValue());
                    System.out.println(convertUrl(h.getValue()));
                }    
            }    
            finally
                {
                    response.close();
                }
         } catch (Exception e) {
            e.printStackTrace();
        }
        finally
        {
            try {
                client.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
   }


   @Test
   public void testNum() {
        char tmp1 = 229;
        System.out.println(tmp1);
        long tmp2 = tmp1;
        System.out.println(Long.toHexString(tmp2).toUpperCase());
   }

   private static String convertUrl(String url) {
        char[] chars = url.toCharArray();
        String tmp = "";
        for (char charTmp : chars) {
            if (charTmp <= 120) {
                tmp += charTmp;
                continue;
            }
            long tmp2 = charTmp;
            tmp += "%" + Long.toHexString(tmp2).toUpperCase();
        }
        return "https://kgbook.com" + tmp;
   }

   private static String getFileName(String url) throws UnsupportedEncodingException {
        url = URLDecoder.decode(url, "utf8");
        String[] tmps = url.split("/");
        return tmps[tmps.length -1];
   }

    private static String getQinKanUrl(String url) throws UnsupportedEncodingException {
        String[] tmps = url.split("/");
        String str = "";
        for (int i= 0 ;i< tmps.length ; i++) {
           if (i == tmps.length - 1) {
               str += URLEncoder.encode(tmps[i]);
           } else {
               str += tmps[i] + "/" ;
           }
        }
        return str;
    }

   public void saveAibookContent(FileUrl fileUrl, String fileName) {
       try {
           fileName = fileName + fileUrl.getName() + ".txt";
           FileOutputStream fos = new FileOutputStream(fileName);
           fos.write(fileUrl.getContent().getBytes());
           fos.close();
       } catch (Exception e) {
           e.printStackTrace();
       }
   }

   @Test
   public void testTextFile() {
       try {
           FileOutputStream fos = new FileOutputStream("E:\\ebook\\1.txt");
           String str = "www.baidu.com";
           fos.write(str.getBytes());
           fos.write("\n".getBytes());
           fos.write(str.getBytes());
           fos.close();
       } catch (Exception e) {
           e.printStackTrace();
       }
   }
}
