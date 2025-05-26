/**
 * 广州市两棵树网络科技有限公司版权所有
 * DT Group Technology & commerce Co., LtdAll rights reserved.
 * <p>
 * 广州市两棵树网络科技有限公司，创立于2009年。旗下运营品牌洋葱小姐。
 * 洋葱小姐（Ms.Onion） 下属三大业务模块 [洋葱海外仓] , [洋葱DSP] , [洋葱海外聚合供应链]
 * [洋葱海外仓]（DFS）系中国海关批准的跨境电商自营平台(Cross-border ecommerce platform)，
 * 合法持有海外直邮保税模式的跨境电商营运资格。是渠道拓展，平台营运，渠道营运管理，及客户服务等前端业务模块。
 * [洋葱DSP]（DSP）系拥有1.3亿消费者大数据分析模型。 是基于客户的消费行为，消费轨迹，及多维度云算法(MDPP)
 * 沉淀而成的精准消费者模型。洋葱DSP能同时为超过36种各行业店铺 及200万个销售端口
 * 进行多店铺高精度配货，并能预判消费者购物需求进行精准推送。同时为洋葱供应链提供更前瞻的商品采买需求模型 。
 * [洋葱海外聚合供应链]（Super Supply Chain）由中国最大的进口贸易集团共同
 * 合资成立，拥有20余年的海外供应链营运经验。并已入股多家海外贸易企业，与欧美澳等9家顶级全球供应商达成战略合作伙伴关系。
 * 目前拥有835个国际品牌直接采买权，12万个单品的商品供应库。并已建设6大海外直邮仓库，为国内客户提供海外商品采买集货供应，
 * 跨境 物流，保税清关三合一的一体化模型。目前是中国唯一多模式聚合的海外商品供应链 。
 * <p>
 * 洋葱商城：http://m.msyc.cc/wx/indexView?tmn=1
 * <p>
 * 洋桃商城：http://www.yunyangtao.com
 *  
 */


package com.zhengcq.srv.core.common.util;

import com.alibaba.fastjson.JSONObject;
import com.zhengcq.srv.core.common.exception.BizException;
import okhttp3.*;
import okio.ByteString;

import javax.net.ssl.*;
import java.io.File;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.TimeUnit;


/**
 * 系统工具类
 *
 * @ClassName: xx
 * @Description: xx
 * @Company: xxxxx
 * @Author: xxx
 * @Date: 2017-05-05
 */
public final class OkHttp3Utils {

    private OkHttp3Utils() {
    }

    /**
     * OkHttpClient
     */
    private static OkHttpClient client;

    /**
     * proxyClient
     */
    private static OkHttpClient proxyClient;

    /**
     * 超时时间
     */
    public static final int TIMEOUT = 90;


    /**
     * 超时时间
     */
    public static final int TIMEOUT2 = 30;


    /**
     * application/x-www-form-urlencoded
     */
    public static final MediaType JSON_MEDIA_TYPE = MediaType.parse("application/json; charset=utf-8");

    /**
     * application/x-www-form-urlencoded
     */
    public static final MediaType MEDIA_TYPE = MediaType.parse("application/x-www-form-urlencoded; charset=utf-8");

    /**
     * application/octet-stream
     */
    public static final MediaType MEDIA_OCTET_STREAM = MediaType.parse("application/octet-stream");

    static {
        TrustAllManager trustAllManager = new TrustAllManager();
        client = new OkHttpClient().newBuilder()
                .sslSocketFactory(createTrustAllSSLFactory(trustAllManager), trustAllManager)
                .hostnameVerifier(new HostnameVerifier() {
                    @Override
                    public boolean verify(String hostname, SSLSession session) {
                        return true;
                    }
                })
                .connectTimeout(TIMEOUT, TimeUnit.SECONDS)
                .readTimeout(TIMEOUT, TimeUnit.SECONDS)
                .writeTimeout(TIMEOUT, TimeUnit.SECONDS)
                .build();
    }

