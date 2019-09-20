package com.oseasy.util.common.utils.httpclient;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.List;

import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.BufferedHttpEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HTTP;
import org.apache.log4j.Logger;
import org.slf4j.LoggerFactory;


public class HttpClientUtil {
    private static org.slf4j.Logger logger = LoggerFactory.getLogger(HttpClientUtil.class);

    public HttpClientUtil() {
    }

//    public static String doGet(String url) throws Exception {
//            BufferedReader in = null;
//            String content = null;
//            try {
//                // 定义HttpClient
//                HttpClient client = new DefaultHttpClient();
//                // 实例化HTTP方法
//                HttpGet request = new HttpGet();
//                request.setURI(new URI(url));
//                HttpResponse response = client.execute(request);
//
//                in = new BufferedReader(new InputStreamReader(response.getEntity()
//                        .getContent()));
//                StringBuffer sb = new StringBuffer("");
//                String line = "";
//                String NL = System.getProperty("line.separator");
//                while ((line = in.readLine()) != null) {
//                    sb.append(line + NL);
//                }
//                in.close();
//                content = sb.toString();
//            } finally {
//                if (in != null) {
//                    try {
//                        in.close();// 最后要关闭BufferedReader
//                    } catch (Exception e) {
//                        logger.error("关闭http流出现异常"+e.getMessage());
////                        e.printStackTrace();
//                    }
//                }
//                return content;
//            }
//        }


    public static String doGet(String url) {
//        logger.info("url = " + url);
        HttpGet httpGet = new HttpGet(url);
        HttpResponse httpResponse = null;
        StringBuffer result = new StringBuffer();
        try {
            httpResponse = new DefaultHttpClient().execute(httpGet);
            if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                HttpEntity het = httpResponse.getEntity();
                InputStream is = null;
                BufferedReader br = null;
                String readLine = null;
                try {
                    is = het.getContent();
                    br = new BufferedReader(new InputStreamReader(is, "UTF-8"));
                    while ((readLine = br.readLine()) != null) {
                        result.append(readLine);
                    }
                } catch (Exception ex) {
                    logger.error(ex.getMessage());
                    ex.printStackTrace();
                } finally {
                    if (is != null) {
                        is.close();
                    }
                    if (br != null) {
                        br.close();
                    }
                }
            }
        } catch (ClientProtocolException e) {
            logger.error("调用远程接口异常" + e.getMessage());
            e.printStackTrace();
        } catch (IOException e) {
            logger.error("调用远程接口异常" + e.getMessage());
            e.printStackTrace();
        }
        logger.debug("####### result = " + result);
        return result.toString();
    }

    @SuppressWarnings("unchecked")
    public static String doPost(String url, List params) {
        logger.info("url = " + url);
        logger.info("params = " + params);
        HttpPost httpPost = new HttpPost(url);
        HttpEntity he;
        HttpClient client = new DefaultHttpClient();
        HttpResponse response;
        StringBuffer result = new StringBuffer();
        try {
            he = new UrlEncodedFormEntity(params, "UTF-8");
            httpPost.setEntity(he);
        } catch (UnsupportedEncodingException e1) {
            e1.printStackTrace();
        }
        try {
            response = client.execute(httpPost);
            int statuscode = response.getStatusLine().getStatusCode();
            if ((statuscode == HttpStatus.SC_MOVED_TEMPORARILY) || (statuscode == HttpStatus.SC_MOVED_PERMANENTLY) || (statuscode == HttpStatus.SC_SEE_OTHER)
                    || (statuscode == HttpStatus.SC_TEMPORARY_REDIRECT)) {
                // 读取新的 URL 地址
                Header header = response.getFirstHeader("location");
                if (header != null) {
                    String newuri = header.getValue();
                    if ((newuri == null) || (newuri.equals(""))) {
                        newuri = "/";
                    }
                    HttpGet httpget = new HttpGet(newuri);
                    client = new DefaultHttpClient();
                    response = client.execute(httpget);
                }
            }
            // 判断连接是否成功
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                InputStream is = null;
                BufferedReader br = null;
                try {
                    HttpEntity het = response.getEntity();
                    is = het.getContent();
                    br = new BufferedReader(new InputStreamReader(is, "UTF-8"));
                    String readLine = null;
                    while ((readLine = br.readLine()) != null) {
                        result.append(readLine);
                    }
                } catch (Exception ex) {
                    logger.error(ex.getMessage());
                    ex.printStackTrace();
                } finally {
                    if (is != null) {
                        is.close();
                    }
                    if (br != null) {
                        br.close();
                    }
                }
            }
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        logger.info("####### result = " + result);
        return result.toString();
    }

    private static final String APPLICATION_JSON = "application/json;charset=UTF-8";

