package com.oseasy.util.common.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * 对象序列化和反序列化工具类.
 * @author chenhao
 */
public class EDSerUtil {
    /**
     * 序列化.
     * @param obj 序列化对象
     * @param path 文件路径
     * @return File
     */
    public static File serializable(Object obj, String path) {
        File file = new File(path);
        if(!file.exists()){
            return null;
        }
        return serializable(obj, file);
    }

    /**
     * 序列化.
     * @param obj 序列化对象
     * @param file 文件
     * @return File
     */
    public static File serializable(Object obj, File file) {
        ObjectOutputStream oo = null;
        try {
            oo = new ObjectOutputStream(new FileOutputStream(file));
            oo.writeObject(obj);
            oo.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return file;
    }

    /**
     * 序列化.
     * @param obj 序列化对象
     * @param os 输入流
     * @return File
     */
    public static FileOutputStream serializable(Object obj, FileOutputStream os) {
        ObjectOutputStream oo = null;
        try {
            oo = new ObjectOutputStream(os);
            oo.writeObject(obj);
            oo.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                if (oo != null) {
                    oo.close();
                }

                if (os != null) {
                    os.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return os;
    }

    /**
     * 反序列化.
     * @param clazz 类型
     * @param path 文件路径
     * @return Object
     */
    public static <T> T deserialize(Class<T> clazz, String path) {
        File file = new File(path);
        if(!file.exists()){
            return null;
        }
        return deserialize(clazz, file);
    }

    /**
     * 反序列化.
     * @param clazz 类型
     * @param file 文件
     * @return Object
     */
    public static <T> T deserialize(Class<T> clazz, File file) {
        try {
            return deserialize(clazz, new FileInputStream(file));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 反序列化.
     * @param clazz 类型
     * @param is 输出流
     * @return Object
     */
    @SuppressWarnings({ "unchecked" })
    public static <T> T deserialize(Class<T> clazz, FileInputStream is) {
        ObjectInputStream ois = null;
        try {
            ois = new ObjectInputStream(is);
            return (T) ois.readObject();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }finally {
            try {
                if (ois != null) {
                    ois.close();
                }

                if (is != null) {
                    is.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

//    public static void main(String[] args) {
//        System.out.println("evnFile=" + AuthSval.getEvnFile());
//        KV kv = EDSerUtil.deserialize(KV.class, AuthSval.getEvnFile());
//        if(kv != null){
//            System.out.println("密文:" + kv.getV());
//            String ss = MD5Util.convertMD5(MD5Util.convertMD5(kv.getV()));
//            System.out.println("解密:" + ss);
//            Minfo minfo = JsonAliUtils.readBeano(MD5Util.convertMD5(MD5Util.convertMD5(kv.getV())), Minfo.class);
//            System.out.println("cpu:" + minfo.getCpu());
//            System.out.println("mac:" + minfo.getMac());
//        }
//    }

    public static void main(String[] args) {
        String sefile = "D:\\jeesite\\serial.bar";
        System.out.println("evnFile=" + sefile);
//        Map<String, String> sernuberMap = new HashMap<String, String>();
//        sernuberMap.put("cpu", CPU.getCPUSerial());
//        sernuberMap.put("mac", MAC.getMACAddress());
//        JSONObject json = new JSONObject(sernuberMap);
//        System.out.println(json.toString());
//        String rscEnvInfo = MD5Util.convertMD5(json.toString());
//        KV skv = new KV("OS.E.V", rscEnvInfo);
//        EDSerUtil.serializable(skv, sefile);
    }
}
