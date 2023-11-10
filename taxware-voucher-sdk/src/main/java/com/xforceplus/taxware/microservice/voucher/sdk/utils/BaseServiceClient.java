package com.xforceplus.taxware.microservice.voucher.sdk.utils;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.io.xml.DomDriver;
import com.thoughtworks.xstream.io.xml.XmlFriendlyNameCoder;
import okhttp3.*;

import java.net.URLEncoder;
import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * 服务调用客户端
 *
 * @author gulei
 */
public class BaseServiceClient {
    private String baseUrl;
    private OkHttpClient client;
    public MediaType mediaType = MediaType.parse("application/xml");
    public MediaType jsonMediaType = MediaType.parse("application/json");
    private XStream xStream;
    protected String format;
    /**
     * JSON格式
     */
    public static final String JSON_FORMAT = "JSON";
    /**
     * XML格式，默认格式
     */
    public static final String XML_FORMAT = "XML";
    /**
     * UTF-8编码格式
     */
    public static final String CHARSET_NAME_UTF_8 = "UTF-8";
    /**
     * GBK格式编码
     */
    public static final String CHARSET_NAME_GBK = "GBK";

    /**
     * 编码格式
     */
    private String charset = CHARSET_NAME_UTF_8;

    /**
     * 请求系统别
     */
    private String systemType;

    /**
     * 给开发者颁发的 key
     */
    private String apiKey;

    /**
     * JSON格式的请求Path
     */
    public static final String JSON_PATH = "api/ApiService";

    public BaseServiceClient(String baseUrl, long timeout, String format) {
        this.baseUrl = baseUrl;
        this.format = format;
        // 初始化HTTPClient
        this.client = new OkHttpClient.Builder()
                .connectTimeout(timeout, TimeUnit.MILLISECONDS)
                .readTimeout(timeout, TimeUnit.MILLISECONDS)
                .writeTimeout(timeout, TimeUnit.MILLISECONDS)
                .build();

        // 初始化XML处理器
        XmlFriendlyNameCoder xmlFriendlyNameCoder = new XmlFriendlyNameCoder("_-", "_");
        DomDriver domDriver = new DomDriver("utf-8", xmlFriendlyNameCoder);
        this.xStream = new XStream(domDriver);
        this.xStream.autodetectAnnotations(true);
    }

    public <Treq, Trep> Trep call(String path, Treq request, Class<Trep> clazz) {
        String url = "";
        String requestContent = "";
        try {
            url = String.format("%s/%s", this.baseUrl, path);
            requestContent = getRequestContent(request);
            RequestBody requestBody;
            if (this.format.equals(JSON_FORMAT)) {
                requestBody = RequestBody.create(jsonMediaType, requestContent.getBytes(this.charset));
            } else {
                requestBody = RequestBody.create(mediaType, requestContent.getBytes(CHARSET_NAME_GBK));
            }
            Request httpRequest = new Request.Builder().url(url).post(requestBody).build();
            try (Response httpResponse = client.newCall(httpRequest).execute()) {
                ResponseBody responseBody = httpResponse.body();

                String result = responseBody != null ? responseBody.string() : "";

                return getResponseResult(result, clazz);
            }
        } catch (Exception ex) {
        }
        return null;
    }

    public String call(String path, String request) throws Exception {
        String requestContent = request;

        String url = String.format("%s/%s", this.baseUrl, path);
        RequestBody requestBody;
        if (this.format.equals(JSON_FORMAT)) {
            requestBody = RequestBody.create(jsonMediaType, requestContent.getBytes(this.charset));
        } else {
            requestBody = RequestBody.create(mediaType, requestContent.getBytes(this.charset));
        }
        Request httpRequest = new Request.Builder().url(url).post(requestBody).build();
        try (Response httpResponse = client.newCall(httpRequest).execute()) {
            ResponseBody responseBody = httpResponse.body();

            String result = responseBody != null ? responseBody.string() : "";

            return result;
        }
    }