//    private static final String CONTENT_TYPE_TEXT_JSON = "text/json;charset=UTF-8";

    @SuppressWarnings("unchecked")
    public static String doPostWithJson(String url, String params) {
        logger.info("url = " + url);
        logger.info("params = " + params);
        HttpPost httpPost = new HttpPost(url);
        HttpEntity he;
        HttpClient client = new DefaultHttpClient();
        HttpResponse response;
        StringBuffer result = new StringBuffer();
        StringEntity entity;
        try {
            httpPost.addHeader("Content-Type", APPLICATION_JSON);
            httpPost.addHeader("Accept-Language", "zh-CN,zh;");

            entity = new StringEntity(params, "UTF-8");
            entity.setContentType(APPLICATION_JSON);
            entity.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE, APPLICATION_JSON));
            entity.setContentEncoding(HTTP.UTF_8);

            httpPost.setEntity(entity);
            response = client.execute(httpPost);
            int statuscode = response.getStatusLine().getStatusCode();
            if ((statuscode == HttpStatus.SC_MOVED_TEMPORARILY) || (statuscode == HttpStatus.SC_MOVED_PERMANENTLY) || (statuscode == HttpStatus.SC_SEE_OTHER)
                    || (statuscode == HttpStatus.SC_TEMPORARY_REDIRECT)) { // 读取新的
                Header header = response.getFirstHeader("location");
                if (header != null) {
                    String newuri = header.getValue();
                    if ((newuri == null) || (newuri.equals(""))) {
                        newuri = "/";
                    }
                    HttpGet httpget = new HttpGet(newuri);
                    client = new DefaultHttpClient();
                    response = client.execute(httpget);
                }
            } // 判断连接是否成功
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                HttpEntity het = response.getEntity();
                if (het != null) {
                    het = new BufferedHttpEntity(het);
                }
                if (het == null) {
                  return null;
                }
                InputStream is = het.getContent();
                BufferedReader br = null;
                try {
                    byte[] buffer = new byte[1024];
                    BufferedInputStream bis = new BufferedInputStream(is);
                    int len;
                    while ((len = bis.read(buffer)) != -1) {
                        result.append(new String(buffer, "UTF-8"));
                    }

                    // br = new BufferedReader(new InputStreamReader(is,
                    // "UTF-8"));
                    // String readLine = null;
                    // while ((readLine = br.readLine()) != null) {
                    // result.append(readLine);
                    // }
                } catch (Exception e) {
                    logger.error("调用接口异常 ：" + e);
                    e.printStackTrace();
                } finally {
                    if (is != null) {
                        try {
                            is.close();
                        } catch (IOException e) {
                            logger.error("读取返回的流异常" + e);
                            e.printStackTrace();
                        }
                    }
                    if (br != null) {
                        try {
                            br.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                }
            }
        } catch (UnsupportedEncodingException e1) {
            logger.error("读取返回的流异常" + e1);
            e1.printStackTrace();
        } catch (ClientProtocolException e) {
            logger.error("读取返回的流异常" + e);
            e.printStackTrace();
        } catch (IOException e) {
            logger.error("读取返回的流异常" + e);
            e.printStackTrace();
        }
        logger.info("#######doPostWithJson result = " + result);
        return result.toString();
    }
}
