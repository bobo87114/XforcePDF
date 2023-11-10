
package com.xforceplus.taxware.microservice.voucher.sdk.utils.image;


import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.geom.Point2D.Double;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ImageUtil {
    public static final int COLOR_MODE_RED = 1;
    public static final int COLOR_MODE_GREEN = 2;
    public static final int COLOR_MODE_BLUE = 3;
    public static final int COLOR_MODE_BLACK = 4;
    static final int STARDER_ARGB = 210;
    static final int STARDER_ARGB_LOW = 130;
    static final int STARDER_ARGB_SAMPLING_DISTANCE = 5;

    public static void main(String[] srgs) throws Exception {
        System.out.println("success");
    }

    public ImageUtil() {
    }

    public static boolean isPng(InputStream is) {
        byte[] pngData = new byte[4];

        try {
            is.read(pngData);
        } catch (IOException var4) {
            return false;
        }

        if (pngData[1] == 80 && pngData[2] == 78 && pngData[3] == 71) {
            return true;
        } else {
            try {
                is.close();
            } catch (Exception var3) {
                var3.printStackTrace();
            }

            return false;
        }
    }

    public static boolean isPng(byte[] pngData) {
        return pngData[1] == 80 && pngData[2] == 78 && pngData[3] == 71;
    }

    public static void converterImage(File imgFile, String format, File formatFile) throws IOException {
        BufferedImage bIMG = ImageIO.read(imgFile);
        ImageIO.write(bIMG, format, formatFile);
    }

    public static byte[] textToImage(String text, String fontFamily, int fontWeight, int fontSize, Color fontColor) {
        try {
            if (fontSize == 0) {
                fontSize = Integer.valueOf(10);
            }

            char[] chars = text.toCharArray();
            Font font = new Font(fontFamily, fontWeight, fontSize);
            BufferedImage preImg = new BufferedImage(fontSize, fontSize, 1);
            Graphics2D preGD = preImg.createGraphics();
            FontMetrics preM = preGD.getFontMetrics(font);
            int width = preM.stringWidth(text);
            int height = preM.getHeight() - preM.getDescent();
            BufferedImage buffImg = new BufferedImage(width, height, 3);
            Graphics2D gd = buffImg.createGraphics();
            FontRenderContext context = gd.getFontRenderContext();
            gd.setFont(font);
            gd.setColor(fontColor);
            gd.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            gd.drawString(text, 0, fontSize - preM.getDescent() / 2);
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            ImageIO.write(buffImg, "png", out);
            return out.toByteArray();
        } catch (Exception var16) {
            var16.printStackTrace();
            return null;
        }
    }

    public static byte[] textToImage(String text, String fontFamily, int fontWeight, int fontSize, double maxWidth, int forceRowNum, Color color) {
        try {
            if (fontSize == 0) {
                fontSize = Integer.valueOf(10);
            }

            char[] chars = text.toCharArray();
            Font font = new Font(fontFamily, fontWeight, fontSize);
            BufferedImage preImg = new BufferedImage(fontSize, fontSize, 1);
            Graphics2D preGD = preImg.createGraphics();
            FontMetrics preM = preGD.getFontMetrics(font);
            int charWidth = fontSize;
            int width = preM.stringWidth(text);
            if (Math.ceil((double)width / maxWidth) < (double)forceRowNum) {
                forceRowNum = forceRowNum;
            } else {
                forceRowNum = (int)Math.ceil((double)width / maxWidth);
            }

            width = (int)((double)width / (double)forceRowNum);
            int height = forceRowNum * (preM.getHeight() - preM.getDescent());
            List<String> rows = new ArrayList();
            StringBuffer sb = new StringBuffer();

            for(int i = 0; i < text.length(); ++i) {
                int tempWidth = preM.stringWidth(sb.toString());
                if (tempWidth >= width + charWidth) {
                    rows.add(sb.toString());
                    sb = new StringBuffer();
                    sb.append(chars[i]);
                } else {
                    sb.append(chars[i]);
                }

                if (i == text.length() - 1) {
                    rows.add(sb.toString());
                }
            }

            width += charWidth * 2;
            BufferedImage buffImg = new BufferedImage(width, height, 3);
            Graphics2D gd = buffImg.createGraphics();
            gd.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            FontRenderContext context = gd.getFontRenderContext();
            font.getStringBounds(text, context);
            gd.setFont(font);
            gd.setColor(color);

            for(int i = 0; i < rows.size(); ++i) {
                gd.drawString((String)rows.get(i), 0, fontSize * (i + 1) - preM.getDescent() / 2);
            }

            ByteArrayOutputStream out = new ByteArrayOutputStream();
            ImageIO.write(buffImg, "png", out);
            return out.toByteArray();
        } catch (Exception var25) {
            var25.printStackTrace();
            return null;
        }
    }

    public static byte[] mergeImage(byte[] sourceImage, float sourceImageScale, byte[] targetImage, int onTargetX, int onTargetY) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        try {
            BufferedImage imageSource = ImageIO.read(new ByteArrayInputStream(sourceImage));
            BufferedImage imageTarget = ImageIO.read(new ByteArrayInputStream(targetImage));
            int sourceWidth = imageSource.getWidth();
            int sourceHeight = imageSource.getHeight();
            if (sourceImageScale != 1.0F) {
                sourceWidth = (int)((float)imageSource.getWidth() * sourceImageScale);
                sourceHeight = (int)((float)imageSource.getHeight() * sourceImageScale);
            }

            int targetWidth = imageTarget.getWidth();
            int targetHeight = imageTarget.getHeight();
            int destWidth = sourceWidth + onTargetX > targetWidth ? sourceWidth + onTargetX : targetWidth;
            int destHeight = sourceHeight + onTargetY > targetHeight ? sourceHeight + onTargetY : targetHeight;
            BufferedImage imageDest = new BufferedImage(destWidth, destHeight, 6);
            Graphics2D g = imageDest.createGraphics();
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g.setStroke(new BasicStroke(1.0F));
            g.drawImage(imageTarget, 0, 0, (ImageObserver)null);
            g.drawImage(imageSource.getScaledInstance(sourceWidth, sourceHeight, 4), onTargetX, onTargetY, (ImageObserver)null);
            ImageIO.write(imageDest, "png", out);
            return out.toByteArray();
        } catch (IOException var16) {
            var16.printStackTrace();
            return null;
        }
    }

    public static byte[] mergeImage(byte[] sourceImage, byte[] targetImage, int onTargetX, int onTargetY, int onTagetMaxWidth, int onTagetMaxHeight) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        try {
            BufferedImage image1 = ImageIO.read(new ByteArrayInputStream(sourceImage));
            int sourceWidth = image1.getWidth();
            int sourceHeight = image1.getHeight();
            float scale = 1.0F;
            float sourceRatio = (float)sourceWidth / (float)sourceHeight;
            float targetRatio = (float)onTagetMaxWidth / (float)onTagetMaxHeight;
            if (sourceRatio > targetRatio) {
                scale = (float)onTagetMaxWidth / (float)sourceWidth;
            } else {
                scale = (float)onTagetMaxHeight / (float)sourceHeight;
            }

            BufferedImage image2 = null;
            if (targetImage == null) {
                image2 = new BufferedImage((int)((float)image1.getWidth() * scale + (float)onTargetX), (int)((float)image1.getHeight() * scale + (float)onTargetY), 6);
            } else {
                image2 = ImageIO.read(new ByteArrayInputStream(targetImage));
            }

            float combinedWidth = (float)image1.getWidth() * scale + (float)onTargetX > (float)image2.getWidth() ? (float)image1.getWidth() * scale + (float)onTargetX : (float)image2.getWidth();
            float combinedHeight = (float)image1.getHeight() * scale + (float)onTargetY > (float)image2.getHeight() ? (float)image1.getHeight() * scale + (float)onTargetY : (float)image2.getHeight();
            BufferedImage combined = new BufferedImage((int)combinedWidth, (int)combinedHeight, 6);
            Graphics2D g = combined.createGraphics();
            g.setStroke(new BasicStroke(1.0F));
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g.drawImage(image2, 0, 0, (ImageObserver)null);
            g.drawImage(image1.getScaledInstance((int)((float)sourceWidth * scale), (int)((float)sourceHeight * scale), 4), onTargetX, onTargetY, (ImageObserver)null);
            ImageIO.write(combined, "png", out);
            return out.toByteArray();
        } catch (IOException var18) {
            var18.printStackTrace();
            return null;
        }
    }
