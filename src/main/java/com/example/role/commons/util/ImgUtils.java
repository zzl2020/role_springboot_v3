package com.example.role.commons.util;


import com.example.role.commons.create.Const;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.*;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class ImgUtils {
    private static  Map<String,String> map;
    private final static String FILE_PATH="D:\\img\\";
    static {
        map = new HashMap<>();
        map.put("/9j","jpg");
        map.put("iVB","png");
        map.put("Qk0","bmp");
        map.put("SUk","tiff");
        map.put("JVB","pdf");
        map.put("UEs","ofd");
        map.put("R0l","gif");
    }
    public static void main(String[] args) throws IOException {
       // String s = imgToBase64("D:\\img\\f.gif");
       // String path = base64ToImg(s);
        //System.out.println(path);
        //aa();
       // long l = System.currentTimeMillis();
        //String time = simpleTime(l);
        //System.out.println("time = " + time);
       // System.out.println(getHead("dasd"));
     /*   Map<String,Object> map1 = new HashMap<>();
        map1.put("a1","你好java");
        map1.put("a2","你好张三");
        //判断当前map中key是否存在
        System.out.println(map1.containsKey("a1"));
        System.out.println(map1.containsValue("你好张zj"));
        List<String> list = new ArrayList<>();
       list.add("1");
        list.add("1");
        list.add("1");
        list.add("3");
        list.set(3,"wanger");

        list = new LinkedList<>();*/

        //boolean b = list.contains(list);
        //System.out.println(b);
   /*     String file="D:/360Downloads";
        String newFile ="D:/avc";
        recursionCopyFile(file,newFile);*/
       // String s = imgToBase64("C:\\Users\\Administrator\\Desktop\\03.gif");
        //System.out.println(s);
        //sum(5,0);
/*        List<File> list = new ArrayList<>();
        list.add(new File("D:\\360Downloads\\6a0396be981aa8bf.jpg"));
        list.add(new File("D:\\360Downloads\\37cd1c097053610cc4f3c02e7fa4a09e.jpg"));
        list.add(new File("D:\\360Downloads\\360wallpaper_dt.jpg"));
        list.add(new File("D:\\360Downloads\\469e138330f926471dd9e62ac6031194.jpeg"));
        list.add(new File("D:\\360Downloads\\103840-15009503208823.jpg"));
        list.add(new File("D:\\360Downloads\\183736-157502385671ef.jpg"));
        list.add(new File("D:\\360Downloads\\184149-1575024109b3bd.jpg"));
        list.add(new File("D:\\360Downloads\\184605-1543056365d011.jpg"));
        list.add(new File("D:\\360Downloads\\213829-15754667093f3a.jpg"));
        OutputStream out = new FileOutputStream("D:"+File.separator+"a.zip");
        toZip(list,out);*/
        System.out.println(System.getProperty("user.dir"));
    }
    static String getHead(String key){
        return map.get(key);
    }
    public static String base64ToImg(String base64Str ){
        OutputStream outputStream =null;
        String filePath=FILE_PATH;
        try {
            File file = new File(filePath);
            if (!file.exists()) {
                file.mkdirs();
            }
            String head=base64Str.substring(0,3);
            String head1 = getHead(head);
            if(head1==null){
                return  "";
            }
            String filename= UUID.randomUUID().toString()+"-1";
            filePath +=filename+"."+head1;

            outputStream =  new FileOutputStream(filePath);

            base64Str.substring(base64Str.indexOf(",")+1,base64Str.length());
            byte[] decode = Base64.getDecoder().decode(base64Str);
            // 处理数据
            for (int i = 0; i < decode.length; ++i) {
                if (decode[i] < 0) {
                    decode[i] += 256;
                }
            }
            outputStream.write(decode);
            System.out.println("base64转文件完成");
        }catch (Exception e){
            filePath="";
            e.printStackTrace();
        }finally {
            if(outputStream!=null){
                try {
                    outputStream.close();
                } catch (IOException e) {
                    System.out.println("base64转文件失败");
                    throw new RuntimeException(e);
                }
            }
        }
     return filePath;
    }
   public static String imgToBase64(String path){
        ByteArrayOutputStream outputStream = null;
        InputStream  inputStream= null;
        byte[] bytes = new byte[1024];
        String base64Str=null;
        try {
            outputStream = new ByteArrayOutputStream();
            inputStream = new FileInputStream(path);
            int len =0;
            while ((len=inputStream.read(bytes))>0){
                outputStream.write(bytes,0,len);
            }
            byte[] encode = Base64.getEncoder().encode(outputStream.toByteArray());
            base64Str = new String(encode);
            System.out.println("文件转base64完成");
        }catch (Exception e){
            base64Str="";
            e.printStackTrace();
        }finally {
            if(outputStream!=null){
                try {
                    outputStream.close();
                } catch (IOException e) {
                    System.out.println("文件转base64失败");
                    throw new RuntimeException(e);
                }
            }
            if(outputStream!=null){
                try {
                    outputStream.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        return base64Str;
    }

    public static String simpleTime(long time){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String format = simpleDateFormat.format(new Date(time));
        return format;
    }
    static  void readXmlFile() throws DocumentException {
        String filePath="D:\\bb\\netty\\src\\main\\resources\\";
        String[] list = new File(filePath).list();
        for (String fileName: list) {
            filePath +=fileName;
            Document document = new SAXReader().read(new File(filePath));
            Element rootElement = document.getRootElement();
            Element msgs = rootElement.element("msgs");
            String charset = msgs.attributeValue("charset", "").trim();
            if(!charset.isEmpty()){
                //设置nio发送的字符编码级
                Charset.forName(charset);
            }
            List<Element> elements= msgs.elements();
            for (int i = 0,j=elements.size(); i < j; i++) {

            }
        }

    }




    //递归复制文件中的内容
    public static  void   recursionCopyFile(String filePath, String copyPath){
        File file = new File(filePath);
        File copyFile = new File(copyPath);
        System.out.println("第"+a+"次，地址为："+file.getAbsolutePath());
        String[] list = file.list();
        if(list.length>0) {
            for (int i = 0; i < list.length; i++) {
                File file1 = new File(file.getAbsoluteFile() + "/" + list[i]);
                if (file1.isDirectory()) {
                    filePath = file.getAbsoluteFile() + "/" + list[i];
                    copyPath = copyFile.getAbsolutePath() + "/" + list[i];
                    if (!copyFile.exists()) {
                        copyFile.mkdirs();
                    }
                    recursionCopyFile(filePath, copyPath);
                } else {
                    if (!copyFile.exists()) {
                        copyFile.mkdirs();
                    }
                    copyPath = copyFile.getAbsoluteFile() + "/" + list[i];
                    File copyFile2 = new File(copyPath);
                    try (
                            InputStream inputStream = new FileInputStream(file1);
                            OutputStream outputStream = new FileOutputStream(copyFile2);
                    ) {
                        byte[] bytes = new byte[1024];
                        int len = 0;
                        while ((len = inputStream.read(bytes)) > 0) {
                            outputStream.write(bytes, 0, len);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

            }
        }else {
            if (!copyFile.exists()) {
                copyFile.mkdirs();
            }
        }
        a++;
    }
    private static int  a=1;
    //递归求和
    public static void sum(int b,int c){
        //1+2+3+4+5
        if(b==0){
            System.out.println(c);
        }else {
            c = c+ b--;
            sum(b,c);
        }
        a++;
        System.out.println("a="+a);
    }

    public static void toZip(List<File> srcFiles, OutputStream out) throws RuntimeException {
        long start = System.currentTimeMillis();
        ZipOutputStream zos = null;
        try {
            zos = new ZipOutputStream(out);
            for (File srcFile : srcFiles) {

                byte[] buf = new byte[Const.BUFFER_SIZE];
                zos.putNextEntry(new ZipEntry(srcFile.getName()));
                int len;
                FileInputStream in = new FileInputStream(srcFile);
                while ((len = in.read(buf)) != -1) {
                    zos.write(buf, 0, len);
                }
                zos.closeEntry();
                in.close();
            }
            long end = System.currentTimeMillis();
            System.out.println("压缩完成，耗时：" + (end - start) + " ms");
        } catch (Exception e) {
            throw new RuntimeException("zip error from ZipUtils", e);
        } finally {
            if (zos != null) {
                try {
                    zos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }


}
