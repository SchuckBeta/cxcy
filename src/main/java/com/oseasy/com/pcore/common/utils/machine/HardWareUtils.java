package com.oseasy.com.pcore.common.utils.machine;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
/**
 * Created by victor on 2017/3/21.
 */
public class HardWareUtils {
    /**
        * 获取主板序列号
        *
        * @return
        */
       public static String getMotherboardSN() {
           String result = "";
           try {
               File file = File.createTempFile("realhowto", ".vbs");
               file.deleteOnExit();
               FileWriter fw = new java.io.FileWriter(file);

               String vbs = "Set objWMIService = GetObject(\"winmgmts:\\\\.\\root\\cimv2\")\n"
                       + "Set colItems = objWMIService.ExecQuery _ \n"
                       + "   (\"Select * from Win32_BaseBoard\") \n"
                       + "For Each objItem in colItems \n"
                       + "    Wscript.Echo objItem.SerialNumber \n"
                       + "    exit for  ' do the first cpu only! \n" + "Next \n";

               fw.write(vbs);
               fw.close();
               Process p = Runtime.getRuntime().exec(
                       "cscript //NoLogo " + file.getPath());
               BufferedReader input = new BufferedReader(new InputStreamReader(
                       p.getInputStream()));
               String line;
               while ((line = input.readLine()) != null) {
                   result += line;
               }
               input.close();
           } catch (Exception e) {
               e.printStackTrace();
           }
           return result.trim();
       }

       /**
        * 获取硬盘序列号
        *
        * @param drive
        *            盘符
        * @return
        */
       public static String getHardDiskSN(String drive) {
           String result = "";
           try {
               File file = File.createTempFile("realhowto", ".vbs");
               file.deleteOnExit();
               FileWriter fw = new java.io.FileWriter(file);

               String vbs = "Set objFSO = CreateObject(\"Scripting.FileSystemObject\")\n"
                       + "Set colDrives = objFSO.Drives\n"
                       + "Set objDrive = colDrives.item(\""
                       + drive
                       + "\")\n"
                       + "Wscript.Echo objDrive.SerialNumber"; // see note
               fw.write(vbs);
               fw.close();
               Process p = Runtime.getRuntime().exec(
                       "cscript //NoLogo " + file.getPath());
               BufferedReader input = new BufferedReader(new InputStreamReader(
                       p.getInputStream()));
               String line;
               while ((line = input.readLine()) != null) {
                   result += line;
               }
               input.close();
           } catch (Exception e) {
               e.printStackTrace();
           }
           return result.trim();
       }
       public static String getCPUSerialForUnix() {
           String mac = null;
           BufferedReader bufferedReader = null;
           Process process = null;
           try {
               process = Runtime.getRuntime().exec("dmidecode -t 4");
               bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
               String line = null;
               int index = -1;
               while ((line = bufferedReader.readLine()) != null) {
                   index = line.toLowerCase().indexOf("id:");
                   if (index >= 0) {
                       mac = line.substring(index + "id:".length() + 1).replaceAll(" ", "");
                       break;
                   }
               }
           }
           catch (IOException e) {
//               System.out.println("unix/linux方式未获取到CPU序列号");
           }
           finally {
               try {
                   if (bufferedReader != null) {
                       bufferedReader.close();
                   }
               }
               catch (IOException e1) {
                   e1.printStackTrace();
               }
               bufferedReader = null;
               process = null;
           }
           return mac;
       }
       /**
        * 获取CPU序列号
        *
        * @return
        */
       public static String getCPUSerialForWindows() {
           String result = "";
           try {
               File file = File.createTempFile("tmp", ".vbs");
               file.deleteOnExit();
               FileWriter fw = new java.io.FileWriter(file);
               String vbs = "Set objWMIService = GetObject(\"winmgmts:\\\\.\\root\\cimv2\")\n"
                       + "Set colItems = objWMIService.ExecQuery _ \n"
                       + "   (\"Select * from Win32_Processor\") \n"
                       + "For Each objItem in colItems \n"
                       + "    Wscript.Echo objItem.ProcessorId \n"
                       + "    exit for  ' do the first cpu only! \n" + "Next \n";

               // + "    exit for  \r\n" + "Next";
               fw.write(vbs);
               fw.close();
               Process p = Runtime.getRuntime().exec(
                       "cscript //NoLogo " + file.getPath());
               BufferedReader input = new BufferedReader(new InputStreamReader(
                       p.getInputStream()));
               String line;
               while ((line = input.readLine()) != null) {
                   result += line;
               }
               input.close();
               file.delete();
           } catch (Exception e) {
               e.fillInStackTrace();
           }
           if (result.trim().length() < 1 || result == null) {
        	   return null;
           }
           return result.trim();
       }

       /**
        * 获取MAC地址
        */
       public static String getMac() {
           String result = "";
           try {

               Process process = Runtime.getRuntime().exec("ipconfig /all");

               InputStreamReader ir = new InputStreamReader(
                       process.getInputStream());

               LineNumberReader input = new LineNumberReader(ir);

               String line;

               while ((line = input.readLine()) != null)

                   if (line.indexOf("Physical Address") > 0) {

                       String MACAddr = line.substring(line.indexOf("-") - 2);

                       result = MACAddr;

                   }

           } catch (java.io.IOException e) {

               System.err.println("IOException " + e.getMessage());

           }
           return result;
       }
       public static String getCPUSerial() {
           // windows
           String cpu = getCPUSerialForWindows();
           // unix
           if (isNull(cpu)) {
        	   cpu = getCPUSerialForUnix();
           }

           if (!isNull(cpu)) {
        	   cpu = cpu.replace("-", "");
           }else {
        	   return null;
           }
           return cpu.toLowerCase();
       }
       public static boolean isNull(Object strData) {
           if (strData == null || String.valueOf(strData).trim().equals("")) {
               return true;
           }
           return false;
       }
       public static void main(String[] args) {
           System.out.println("CPU  SN:" + HardWareUtils.getCPUSerial());
           System.out.println("主板  SN:" + HardWareUtils.getMotherboardSN());
           System.out.println("C盘   SN:" + HardWareUtils.getHardDiskSN("c"));
           System.out.println("MAC  SN:" + HardWareUtils.getMac());
           System.out.println(MacUtil.getMACAddress() );
       }




   }