//
//    public static byte[] create41pxUrlQRCode(String url, byte[] logoBytes) {
//        ByteArrayOutputStream out = new ByteArrayOutputStream();
//
//        try {
//            Qrcode qrcodeHandler = new Qrcode();
//            qrcodeHandler.setQrcodeErrorCorrect('L');
//            qrcodeHandler.setQrcodeEncodeMode('B');
//            qrcodeHandler.setQrcodeVersion(5);
//            int imgSize = 41;
//            byte[] urlBytes = url.getBytes("UTF-8");
//            BufferedImage image = new BufferedImage(imgSize, imgSize, 1);
//            Graphics2D gs = image.createGraphics();
//            gs.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
//            gs.setBackground(Color.WHITE);
//            gs.clearRect(0, 0, imgSize, imgSize);
//            gs.setColor(Color.BLACK);
//            int pixoff = 2;
//            if (urlBytes.length > 0 && urlBytes.length < 800) {
//                boolean[][] codeOut = qrcodeHandler.calQrcode(urlBytes);
//
//                int i;
//                int j;
//                for(i = 0; i < codeOut.length; ++i) {
//                    for(j = 0; j < codeOut.length; ++j) {
//                        if (codeOut[j][i]) {
//                            gs.fillRect(j * 1 + pixoff, i * 1 + pixoff, 1, 1);
//                        }
//                    }
//                }
//
//                if (logoBytes != null) {
//                    BufferedImage logo = ImageIO.read(new ByteArrayInputStream(logoBytes));
//                    i = logo.getWidth((ImageObserver)null) > image.getWidth() * 2 / 10 ? image.getWidth() * 2 / 10 : logo.getWidth((ImageObserver)null);
//                    j = logo.getHeight((ImageObserver)null) > image.getHeight() * 2 / 10 ? image.getHeight() * 2 / 10 : logo.getWidth((ImageObserver)null);
//                    int x = (image.getWidth() - i) / 2;
//                    int y = (image.getHeight() - j) / 2;
//                    gs.drawImage(logo, x, y, i, j, (ImageObserver)null);
//                }
//
//                gs.dispose();
//                image.flush();
//                ImageIO.write(image, "png", out);
//                return out.toByteArray();
//            } else {
//                throw new Exception("QRCode content bytes length = " + urlBytes.length + " not in [0, 800].");
//            }
//        } catch (Exception var14) {
//            var14.printStackTrace();
//            return null;
//        }
//    }
//
//    public static byte[] create78pxUrlQRCode(String url, byte[] logoBytes) {
//        ByteArrayOutputStream out = new ByteArrayOutputStream();
//
//        try {
//            Qrcode qrcodeHandler = new Qrcode();
//            qrcodeHandler.setQrcodeErrorCorrect('L');
//            qrcodeHandler.setQrcodeEncodeMode('B');
//            qrcodeHandler.setQrcodeVersion(5);
//            int imgSize = 78;
//            byte[] urlBytes = url.getBytes("UTF-8");
//            BufferedImage image = new BufferedImage(imgSize, imgSize, 1);
//            Graphics2D gs = image.createGraphics();
//            gs.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
//            gs.setBackground(Color.WHITE);
//            gs.clearRect(0, 0, imgSize, imgSize);
//            gs.setColor(Color.BLACK);
//            int pixoff = 2;
//            if (urlBytes.length > 0 && urlBytes.length < 800) {
//                boolean[][] codeOut = qrcodeHandler.calQrcode(urlBytes);
//
//                int i;
//                int j;
//                for(i = 0; i < codeOut.length; ++i) {
//                    for(j = 0; j < codeOut.length; ++j) {
//                        if (codeOut[j][i]) {
//                            gs.fillRect(j * 2 + pixoff, i * 2 + pixoff, 2, 2);
//                        }
//                    }
//                }
//
//                if (logoBytes != null) {
//                    BufferedImage logo = ImageIO.read(new ByteArrayInputStream(logoBytes));
//                    i = logo.getWidth((ImageObserver)null) > image.getWidth() * 2 / 10 ? image.getWidth() * 2 / 10 : logo.getWidth((ImageObserver)null);
//                    j = logo.getHeight((ImageObserver)null) > image.getHeight() * 2 / 10 ? image.getHeight() * 2 / 10 : logo.getWidth((ImageObserver)null);
//                    int x = (image.getWidth() - i) / 2;
//                    int y = (image.getHeight() - j) / 2;
//                    gs.drawImage(logo, x, y, i, j, (ImageObserver)null);
//                }
//
//                gs.dispose();
//                image.flush();
//                ImageIO.write(image, "png", out);
//                return out.toByteArray();
//            } else {
//                throw new Exception("QRCode content bytes length = " + urlBytes.length + " not in [0, 800].");
//            }
//        } catch (Exception var14) {
//            var14.printStackTrace();
//            return null;
//        }
//    }
//
//    public static byte[] create152pxUrlQRCode(String url, byte[] logoBytes) {
//        ByteArrayOutputStream out = new ByteArrayOutputStream();
//
//        try {
//            Qrcode qrcodeHandler = new Qrcode();
//            qrcodeHandler.setQrcodeErrorCorrect('L');
//            qrcodeHandler.setQrcodeEncodeMode('B');
//            qrcodeHandler.setQrcodeVersion(5);
//            int imgSize = 152;
//            byte[] urlBytes = url.getBytes("UTF-8");
//            BufferedImage image = new BufferedImage(imgSize, imgSize, 1);
//            Graphics2D gs = image.createGraphics();
//            gs.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
//            gs.setBackground(Color.WHITE);
//            gs.clearRect(0, 0, imgSize, imgSize);
//            gs.setColor(Color.BLACK);
//            int pixoff = 2;
//            if (urlBytes.length > 0 && urlBytes.length < 800) {
//                boolean[][] codeOut = qrcodeHandler.calQrcode(urlBytes);
//
//                int i;
//                int j;
//                for(i = 0; i < codeOut.length; ++i) {
//                    for(j = 0; j < codeOut.length; ++j) {
//                        if (codeOut[j][i]) {
//                            gs.fillRect(j * 4 + pixoff, i * 4 + pixoff, 4, 4);
//                        }
//                    }
//                }
//
//                if (logoBytes != null) {
//                    BufferedImage logo = ImageIO.read(new ByteArrayInputStream(logoBytes));
//                    i = logo.getWidth((ImageObserver)null) > image.getWidth() * 2 / 10 ? image.getWidth() * 2 / 10 : logo.getWidth((ImageObserver)null);
//                    j = logo.getHeight((ImageObserver)null) > image.getHeight() * 2 / 10 ? image.getHeight() * 2 / 10 : logo.getWidth((ImageObserver)null);
//                    int x = (image.getWidth() - i) / 2;
//                    int y = (image.getHeight() - j) / 2;
//                    gs.drawImage(logo, x, y, i, j, (ImageObserver)null);
//                }
//
//                gs.dispose();
//                image.flush();
//                ImageIO.write(image, "png", out);
//                return out.toByteArray();
//            } else {
//                throw new Exception("QRCode content bytes length = " + urlBytes.length + " not in [0, 800].");
//            }
//        } catch (Exception var14) {
//            var14.printStackTrace();
//            return null;
//        }
//    }
//
//    public static byte[] create300pxUrlQRCode(String url, byte[] logoBytes) {
//        ByteArrayOutputStream out = new ByteArrayOutputStream();
//
//        try {
//            Qrcode qrcodeHandler = new Qrcode();
//            qrcodeHandler.setQrcodeErrorCorrect('L');
//            qrcodeHandler.setQrcodeEncodeMode('B');
//            qrcodeHandler.setQrcodeVersion(5);
//            int imgSize = 300;
//            byte[] urlBytes = url.getBytes("UTF-8");
//            BufferedImage image = new BufferedImage(imgSize, imgSize, 1);
//            Graphics2D gs = image.createGraphics();
//            gs.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
//            gs.setBackground(Color.WHITE);
//            gs.clearRect(0, 0, imgSize, imgSize);
//            gs.setColor(Color.BLACK);
//            int pixoff = 2;
//            if (urlBytes.length > 0 && urlBytes.length < 800) {
//                boolean[][] codeOut = qrcodeHandler.calQrcode(urlBytes);
//
//                int i;
//                int j;
//                for(i = 0; i < codeOut.length; ++i) {
//                    for(j = 0; j < codeOut.length; ++j) {
//                        if (codeOut[j][i]) {
//                            gs.fillRect(j * 8 + pixoff, i * 8 + pixoff, 8, 8);
//                        }
//                    }
//                }
//
//                if (logoBytes != null) {
//                    BufferedImage logo = ImageIO.read(new ByteArrayInputStream(logoBytes));
//                    i = logo.getWidth((ImageObserver)null) > image.getWidth() * 2 / 10 ? image.getWidth() * 2 / 10 : logo.getWidth((ImageObserver)null);
//                    j = logo.getHeight((ImageObserver)null) > image.getHeight() * 2 / 10 ? image.getHeight() * 2 / 10 : logo.getWidth((ImageObserver)null);
//                    int x = (image.getWidth() - i) / 2;
//                    int y = (image.getHeight() - j) / 2;
//                    gs.drawImage(logo, x, y, i, j, (ImageObserver)null);
//                }
//
//                gs.dispose();
//                image.flush();
//                ImageIO.write(image, "png", out);
//                return out.toByteArray();
//            } else {
//                throw new Exception("QRCode content bytes length = " + urlBytes.length + " not in [0, 800].");
//            }
//        } catch (Exception var14) {
//            var14.printStackTrace();
//            return null;
//        }
//    }

