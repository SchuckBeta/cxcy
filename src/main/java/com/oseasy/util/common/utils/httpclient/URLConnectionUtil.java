package com.oseasy.util.common.utils.httpclient;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.TimeUnit;

public class URLConnectionUtil {
	private static Logger logger = LoggerFactory.getLogger(URLConnectionUtil.class);
	public static DefaultHttpClient httpclient = null;

	public static String sendHttpRequest(String url, Map<String, String> cloneParamMap) throws Exception {
		return sendHttpRequest(url, cloneParamMap, "UTF-8");
	}
	public static String sendHttpRequest(String url, Map<String, String> cloneParamMap, String encode) throws Exception {
		List<NameValuePair> nvp = new ArrayList<NameValuePair>();
		HttpResponse response = null;
		HttpEntity entity = null;
		String postResp = null;
		httpclient = new DefaultHttpClient();
		httpclient.getParams().setIntParameter(HttpConnectionParams.SO_TIMEOUT, 15000); // 超时设置
		httpclient.getParams().setIntParameter(HttpConnectionParams.CONNECTION_TIMEOUT, 15000);// 连接超时
		httpclient.getParams().setIntParameter(HttpConnectionParams.SO_LINGER, 30);
		HttpPost httpost = new HttpPost(url);
		httpost.addHeader("Content-uid", "merchant");
		httpost.addHeader("Content-Type","application/x-www-form-urlencoded");
		httpost.addHeader("Accept-Language", "zh-cn");
		httpost.addHeader("Accept-Encoding", "gzip, deflate");
		try {

			nvp = getNameValuePairArr(cloneParamMap);
			httpost.setEntity(new UrlEncodedFormEntity(nvp, encode));
			response = httpclient.execute(httpost);
			entity = response.getEntity();
			if (response.getStatusLine().getStatusCode() != 200) {
				httpost.abort();
				return null;
			}
			if (entity != null) {
				return EntityUtils.toString(entity, "UTF-8");
			}
		} catch (Exception e) {
			httpost.abort();
			throw new Exception();
		} finally {
			httpclient.getConnectionManager().closeExpiredConnections();
			httpclient.getConnectionManager().closeIdleConnections(5, TimeUnit.SECONDS);
		}
		return postResp;
	}
	public static String sendHttp(String url, Map<String, String> cloneParamMap) throws Exception {
		List<NameValuePair> nvp = new ArrayList<NameValuePair>();
		HttpResponse response = null;
		HttpEntity entity = null;
		String postResp = null;
		httpclient = new DefaultHttpClient();
		httpclient.getParams().setIntParameter(HttpConnectionParams.SO_TIMEOUT, 15000); // 超时设置
		httpclient.getParams().setIntParameter(HttpConnectionParams.CONNECTION_TIMEOUT, 15000);// 连接超时
		httpclient.getParams().setIntParameter(HttpConnectionParams.SO_LINGER, 30);
		HttpPost httpost = new HttpPost(url);
		httpost.addHeader("Content-uid", "merchant");
		httpost.addHeader("Content-Type","application/x-www-form-urlencoded");
		httpost.addHeader("Accept-Language", "zh-cn");
		httpost.addHeader("Accept-Encoding", "gzip, deflate");
		try {
			
			nvp = getNameValuePairArr(cloneParamMap);
			httpost.setEntity(new UrlEncodedFormEntity(nvp, "UTF-8"));
			httpclient.execute(httpost);
			
		} catch (Exception e) {
			httpost.abort();
			throw new Exception();
		} finally {
			httpclient.getConnectionManager().closeExpiredConnections();
			httpclient.getConnectionManager().closeIdleConnections(5, TimeUnit.SECONDS);
		}
		return postResp;
	}

	public static List<NameValuePair> getNameValuePairArr(Map<String, String> parasMap) {
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		for (Entry<String, String> parasEntry : parasMap.entrySet()) {
			String parasName = parasEntry.getKey();
			String parasValue = parasEntry.getValue();
			nvps.add(new BasicNameValuePair(parasName, parasValue));
		}
		return nvps;
	}
	
	
	 /**
     * 使用Get方式获取数据
     * 
     * @param url URL包括参数，http://HOST/XX?XX=XX&XXX=XXX
     * @return
     */
    public static String sendGet(String url) {
        String result = "";
        BufferedReader in = null;
        try {
            URL realUrl = new URL(url);
            // 打开和URL之间的连接
            URLConnection connection = realUrl.openConnection();
            // 设置通用的请求属性
            connection.setRequestProperty("accept", "*/*");
            // 建立实际的连接
            connection.connect();
            // 定义 BufferedReader输入流来读取URL的响应
            in = new BufferedReader(new InputStreamReader(connection.getInputStream(), "utf-8"));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
			logger.error("发送GET请求出现异常！"+ e.getMessage());
            e.printStackTrace();
        }
        // 使用finally块来关闭输入流
        finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (Exception e2) {
				logger.error("关闭http流出现异常！"+ e2.getMessage());
                e2.printStackTrace();
            }
        }
        return result;
    }
}
