package com.xforceplus.taxware.microservice.voucher.sdk.utils;

import com.itextpdf.text.Image;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfSignatureAppearance;
import com.itextpdf.text.pdf.PdfStamper;
import com.itextpdf.text.pdf.security.*;

import java.io.*;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.cert.Certificate;
import java.util.HashMap;
import java.util.Map;

/**
 * PDF签名
 *
 * @author Bobo
 * @create 2018/9/29 10:06
 * @since 1.0.0
 */
public class SignPDFUtils {
    /**
     *  PDF 签名
     * @param inputStream
     * @param certInputStream
     * @param certPwd
     * @param signClearPath
     * @param signBytes
     * @param imagePath
     * @param digestAlgorithm
     * @param provider
     * @param subfilter
     * @param reason
     * @param location
     * @return
     */
    public static Map signPdf(InputStream inputStream // 需要签章的pdf文件流
            , InputStream certInputStream // 签名证书文件流
            , String certPwd// 签名私钥
            , String signClearPath//签章北京透明图片
            , byte[] signBytes// 签名图片位置
            , String imagePath// 省份图片
            , String digestAlgorithm // 摘要算法名称，例如SHA-1
            , String provider // 密钥算法提供者，可以为null
            , MakeSignature.CryptoStandard subfilter // 数字签名格式，itext有2种
            , String reason // 签名的原因，显示在pdf签名属性中，随便填
            , String location) {
        byte[] signedFileData = null;
        Map resultMap = new HashMap();
        String ret = "-1";
        String desc = "签名失败";
        ByteArrayOutputStream result = new ByteArrayOutputStream();
        try {

            //将证书文件放入指定路径，并读取keystore ，获得私钥和证书链
            KeyStore ks = KeyStore.getInstance("PKCS12");
            ks.load(certInputStream, certPwd.toCharArray());
            String alias = ks.aliases().nextElement();
            PrivateKey pk = (PrivateKey) ks.getKey(alias, certPwd.toCharArray());
            Certificate[] chain = ks.getCertificateChain(alias);
            inputStream = inputStream2ResetInputStream(inputStream);
            PdfReader reader = new PdfReader(inputStream);
            int pages = reader.getNumberOfPages();


            for (int i = 0; i < pages; i++) {
//	            	SignatureInfo signatureInfo = signatureInfos.get(i);
                ByteArrayOutputStream tempArrayOutputStream = new ByteArrayOutputStream();
                inputStream.reset();
                reader = new PdfReader(inputStream);
                //创建签章工具PdfStamper ，最后一个boolean参数是否允许被追加签名
                PdfStamper stamper = PdfStamper.createSignature(reader, tempArrayOutputStream, '\0', null, true);
                if (i == 0) {
                    // 增加省份图片
                    PdfContentByte over = stamper.getOverContent(1);// 设置在第几页打印印章
                    Image otherImage = Image.getInstance(imagePath);// 选择图片
                    otherImage.setAlignment(1);
                    otherImage.scaleAbsolute(80, 50);// 控制图片大小
                    otherImage.setAbsolutePosition(265, 312);// 控制图片位置水平 垂直
                    over.addImage(otherImage);

                    Image otherImage2 = Image.getInstance(signBytes);// 选择图片
                    otherImage2.setAlignment(2);
                    otherImage2.scaleAbsolute(108.8f, 81.3f);// 控制图片大小
                    otherImage2.setAbsolutePosition(470, 7.5f);// 控制图片位置水平 垂直 //477, 8, 607, 94;488, 2, 618, 88
                    over.addImage(otherImage2);

                    for (int j = 2; j < pages + 1; j++) {
                        // 增加章图片
                        PdfContentByte over2 = stamper.getOverContent(j);// 设置在第几页打印印章
                        Image otherImage3 = Image.getInstance(signBytes);// 选择图片
                        otherImage3.setAlignment(j + 1);
                        otherImage3.scaleAbsolute(95.8f, 70.3f);// 控制图片大小
                        otherImage3.setAbsolutePosition(62.5f, 20.5f);// 控制图片位置水平 垂直
                        over2.addImage(otherImage3);
                    }
                }
                // 获取数字签章属性对象
                PdfSignatureAppearance appearance = stamper.getSignatureAppearance();
                appearance.setReason(reason);
                appearance.setLocation(location);
                //设置签名的签名域名称，多次追加签名的时候，签名预名称不能一样，图片大小受表单域大小影响（过小导致压缩）
                if (i == 0) {
                    appearance.setVisibleSignature(new Rectangle(488, 2, 618, 88), (i + 1), "Signature" + (i + 1));
                } else {
                    appearance.setVisibleSignature(new Rectangle(60, 15, 160, 95), (i + 1), "Signature" + (i + 1));
                }
                //读取图章图片
                Image image = Image.getInstance(signClearPath);
//	                Image image = Image.getInstance(signPath);
                appearance.setSignatureGraphic(image);
                appearance.setCertificationLevel(PdfSignatureAppearance.NOT_CERTIFIED);
                //设置图章的显示方式，如下选择的是只显示图章（还有其他的模式，可以图章和签名描述一同显示）
                appearance.setRenderingMode(PdfSignatureAppearance.RenderingMode.GRAPHIC);
                // 摘要算法
                ExternalDigest digest = new BouncyCastleDigest();
                // 签名算法
                ExternalSignature signature = new PrivateKeySignature(pk, digestAlgorithm, null);
                // 调用itext签名方法完成pdf签章
                MakeSignature.signDetached(appearance, digest, signature, chain, null, null, null, 0, subfilter);
                //定义输入流为生成的输出流内容，以完成多次签章的过程
                inputStream = new ByteArrayInputStream(tempArrayOutputStream.toByteArray());
                result = tempArrayOutputStream;
            }
            reader.close();
            ret = "0";
            desc = "签名成功";
            signedFileData = result.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
            ret = "-1";
            desc = "签名失败：" + "[" + getStrForLen(e.getMessage(), 20) + "]";
        } finally {
            try {
                if (null != inputStream) {
                    inputStream.close();
                }
                if (null != result) {
                    result.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        resultMap.put("ret", ret);
        resultMap.put("desc", desc);
        resultMap.put("signedFileData", signedFileData);
        return resultMap;
    }

    public static String getStrForLen(String str, int len) {
        str = str + "";
        if (str.length() >= len) {
            str = str.substring(0, len - 1);
        }
        return str;
    }

    public static InputStream inputStream2ResetInputStream(InputStream inputStream) {
        //封装签章信息
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        InputStream bis = null;
        try {
            BufferedInputStream br = new BufferedInputStream(inputStream);
            byte[] b = new byte[1024];
            for (int c = 0; (c = br.read(b)) != -1; ) {
                bos.write(b, 0, c);
            }
            b = null;
            br.close();

            // 生成一个新的可重复读取的InputStream
            //这个时候的bis是可以被多次重复读取，close对其无效，但是要注意每次读前，调用bis.reset();方法需要将游标重置到流的头部。
            bis = new ByteArrayInputStream(bos.toByteArray());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bis;
    }

}