//    public static byte[] create410pxUrlQRCode(String url, byte[] logoBytes) {
//        ByteArrayOutputStream out = new ByteArrayOutputStream();
//
//        try {
//            Qrcode qrcodeHandler = new Qrcode();
//            qrcodeHandler.setQrcodeErrorCorrect('L');
//            qrcodeHandler.setQrcodeEncodeMode('B');
//            qrcodeHandler.setQrcodeVersion(5);
//            int imgSize = 410;
//            byte[] urlBytes = url.getBytes("UTF-8");
//            BufferedImage image = new BufferedImage(imgSize, imgSize, 1);
//            Graphics2D gs = image.createGraphics();
//            gs.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
//            gs.setBackground(Color.WHITE);
//            gs.clearRect(0, 0, imgSize, imgSize);
//            gs.setColor(Color.BLACK);
//            int pixoff = 20;
//            if (urlBytes.length > 0 && urlBytes.length < 800) {
//                boolean[][] codeOut = qrcodeHandler.calQrcode(urlBytes);
//
//                int i;
//                int j;
//                for(i = 0; i < codeOut.length; ++i) {
//                    for(j = 0; j < codeOut.length; ++j) {
//                        if (codeOut[j][i]) {
//                            gs.fillRect(j * 10 + pixoff, i * 10 + pixoff, 10, 10);
//                        }
//                    }
//                }
//
//                if (logoBytes != null) {
//                    BufferedImage logo = ImageIO.read(new ByteArrayInputStream(logoBytes));
//                    i = logo.getWidth((ImageObserver)null) > image.getWidth() * 2 / 10 ? image.getWidth() * 2 / 10 : logo.getWidth((ImageObserver)null);
//                    j = logo.getHeight((ImageObserver)null) > image.getHeight() * 2 / 10 ? image.getHeight() * 2 / 10 : logo.getWidth((ImageObserver)null);
//                    int x = (image.getWidth() - i) / 2;
//                    int y = (image.getHeight() - j) / 2;
//                    gs.drawImage(logo, x, y, i, j, (ImageObserver)null);
//                }
//
//                gs.dispose();
//                image.flush();
//                ImageIO.write(image, "png", out);
//                return out.toByteArray();
//            } else {
//                throw new Exception("QRCode content bytes length = " + urlBytes.length + " not in [0, 800].");
//            }
//        } catch (Exception var14) {
//            var14.printStackTrace();
//            return null;
//        }
//    }
//
//    public static byte[] create820pxUrlQRCode(String url, byte[] logoBytes) {
//        ByteArrayOutputStream out = new ByteArrayOutputStream();
//
//        try {
//            Qrcode qrcodeHandler = new Qrcode();
//            qrcodeHandler.setQrcodeErrorCorrect('L');
//            qrcodeHandler.setQrcodeEncodeMode('B');
//            qrcodeHandler.setQrcodeVersion(16);
//            int imgSize = 820;
//            byte[] urlBytes = url.getBytes("UTF-8");
//            BufferedImage image = new BufferedImage(imgSize, imgSize, 1);
//            Graphics2D gs = image.createGraphics();
//            gs.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
//            gs.setBackground(Color.WHITE);
//            gs.clearRect(0, 0, imgSize, imgSize);
//            gs.setColor(Color.BLACK);
//            int pixoff = 40;
//            if (urlBytes.length > 0 && urlBytes.length < 800) {
//                boolean[][] codeOut = qrcodeHandler.calQrcode(urlBytes);
//
//                int i;
//                int j;
//                for(i = 0; i < codeOut.length; ++i) {
//                    for(j = 0; j < codeOut.length; ++j) {
//                        if (codeOut[j][i]) {
//                            gs.fillRect(j * 20 + pixoff, i * 20 + pixoff, 20, 20);
//                        }
//                    }
//                }
//
//                if (logoBytes != null) {
//                    BufferedImage logo = ImageIO.read(new ByteArrayInputStream(logoBytes));
//                    i = logo.getWidth((ImageObserver)null) > image.getWidth() * 2 / 10 ? image.getWidth() * 2 / 10 : logo.getWidth((ImageObserver)null);
//                    j = logo.getHeight((ImageObserver)null) > image.getHeight() * 2 / 10 ? image.getHeight() * 2 / 10 : logo.getWidth((ImageObserver)null);
//                    int x = (image.getWidth() - i) / 2;
//                    int y = (image.getHeight() - j) / 2;
//                    gs.drawImage(logo, x, y, i, j, (ImageObserver)null);
//                }
//
//                gs.dispose();
//                image.flush();
//                ImageIO.write(image, "png", out);
//                return out.toByteArray();
//            } else {
//                throw new Exception("QRCode content bytes length = " + urlBytes.length + " not in [0, 800].");
//            }
//        } catch (Exception var14) {
//            var14.printStackTrace();
//            return null;
//        }
//    }
//
//    public static byte[] createQRCode(String url, byte[] logoBytes, int codeSize, int version) {
//        ByteArrayOutputStream out = new ByteArrayOutputStream();
//
//        try {
//            Qrcode qrcodeHandler = new Qrcode();
//            qrcodeHandler.setQrcodeErrorCorrect('L');
//            qrcodeHandler.setQrcodeEncodeMode('B');
//            qrcodeHandler.setQrcodeVersion(version);
//            int imgSize = 67 + 12 * (version - 1);
//            byte[] urlBytes = url.getBytes("UTF-8");
//            BufferedImage image = new BufferedImage(imgSize, imgSize, 1);
//            Graphics2D gs = image.createGraphics();
//            gs.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
//            gs.setBackground(Color.WHITE);
//            gs.clearRect(0, 0, imgSize, imgSize);
//            gs.setColor(Color.BLACK);
//            int pixoff = 2;
//            if (urlBytes.length > 0 && urlBytes.length < 130) {
//                boolean[][] codeOut = qrcodeHandler.calQrcode(urlBytes);
//
//                int i;
//                int j;
//                for(i = 0; i < codeOut.length; ++i) {
//                    for(j = 0; j < codeOut.length; ++j) {
//                        if (codeOut[j][i]) {
//                            gs.fillRect(j * 3 + pixoff, i * 3 + pixoff, 3, 3);
//                        }
//                    }
//                }
//
//                if (logoBytes != null) {
//                    BufferedImage logo = ImageIO.read(new ByteArrayInputStream(logoBytes));
//                    i = logo.getWidth((ImageObserver)null) > image.getWidth() * 2 / 10 ? image.getWidth() * 2 / 10 : logo.getWidth((ImageObserver)null);
//                    j = logo.getHeight((ImageObserver)null) > image.getHeight() * 2 / 10 ? image.getHeight() * 2 / 10 : logo.getWidth((ImageObserver)null);
//                    int x = (image.getWidth() - i) / 2;
//                    int y = (image.getHeight() - j) / 2;
//                    gs.drawImage(logo, x, y, i, j, (ImageObserver)null);
//                }
//
//                gs.dispose();
//                image.flush();
//                ImageIO.write(image, "png", out);
//                return out.toByteArray();
//            } else {
//                System.err.println("QRCode url bytes length = " + urlBytes.length + " not in [ 0,125]. ");
//                return null;
//            }
//        } catch (Exception var16) {
//            var16.printStackTrace();
//            return null;
//        }
//    }

    public static byte[] serialNOOntoImage(String serialNO, byte[] backgroundBytes, int fontSize, float x, float y, Color color) {
        try {
            BufferedImage buffImg = ImageIO.read(new ByteArrayInputStream(backgroundBytes));
            Graphics2D gd = buffImg.createGraphics();
            FontRenderContext context = gd.getFontRenderContext();
            gd.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            Font font = new Font("TimesRoman", 1, fontSize);
            font.getStringBounds(serialNO, context);
            gd.setFont(font);
            gd.setColor(color);
            gd.drawString(serialNO, x, y);
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            ImageIO.write(buffImg, "png", out);
            return out.toByteArray();
        } catch (Exception var12) {
            var12.printStackTrace();
            return null;
        }
    }

    public static void createSealImage(String drawStr, int width, int height, Integer fontHeight, String sealImagePath) throws IOException {
        if (fontHeight == null) {
            fontHeight = 10;
        }

        BufferedImage buffImg = new BufferedImage(width, height, 1);
        Graphics2D gd = buffImg.createGraphics();
        gd.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        buffImg = gd.getDeviceConfiguration().createCompatibleImage(width, height, 3);
        gd = buffImg.createGraphics();
        FontRenderContext context = gd.getFontRenderContext();
        Font font = new Font("微软雅黑", 0, fontHeight);
        Rectangle2D bounds = font.getStringBounds(drawStr, context);
        double y = ((double)height - bounds.getHeight()) / 2.0D;
        double ascent = -bounds.getY();
        double baseY = y + ascent;
        gd.setFont(font);
        gd.setColor(Color.RED);
        gd.drawRect(0, 0, width - 1, height - 1);
        gd.drawString(drawStr, width / 2 - fontHeight * drawStr.length() / 2, (int)baseY);
        ImageIO.write(buffImg, "png", new File(sealImagePath));
    }

    public static byte[] transferAlpha(Image image) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        try {
            ImageIcon imageIcon = new ImageIcon(image);
            BufferedImage bufferedImage = new BufferedImage(imageIcon.getIconWidth(), imageIcon.getIconHeight(), 6);
            Graphics2D g2D = (Graphics2D)bufferedImage.getGraphics();
            g2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2D.drawImage(imageIcon.getImage(), 0, 0, imageIcon.getImageObserver());
            int alpha = 0;

            for(int j1 = bufferedImage.getMinY(); j1 < bufferedImage.getHeight(); ++j1) {
                for(int j2 = bufferedImage.getMinX(); j2 < bufferedImage.getWidth(); ++j2) {
                    int rgb = bufferedImage.getRGB(j2, j1);
                    if (rgb == 0) {
                        rgb = -1;
                    }

                    int R = (rgb & 16711680) >> 16;
                    int G = (rgb & '\uff00') >> 8;
                    int B = rgb & 255;
                    if (R > 210 && G > 210 && B > 210) {
                        rgb = alpha + 1 << 24 | rgb & 16777215;
                    }

                    bufferedImage.setRGB(j2, j1, rgb);
                }
            }

            g2D.drawImage(bufferedImage, 0, 0, imageIcon.getImageObserver());
            ImageIO.write(bufferedImage, "png", byteArrayOutputStream);
            return byteArrayOutputStream.toByteArray();
        } catch (Exception var12) {
            return null;
        }
    }

    public static byte[] transferAlpha(byte[] imageByte) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        try {
            ImageIcon imageIcon = new ImageIcon(imageByte);
            BufferedImage bufferedImage = new BufferedImage(imageIcon.getIconWidth(), imageIcon.getIconHeight(), 6);
            Graphics2D g2D = (Graphics2D)bufferedImage.getGraphics();
            g2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2D.drawImage(imageIcon.getImage(), 0, 0, imageIcon.getImageObserver());
            int alpha = 0;

            for(int j1 = bufferedImage.getMinY(); j1 < bufferedImage.getHeight(); ++j1) {
                for(int j2 = bufferedImage.getMinX(); j2 < bufferedImage.getWidth(); ++j2) {
                    int rgb = bufferedImage.getRGB(j2, j1);
                    if (rgb == 0) {
                        rgb = -1;
                    }

                    int R = (rgb & 16711680) >> 16;
                    int G = (rgb & '\uff00') >> 8;
                    int B = rgb & 255;
                    if (R > 210 && G > 210 && B > 210) {
                        rgb = alpha + 1 << 24 | rgb & 16777215;
                    }

                    bufferedImage.setRGB(j2, j1, rgb);
                }
            }

            g2D.drawImage(bufferedImage, 0, 0, imageIcon.getImageObserver());
            ImageIO.write(bufferedImage, "png", byteArrayOutputStream);
            return byteArrayOutputStream.toByteArray();
        } catch (Exception var12) {
            return null;
        }
    }

    private static int samplingDistance(BufferedImage bufferedImage) {
        int distance = 0;
        int rgb = bufferedImage.getRGB(0, bufferedImage.getMinY());
        int R = (rgb & 16711680) >> 16;
        int G = (rgb & '\uff00') >> 8;
        int B = rgb & 255;
        int leftTop = getMaxColor(R - (G + B) / 2, G - (R + B) / 2, B - (G + R) / 2);
        if (leftTop > 50) {
            leftTop = 0;
        }

        rgb = bufferedImage.getRGB(bufferedImage.getWidth() - 1, bufferedImage.getMinY());
        R = (rgb & 16711680) >> 16;
        G = (rgb & '\uff00') >> 8;
        B = rgb & 255;
        int rightTop = getMaxColor(R - (G + B) / 2, G - (R + B) / 2, B - (G + R) / 2);
        if (rightTop > 50) {
            rightTop = 0;
        }

        rgb = bufferedImage.getRGB(0, bufferedImage.getHeight() - 1);
        R = (rgb & 16711680) >> 16;
        G = (rgb & '\uff00') >> 8;
        B = rgb & 255;
        int leftBottom = getMaxColor(R - (G + B) / 2, G - (R + B) / 2, B - (G + R) / 2);
        if (leftBottom > 50) {
            leftBottom = 0;
        }

        rgb = bufferedImage.getRGB(bufferedImage.getWidth() - 1, bufferedImage.getHeight() - 1);
        R = (rgb & 16711680) >> 16;
        G = (rgb & '\uff00') >> 8;
        B = rgb & 255;
        int rightBottom = getMaxColor(R - (G + B) / 2, G - (R + B) / 2, B - (G + R) / 2);
        if (rightBottom > 50) {
            rightBottom = 0;
        }

        if (leftTop > distance) {
            distance = leftTop;
        }

        if (rightTop > distance) {
            distance = rightTop;
        }

        if (leftBottom > distance) {
            distance = leftBottom;
        }

        if (rightBottom > distance) {
            distance = rightBottom;
        }

        return distance;
    }

    private static int samplingStarder(BufferedImage bufferedImage) {
        int minStarder = 255;
        int rgb = bufferedImage.getRGB(0, bufferedImage.getMinY());
        int R = (rgb & 16711680) >> 16;
        int G = (rgb & '\uff00') >> 8;
        int B = rgb & 255;
        if (R < 210 && G < 210 && B < 210 && R > 130 && G > 130 && B > 130) {
            minStarder = getMinColor(R, G, B) < minStarder ? getMinColor(R, G, B) : minStarder;
        }

        rgb = bufferedImage.getRGB(bufferedImage.getWidth() - 1, bufferedImage.getMinY());
        R = (rgb & 16711680) >> 16;
        G = (rgb & '\uff00') >> 8;
        B = rgb & 255;
        if (R < 210 && G < 210 && B < 210 && R > 130 && G > 130 && B > 130) {
            minStarder = getMinColor(R, G, B) < minStarder ? getMinColor(R, G, B) : minStarder;
        }

        rgb = bufferedImage.getRGB(0, bufferedImage.getHeight() - 1);
        R = (rgb & 16711680) >> 16;
        G = (rgb & '\uff00') >> 8;
        B = rgb & 255;
        if (R < 210 && G < 210 && B < 210 && R > 130 && G > 130 && B > 130) {
            minStarder = getMinColor(R, G, B) < minStarder ? getMinColor(R, G, B) : minStarder;
        }

        rgb = bufferedImage.getRGB(bufferedImage.getWidth() - 1, bufferedImage.getHeight() - 1);
        R = (rgb & 16711680) >> 16;
        G = (rgb & '\uff00') >> 8;
        B = rgb & 255;
        if (R < 210 && G < 210 && B < 210 && R > 130 && G > 130 && B > 130) {
            minStarder = getMinColor(R, G, B) < minStarder ? getMinColor(R, G, B) : minStarder;
        }

        return minStarder;
    }

    private static int colorToRGB(int alpha, int red, int green, int blue) {
        int newPixel = + alpha;
        newPixel <<= 8;
        newPixel += red;
        newPixel <<= 8;
        newPixel += green;
        newPixel <<= 8;
        newPixel += blue;
        return newPixel;
    }

    public static byte[] otsuColorAndTransferAlpha(byte[] imageByte, int colorMode) {
        try {
            ImageIcon imageIcon = new ImageIcon(imageByte);
            BufferedImage bufferedImage = new BufferedImage(imageIcon.getIconWidth(), imageIcon.getIconHeight(), 6);
            Graphics2D g2D = (Graphics2D)bufferedImage.getGraphics();
            g2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2D.drawImage(imageIcon.getImage(), 0, 0, imageIcon.getImageObserver());
            int h = bufferedImage.getHeight();
            int w = bufferedImage.getWidth();
            int[] data = bufferedImage.getRGB(0, 0, w, h, (int[])null, 0, w);

            int len;
            int num;
            int sum;
            for(int y = 0; y < h; ++y) {
                for(len = 0; len < w; ++len) {
                    num = data[len + y * w];
                    sum = num >> 16 & 255;
                    int G = num >> 8 & 255;
                    int B = num >> 0 & 255;
                    data[len + y * w] = (int)(0.3F * (float)sum + 0.59F * (float)G + 0.11F * (float)B);
                }
            }

            int[] hist = new int[256];
            len = h * w;

            for(num = 0; num < len; ++num) {
                ++hist[data[num]];
            }

            num = h * w;
            sum = sum(hist);
            double[] res = new double[256];
            double m1 = 0.0D;
            double m2 = 0.0D;
            double w1 = 0.0D;
            double w2 = 0.0D;

            int Threshold;
            int y;
            for(Threshold = 0; Threshold < 256; ++Threshold) {
                for(y = 0; y < Threshold; ++y) {
                    m1 += (double)hist[y];
                }

                w1 = m1 / (double)num;
                w2 = 1.0D - w1;
                m2 = ((double)sum - m1) / (double)(255 - Threshold);
                m1 /= (double)(Threshold + 1);
                res[Threshold] = w1 * w2 * Math.abs(m1 - m2) * Math.abs(m1 - m2);
            }

            Threshold = maxIndex(res);

            for(y = 0; y < h; ++y) {
                for(int x = 0; x < w; ++x) {
                    if (data[x + y * w] < Threshold) {
                        data[x + y * w] = 0;
                    } else {
                        data[x + y * w] = 255;
                    }
                }
            }

            BufferedImage image = new BufferedImage(w, h, 2);
            int[] d = new int[w * h];

            for(int i = 0; i < h; ++i) {
                for(int j = 0; j < w; ++j) {
                    d[j + i * w] = -16777216 | data[j + i * w] << 16 | data[j + i * w] << 8 | data[j + i * w];
                }
            }

            image.setRGB(0, 0, w, h, d, 0, w);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(image, "png", baos);
            byte[] bytes = baos.toByteArray();
            return bytes;
        } catch (Exception var26) {
            var26.printStackTrace();
            return null;
        }
    }

    public static byte[] changeColorAndTransferAlpha(byte[] imageBytes, int colorMode, boolean isBolder) throws IOException {
        ByteArrayInputStream bais = new ByteArrayInputStream(imageBytes);
        BufferedImage bi1 = ImageIO.read(bais);
        float ratio = (float)bi1.getWidth() / (float)bi1.getHeight();
        int distance;
        if (bi1.getWidth() > 600 || bi1.getHeight() > 600) {
            int ww = bi1.getWidth();
            distance = bi1.getHeight();
            if (ratio > 1.0F) {
                ww = 600;
                distance = (int)(600.0F / ratio);
            } else {
                ww = (int)(600.0F * ratio);
                distance = 600;
            }

            BufferedImage buffImg = null;
            buffImg = new BufferedImage(ww, distance, 6);
            buffImg.getGraphics().drawImage(bi1.getScaledInstance(ww, distance, 4), 0, 0, (ImageObserver)null);
            bi1 = buffImg;
        }

        int bolderPx;
        if (ratio > 1.0F) {
            bolderPx = bi1.getWidth() / 150;
        } else {
            bolderPx = bi1.getHeight() / 150;
        }

        BufferedImage bufferedImage = bi1;
        distance = samplingDistance(bi1) + 5;
        int starder = samplingStarder(bi1);
        starder = starder < 210 ? starder : 210;
        starder -= 5;
        int rTotal = 0;
        int gTotal = 0;
        int bTotal = 0;
        int[][] greyMatrix = new int[bi1.getHeight()][bi1.getWidth()];
        int minGrey = 255;
        int redMinGrey = 255;
        int greenMinGrey = 255;
        int blueMinGrey = 255;
        int[] hist = new int[256];
        int alpha = 0;

        int j1;
        int j2;
        int rgb;
        int R;
        int G;
        int B;
        for(j1 = bi1.getMinY(); j1 < bufferedImage.getHeight(); ++j1) {
            for(j2 = bufferedImage.getMinX(); j2 < bufferedImage.getWidth(); ++j2) {
                rgb = bufferedImage.getRGB(j2, j1);
                if (rgb != 0 && rgb != -1) {
                    R = (rgb & 16711680) >> 16;
                    G = (rgb & '\uff00') >> 8;
                    B = rgb & 255;
                    if (R > starder && G > starder && B > starder) {
                        greyMatrix[j1][j2] = -1;
                    } else if (R > 130 && G > 130 && B > 130) {
                        greyMatrix[j1][j2] = (int)((double)R * 0.299D + (double)G * 0.587D + (double)B * 0.114D);
                        minGrey = greyMatrix[j1][j2] < minGrey ? greyMatrix[j1][j2] : minGrey;
                    } else if (R > getMaxColor2(G, B) + distance) {
                        greyMatrix[j1][j2] = (int)((double)R * 0.299D + (double)G * 0.587D + (double)B * 0.114D);
                        ++hist[greyMatrix[j1][j2]];
                        redMinGrey = greyMatrix[j1][j2] < redMinGrey ? greyMatrix[j1][j2] : redMinGrey;
                        ++rTotal;
                    } else if (G > getMaxColor2(R, B) + distance) {
                        greyMatrix[j1][j2] = (int)((double)R * 0.299D + (double)G * 0.587D + (double)B * 0.114D);
                        ++hist[greyMatrix[j1][j2]];
                        greenMinGrey = greyMatrix[j1][j2] < greenMinGrey ? greyMatrix[j1][j2] : greenMinGrey;
                        ++gTotal;
                    } else if (B > getMaxColor2(G, R) + distance) {
                        greyMatrix[j1][j2] = (int)((double)R * 0.299D + (double)G * 0.587D + (double)B * 0.114D);
                        ++hist[greyMatrix[j1][j2]];
                        blueMinGrey = greyMatrix[j1][j2] < blueMinGrey ? greyMatrix[j1][j2] : blueMinGrey;
                        ++bTotal;
                    } else {
                        greyMatrix[j1][j2] = (int)((double)R * 0.299D + (double)G * 0.587D + (double)B * 0.114D);
                        ++hist[greyMatrix[j1][j2]];
                        minGrey = greyMatrix[j1][j2] < minGrey ? greyMatrix[j1][j2] : minGrey;
                    }
                }
            }
        }

        byte color;
        if (maxIndex(hist) < 20) {
            color = 48;
        } else if (rTotal > gTotal + bTotal) {
            color = 114;
        } else if (gTotal > rTotal + bTotal) {
            color = 103;
        } else if (bTotal > rTotal + gTotal) {
            color = 98;
        } else {
            color = 48;
        }

        for(j1 = bufferedImage.getMinY(); j1 < bufferedImage.getHeight(); ++j1) {
            for(j2 = bufferedImage.getMinX(); j2 < bufferedImage.getWidth(); ++j2) {
                rgb = bufferedImage.getRGB(j2, j1);
                if (rgb == 0) {
                    rgb = -1;
                }

                R = (rgb & 16711680) >> 16;
                G = (rgb & '\uff00') >> 8;
                B = rgb & 255;
                boolean isVisible = false;
                if (R > starder && G > starder && B > starder) {
                    rgb = alpha + 1 << 24 | 16777215;
                } else {
                    switch(color) {
                        case 48:
                            isVisible = true;
                            R = greyMatrix[j1][j2] + 185 > 255 ? 255 : greyMatrix[j1][j2] + 185;
                            B = 0;
                            G = 0;
                            break;
                        case 98:
                            if (B > getMaxColor2(G, R) + distance) {
                                isVisible = true;
                                B = B > 185 ? B : 185;
                                G = 0;
                                R = 0;
                            } else {
                                rgb = alpha + 1 << 24 | 16777215;
                                isVisible = false;
                            }
                            break;
                        case 103:
                            if (G > getMaxColor2(R, B) + distance) {
                                isVisible = true;
                                G = G > 185 ? G : 185;
                                B = 0;
                                R = 0;
                            } else {
                                rgb = alpha + 1 << 24 | 16777215;
                                isVisible = false;
                            }
                            break;
                        case 114:
                            if (R > getMaxColor2(G, B) + distance) {
                                isVisible = true;
                                R = R > 185 ? R : 185;
                                B = 0;
                                G = 0;
                            } else {
                                rgb = alpha + 1 << 24 | 16777215;
                                isVisible = false;
                            }
                    }
                }

                if (!isVisible) {
                    rgb = alpha + 1 << 24 | 16777215;
                } else {
                    int m;
                    switch(colorMode) {
                        case 1:
                            rgb = (new Color(getMaxColor(R, G, B), 0, 0)).getRGB();
                            break;
                        case 2:
                            rgb = (new Color(0, getMaxColor(R, G, B), 0)).getRGB();
                            break;
                        case 3:
                            rgb = (new Color(0, 0, getMaxColor(R, G, B))).getRGB();
                            break;
                        case 4:
                            m = greyMatrix[j1][j2] / 3;
                            rgb = (new Color(m, m, m)).getRGB();
                    }

                    if (isBolder) {
                        for(m = 0; m < bolderPx; ++m) {
                            if (j2 > m) {
                                bufferedImage.setRGB(j2 - m, j1, rgb);

                                for(int n = 0; n < bolderPx; ++n) {
                                    if (j1 > n) {
                                        bufferedImage.setRGB(j2 - m, j1 - n, rgb);
                                    }
                                }
                            }
                        }
                    }
                }

                bufferedImage.setRGB(j2, j1, rgb);
            }
        }

        byte[] returnImgBytes = marginAdjust(bufferedImage);
        return returnImgBytes;
    }

    private static int sum(int[] arr) {
        int i = 0;
        int[] var5 = arr;
        int var4 = arr.length;

        for(int var3 = 0; var3 < var4; ++var3) {
            int n = var5[var3];
            i += n;
        }

        return i;
    }

    private static int maxIndex(double[] arr) {
        int maxIndex = 0;
        double maxValue = 0.0D;

        for(int i = 0; i < arr.length; ++i) {
            if (arr[i] > maxValue) {
                maxValue = arr[i];
                maxIndex = i;
            }
        }

        return maxIndex;
    }

    private static int maxIndex(int[] arr) {
        int maxIndex = 0;
        int maxValue = 0;

        for(int i = 0; i < arr.length; ++i) {
            if (arr[i] > maxValue) {
                maxValue = arr[i];
                maxIndex = i;
            }
        }

        return maxIndex;
    }

    private static int getMaxColor(int r, int g, int b) {
        int max = 0;
        max = r > max ? r : max;
        max = g > max ? g : max;
        max = b > max ? b : max;
        return max;
    }

    private static int getMinColor(int r, int g, int b) {
        int min = 255;
        min = r < min ? r : min;
        min = g < min ? g : min;
        min = b < min ? b : min;
        return min;
    }

    private static int getMaxColor2(int a, int b) {
        int max = 0;
        max = a > max ? a : max;
        max = b > max ? b : max;
        return max;
    }

    public static byte[] resizeImage(byte[] imageByte, int width, int height) {
        ByteArrayInputStream bais = new ByteArrayInputStream(imageByte);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        try {
            Image srcImg = ImageIO.read(bais);
            BufferedImage buffImg = null;
            buffImg = new BufferedImage(width, height, 6);
            buffImg.getGraphics().drawImage(srcImg.getScaledInstance(width, height, 4), 0, 0, (ImageObserver)null);
            ImageIO.write(buffImg, "png", byteArrayOutputStream);
        } catch (Exception var7) {
            return null;
        }

        return byteArrayOutputStream.toByteArray();
    }

    public static List<byte[]> sliceImageToBytes(byte[] imgBytes, int num, boolean isHorizontal) throws IOException {
        if (imgBytes != null && imgBytes.length != 0) {
            if (num <= 0) {
                throw new RuntimeException("Slice pieces can not be 0!");
            } else {
                ByteArrayInputStream in = new ByteArrayInputStream(imgBytes);
                BufferedImage image = ImageIO.read(in);
                return sliceImageToBytes(image, num, isHorizontal);
            }
        } else {
            throw new IOException("Image object can not be null!");
        }
    }

    public static List<byte[]> sliceImageToBytes(String filePath, int num, boolean isHorizontal) throws IOException {
        if (filePath != null && !filePath.equals("")) {
            if (num <= 0) {
                throw new RuntimeException("Slice pieces can not be 0!");
            } else {
                BufferedImage image = ImageIO.read(new File(filePath));
                return sliceImageToBytes(image, num, isHorizontal);
            }
        } else {
            throw new IOException("Image object can not be null!");
        }
    }

    public static byte[] Rotate(byte[] imgBytes, int degree) throws IOException {
        ByteArrayInputStream bais = new ByteArrayInputStream(imgBytes);
        BufferedImage src = ImageIO.read(bais);
        BufferedImage target = Rotate(src, degree);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(target, "png", baos);
        byte[] rotatedImgBytes = baos.toByteArray();
        return rotatedImgBytes;
    }

    public static BufferedImage Rotate(BufferedImage src, int degree) {
        int width = src.getWidth();
        int height = src.getHeight();
        int new_w;
        int new_h;
        if (degree <= 90) {
            new_w = (int)((double)width * Math.cos(Math.toRadians((double)degree)) + (double)height * Math.sin(Math.toRadians((double)degree)));
            new_h = (int)((double)height * Math.cos(Math.toRadians((double)degree)) + (double)width * Math.sin(Math.toRadians((double)degree)));
        } else {
            int new_radian;
            if (degree <= 180) {
                new_radian = degree - 90;
                new_w = (int)((double)height * Math.cos(Math.toRadians((double)new_radian)) + (double)width * Math.sin(Math.toRadians((double)new_radian)));
                new_h = (int)((double)width * Math.cos(Math.toRadians((double)new_radian)) + (double)height * Math.sin(Math.toRadians((double)new_radian)));
            } else if (degree <= 270) {
                new_radian = degree - 180;
                new_w = (int)((double)width * Math.cos(Math.toRadians((double)new_radian)) + (double)height * Math.sin(Math.toRadians((double)new_radian)));
                new_h = (int)((double)height * Math.cos(Math.toRadians((double)new_radian)) + (double)width * Math.sin(Math.toRadians((double)new_radian)));
            } else {
                new_radian = degree - 270;
                new_w = (int)((double)height * Math.cos(Math.toRadians((double)new_radian)) + (double)width * Math.sin(Math.toRadians((double)new_radian)));
                new_h = (int)((double)width * Math.cos(Math.toRadians((double)new_radian)) + (double)height * Math.sin(Math.toRadians((double)new_radian)));
            }
        }

        BufferedImage toStore = new BufferedImage(new_w, new_h, 1);
        Graphics2D g = toStore.createGraphics();
        AffineTransform affineTransform = new AffineTransform();
        affineTransform.rotate(Math.toRadians((double)degree), (double)(width / 2), (double)(height / 2));
        if (degree != 180) {
            AffineTransform translationTransform = findTranslation(affineTransform, src, degree);
            affineTransform.preConcatenate(translationTransform);
        }

        g.setColor(Color.WHITE);
        g.fillRect(0, 0, new_w, new_h);
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g.drawRenderedImage(src, affineTransform);
        g.dispose();
        return toStore;
    }

    private static AffineTransform findTranslation(AffineTransform at, BufferedImage bi, int angle) {
        double ytrans = 0.0D;
        double xtrans = 0.0D;
        Double p2din;
        Point2D p2dout;
        if (angle <= 90) {
            p2din = new Double(0.0D, 0.0D);
            p2dout = at.transform(p2din, (Point2D)null);
            ytrans = p2dout.getY();
            p2din = new Double(0.0D, (double)bi.getHeight());
            p2dout = at.transform(p2din, (Point2D)null);
            xtrans = p2dout.getX();
        } else if (angle <= 180) {
            p2din = new Double(0.0D, (double)bi.getHeight());
            p2dout = at.transform(p2din, (Point2D)null);
            ytrans = p2dout.getY();
            p2din = new Double((double)bi.getWidth(), (double)bi.getHeight());
            p2dout = at.transform(p2din, (Point2D)null);
            xtrans = p2dout.getX();
        } else if (angle <= 270) {
            p2din = new Double((double)bi.getWidth(), (double)bi.getHeight());
            p2dout = at.transform(p2din, (Point2D)null);
            ytrans = p2dout.getY();
            p2din = new Double((double)bi.getWidth(), 0.0D);
            p2dout = at.transform(p2din, (Point2D)null);
            xtrans = p2dout.getX();
        } else {
            p2din = new Double((double)bi.getWidth(), 0.0D);
            p2dout = at.transform(p2din, (Point2D)null);
            ytrans = p2dout.getY();
            p2din = new Double(0.0D, 0.0D);
            p2dout = at.transform(p2din, (Point2D)null);
            xtrans = p2dout.getX();
        }

        AffineTransform tat = new AffineTransform();
        tat.translate(-xtrans, -ytrans);
        return tat;
    }

    public static List<byte[]> sliceImageToBytes(BufferedImage image, int num, boolean isHorizontal) throws IOException {
        if (image == null) {
            throw new IOException("Image object can not be null!");
        } else if (num <= 0) {
            throw new RuntimeException("Slice pieces can not be 0!");
        } else {
            ByteArrayInputStream bais = new ByteArrayInputStream(marginAdjust(image));
            image = ImageIO.read(bais);
            List<byte[]> images = new ArrayList();
            int width = image.getWidth();
            int height = image.getHeight();
            int newHeight;
            int i;
            int interval;
            BufferedImage im;
            int y;
            int x;
            ByteArrayOutputStream baos;
            byte[] imgBytes;
            if (!isHorizontal) {
                newHeight = width / num;

                for(i = 0; i < num; ++i) {
                    interval = i * newHeight;
                    im = null;
                    if (num - 1 == i) {
                        im = new BufferedImage(width - interval, height, 2);
                    } else {
                        im = new BufferedImage(newHeight, height, 2);
                    }

                    for(y = im.getMinX(); y < im.getWidth(); ++y) {
                        for(x = im.getMinY(); x < im.getHeight(); ++x) {
                            im.setRGB(y, x, image.getRGB(y + interval, x));
                        }
                    }

                    baos = new ByteArrayOutputStream();
                    ImageIO.write(im, "png", baos);
                    imgBytes = baos.toByteArray();
                    images.add(imgBytes);
                }

                return images;
            } else {
                newHeight = height / num;

                for(i = 0; i < num; ++i) {
                    interval = i * newHeight;
                    im = null;
                    if (num - 1 == i) {
                        im = new BufferedImage(width, height - interval, 2);
                    } else {
                        im = new BufferedImage(width, newHeight, 2);
                    }

                    for(y = im.getMinY(); y < im.getHeight(); ++y) {
                        for(x = im.getMinX(); x < im.getWidth(); ++x) {
                            im.setRGB(x, y, image.getRGB(x, y + interval));
                        }
                    }

                    baos = new ByteArrayOutputStream();
                    ImageIO.write(im, "png", baos);
                    imgBytes = baos.toByteArray();
                    images.add(imgBytes);
                }

                return images;
            }
        }
    }

    public static byte[] marginAdjust(byte[] imgBytes) throws IOException {
        ByteArrayInputStream in = new ByteArrayInputStream(imgBytes);
        BufferedImage image = ImageIO.read(in);
        return marginAdjust(image);
    }

    public static byte[] marginAdjust(BufferedImage image) throws IOException {
        if (image == null) {
            throw new IOException("Image object can not be null!");
        } else {
            byte[] imgBytes = transferAlpha((Image)image);
            ByteArrayInputStream in = new ByteArrayInputStream(imgBytes);
            image = ImageIO.read(in);

            class Coord {
                private int x;
                private int y;

                Coord() {
                }

                public int getX() {
                    return this.x;
                }

                public void setX(int x) {
                    this.x = x;
                }

                public int getY() {
                    return this.y;
                }

                public void setY(int y) {
                    this.y = y;
                }

                public String toString() {
                    return "Coord [x=" + this.x + ", y=" + this.y + "]";
                }
            }

            Coord top = new Coord();
            Coord down = new Coord();
            Coord left = new Coord();
            Coord right = new Coord();
            Boolean checkLeft = false;

            int x;
            int y;
            for(int imageMinX = image.getMinX(); imageMinX < image.getWidth(); ++imageMinX) {
                for(imageMinX = image.getMinY(); imageMinX < image.getHeight(); ++imageMinX) {
                    if (getGray(image.getRGB(imageMinX, imageMinX)) < 210 && getGray(image.getRGB(imageMinX, imageMinX)) < 210) {
                        y = image.getRGB(imageMinX, imageMinX);
                        left.setX(imageMinX);
                        left.setY(imageMinX);
                        checkLeft = true;
                        y = (y & 16711680) >> 16;
                        imageMinX = (y & '\uff00') >> 8;
                        int var13 = y & 255;
                        break;
                    }
                }

                if (checkLeft) {
                    break;
                }
            }

            Boolean checkRight = false;

            for(x = image.getWidth() - 1; x >= image.getMinX(); --x) {
                for(y = image.getMinY(); y < image.getHeight(); ++y) {
                    if (getGray(image.getRGB(x, y)) < 210 && getGray(image.getRGB(x, y)) < 210) {
                        right.setX(x);
                        right.setY(y);
                        checkRight = true;
                        break;
                    }
                }

                if (checkRight) {
                    break;
                }
            }

            Boolean checkDown = false;

            for(y = image.getMinY(); y < image.getHeight(); ++y) {
                for(y = image.getMinX(); y < image.getWidth(); ++y) {
                    if (getGray(image.getRGB(y, y)) < 210 && getGray(image.getRGB(y, y)) < 210) {
                        down.setX(y);
                        down.setY(y);
                        checkDown = true;
                        break;
                    }
                }

                if (checkDown) {
                    break;
                }
            }

            Boolean checkTop = false;

            for(y = image.getHeight() - 1; y > image.getMinY(); --y) {
                for(x = image.getMinX(); x < image.getWidth(); ++x) {
                    if (getGray(image.getRGB(x, y)) < 210 && getGray(image.getRGB(x, y)) < 210) {
                        top.setX(x);
                        top.setY(y);
                        checkTop = true;
                        break;
                    }
                }

                if (checkTop) {
                    break;
                }
            }

            y = Math.abs(right.getX() - left.getX() + 1);
            x = Math.abs(top.getY() - down.getY() + 1);
            BufferedImage newImage = new BufferedImage(y, x, 2);

            for(int newImageMinX = newImage.getMinX(); newImageMinX < newImage.getWidth(); ++newImageMinX) {
                for(int newImageMinY = newImage.getMinY(); newImageMinY < newImage.getHeight(); ++newImageMinY) {
                    int rgb = image.getRGB(newImageMinX + left.getX(), newImageMinY + down.getY());
                    newImage.setRGB(newImageMinX, newImageMinY, rgb);
                }
            }

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(newImage, "png", baos);
            byte[] outBytes = baos.toByteArray();
            return outBytes;
        }
    }

    public static byte[] makeCircularStamp(String companyName, int diameter, int type, int alpha) throws FileNotFoundException, IOException {
        return makeCircularStamp(companyName, diameter, type, 1, alpha, (String)null);
    }

    public static byte[] makeCircularStamp(String companyName, int diameter, int type, int alpha, String bottomText) throws FileNotFoundException, IOException {
        return makeCircularStamp(companyName, diameter, type, 1, alpha, bottomText);
    }

    public static byte[] makeCircularStamp(String companyName, int diameter, int type, String bottomText) throws FileNotFoundException, IOException {
        return makeCircularStamp(companyName, diameter, type, 1, 40, bottomText);
    }

    public static byte[] makeCircularStamp(String companyName, int diameter, int type) throws FileNotFoundException, IOException {
        return makeCircularStamp(companyName, diameter, type, 1, 40, (String)null);
    }

    public static byte[] makeCircularStamp(String companyName, int diameter, int type, int colorMode, int alpha, String bottomText) throws FileNotFoundException, IOException {
        int[] topFontSizeMapping = new int[]{0, 72, 72, 72, 72, 72, 72, 72, 72, 72, 72, 66, 64, 62, 60, 58, 56, 54, 50, 48, 46, 44, 42, 40, 39, 38, 36, 35, 34, 33, 32, 32, 31};
        int[] bottomFontSizeMapping = new int[]{0, 36, 36, 36, 36, 36, 36, 36, 36, 36, 36, 33, 32, 31, 30, 29, 28, 27, 25, 24, 23, 22, 21, 20, 19, 19, 18, 17, 17, 16, 16, 16, 16};
        BufferedImage image = null;
        image = ImageIO.read(ImageUtil.class.getResourceAsStream("t" + type + "_" + colorMode + ".png"));
        Graphics2D g2 = image.createGraphics();
        switch(colorMode) {
            case 1:
                g2.setColor(new Color(230, 0, 18));
                break;
            case 2:
                g2.setColor(new Color(18, 230, 18));
                break;
            case 3:
                g2.setColor(new Color(18, 0, 230));
                break;
            case 4:
                g2.setColor(new Color(0, 0, 0));
                break;
            default:
                g2.setColor(new Color(230, 0, 18));
        }

        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        if (companyName != null) {
            drawTopAndBottomText(g2, companyName, 1, topFontSizeMapping, type, 270, -2);
        }

        if (bottomText != null) {
            drawTopAndBottomText(g2, bottomText, 2, bottomFontSizeMapping, type, -295, 4);
        }

        g2.dispose();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        BufferedImage buffImg = setAlpha(image, alpha);
        ImageIO.write(buffImg, "png", byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }

    private static void drawTopAndBottomText(Graphics2D g2, String text, int textType, int[] fontSizeMapping, int type, int radius, int charInterWidth) {
        text = text.replace("(", "（");
        text = text.replace(")", "）");
        String[] texts = new String[text.length()];

        int ilength;
        for(ilength = 0; ilength < text.length(); ++ilength) {
            texts[ilength] = text.substring(ilength, ilength + 1);
        }

        ilength = texts.length;
        int fontsize = fontSizeMapping[ilength];
        Font f = null;
        if (type == 2) {
            f = new Font("SimHei", 0, fontsize);
        } else if (type == 3) {
            f = new Font("SimSun", 1, fontsize);
        } else {
            f = new Font("Microsoft YaHei", 0, fontsize);
        }

        FontRenderContext context = g2.getFontRenderContext();
        Rectangle2D bounds = f.getStringBounds(text, context);
        double char_interval = bounds.getWidth() / (double)ilength + (double)charInterWidth;
        double ascent = 0.0D;
        if (textType == 1) {
            ascent = -bounds.getY();
        } else {
            ascent = bounds.getY();
        }

        int second = 0;
        boolean odd = false;
        int first;
        if (ilength % 2 == 1) {
            first = (ilength - 1) / 2;
            odd = true;
        } else {
            first = ilength / 2 - 1;
            second = ilength / 2;
            odd = false;
        }

        double radius2 = (double)radius - ascent;
        double x0 = 300.0D;
        double y0 = (double)(300 - radius) + ascent;
        double a = 2.0D * Math.asin(char_interval / (2.0D * radius2));
        int i;
        double aa;
        double ax;
        double ay;
        AffineTransform transform;
        Font f2;
        if (odd) {
            g2.setFont(f);
            g2.drawString(texts[first], (float)(x0 - char_interval / 2.0D), (float)y0);

            for(i = first + 1; i < ilength; ++i) {
                aa = (double)(i - first) * a;
                ax = radius2 * Math.sin(aa);
                ay = radius2 - radius2 * Math.cos(aa);
                transform = AffineTransform.getRotateInstance(aa);
                f2 = f.deriveFont(transform);
                g2.setFont(f2);
                g2.drawString(texts[i], (float)(x0 + ax - char_interval / 2.0D * Math.cos(aa)), (float)(y0 + ay - char_interval / 2.0D * Math.sin(aa)));
            }

            for(i = first - 1; i > -1; --i) {
                aa = (double)(first - i) * a;
                ax = radius2 * Math.sin(aa);
                ay = radius2 - radius2 * Math.cos(aa);
                transform = AffineTransform.getRotateInstance(-aa);
                f2 = f.deriveFont(transform);
                g2.setFont(f2);
                g2.drawString(texts[i], (float)(x0 - ax - char_interval / 2.0D * Math.cos(aa)), (float)(y0 + ay + char_interval / 2.0D * Math.sin(aa)));
            }
        } else {
            for(i = second; i < ilength; ++i) {
                aa = ((double)(i - second) + 0.5D) * a;
                ax = radius2 * Math.sin(aa);
                ay = radius2 - radius2 * Math.cos(aa);
                transform = AffineTransform.getRotateInstance(aa);
                f2 = f.deriveFont(transform);
                g2.setFont(f2);
                g2.drawString(texts[i], (float)(x0 + ax - char_interval / 2.0D * Math.cos(aa)), (float)(y0 + ay - char_interval / 2.0D * Math.sin(aa)));
            }

            for(i = first; i > -1; --i) {
                aa = ((double)(first - i) + 0.5D) * a;
                ax = radius2 * Math.sin(aa);
                ay = radius2 - radius2 * Math.cos(aa);
                transform = AffineTransform.getRotateInstance(-aa);
                f2 = f.deriveFont(transform);
                g2.setFont(f2);
                g2.drawString(texts[i], (float)(x0 - ax - char_interval / 2.0D * Math.cos(aa)), (float)(y0 + ay + char_interval / 2.0D * Math.sin(aa)));
            }
        }

    }

    public static byte[] makeCircularStamp(String companyName, String stampName, int diameter, int type, int alpha) throws Exception {
        return makeCircularStamp(companyName, stampName, diameter, type, 1, alpha, (String)null);
    }

    public static byte[] makeCircularStamp(String companyName, String stampName, int diameter, int type, int alpha, String bottomText) throws Exception {
        return makeCircularStamp(companyName, stampName, diameter, type, 1, alpha, bottomText);
    }

    public static byte[] makeCircularStamp(String companyName, String stampName, int diameter, int type) throws Exception {
        return makeCircularStamp(companyName, stampName, diameter, type, 1, 40, (String)null);
    }

    public static byte[] makeCircularStamp(String companyName, String stampName, int diameter, int type, int colorMode, int alpha, String bottomText) throws Exception {
        int[] topFontSizeMapping = new int[]{0, 72, 72, 72, 72, 72, 72, 72, 72, 72, 72, 66, 64, 62, 60, 58, 56, 54, 50, 48, 46, 44, 42, 40, 39, 38, 36, 35, 34, 33, 32};
        int[] bottomFontSizeMapping = new int[]{0, 36, 36, 36, 36, 36, 36, 36, 36, 36, 36, 33, 32, 31, 30, 29, 28, 27, 25, 24, 23, 22, 21, 20, 19, 19, 18, 17, 17, 16, 16, 16, 16};
        BufferedImage image = null;
        if (companyName != null && !companyName.equals("") && stampName != null && !stampName.equals("")) {
            stampName = stampName.replace("(", "（");
            stampName = stampName.replace(")", "）");
            ByteArrayInputStream bais = new ByteArrayInputStream(makeCircularStampName(stampName, 600, type, colorMode));
            image = ImageIO.read(bais);
            Graphics2D g2 = image.createGraphics();
            switch(colorMode) {
                case 1:
                    g2.setColor(new Color(230, 0, 18));
                    break;
                case 2:
                    g2.setColor(new Color(18, 230, 18));
                    break;
                case 3:
                    g2.setColor(new Color(18, 0, 230));
                    break;
                case 4:
                    g2.setColor(new Color(0, 0, 0));
                    break;
                default:
                    g2.setColor(new Color(230, 0, 18));
            }

            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            if (companyName != null) {
                drawTopAndBottomText(g2, companyName, 1, topFontSizeMapping, type, 270, -2);
            }

            if (bottomText != null) {
                drawTopAndBottomText(g2, bottomText, 2, bottomFontSizeMapping, type, -295, 4);
            }

            g2.dispose();
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            BufferedImage buffImg = setAlpha(image, alpha);
            ImageIO.write(buffImg, "png", byteArrayOutputStream);
            return byteArrayOutputStream.toByteArray();
        } else {
            throw new Exception("公司名称或图章名称不能为空！");
        }
    }

    private static byte[] makeCircularStampName(String stampName, int diameter, int type, int colorMode) throws Exception {
        int[] fontSizeMapping = new int[]{0, 54, 54, 54, 54, 54, 54, 54, 48, 42, 40};
        BufferedImage image = null;
        if (stampName != null && !stampName.equals("")) {
            if (stampName.length() > 10) {
                throw new Exception("图章名称不能大于10个字！");
            } else {
                image = ImageIO.read(ImageUtil.class.getResourceAsStream("t0_" + colorMode + ".png"));
                Graphics2D g2 = image.createGraphics();
                switch(colorMode) {
                    case 1:
                        g2.setColor(new Color(230, 0, 18));
                        break;
                    case 2:
                        g2.setColor(new Color(18, 230, 18));
                        break;
                    case 3:
                        g2.setColor(new Color(18, 0, 230));
                        break;
                    case 4:
                        g2.setColor(new Color(0, 0, 0));
                        break;
                    default:
                        g2.setColor(new Color(230, 0, 18));
                }

                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                int ilength = stampName.length();
                int fontsize = fontSizeMapping[ilength];
                Font f = null;
                if (type == 2) {
                    f = new Font("SimHei", 0, fontsize);
                } else if (type == 3) {
                    f = new Font("SimSun", 1, fontsize);
                } else {
                    f = new Font("Microsoft YaHei", 0, fontsize);
                }

                g2.setFont(f);
                FontRenderContext context = g2.getFontRenderContext();
                Rectangle2D bounds = f.getStringBounds(stampName, context);
                double char_interval = bounds.getWidth() / (double)ilength;
                g2.drawString(stampName, 300 - (int)(bounds.getWidth() / 2.0D), 480);
                g2.dispose();
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                BufferedImage buffImg = null;
                buffImg = new BufferedImage(diameter, diameter, 6);
                buffImg.getGraphics().drawImage(image.getScaledInstance(diameter, diameter, 4), 0, 0, (ImageObserver)null);
                ImageIO.write(buffImg, "png", byteArrayOutputStream);
                return byteArrayOutputStream.toByteArray();
            }
        } else {
            throw new Exception("图章名称不能为空！");
        }
    }

    public static byte[] makeSquareStamp(String message, int outHeight, int type, int alpha) throws FileNotFoundException, IOException {
        return makeSquareStamp(message, outHeight, type, 1, alpha);
    }

    public static byte[] makeSquareStamp(String message, int outHeight, int type) throws FileNotFoundException, IOException {
        return makeSquareStamp(message, outHeight, type, 1, 40);
    }

    public static byte[] makeSquareStamp(String message, int outHeight, int type, int colorMode, int alpha) throws FileNotFoundException, IOException {
        BufferedImage image = null;
        int fontSize = 80;
        int fontSpace = 100;
        int stampHeight = 200;
        int lineWidth = 5;
        stampHeight = stampHeight + lineWidth * 2;
        Color color = null;
        switch(colorMode) {
            case 1:
                color = new Color(230, 0, 18);
                break;
            case 2:
                color = new Color(18, 230, 18);
                break;
            case 3:
                color = new Color(18, 0, 230);
                break;
            case 4:
                color = new Color(0, 0, 0);
                break;
            default:
                color = new Color(230, 0, 18);
        }

        if (message != null && !message.equals("")) {
            String newMessage = null;
            if (message.length() == 2) {
                newMessage = message + "之印";
            } else if (message.length() % 2 == 1) {
                newMessage = message + "印";
            } else {
                newMessage = message;
            }

            int stampWidth = fontSpace * (int)Math.floor((double)((float)newMessage.length() * 0.5F + 0.5F));
            stampWidth += lineWidth * 2;
            int var10000 = (int)((float)outHeight / 200.0F * (float)stampWidth);
            ByteArrayInputStream bais = new ByteArrayInputStream(getRect(stampWidth, stampHeight, lineWidth, color));
            image = ImageIO.read(bais);
            Graphics2D g2 = image.createGraphics();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(color);
            Font f = null;
            if (type == 2) {
                f = new Font("SimHei", 0, fontSize);
            } else if (type == 3) {
                f = new Font("SimSun", 0, fontSize);
            } else {
                f = new Font("Microsoft YaHei", 0, fontSize);
            }

            g2.setFont(f);
            char[] charArray = newMessage.toCharArray();

            for(int i = 0; i < charArray.length; ++i) {
                int fontX = stampWidth - lineWidth - (i / 2 + 1) * fontSpace + (fontSpace - fontSize) / 2;
                int fontY = i % 2 * fontSpace + lineWidth + fontSize;
                g2.drawString(new String(new char[]{charArray[i]}), fontX, fontY);
            }

            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            BufferedImage buffImg = setAlpha(image, alpha);
            ImageIO.write(buffImg, "png", byteArrayOutputStream);
            return byteArrayOutputStream.toByteArray();
        } else {
            throw new IOException("图章姓名不能为空！");
        }
    }

    public static byte[] makeSquareStampReverse(String message, int outHeight, int type, int alpha) throws FileNotFoundException, IOException {
        return makeSquareStampReverse(message, outHeight, type, 1, alpha);
    }

    public static byte[] makeSquareStampReverse(String message, int outHeight, int type) throws FileNotFoundException, IOException {
        return makeSquareStampReverse(message, outHeight, type, 1, 40);
    }

    public static byte[] makeSquareStampReverse(String message, int outHeight, int type, int colorMode, boolean frame, int alpha) throws FileNotFoundException, IOException {
        BufferedImage image = null;
        int fontSize = 80;
        int fontSpace = 100;
        int stampHeight = 100;
        int lineWidth = 5;
        stampHeight = stampHeight + lineWidth * 2;
        Color color = null;
        switch(colorMode) {
            case 1:
                color = new Color(230, 0, 18);
                break;
            case 2:
                color = new Color(18, 230, 18);
                break;
            case 3:
                color = new Color(18, 0, 230);
                break;
            case 4:
                color = new Color(0, 0, 0);
                break;
            default:
                color = new Color(230, 0, 18);
        }

        if (message != null && !message.equals("")) {
            int stampWidth = fontSpace * message.length();
            stampWidth += lineWidth * 2;
            int var10000 = (int)((float)outHeight / 100.0F * (float)stampWidth);
            ByteArrayInputStream bais = null;
            if (frame) {
                bais = new ByteArrayInputStream(getRect(stampWidth, stampHeight, lineWidth, color));
            } else {
                bais = new ByteArrayInputStream(getRect(stampWidth, stampHeight, 0, new Color(0, 0, 0)));
            }

            image = ImageIO.read(bais);
            Graphics2D g2 = image.createGraphics();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(color);
            Font f = null;
            if (type == 1) {
                f = new Font("Microsoft YaHei", 0, fontSize);
            } else if (type == 2) {
                f = new Font("SimHei", 0, fontSize);
            } else if (type == 3) {
                f = new Font("SimSun", 0, fontSize);
            } else if (type == 4) {
                f = new Font("华文行楷", 0, fontSize);
            } else if (type == 5) {
                f = new Font("方正舒体", 0, fontSize);
            } else {
                f = new Font("Microsoft YaHei", 0, fontSize);
            }

            g2.setFont(f);
            char[] charArray = message.toCharArray();

            for(int i = 0; i < charArray.length; ++i) {
                int fontX = lineWidth + i * fontSpace + (fontSpace - fontSize) / 2;
                int fontY = lineWidth + fontSize;
                g2.drawString(new String(new char[]{charArray[i]}), fontX, fontY);
            }

            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            BufferedImage buffImg = setAlpha(image, alpha);
            ImageIO.write(buffImg, "png", byteArrayOutputStream);
            return byteArrayOutputStream.toByteArray();
        } else {
            throw new IOException("图章姓名不能为空！");
        }
    }

    public static byte[] makeSquareStampReverse(String message, int outHeight, int type, int colorMode, int alpha) throws FileNotFoundException, IOException {
        return makeSquareStampReverse(message, outHeight, type, colorMode, true, alpha);
    }

    public static byte[] makeSquareStampReverse(String message, int outHeight, int type, int colorMode, boolean frame) throws FileNotFoundException, IOException {
        return makeSquareStampReverse(message, outHeight, type, colorMode, frame, 40);
    }

    public static byte[] getRect(int width, int height, int lineWidth, Color color) throws IOException {
        BufferedImage rectImage = new BufferedImage(width, height, 3);
        Graphics2D g2d = rectImage.createGraphics();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setColor(color);
        g2d.fillRect(0, 0, width, lineWidth);
        g2d.fillRect(0, 0, lineWidth, height);
        g2d.fillRect(width - lineWidth, 0, lineWidth, height);
        g2d.fillRect(0, height - lineWidth, width, lineWidth);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ImageIO.write(rectImage, "png", out);
        return out.toByteArray();
    }

    private static int getGray(int color) {
        int[] rgb = new int[]{(color & 16711680) >> 16, (color & '\uff00') >> 8, color & 255};
        return (rgb[0] + rgb[1] + rgb[2]) / 3;
    }

    public static byte[] getBytes(String filePath) {
        byte[] buffer = null;

        try {
            File file = new File(filePath);
            FileInputStream fis = new FileInputStream(file);
            ByteArrayOutputStream bos = new ByteArrayOutputStream(1000);
            byte[] b = new byte[1000];

            int n;
            while((n = fis.read(b)) != -1) {
                bos.write(b, 0, n);
            }

            fis.close();
            bos.close();
            buffer = bos.toByteArray();
        } catch (FileNotFoundException var7) {
            var7.printStackTrace();
        } catch (IOException var8) {
            var8.printStackTrace();
        }

        return buffer;
    }

    public static void saveImgFile(byte[] bfile, String fileName) {
        BufferedOutputStream bos = null;
        FileOutputStream fos = null;
        File file = null;

        try {
            file = new File(fileName);
            if (!file.exists() && file.isDirectory()) {
                file.mkdirs();
            } else if (!file.isDirectory()) {
                File parentDir = file.getParentFile();
                if (!parentDir.exists()) {
                    parentDir.mkdirs();
                }
            }

            fos = new FileOutputStream(file);
            bos = new BufferedOutputStream(fos);
            bos.write(bfile);
        } catch (Exception var18) {
            var18.printStackTrace();
        } finally {
            if (bos != null) {
                try {
                    bos.close();
                } catch (IOException var17) {
                    var17.printStackTrace();
                }
            }

            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException var16) {
                    var16.printStackTrace();
                }
            }

        }

    }

    public static byte[] makeEllipticStamp(String companyName, String horizontalText, String bottomText, String color, int fontType, int diameter, int alpha) {
        byte[] a = CreateSeal.createEllipticSeal(companyName, horizontalText, bottomText, color, fontType, diameter, alpha);
        return a;
    }

    public static byte[] makeEllipticStamp(String companyName, String horizontalText, String bottomText, String color, int fontType, int diameter) {
        byte[] a = CreateSeal.createEllipticSeal(companyName, horizontalText, bottomText, color, fontType, diameter, 40);
        return a;
    }

    public static BufferedImage setAlpha(BufferedImage bufferedImage2, int alpha) {
        BufferedImage bufferedImage = new BufferedImage(bufferedImage2.getWidth(), bufferedImage2.getHeight(), 6);
        Graphics2D g2D = (Graphics2D)bufferedImage.getGraphics();
        g2D.drawImage(bufferedImage2, 0, 0, (ImageObserver)null);
        if (alpha < 0) {
            alpha = 0;
        } else if (alpha > 10) {
            alpha = 10;
        }

        for(int h = 0; h < bufferedImage.getHeight(); ++h) {
            for(int w = 0; w < bufferedImage.getWidth(); ++w) {
                int a = bufferedImage.getRGB(w, h);
                if (a != 0) {
                    a = alpha * 255 / 10 << 24 | a & 16777215;
                }

                bufferedImage.setRGB(w, h, a);
            }
        }

        g2D.drawImage(bufferedImage, 0, 0, (ImageObserver)null);
        return bufferedImage;
    }
}