    /**
     * 同步发送post请求 map为body
     *
     * @param url 请求url
     * @param map 请求map参数
     * @return 返回回来的JSONObject
     * @throws BizException 异常
     */
    @Deprecated
    public static JSONObject post(String url, Map<String, Object> map) throws BizException {
        return post(url, map, MEDIA_TYPE);
    }

    /**
     * 同步发送post请求 map为body
     *
     * @param url       请求url
     * @param map       请求map参数
     * @param mediaType mediaType
     * @return 返回回来的JSONObject
     * @throws BizException 异常
     */
    @Deprecated
    public static JSONObject post(String url, Map<String, Object> map, MediaType mediaType) throws BizException {

        String httpContent;
        if (MEDIA_TYPE.equals(mediaType)) {
            StringBuilder context = new StringBuilder();
            // 遍历map
            if (map != null) {
                Iterator<Map.Entry<String, Object>> iterator = map.entrySet().iterator();
                while (iterator.hasNext()) {
                    Map.Entry<String, Object> entry = iterator.next();
                    context.append(entry.getKey()).append("=").append(entry.getValue());
                    if (iterator.hasNext()) {
                        context.append("&");
                    }
                }
            }
            httpContent = context.toString();
        } else if (JSON_MEDIA_TYPE.equals(mediaType)) {
            httpContent = JSONObject.toJSONString(map);
        } else {
            throw new BizException("不支持的MIME类型");
        }


        RequestBody body = RequestBody.create(mediaType, httpContent);
        Request request = new Request.Builder().url(url).post(body).build();
        try {
            Response response = client.newCall(request).execute();
            if (response.isSuccessful()) {
                String data = response.body().string();
                return JSONObject.parseObject(data, JSONObject.class);
            }
            throw new BizException(String.format("服务器解析错误##%s",
                    response == null ? "" : response.body().string()));
        } catch (BizException e) {
            throw e;
        } catch (Exception e) {
            throw new BizException(e);
        }

    }


