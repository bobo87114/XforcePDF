package com.xforceplus.taxware.microservice.voucher.sdk.utils.image;

import com.alibaba.fastjson.JSONObject;
import com.itextpdf.text.pdf.codec.Base64;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class CreateSeal {
    private static final Logger logger = LoggerFactory.getLogger(CreateSeal.class);
    private static final String finance_sealName = "财务专用章";
    private static final String contract_sealName = "电子合同章";
    private static final String person_sealName = "人名章";
    private static final String legal_sealName = "法人章";

    public CreateSeal() {
    }

    public static Map createSeal(String sealTemplateType, String content1, String content2, String content3) {
        Map result = new HashMap();
        if (StringUtils.isEmpty(sealTemplateType)) {
            logger.error("miss some params.");
            result.put("code", -1);
            result.put("message", "error!");
            return result;
        } else {
            try {
                JSONObject templateRule = SealTemplateDrawUtils.getSealTemplateByTypeName(sealTemplateType);
                if (templateRule == null) {
                    logger.error("miss some params.");
                    result.put("code", -2);
                    result.put("message", "error!");
                    return result;
                } else {
                    BufferedImage bufferedImage = null;
                    Map<String, Object> parameters = new HashMap();
                    if ("财务专用章".equals(sealTemplateType)) {
                        parameters.put("content1", content1);
                        parameters.put("content2", content2);
                        parameters.put("content3", content3);
                        bufferedImage = SealTemplateDrawUtils.drawSeal((BufferedImage)null, templateRule, parameters, (Properties)null);
                    } else {
                        if (sealTemplateType.equals("电子合同章")) {
                            parameters.put("topName", content1);
                            parameters.put("centerName", content2);
                        } else if (sealTemplateType.equals("人名章")) {
                            parameters.put("personalName", content1);
                        } else if (sealTemplateType.equals("法人章")) {
                            parameters.put("legalPersonName", content1);
                        }

                        bufferedImage = SealTemplateDrawUtils.drawSeal((BufferedImage)null, templateRule, parameters, (Properties)null, (String)null);
                    }

                    String imgBase64 = Base64.encodeBytes(SealUtils.bufferedImageToBytes(bufferedImage, "png"));
                    result.put("sealImg", imgBase64);
                    return result;
                }
            } catch (Exception var9) {
                var9.printStackTrace();
                result.put("code", -3);
                result.put("message", "Exception!");
                return result;
            }
        }
    }

    public static byte[] createEllipticSeal(String content1, String content2, String content3, String color, int fontType, int size, int alpha) {
        try {
            JSONObject templateRule = SealTemplateDrawUtils.getSealTemplateByTypeName("财务专用章");
            if (templateRule == null) {
                logger.error("miss some params.");
                return null;
            } else {
                Map<String, Object> parameters = new HashMap();
                parameters.put("content1", content1);
                parameters.put("content2", content2);
                parameters.put("content3", content3);
                parameters.put("color", color);
                parameters.put("fontType", fontType);
                BufferedImage bufferedImage = SealTemplateDrawUtils.drawSeal((BufferedImage)null, templateRule, parameters, (Properties)null);
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                int height = size * 3 / 4;
                BufferedImage buffImg = new BufferedImage(size, height, 6);
                buffImg.getGraphics().drawImage(bufferedImage.getScaledInstance(size, height, 4), 0, 0, (ImageObserver)null);
                buffImg = ImageUtil.setAlpha(buffImg, alpha);
                ImageIO.write(buffImg, "png", byteArrayOutputStream);
                return byteArrayOutputStream.toByteArray();
            }
        } catch (Exception var14) {
            var14.printStackTrace();
            return null;
        }
    }

//    public static boolean base64ToImage(String imgStr, String imgFilePath) {
//        if (StringUtils.isEmpty(imgStr)) {
//            return false;
//        } else {
//            BASE64Decoder decoder = new BASE64Decoder();
//
//            try {
//                byte[] b = decoder.decodeBuffer(imgStr);
//
//                for(int i = 0; i < b.length; ++i) {
//                    if (b[i] < 0) {
//                        b[i] = (byte)(b[i] + 256);
//                    }
//                }
//
//                OutputStream out = new FileOutputStream(imgFilePath);
//                out.write(b);
//                out.flush();
//                out.close();
//                return true;
//            } catch (Exception var5) {
//                return false;
//            }
//        }
//    }

    public static void main(String[] args) {
        Map rsMap = createSeal("财务专用章", "北京某某科技有限公司", "财务专用章", "23453sdf456");
        String imgBase64 = (String)rsMap.get("sealImg");
        //base64ToImage(imgBase64, "/Users/xws/Desktop/image/seal_cwz1.jpg");
    }
}