    public String call(String path, String request, String appId, String date, String strToBeEncrypt) throws Exception {
        String requestContent = request;

        String url = String.format("%s/%s", this.baseUrl, path);
        RequestBody requestBody;
        if (this.format.equals(JSON_FORMAT)) {
            requestBody = RequestBody.create(jsonMediaType, requestContent.getBytes(this.charset));
        } else {
            requestBody = RequestBody.create(mediaType, requestContent.getBytes(this.charset));
        }
        Request httpRequest = new Request.Builder().url(url)
                .addHeader("Content-type", "application/json;charset=UTF-8")
                .addHeader("appId", appId)
                .addHeader("Date", date)
                .addHeader("Content-MD5", md5(strToBeEncrypt))
                .post(requestBody)
                .build();
        try (Response httpResponse = client.newCall(httpRequest).execute()) {
            ResponseBody responseBody = httpResponse.body();

            String result = responseBody != null ? responseBody.string() : "";

            return result;
        }
    }

    public String call(String path) throws Exception {
        String url = String.format("%s/%s", this.baseUrl, path);
        Request httpRequest = new Request.Builder().url(url).get().build();
        try (Response httpResponse = client.newCall(httpRequest).execute()) {
            ResponseBody responseBody = httpResponse.body();

            String result = responseBody != null ? responseBody.string() : "";
            return result;
        }
    }

    /**
     * 将请求参数内容转成按照请求格式转成字符串
     *
     * @param request
     * @param <Treq>
     * @return
     */
    private <Treq> String getRequestContent(Treq request) {
        String requestContent;
        if (this.format.equals(JSON_FORMAT)) {
            requestContent = JSONObject.toJSONString(request, SerializerFeature.WriteNullStringAsEmpty);
        } else {
            requestContent = this.xStream.toXML(request);
        }
        return requestContent;
    }

    /**
     * 封装返回参数
     *
     * @param result
     * @param clazz
     * @param <Trep>
     * @return
     */
    private <Trep> Trep getResponseResult(String result, Class<Trep> clazz) {
        if (this.format.equals(JSON_FORMAT)) {
            try {
                return JSONObject.parseObject(result, clazz);
            } catch (Exception e) {
                try {
                    Trep trep = clazz.newInstance();
                    return trep;
                } catch (Exception e1) {
                    e1.printStackTrace();
                }

            }
            return null;

        } else {
            String aliasName = clazz.getDeclaredAnnotation(XStreamAlias.class).value();
            this.xStream.alias(aliasName, clazz);
            return (Trep) this.xStream.fromXML(result);
        }
    }

    /**
     * 获取访问路径
     *
     * @param method
     * @return
     */
    public String getPath(String method) {
        try {
            return String.format("ApiService?systemType=%s&method=%s&APIKey=%s"
                    , systemType
                    , method
                    , URLEncoder.encode(apiKey, "UTF-8")
            );
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public String getPath(String method, String invoiceType) {
        try {
            return String.format("ApiService?systemType=%s&method=%s&APIKey=%s&invoiceType=%s"
                    , systemType
                    , method
                    , URLEncoder.encode(apiKey, "UTF-8")
                    , invoiceType
            );
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }


    public void setCharset(String charset) {
        this.charset = charset;
    }

    public void setSystemType(String systemType) {
        this.systemType = systemType;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    public String getSystemType() {
        return systemType;
    }

    public String getApiKey() {
        return apiKey;
    }

    //获取md5值
    public static String md5(String str) throws Exception {
        MessageDigest md = MessageDigest.getInstance("MD5");
        md.update(str.getBytes("utf-8"));

        byte[] b = md.digest();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < b.length; i++) {
            int v = (int) b[i];
            v = v < 0 ? 0x100 + v : v;
            String cc = Integer.toHexString(v);
            if (cc.length() == 1)
                sb.append('0');
            sb.append(cc);
        }
        System.out.println("MD5:" + sb.toString());
        String encodeToString = Base64.getEncoder().encodeToString(sb.toString().getBytes("utf-8"));
        System.out.println("Base64:" + encodeToString);
        return encodeToString;
    }

    // 获取数据请求时间
    public static String getParamsDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        Date startDate_temp = calendar.getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.sss");
        String date = sdf.format(startDate_temp);
        return date;
    }
}
