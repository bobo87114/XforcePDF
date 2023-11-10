package com.xforceplus.taxware.microservice.voucher.sdk.utils;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.util.Hashtable;

/**
 * 二维码工具类--ZXING
 *
 * @author Bobo
 * @create 2018/9/29 11:24
 * @since 1.0.0
 */
public class QrCodeUtils {
    public static String FORMAT_PNG = "png";

    /**
     * 生成二维码
     * @param content
     * @param format
     * @param width
     * @param height
     * @return
     * @throws Exception
     */
    public static byte[] createQr(String content, String format, int width, int height) throws Exception {
        //生成二维码
        Hashtable<EncodeHintType, Object> hints = new Hashtable<>();
        hints.put(com.google.zxing.EncodeHintType.CHARACTER_SET, "utf-8");
        hints.put(com.google.zxing.EncodeHintType.MARGIN, 0);
        BitMatrix bitMatrix = new MultiFormatWriter().encode(content, BarcodeFormat.QR_CODE, width, height, hints);
        BufferedImage bufferedImage = MatrixToImageWriter.toBufferedImage(bitMatrix);
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        ImageIO.write(bufferedImage, format, os);
        return os.toByteArray();
    }
}
