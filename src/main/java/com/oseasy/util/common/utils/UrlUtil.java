package com.oseasy.util.common.utils;

import eu.bitwalker.useragentutils.Browser;
import eu.bitwalker.useragentutils.OperatingSystem;
import eu.bitwalker.useragentutils.UserAgent;
import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Url地址处理类.
 */
public class UrlUtil {
    public static final String HTTP = "http";
    public static final String HTTPLINE = "://";
    public static final String REFERER = "referer";
    public static final String HTTP_LOOKUP = "http://int.dpool.sina.com.cn/iplookup/iplookup.php?format=json&ip=";

    public static String referer(HttpServletRequest request) {
        return request.getHeader(REFERER);
    }

    public static String requestUrl(HttpServletRequest request) {
        return request.getRequestURL().toString();
    }

    public static String queryString(HttpServletRequest request) {
        return request.getQueryString();
    }

    public static boolean isAjax(HttpServletRequest request){
        String xrwith = request.getHeader("x-requested-with");
        if (StringUtil.isNotEmpty(xrwith) && (xrwith).equalsIgnoreCase("XMLHttpRequest")) {
           return true;
        }
        return false;
    }

    /**
     * 获得用户远程地址
     */
    public static String getRemoteAddr(HttpServletRequest request) {
        String remoteAddr = request.getHeader("X-Real-IP");
        if (StringUtil.isNotBlank(remoteAddr)) {
            remoteAddr = request.getHeader("X-Forwarded-For");
        }else if (StringUtil.isNotBlank(remoteAddr)) {
            remoteAddr = request.getHeader("Proxy-Client-IP");
        }else if (StringUtil.isNotBlank(remoteAddr)) {
            remoteAddr = request.getHeader("WL-Proxy-Client-IP");
        }
        return remoteAddr != null ? remoteAddr : request.getRemoteAddr();
    }

    public static String url(HttpServletRequest request) {
        StringBuffer url = new StringBuffer(request.getScheme());
        url.append(HTTPLINE);
        url.append(request.getServerName());
        url.append(":");
        url.append(request.getServerPort());
        url.append(request.getServletPath());
        if (StringUtil.isNotEmpty(request.getQueryString())){
            url.append("?");
            url.append(request.getQueryString());
        }
        return url.toString();
    }

    public static String wwwPort(HttpServletRequest request) {
        return request.getServerName();
    }

    public static String wwwPorts(HttpServletRequest request) {
        StringBuffer url = new StringBuffer(request.getServerName());
        url.append(":");
        url.append(request.getServerPort());
        return url.toString();
    }

    public static String wwwPortKey(HttpServletRequest request) {
        StringBuffer url = new StringBuffer(request.getServerName());
        url.append(StringUtil.LINE_D);
        url.append(request.getServerPort());
        return url.toString();
    }

    public static String getRemoteIp(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        if (StringUtil.isEmpty(ip)) {
            ip="unknown";
        }
        return ip;

    }

    /**
     * 获取当前用户浏览器信息.
     *
     * @param request
     *            HttpServletRequest
     * @return 当前用户浏览器信息
     */
    public static String getHeader(HttpServletRequest request) {
        return request.getHeader("User-Agent");
    }

    /**
     * 获取当前用户浏览器型号.
     *
     * @param request
     *            HttpServletRequest
     * @return 当前用户浏览器型号
     */
    public static String getBrowser(HttpServletRequest request) {
        UserAgent userAgent = UserAgent.parseUserAgentString(request.getHeader("User-Agent"));
        Browser browser = userAgent.getBrowser();
        return browser.toString();
    }

    /**
     * 获取当前用户系统型号.
     *
     * @param request
     *            HttpServletRequest
     * @return 当前用户系统型号
     */
    public static String getOperatingSystem(HttpServletRequest request) {
        UserAgent userAgent = UserAgent.parseUserAgentString(request.getHeader("User-Agent"));
        OperatingSystem operatingSystem = userAgent.getOperatingSystem();
        return operatingSystem.toString();
    }



