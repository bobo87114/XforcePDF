package com.xforceplus.taxware.microservice.voucher.sdk.utils;

import com.aliyun.oss.OSSClient;
import com.aliyun.oss.model.*;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 连接测试
 *
 * @author xcj-pc
 */

public class OSSUtils {

    // 本地配置
    private static final String ACCESS_ID = "Z2nVA9OmlwdtWaD6";
    private static final String ACCESS_KEY = "W4oMLiIn43APTVu6RfpBRtCMubug8a";
    public static final String OSS_ENDPOINT =
            "http://oss-cn-hangzhou.aliyuncs.com/";
    static OSSClient client = new OSSClient(OSS_ENDPOINT, ACCESS_ID,
            ACCESS_KEY);
    public static String bucketName = "imsc-dvlp-files";

    // 测试环境配置
//    private static final String ACCESS_ID = Constant.ACCESS_ID;
//    private static final String ACCESS_KEY = Constant.ACCESS_KEY;
//    private static final String OSS_ENDPOINT = Constant.OSS_ENDPOINT;
//    static OSSClient client = new OSSClient(OSS_ENDPOINT, ACCESS_ID, ACCESS_KEY);
//    static String bucketName = Constant.bucketName;

    // 创建文件夹
    public static void createFolder(String objectName) throws IOException {
        ObjectMetadata objectMeta = new ObjectMetadata();
        /*
         * 这里的size为0,注意OSS本身没有文件夹的概念,这里创建的文件夹本质上是一个size为0的Object,
         * dataStream仍然可以有数据
         */
        byte[] buffer = new byte[0];
        ByteArrayInputStream in = new ByteArrayInputStream(buffer);
        objectMeta.setContentLength(0);
        try {
            client.putObject(bucketName, objectName, in, objectMeta);
        } finally {
            in.close();
        }

    }

    // 上传文件
    public static void putObject(String key, String filePath) throws FileNotFoundException {
        // 获取指定文件的输入流
        File file = new File(filePath);
        InputStream content = new FileInputStream(file);
        // 创建上传Object的Metadata
        ObjectMetadata meta = new ObjectMetadata();
        // 必须设置ContentLength
        meta.setContentLength(file.length());
        Map map = new HashMap();
        map.put("folderName", "20160307");
        meta.setUserMetadata(map);
        // 上传Object.
        PutObjectResult result = client.putObject(bucketName, key, content, meta);
        // 打印ETag
        System.out.println(result.getETag());
    }

    // 传入文件流上传文件
    public static void putObjectByInputStream(String key, InputStream content) throws FileNotFoundException {

        // 创建上传Object的Metadata
        ObjectMetadata meta = new ObjectMetadata();
        // 必须设置ContentLength
        // meta.setContentLength(file.length());
        // 上传Object.
        PutObjectResult result = client.putObject(bucketName, key, content, meta);
        // 打印ETag
        System.out.println(result.getETag());
    }

    // 删除文件
    public static void deleteObject(String key) {
        // 删除Object
        client.deleteObject(bucketName, key);
        List<String> keys = new ArrayList<String>();
        keys.add("key0");
        keys.add("key1");
        keys.add("key2");

        // DeleteObjectsResult deleteObjectsResult = client.deleteObjects(
        // new DeleteObjectsRequest(bucketName).withKeys(keys));
        // List<String> deletedObjects =
        // deleteObjectsResult.getDeletedObjects();
        // ObjectMetadata objectMetadata =
        // client.getObjectMetadata(bucketName,"");
    }