    /**
     * 同步发送post请求 map为body
     * 返回字符串
     *
     * @param url 请求url
     * @param map 请求map参数
     * @return 返回回来的JSONObject
     * @throws BizException 异常
     */
    public static String postMap(String url, Map<String, Object> map) throws BizException {
        FormBody.Builder builder = new FormBody.Builder();
        // 遍历map
        if (map != null) {
            Iterator<Map.Entry<String, Object>> iterator = map.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<String, Object> entry = iterator.next();
                builder.add(entry.getKey(), entry.getValue().toString());
            }
        }
        RequestBody formBody = builder.build();
        Request request = new Request.Builder().url(url).post(formBody).build();
        try {
            Response response = client.newCall(request).execute();
            if (response.isSuccessful()) {
                String data = response.body().string();
                return data;
            }
            throw new BizException(String.format("服务器解析错误##%s",
                    response == null ? "" : response.body().string()));
        } catch (BizException e) {
            throw e;
        } catch (Exception e) {
            throw new BizException(e);
        }

    }

    /**
     * 同步发送post请求 map为body
     *
     * @param url 请求url
     * @param map 请求map参数
     * @return 返回回来的JSONObject
     * @throws BizException 异常
     */
    public static String postXml(String url, Map<String, Object> map) throws BizException {
        StringBuilder context = new StringBuilder();
        // 遍历map
        if (map != null) {
            Iterator<Map.Entry<String, Object>> iterator = map.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<String, Object> entry = iterator.next();
                context.append(entry.getKey()).append("=").append(entry.getValue());
                if (iterator.hasNext()) {
                    context.append("&");
                }
            }
        }

        RequestBody body = RequestBody.create(MEDIA_TYPE, context.toString());
        Request request = new Request.Builder().url(url).post(body).build();
        try {
            Response response = client.newCall(request).execute();
            if (response.isSuccessful()) {
                String data = response.body().string();
                return data;
            }
            throw new BizException(String.format("服务器解析错误##%s",
                    response == null ? "" : response.body().string()));
        } catch (BizException e) {
            throw e;
        } catch (Exception e) {
            throw new BizException(e);
        }

    }


    /**
     * 同步发送post请求 map为body
     *
     * @param url     请求url
     * @param context 请求参数
     * @return 返回回来的JSONObject
     * @throws BizException 异常
     */
    public static String postString(String url, String context) throws BizException {
        return postString(url, MEDIA_TYPE, context);

    }

    /**
     * 同步发送post请求 map为body
     *
     * @param url       请求url
     * @param mediaType mediaType
     * @param context   请求参数
     * @return 返回回来的JSONObject
     * @throws BizException 异常
     */
    public static String postString(String url, MediaType mediaType, String context) throws BizException {

        RequestBody body = RequestBody.create(mediaType, context);
        Request request = new Request.Builder().url(url).post(body).build();
        try {
            Response response = client.newCall(request).execute();
            if (response.isSuccessful()) {
                String data = response.body().string();
                return data;
            }
            throw new BizException(String.format("服务器解析错误##%s",
                    response == null ? "" : response.body().string()));
        } catch (BizException e) {
            throw e;
        } catch (Exception e) {
            throw new BizException(e);
        }

    }

    /**
     * 同步发送post请求 map为body
     *
     * @param url  请求url
     * @param json 请求参数
     * @return 返回回来的JSONObject
     * @throws Exception 异常
     */
    public static String postJson(String url, String json) throws Exception {
        RequestBody body = RequestBody.create(JSON_MEDIA_TYPE, json);
        Request request = new Request.Builder().url(url).post(body).build();
        Response response = client.newCall(request).execute();
        if (response.isSuccessful()) {
            String data = response.body().string();
            return data;
        }
        throw new BizException(String.format("请求失败，##%s",
                response == null ? "" : response.body().string()));
//        return "请求失败，请检查参数";
    }

    /**
     * 同步发送post请求 map为body
     *
     * @param url       请求url
     * @param headerMap 请求map参数
     * @param json      请求参数
     * @return 返回回来的JSONObject
     * @throws Exception 异常
     */
    public static String postJson(String url, Map headerMap, String json) throws Exception {
        if (headerMap == null) {
            headerMap = new HashMap();
        }
        RequestBody body = RequestBody.create(JSON_MEDIA_TYPE, json);
        Request request = new Request.Builder().url(url).headers(Headers.of(headerMap)).post(body).build();
        Response response = client.newCall(request).execute();
        if (response.isSuccessful()) {
            String data = response.body().string();
            return data;
        }
        throw new BizException(String.format("请求失败，##%s",
                response == null ? "" : response.body().string()));
//        return "请求失败，请检查参数";
    }

    /**
     * 同步发送post请求 map为body
     *
     * @param url     请求url
     * @param json    请求参数
     * @param timeout 超时时间
     * @return 返回回来的JSONObject
     * @throws Exception 异常
     */
    public static String postJson(String url, String json, int timeout) throws Exception {
        OkHttpClient httpClient = null;
        RequestBody body = RequestBody.create(JSON_MEDIA_TYPE, json);
        Request request = new Request.Builder().url(url).post(body).build();
        TrustAllManager trustAllManager = new TrustAllManager();
        httpClient = new OkHttpClient().newBuilder()
                .sslSocketFactory(createTrustAllSSLFactory(trustAllManager), trustAllManager)
                .connectTimeout(timeout, TimeUnit.SECONDS)
                .readTimeout(timeout, TimeUnit.SECONDS)
                .writeTimeout(timeout, TimeUnit.SECONDS)
                .build();

        Response response = httpClient.newCall(request).execute();
        if (response.isSuccessful()) {
            String data = response.body().string();
            return data;
        }
        throw new BizException(String.format("请求失败，##%s",
                response == null ? "" : response.body().string()));
//        return "请求失败，请检查参数";
    }

    /**
     * 同步发送post请求 map为body
     *
     * @param url 请求url
     * @param map 请求map参数
     * @return 返回回来的JSONObject
     * @throws BizException 异常
     */
    public static JSONObject postFormBody(String url, Map<String, Object> map) throws BizException {
        FormBody.Builder builder = new FormBody.Builder();
        // 遍历map
        if (map != null) {
            Iterator<Map.Entry<String, Object>> iterator = map.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<String, Object> entry = iterator.next();
                builder.add(entry.getKey(), entry.getValue().toString());
            }
        }
        RequestBody formBody = builder.build();
        Request request = new Request.Builder().url(url).post(formBody).build();
        try {
            Response response = client.newCall(request).execute();
            if (response.isSuccessful()) {
                String data = response.body().string();
                return JSONObject.parseObject(data, JSONObject.class);
            }
            throw new BizException(String.format("服务器解析错误##%s",
                    response == null ? "" : response.body().string()));
        } catch (BizException e) {
            throw e;
        } catch (Exception e) {
            throw new BizException(e);
        }

    }


    /**
     * 同步发送post请求 文件byte[]
     *
     * @param url   请求url
     * @param map   请求map参数放入header中 (header中不能传中文，value 如果是字符串需要转码 URLEncoder.encode)
     * @param bytes 文件byte[]
     * @return 返回回来的JSONObject
     * @throws BizException 异常
     */
    public static JSONObject postByteArr(String url, Map map, byte[] bytes) throws BizException {
        if (map == null) {
            map = new HashMap();
        }
        try {
            Request request = new Request.Builder()
                    .url(url)
                    .post(RequestBody.create(MEDIA_OCTET_STREAM, ByteString.of(bytes)))
                    .headers(Headers.of(map))
                    .build();
            Response response = client.newCall(request).execute();
            if (response.isSuccessful()) {
                String data = response.body().string();
                return JSONObject.parseObject(data, JSONObject.class);
            }
            throw new BizException(String.format("服务器解析错误##%s",
                    response == null ? "" : response.body().string()));
        } catch (BizException e) {
            throw e;
        } catch (Exception e) {
            throw new BizException(e);
        }

    }

    /**
     * 同步发送post请求 文件
     *
     * @param url   请求url
     * @param map   请求map参数
     * @param files 文件集合
     * @return 返回回来的JSONObject
     * @throws BizException 异常
     */
    public static JSONObject postFiles(String url, Map map, File... files) throws BizException {
        if (map == null) {
            map = new HashMap();
        }
        if (files.length == 0) {
            // if (null == file || !file.exists() || !file.canRead() || !file.isFile()) {
            throw new BizException("上传文件非法");
            // }
        }

        try {
            MultipartBody.Builder builder = new MultipartBody.Builder();
            builder.setType(MultipartBody.FORM);

            // 遍历map
            Iterator<Map.Entry<String, Object>> iterator = map.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<String, Object> entry = iterator.next();
                builder.addFormDataPart(entry.getKey(), entry.getValue() == null ? "" : entry.getValue().toString());
            }

            for (int i = 0; i < files.length; i++) {
                File file = files[i];
                builder.addFormDataPart("file" + i, file.getName(), RequestBody.create(MEDIA_OCTET_STREAM, file));
            }

            RequestBody requestBody = builder.build();
            Request request = new Request.Builder()
                    .url(url)
                    .post(requestBody)
                    .build();
            Response response = client.newCall(request).execute();
            if (response.isSuccessful()) {
                String data = response.body().string();
                return JSONObject.parseObject(data, JSONObject.class);
            }
            throw new BizException(String.format("服务器解析错误##%s",
                    response == null ? "" : response.body().string()));
        } catch (BizException e) {
            throw e;
        } catch (Exception e) {
            throw new BizException(e);
        }

    }

    /**
     * 同步发送post请求 map为body，带请求头
     *
     * @param url      请求url
     * @param map      请求头
     * @param postJson 请求报文
     * @return 返回回来的JSONObject
     * @throws BizException 异常
     */
    public static JSONObject postHeaderBody(String url, Map map, String postJson) throws BizException {

        RequestBody body = RequestBody.create(JSON_MEDIA_TYPE, postJson);
        Request request = new Request.Builder().url(url).headers(Headers.of(map)).post(body).build();
        try {
            Response response = client.newCall(request).execute();
            if (response.isSuccessful()) {
                String data = response.body().string();
                return JSONObject.parseObject(data, JSONObject.class);
            }
            throw new BizException(String.format("服务器解析错误##%s",
                    response == null ? "" : response.body().string()));
        } catch (BizException e) {
            throw e;
        } catch (Exception e) {
            throw new BizException(e);
        }
    }

    /**
     * 同步发送post表单请求
     *
     * @param url       请求url
     * @param headerMap 请求头
     * @param bodys     请求内容
     * @return 返回回来的JSONObject
     * @throws BizException 自定义异常
     */
    public static JSONObject post(String url, Map<String, String> headerMap, Map<String, Object> bodys) throws BizException {
        FormBody.Builder builder = new FormBody.Builder();
        // 遍历bodys
        if (bodys != null) {
            Iterator<Map.Entry<String, Object>> iterator = bodys.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<String, Object> entry = iterator.next();
                builder.add(entry.getKey(), entry.getValue().toString());
            }
        }
        RequestBody formBody = builder.build();
        Request request = new Request.Builder().url(url).headers(Headers.of(headerMap)).post(formBody).build();
        try {
            Response response = client.newCall(request).execute();
            if (response.isSuccessful()) {
                String data = response.body().string();
                return JSONObject.parseObject(data, JSONObject.class);
            }
            throw new BizException(String.format("服务器解析错误##%s",
                    response == null ? "" : response.body().string()));
        } catch (BizException e) {
            throw e;
        } catch (Exception e) {
            throw new BizException(e);
        }
    }

    /**
     * 同步发送post请求 map为body 【洋桃同步洋葱专用】
     *
     * @param url     请求url
     * @param headMap 请求头map参数
     * @param map     请求map参数
     * @return 返回回来的JSONObject
     * @throws BizException 异常
     */
    public static JSONObject postHeaderBody(String url, Map headMap, Map<String, Object> map) throws BizException {
        FormBody.Builder builder = new FormBody.Builder();
        // 遍历map
        if (map != null) {
            Iterator<Map.Entry<String, Object>> iterator = map.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<String, Object> entry = iterator.next();
                builder.add(entry.getKey(), entry.getValue().toString());
            }
        }
        RequestBody formBody = builder.build();
        Request request = new Request.Builder().url(url).headers(Headers.of(headMap)).post(formBody).build();
        try {
            Response response = client.newCall(request).execute();
            if (response.isSuccessful()) {
                String data = response.body().string();
                return JSONObject.parseObject(data, JSONObject.class);
            }
            throw new BizException(String.format("服务器解析错误##%s",
                    response == null ? "" : response.body().string()));
        } catch (BizException e) {
            throw e;
        } catch (Exception e) {
            throw new BizException(e);
        }

    }

    /**
     * 创建信任所有sslFactory
     *
     * @param trustAllManager TrustAllManager
     * @return SSLSocketFactory
     */
    protected static SSLSocketFactory createTrustAllSSLFactory(TrustAllManager trustAllManager) {
        SSLSocketFactory ssfFactory = null;
        try {
            SSLContext sc = SSLContext.getInstance("TLSv1.2");
            sc.init(null, new TrustManager[]{trustAllManager}, new SecureRandom());
            ssfFactory = sc.getSocketFactory();
        } catch (Exception ignored) {
            ignored.printStackTrace();
        }

        return ssfFactory;
    }

    /**
     * 发起get请求
     *
     * @param url url
     * @return 处理结果
     * @throws BizException 异常
     */
    public static String get(String url) throws BizException {
        Request request = new Request.Builder().url(url).get().build();
        try {
            Response response = client.newCall(request).execute();
            if (response.isSuccessful()) {
                return response.body().string();
            }
            throw new BizException(String.format("服务器解析错误##%s",
                    response == null ? "" : response.body().string()));
        } catch (BizException e) {
            throw e;
        } catch (Exception e) {
            throw new BizException(e);
        }
    }


    /**
     * 发起get请求,带请求头
     *
     * @param url url
     * @return 处理结果
     * @throws BizException 异常
     */
    public static JSONObject getHeader(String url, Map headerMap) throws BizException {

        Request request = new Request.Builder().url(url).headers(Headers.of(headerMap)).url(url).get().build();
        try {
            Response response = client.newCall(request).execute();
            if (response.isSuccessful()) {
                String data = response.body().string();
                return JSONObject.parseObject(data, JSONObject.class);
            }
            throw new BizException("服务器解析错误...");
        } catch (BizException e) {
            throw e;
        } catch (Exception e) {
            throw new BizException(e);
        }
    }


    /**
     * 发起get请求,返回一个字节流
     *
     * @param url url
     * @return 处理结果
     * @throws BizException 异常
     */
    public static InputStream getByteStream(String url) throws BizException {
        Request request = new Request.Builder().url(url).get().build();
        try {
            Response response = client.newCall(request).execute();
            if (response.isSuccessful()) {
                return response.body().byteStream();
            }
            throw new BizException("服务器解析错误...");
        } catch (BizException e) {
            throw e;
        } catch (Exception e) {
            throw new BizException(e);
        }
    }

    /**
     * 同步发送post请求 map为body
     *
     * @param url      请求url
     * @param map      请求map参数
     * @param hostName ip
     * @param port     端口
     * @return 返回回来的JSONObject
     * @throws BizException 异常
     */
    public static JSONObject postFormBodyProxy(String url, Map<String, Object> map, String hostName, int port) throws BizException {
        FormBody.Builder builder = new FormBody.Builder();
        // 遍历map
        if (map != null) {
            Iterator<Map.Entry<String, Object>> iterator = map.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<String, Object> entry = iterator.next();
                builder.add(entry.getKey(), entry.getValue().toString());
            }
        }
        RequestBody formBody = builder.build();
        Request request = new Request.Builder().url(url).post(formBody).build();
        try {
            Response response = getProxyClient(hostName, port).newCall(request).execute();
            if (response.isSuccessful()) {
                String data = response.body().string();
                return JSONObject.parseObject(data, JSONObject.class);
            }
            throw new BizException(String.format("服务器解析错误##%s",
                    response == null ? "" : response.body().string()));
        } catch (Exception e) {
            throw new BizException(e);
        }

    }



    /**
     * 同步发送post请求 map为body
     *
     * @param url      请求url
     * @param map      请求map参数
     * @param hostName ip
     * @param port     端口
     * @return 返回回来的JSONObject
     * @throws BizException 异常
     */
    public static String postXmlProxy(String url, Map<String, Object> map, String hostName, int port) throws BizException {
        StringBuilder context = new StringBuilder();
        // 遍历map
        if (map != null) {
            Iterator<Map.Entry<String, Object>> iterator = map.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<String, Object> entry = iterator.next();
                context.append(entry.getKey()).append("=").append(entry.getValue());
                if (iterator.hasNext()) {
                    context.append("&");
                }
            }
        }

        RequestBody body = RequestBody.create(MEDIA_TYPE, context.toString());
        Request request = new Request.Builder().url(url).post(body).build();
        try {
            Response response = getProxyClient(hostName, port).newCall(request).execute();
            if (response.isSuccessful()) {
                String data = response.body().string();
                return data;
            }
            throw new BizException(String.format("服务器解析错误##%s",
                    response == null ? "" : response.body().string()));
        } catch (Exception e) {
            throw new BizException(e);
        }

    }


    /**
     * 使用代理发送Http
     *
     * @param url       请求url
     * @param mediaType mediaType
     * @param context   请求参数
     * @param hostName  ip
     * @param port      端口
     * @return 返回回来的JSONObject
     * @throws BizException 异常
     */
    public static String postStringProxy(String url, MediaType mediaType, String context, String hostName, int port) throws BizException {

        RequestBody body = RequestBody.create(mediaType, context);
        Request request = new Request.Builder().url(url).post(body).build();
        try {
            Response response = getProxyClient(hostName, port).newCall(request).execute();
            if (response.isSuccessful()) {
                String data = response.body().string();
                return data;
            }
            throw new BizException(String.format("服务器解析错误##%s",
                    response == null ? "" : response.body().string()));
        } catch (Exception e) {
            throw new BizException(e);
        }

    }

    /**
     * 同步发送post请求 map为body
     *
     * @param url       请求url
     * @param headerMap 请求map参数
     * @param json      请求参数
     * @param hostName  ip
     * @param port      端口
     * @return 返回回来的JSONObject
     * @throws Exception 异常
     */
    public static String postJsonProxy(String url, Map headerMap, String json, String hostName, int port) throws Exception {
        Response response = postJsonProxyWithResponse(url, headerMap, json, hostName, port);
        if (response.isSuccessful()) {
            return response.body().string();
        }
        throw new BizException(String.format("请求失败，##%s", response.body().string()));
    }

    /**
     * 单例创建 proxyClient
     *
     * @param hostname
     * @param port
     * @return
     */
    private static synchronized OkHttpClient getProxyClient(String hostname, int port) {
        if (proxyClient == null) {
            TrustAllManager trustAllManager = new TrustAllManager();
            proxyClient = new OkHttpClient().newBuilder()
                    .sslSocketFactory(createTrustAllSSLFactory(trustAllManager), trustAllManager)
                    .hostnameVerifier(new HostnameVerifier() {
                        @Override
                        public boolean verify(String hostname, SSLSession session) {
                            return true;
                        }
                    })
                    .connectTimeout(TIMEOUT, TimeUnit.SECONDS)
                    .readTimeout(TIMEOUT, TimeUnit.SECONDS)
                    .writeTimeout(TIMEOUT, TimeUnit.SECONDS)
                    .proxy(new Proxy(Proxy.Type.HTTP, new InetSocketAddress(hostname, port)))
                    .build();

        }
        return proxyClient;
    }

    /**
     * 同步发送post请求
     *
     * @param url       请求url
     * @param headerMap 请求map参数
     * @param json      请求参数
     * @param hostName  ip
     * @param port      端口
     * @return 响应信息
     * @throws BizException 异常
     */
    public static Response postJsonProxyWithResponse(String url, Map headerMap, String json, String hostName, int port) throws BizException {
        try {
            if (headerMap == null) {
                headerMap = new HashMap();
            }
            RequestBody body = RequestBody.create(JSON_MEDIA_TYPE, json);
            Request request = new Request.Builder().url(url).headers(Headers.of(headerMap)).post(body).build();
            return getProxyClient(hostName, port).newCall(request).execute();
        } catch (Exception e) {
            throw new BizException(e);
        }
    }
}



/**
 * 信任ssl的类
 */
class TrustAllManager implements X509TrustManager {
    @Override
    public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
    }

    @Override
    public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
    }

    @Override
    public X509Certificate[] getAcceptedIssuers() {
        return new X509Certificate[0];
    }
}