    /**
     * 获取请求主机IP地址,如果通过代理进来，则透过防火墙获取真实IP地址;
     *
     * @param request
     *            HttpServletRequest
     * @return 请求主机IP地址
     * @throws IOException
     */
    public final static String getIpAddress(HttpServletRequest request) throws IOException {
        // 获取请求主机IP地址,如果通过代理进来，则透过防火墙获取真实IP地址
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getHeader("Proxy-Client-IP");
            }
            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getHeader("WL-Proxy-Client-IP");
            }
            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getHeader("HTTP_CLIENT_IP");
            }
            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getHeader("HTTP_X_FORWARDED_FOR");
            }
            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getRemoteAddr();
            }
        } else if (ip.length() > 15) {
            String[] ips = ip.split(",");
            for (int index = 0; index < ips.length; index++) {
                String strIp = (String) ips[index];
                if (!("unknown".equalsIgnoreCase(strIp))) {
                    ip = strIp;
                    break;
                }
            }
        }
        return ip;
    }

    /**
     * 根据IP地址获取所在市区
     *
     * @param ip
     *            ip地址 请求的参数 格式为：name=xxx&pwd=xxx
     * @param encoding
     *            服务器端请求编码。如GBK,UTF-8等
     * @return 所在市区
     * @throws IOException
     */
    public String getAddresses(String ip, String encodingString) throws IOException {
        // 这里调用pconline的接口
        String urlStr = HTTP_LOOKUP + ip;
        // 从http://whois.pconline.com.cn取得IP所在的省市区信息
        String returnStr = this.getResult(urlStr, encodingString);
        if (returnStr != null) {
            // 处理返回的省市区信息
            String[] temp = returnStr.split(",");
            if (temp.length < 3) {
                return "0";// 无效IP，局域网测试
            }
            String country = "";
            String region = "";
            String city = "";
            country = (temp[3].split(":"))[1].replaceAll("\"", "");
            country = decodeUnicode(country);// 国家
            region = (temp[4].split(":"))[1].replaceAll("\"", "");
            region = decodeUnicode(region);// 省份
            city = (temp[5].split(":"))[1].replaceAll("\"", "");
            city = decodeUnicode(city);// 市区
            // System.out.println("国家=" + country + " 省份=" + region + " 市区="
            // + city);
            return city;
        }
        return null;
    }

    /**
     * 请求URL
     *
     * @param urlStr
     *            请求的地址
     * @param encoding
     *            服务器端请求编码。如GBK,UTF-8等
     * @return String
     * @throws IOException
     */
    private String getResult(String urlStr, String encoding) throws IOException {
        URL url = null;
        HttpURLConnection connection = null;
        try {
            url = new URL(urlStr);
            connection = (HttpURLConnection) url.openConnection();// 新建连接实例
            connection.setConnectTimeout(2000);// 设置连接超时时间，单位毫秒
            connection.setReadTimeout(2000);// 设置读取数据超时时间，单位毫秒
            connection.setDoOutput(true);// 是否打开输出流 true|false
            connection.setDoInput(true);// 是否打开输入流true|false
            connection.setRequestMethod("POST");// 提交方法POST|GET
            connection.setUseCaches(false);// 是否缓存true|false
            connection.connect();// 打开连接端口
            DataOutputStream out = new DataOutputStream(connection.getOutputStream());// 打开输出流往对端服务器写数据
            // out.writeBytes(content);// 写数据,也就是提交你的表单 name=xxx&pwd=xxx
            out.flush();// 刷新
            out.close();// 关闭输出流
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), encoding));// 往对端写完数据对端服务器返回数据
            // ,以BufferedReader流来读取
            StringBuffer buffer = new StringBuffer();
            String line = "";
            while ((line = reader.readLine()) != null) {
                buffer.append(line);
            }
            reader.close();
            return buffer.toString();
        } catch (IOException e) {
            e.printStackTrace();
            throw e;
        } finally {
            if (connection != null) {
                connection.disconnect();// 关闭连接
            }
        }
    }

    /**
     * Unicode 转换成 中文
     *
     * @param theString
     *            Unicode
     * @return String
     */
    public static String decodeUnicode(String theString) {
        char aChar;
        int len = theString.length();
        StringBuffer outBuffer = new StringBuffer(len);
        for (int x = 0; x < len;) {
            aChar = theString.charAt(x++);
            if (aChar == '\\') {
                aChar = theString.charAt(x++);
                if (aChar == 'u') {
                    int value = 0;
                    for (int i = 0; i < 4; i++) {
                        aChar = theString.charAt(x++);
                        switch (aChar) {
                            case '0':
                            case '1':
                            case '2':
                            case '3':
                            case '4':
                            case '5':
                            case '6':
                            case '7':
                            case '8':
                            case '9':
                                value = (value << 4) + aChar - '0';
                                break;
                            case 'a':
                            case 'b':
                            case 'c':
                            case 'd':
                            case 'e':
                            case 'f':
                                value = (value << 4) + 10 + aChar - 'a';
                                break;
                            case 'A':
                            case 'B':
                            case 'C':
                            case 'D':
                            case 'E':
                            case 'F':
                                value = (value << 4) + 10 + aChar - 'A';
                                break;
                            default:
                                throw new IllegalArgumentException("Malformed      encoding.");
                        }
                    }
                    outBuffer.append((char) value);
                } else {
                    if (aChar == 't') {
                        aChar = '\t';
                    } else if (aChar == 'r') {
                        aChar = '\r';
                    } else if (aChar == 'n') {
                        aChar = '\n';
                    } else if (aChar == 'f') {
                        aChar = '\f';
                    }
                    outBuffer.append(aChar);
                }
            } else {
                outBuffer.append(aChar);
            }
        }
        return outBuffer.toString();
    }

    // 测试
    public static void main(String[] args) throws IOException {
        UrlUtil addressUtils = new UrlUtil();
        // 测试ip 219.136.134.157 中国=华南=广东省=广州市=越秀区=电信
        String ip = "61.235.13.252";
        String address = "";
        try {
            address = addressUtils.getAddresses(ip, "utf-8");
            System.out.println(address);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        // 输出结果为：广东省,广州市,越秀区
    }
}