    // 下载文件
    public static void download(OutputStream out, String key) {
        GetObjectRequest getObjectRequest = new GetObjectRequest(bucketName, key);
        // 下载Object到文件
        OSSObject ossObject = client.getObject(getObjectRequest);
        InputStream in = ossObject.getObjectContent();
        int read = 0;
        byte[] bufs = new byte[1024 * 10];
        try {
            while ((read = in.read(bufs, 0, 1024 * 10)) != -1) {
                out.write(bufs, 0, read);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (in != null || out != null) {
                    in.close();
                    out.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /*
     * 下载文件到某路径下（服务器地址，暂用于结算单压缩包解压存储路径）
     */
    public static void downLoadToLocalPath(String key, String localKey) {
        // 下载文件
        GetObjectRequest getObjectRequest = new GetObjectRequest(bucketName, key);
        // 下载Object到文件
        ObjectMetadata objectMetadata = client.getObject(getObjectRequest, new File(localKey));

    }

    // 获取OSS文件流
    public static InputStream getOSSFileInputStream(String key) {
        GetObjectRequest getObjectRequest = new GetObjectRequest(bucketName, key);
        // 下载Object到文件
        OSSObject ossObject = client.getObject(getObjectRequest);
        InputStream in = ossObject.getObjectContent();
        return in;
    }

    // 获取OSS文件流,判断文件是否存在,不存在返回null,防止报错.
    public static InputStream getOSSFileInputStreamNoErr(String key) {
        // 下载Object到文件
        if (client.doesObjectExist(bucketName, key)) {

            OSSObject ossObject = client.getObject(bucketName, key);
            InputStream in = ossObject.getObjectContent();
            return in;
        } else {
            return null;
        }
    }

    // 获取文件流
    public static byte[] download(String key) {
        ByteArrayOutputStream baos = null;
        InputStream in = null;
        try {

            GetObjectRequest getObjectRequest = new GetObjectRequest(bucketName, key);
            // 下载Object到文件
            OSSObject ossObject = client.getObject(getObjectRequest);
            in = ossObject.getObjectContent();
            baos = new ByteArrayOutputStream();
            int i = -1;
            while ((i = in.read()) != -1) {
                baos.write(i);
            }
            return baos.toByteArray();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (baos != null) {
                try {
                    baos.close();
                } catch (Exception e2) {
                    e2.printStackTrace();
                }
            }
            if (in != null) {
                try {
                    in.close();
                } catch (Exception e2) {
                    e2.printStackTrace();
                }
            }
        }
        return null;
    }

    /**
     * 批量删除,，某日期前的文件
     *
     * @param path
     */
    public static void deleteObjectBatch(String path, String dateStr) {
        String tmpDateStr = path + dateStr;// 当前日期类型文件夹
        System.out.println("正在删除的文件夹为：" + path);// 当前path包

        ObjectListing listing = client.listObjects(bucketName, path);
        listing.setMaxKeys(10000);
        // 遍历所有Object
        for (OSSObjectSummary objectSummary : listing.getObjectSummaries()) {
            String key = objectSummary.getKey();
            System.out.println("全文件：" + key);
            try {
                int index = key.indexOf("/", key.indexOf("/") + 1);
                if (index >= 0) {
                    String tmpData = key.substring(0, index);
                    int n = tmpDateStr.compareToIgnoreCase(tmpData);
                    if (n > 0) {
                        // 删除
                        deleteObject(key);
                    }
                }
            } catch (Exception e) {
                // TODO: handle exception
            }
        }
    }

    /**
     * 分块上传1
     *
     * @param key
     * @param path
     * @return
     * @throws Exception
     */
    public static String multipartUpload(String key, String path) throws Exception {

        InitiateMultipartUploadRequest initiateMultipartUploadRequest = new InitiateMultipartUploadRequest(bucketName,
                key);
        InitiateMultipartUploadResult initiateMultipartUploadResult = client
                .initiateMultipartUpload(initiateMultipartUploadRequest);

        // 打印UploadId
        System.out.println("UploadId: " + initiateMultipartUploadResult.getUploadId());
        final long partSize = 1024 * 1024 * 5L;
        System.out.println(partSize);

        File partFile = new File(path);

        // 计算分块数目
        int partCount = (int) (partFile.length() / partSize);
        if (partFile.length() % partSize != 0) {
            partCount++;
        }
        System.out.println(
                "==================准备上传OSS，文件大小 ： " + partFile.length() / (1024 * 1024) + "M，分成：" + partCount + "块儿");
        // 新建一个List保存每个分块上传后的ETag和PartNumber
        List partETags = new ArrayList();

        long startTime = System.currentTimeMillis();

        for (int i = 0; i < partCount; i++) {
            System.out.println("正在上传第" + (i + 1) + "部分");
            // 获取文件流
            long startTime1 = System.currentTimeMillis();
            FileInputStream fis = new FileInputStream(partFile);
            long endTime1 = System.currentTimeMillis();
            System.out.println("==================文件创建的时间为：" + (endTime1 - startTime1) + "ms");
            int fisSize = fis.available();
            // 跳到每个分块的开头
            long skipBytes = partSize * i;
            fis.skip(skipBytes);

            // 计算每个分块的大小
            long size = partSize < partFile.length() - skipBytes ? partSize : partFile.length() - skipBytes;

            // 创建UploadPartRequest，上传分块
            UploadPartRequest uploadPartRequest = new UploadPartRequest();
            uploadPartRequest.setBucketName(bucketName);
            uploadPartRequest.setKey(key);
            uploadPartRequest.setUploadId(initiateMultipartUploadResult.getUploadId());
            uploadPartRequest.setInputStream(fis);
            uploadPartRequest.setPartSize(size);
            uploadPartRequest.setPartNumber(i + 1);
            UploadPartResult uploadPartResult = client.uploadPart(uploadPartRequest);

            // 将返回的PartETag保存到List中。
            partETags.add(uploadPartResult.getPartETag());

            // 关闭文件
            fis.close();
        }
        long endTime = System.currentTimeMillis();
        System.out.println("==================上传OSS完成，花费时间约：" + (endTime - startTime) + " ms");
        CompleteMultipartUploadRequest completeMultipartUploadRequest = new CompleteMultipartUploadRequest(bucketName,
                key, initiateMultipartUploadResult.getUploadId(), partETags);

        // 完成分块上传
        CompleteMultipartUploadResult completeMultipartUploadResult = client
                .completeMultipartUpload(completeMultipartUploadRequest);

        // 打印Object的ETag
        System.out.println(completeMultipartUploadResult.getETag());
        return completeMultipartUploadResult.getETag();
    }

    /**
     * 从OSS上下载文件到指定位置
     *
     * @param srcXlsSummaryPath
     * @param xlsStorePath
     */
    public static void genYearReport(String srcXlsSummaryPath, String xlsStorePath) {
        InputStream in = OSSUtils.getOSSFileInputStream(srcXlsSummaryPath);
        HSSFWorkbook wb = null;
        FileOutputStream fileOut = null;
        try {
            wb = new HSSFWorkbook(in);

            fileOut = new FileOutputStream(xlsStorePath);
            wb.write(fileOut);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (fileOut != null) {
                    fileOut.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 获取OSS文件访问路径
     *
     * @param key
     * @return
     */
    public static String getOSSFileURL(String key) {
        if (client.doesObjectExist(bucketName, key)) {
            String url;
            if (OSS_ENDPOINT.contains("http://")) {
                url = OSS_ENDPOINT.replace("http://", "");
                url = "http://" + bucketName + "." + url;
            } else if (OSS_ENDPOINT.contains("https://")) {
                url = OSS_ENDPOINT.replace("https://", "");
                url = "https://" + bucketName + "." + url;
            } else {
                url = bucketName + "." + OSS_ENDPOINT;
            }
            url += key;
            return url;
        } else {
            return null;
        }
    }


    @SuppressWarnings("unused")
    public static void main(String[] args) {
        String filePath = "/Users/lvwenjing/Desktop/副本结算单.xls";
        // String filePath = "C:/Users/lvwenjing/Desktop/体外认证导入模板.xls";
        // File file = new File(filePath);
        // try {
        // createFolder("测试/");
        // } catch (IOException e) {
        // e.printStackTrace();
        // }
        try {
            putObject("tmp/20160307/结算单导入模板.xls", filePath);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        // download("测试/266300测试.png",
        // "C:/Users/lvwenjing/Desktop/266300测试.png");
        // download("测试/测试2.txt", "C:/Users/lvwenjing/Desktop/");
        // deleteObject("test/ceshi.xls");
        // deleteObject("importTemplates/VATInvoice.xls");
        // deleteObject("importTemplates/AuthVATSpecialInvoice.xls");
        // deleteObject("importTemplates/settlementTemplate.xls");
        // 列出Bucket内所有文件
        // 获取指定bucket下的所有Object信息
        ObjectListing listing = client.listObjects(bucketName, "tmp/20160307/");
        // deleteObject("tmp/20160307/");
        // ObjectListing listing = client.listObjects(bucketName);
        System.out.println(listing.getMaxKeys());
        listing.setMaxKeys(1000);
        System.out.println(listing.getMaxKeys());
        // 遍历所有Object
        for (OSSObjectSummary objectSummary : listing.getObjectSummaries()) {
            String key = objectSummary.getKey();
            ObjectMetadata objectMetadata = client.getObjectMetadata(bucketName, key);
            System.out.println("全文件：" + key);
            try {
                int index = key.indexOf("/", key.indexOf("/") + 1);
                String tmpData = key.substring(0, index);
                System.out.println("截取后：" + tmpData);
                int n = "tmp/20160308".compareToIgnoreCase(tmpData);
                System.out.println("比较大小：" + n);
                if (n > 0) {
                    // 删除
                }
            } catch (Exception e) {
                // TODO: handle exception
            }
        }
        // InputStream in =
        // getOSSFileInputStream("reportTemplate/eea408a9-3a31-4d2a-9a4c-a002a910e4cc.xls");
        // System.out.println(in);
        // try {
        // putObjectByInputStream("test/ceshi.xls", in);
        // } catch (FileNotFoundException e) {
        // // TODO Auto-generated catch block
        // e.printStackTrace();
        // }
        // try {
        // HSSFWorkbook wb = new HSSFWorkbook(in);
        // System.out.println(wb.getSheetName(0));
        // System.out.println(wb.getSheetName(1));
        // } catch (IOException e) {
        // // TODO Auto-generated catch block
        // e.printStackTrace();
        // }
    }
}
