package com.xforceplus.taxware.microservice.voucher.sdk.utils.image;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Line2D.Double;
import java.awt.geom.Rectangle2D;
import java.awt.image.*;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class SealUtils {
    public SealUtils() {
    }

    public static BufferedImage cropImageHorizontal(BufferedImage bufferedImage, int pieces, int pieceNum) {
        if (pieces == 1 && pieceNum == 0) {
            return bufferedImage;
        } else {
            int width = bufferedImage.getWidth();
            int height = bufferedImage.getHeight();
            int pieceWidth = width / pieces;
            ImageFilter cropFilter = new CropImageFilter(pieceWidth * pieceNum, 0, pieceWidth, height);
            Image pieceImage = Toolkit.getDefaultToolkit().createImage(new FilteredImageSource(bufferedImage.getSource(), cropFilter));
            return imageToBufferedImage(pieceImage);
        }
    }

    public static void drawStringCentrally(BufferedImage buffededImage, String text, Font textFont, Color textColor, int centerYOffset) throws IOException {
        Graphics2D g2d = (Graphics2D)buffededImage.getGraphics();
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2d.setColor(textColor);
        g2d.setFont(textFont);
        int strWidth = g2d.getFontMetrics().stringWidth(text);
        FontMetrics fontMetrics = g2d.getFontMetrics();
        g2d.drawString(text, (buffededImage.getWidth() - strWidth) / 2, (buffededImage.getHeight() + g2d.getFontMetrics().getHeight()) / 2 - fontMetrics.getDescent() + centerYOffset);
    }

    /** @deprecated */
    public static BufferedImage getComposedSeal(BufferedImage originalSealImage, String additionalText, Font additionalTextFont, Color textColor) throws IOException {
        int width = originalSealImage.getWidth() * 4;
        int height = originalSealImage.getHeight() * 4;
        BufferedImage bufferedImage = createTransparentBufferedImage(width, height);
        Graphics2D graphics2d = getSmoothGraphics2D(bufferedImage);
        graphics2d.setFont(additionalTextFont);
        graphics2d.setColor(textColor);
        int strWidth = graphics2d.getFontMetrics().stringWidth(additionalText);
        graphics2d.drawString(additionalText, (width - strWidth) / 2, originalSealImage.getHeight() + graphics2d.getFontMetrics().getHeight());
        graphics2d.drawImage(originalSealImage, (width - originalSealImage.getWidth()) / 2, 0, (ImageObserver)null);
        int sWidth = originalSealImage.getWidth() >= strWidth ? originalSealImage.getWidth() : strWidth;
        int sHeight = originalSealImage.getHeight() + graphics2d.getFontMetrics().getHeight() + graphics2d.getFontMetrics().getDescent();
        ImageFilter cropFilter = new CropImageFilter((width - sWidth) / 2, 0, sWidth, sHeight);
        Image slicedImage = Toolkit.getDefaultToolkit().createImage(new FilteredImageSource(bufferedImage.getSource(), cropFilter));
        BufferedImage newBufferedIamge = createTransparentBufferedImage(sWidth, sHeight);
        Graphics newGraphics2d = getSmoothGraphics2D(newBufferedIamge);
        newGraphics2d.drawImage(slicedImage, 0, 0, (ImageObserver)null);
        return newBufferedIamge;
    }

    /** @deprecated */
    public static BufferedImage generatePersonSeal(Color inkpadColor, String personName, Font personNameFont, int wordSpacing, int borderWidth, int wordXOffset, int wordYOffset, int rectWidthOffset, int rectHeightOffset, boolean showReferenceLine) throws IOException {
        int width = 800;
        int height = 200;
        int centerX = width / 2;
        int centerY = height / 2;
        BufferedImage bufferedImage = createTransparentBufferedImage(width, height);
        Graphics2D graphics2d = getSmoothGraphics2D(bufferedImage);
        graphics2d.setColor(inkpadColor);
        char[] arrSealName = personName.toCharArray();
        graphics2d.setFont(personNameFont);
        FontMetrics fontMetrics = graphics2d.getFontMetrics();
        int wordHeight = fontMetrics.getHeight();
        int wordY = centerY + wordHeight / 2 - fontMetrics.getDescent() + wordYOffset;
        double nameWidth = (double)(fontMetrics.stringWidth(personName) + wordSpacing * (arrSealName.length - 1));
        double wordWidth = (double)(fontMetrics.stringWidth(personName) / arrSealName.length);
        int rectWidth = (int)(nameWidth + (double)rectWidthOffset);
        int rectHeight = wordHeight + rectHeightOffset;
        int rectX = centerX - rectWidth / 2;
        int rectY = centerY + rectHeight / 2 - rectHeight;
        int border = 6;
        graphics2d.setStroke(new BasicStroke((float)border));
        graphics2d.drawRoundRect(rectX, rectY, rectWidth, rectHeight, 20, 20);
        graphics2d.setColor(inkpadColor);

        int imageWidth;
        for(imageWidth = 0; imageWidth < arrSealName.length; ++imageWidth) {
            graphics2d.drawString(String.valueOf(arrSealName[imageWidth]), (int)((double)centerX - nameWidth / 2.0D + wordWidth * (double)imageWidth + (double)(wordSpacing * imageWidth) + (double)wordXOffset), wordY);
        }

        graphics2d.setStroke(new BasicStroke(1.0F));
        if (showReferenceLine) {
            Line2D line1 = new Double(0.0D, (double)(height / 2), (double)width, (double)(height / 2));
            graphics2d.draw(line1);
            Line2D line2 = new Double((double)(width / 2), 0.0D, (double)(width / 2), (double)height);
            graphics2d.draw(line2);
            Line2D line3 = new Double(0.0D, 0.0D, (double)width, (double)height);
            graphics2d.draw(line3);
            Line2D line4 = new Double(0.0D, (double)height, (double)width, 0.0D);
            graphics2d.draw(line4);
            Line2D line5 = new Double(0.0D, (double)wordY, (double)width, (double)wordY);
            graphics2d.draw(line5);
            Line2D line6 = new Double(0.0D, (double)(wordY - wordHeight), (double)width, (double)(wordY - wordHeight));
            graphics2d.draw(line6);
        }

        graphics2d.dispose();
        border = border + 1;
        imageWidth = rectWidth + border;
        int imageHeight = rectHeight + border;
        ImageFilter cropFilter = new CropImageFilter(rectX - border / 2, rectY - border / 2, imageWidth, imageHeight);
        Image slicedImage = Toolkit.getDefaultToolkit().createImage(new FilteredImageSource(bufferedImage.getSource(), cropFilter));
        BufferedImage bufferedSlicedImage = createTransparentBufferedImage(imageWidth, imageHeight);
        Graphics smallGraphics = getSmoothGraphics2D(bufferedSlicedImage);
        smallGraphics.drawImage(slicedImage, 0, 0, (ImageObserver)null);
        smallGraphics.dispose();
        return bufferedSlicedImage;
    }

    /** @deprecated */
    public static BufferedImage generateCompanySeal(int width, int height, int circleBorder, int pentagramRadius, String arcCompany, Font arcCompanyFont, int arcMargin, double arcDegree, String sealTitle, Font sealTitleFont, Color inkpadColor, boolean showReferenceLine) throws IOException {
        int centerX = width / 2;
        int centerY = height / 2;
        BufferedImage image = new BufferedImage(width, height, 1);
        Graphics2D g2d = (Graphics2D)image.getGraphics();
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2d.setBackground(Color.WHITE);
        g2d.clearRect(0, 0, width, height);
        g2d.setColor(inkpadColor);
        double outerCircleSmallR = (double)(width / 2 - circleBorder);
        if (showReferenceLine) {
            Line2D line1 = new Double(0.0D, (double)(height / 2), (double)width, (double)(height / 2));
            g2d.draw(line1);
            Line2D line2 = new Double((double)(width / 2), 0.0D, (double)(width / 2), (double)height);
            g2d.draw(line2);
            Line2D line3 = new Double(0.0D, 0.0D, (double)width, (double)height);
            g2d.draw(line3);
            Line2D line4 = new Double(0.0D, (double)height, (double)width, 0.0D);
            g2d.draw(line4);
            Rectangle2D sealNameRect = new Rectangle2D.Double((double)(centerX / 2), (double)(centerY + centerY / 2), (double)(width / 2), outerCircleSmallR * Math.sqrt(3.0D) / 2.0D - (double)(width / 4));
            g2d.draw(sealNameRect);
        }

        if (showReferenceLine) {
            Ellipse2D circle = new Ellipse2D.Double();
            circle.setFrameFromCenter((double)centerX, (double)centerY, (double)(centerX + pentagramRadius), (double)(centerY + pentagramRadius));
            g2d.draw(circle);
        }

        drawPentagram(g2d, centerX, centerY, pentagramRadius);
        g2d.setStroke(new BasicStroke((float)circleBorder));
        g2d.drawOval(0 + circleBorder / 2, 0 + circleBorder / 2, width - circleBorder, height - circleBorder);
        g2d.setFont(sealTitleFont);
        FontMetrics fontMetrics = g2d.getFontMetrics();
        char[] arrSealName = sealTitle.toCharArray();
        int wHeight = fontMetrics.getHeight();
        double rectW = (double)(width / 2);
        double rectH = outerCircleSmallR * Math.sqrt(3.0D) / 2.0D - (double)(width / 4);
        int sealNameY = (int)((double)(centerY + width / 4) + rectH / 2.0D + (double)(wHeight / 2) - (double)fontMetrics.getDescent());
        double www = rectW * 0.9D;
        double deltaW = www / (double)arrSealName.length;
        double wW = (double)(g2d.getFontMetrics().stringWidth(sealTitle) / arrSealName.length);
        double left = (deltaW - wW) / 2.0D;

        for(int i = 0; i < arrSealName.length; ++i) {
            g2d.drawString(String.valueOf(arrSealName[i]), (int)((double)centerX - www / 2.0D + left + (double)i * deltaW), sealNameY);
        }

        if (showReferenceLine) {
            g2d.setStroke(new BasicStroke(0.0F));
            Line2D line5 = new Double((double)(centerX / 2), (double)(centerY + centerY / 2) + rectH / 2.0D - (double)(wHeight / 2), (double)(centerX + centerX / 2), (double)(centerY + centerY / 2) + rectH / 2.0D - (double)(wHeight / 2));
            g2d.draw(line5);
            Line2D line6 = new Double((double)(centerX - centerX / 2), (double)centerY + 2.0D * outerCircleSmallR * Math.sqrt(3.0D) / 4.0D - (rectH / 2.0D - (double)(wHeight / 2)), (double)(centerX + centerX / 2), (double)centerY + 2.0D * outerCircleSmallR * Math.sqrt(3.0D) / 4.0D - (rectH / 2.0D - (double)(wHeight / 2)));
            g2d.draw(line6);
        }

        g2d.setFont(arcCompanyFont);
        fontMetrics = g2d.getFontMetrics();
        char[] arrCompanyName = arcCompany.toCharArray();
        int wordWidth = fontMetrics.stringWidth(String.valueOf(arrCompanyName[0]));
        int wordHeight = fontMetrics.getHeight();
        int textR = width / 2 - wordHeight + fontMetrics.getDescent() - arcMargin;
        if (showReferenceLine) {
            Ellipse2D companyNameCircle = new Ellipse2D.Double();
            g2d.setStroke(new BasicStroke(0.0F));
            companyNameCircle.setFrameFromCenter((double)centerX, (double)centerY, (double)(centerX + textR), (double)(centerY + textR));
            g2d.draw(companyNameCircle);
        }

        double xx = Math.sqrt((double)(textR * textR - wordWidth * wordWidth / 4)) - (double)g2d.getFontMetrics().getDescent();
        double wordDegree = 2.0D * Math.toDegrees(Math.atan2((double)(wordWidth / 2), xx));
        double r1 = Math.sqrt((double)(textR * textR - wordWidth * wordWidth / 4));
        double c = Math.toDegrees(Math.atan((double)(wordWidth / 2) / r1));
        double totalDegree = arcDegree - wordDegree + 2.0D * c;
        double deltaDegree = totalDegree / (double)(arcCompany.length() - 1);
        int i = arrCompanyName.length - 1;

        for(int j = 0; i >= 0; ++j) {
            double a = deltaDegree * (double)j - (arcDegree / 2.0D - 90.0D) + wordDegree - c;
            double x = (double)textR * Math.cos(Math.toRadians(a));
            double y = (double)textR * Math.sin(Math.toRadians(a));
            double b = 90.0D - Math.toDegrees(Math.atan2(y, x));
            double b1 = b + c;
            AffineTransform transform = AffineTransform.getRotateInstance(Math.toRadians(b1));
            Font thisFont = arcCompanyFont.deriveFont(transform);
            g2d.setFont(thisFont);
            String word = String.valueOf(arrCompanyName[i]);
            g2d.drawString(word, (int)(x + (double)centerX), (int)((double)centerY - y));
            --i;
        }

        g2d.dispose();
        Image imageWithTransparency = makeColorTransparent(image, Color.WHITE);
        BufferedImage transparentImage = imageToBufferedImage(imageWithTransparency);
        return transparentImage;
    }

    public static BufferedImage createTransparentBufferedImage(int width, int height) {
        BufferedImage bufferedImage = new BufferedImage(width, height, 1);
        Graphics2D graphics2d = (Graphics2D)bufferedImage.getGraphics();
        graphics2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        graphics2d.setBackground(Color.WHITE);
        graphics2d.clearRect(0, 0, width, height);
        graphics2d.dispose();
        Image image = makeColorTransparent(bufferedImage, Color.WHITE);
        bufferedImage = imageToBufferedImage(image);
        return bufferedImage;
    }

    public static Graphics2D getSmoothGraphics2D(BufferedImage bufferedImage) {
        Graphics2D graphics2d = (Graphics2D)bufferedImage.getGraphics();
        graphics2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        return graphics2d;
    }

    public static void drawBorderedRoundRect(Graphics2D graphics2d, int width, int height, int border, int arcWidth, int arcHeight, int offset) {
        graphics2d.setStroke(new BasicStroke((float)border));
        graphics2d.drawRoundRect(0 + border / 2 + offset, 0 + border / 2 + offset, width - border - offset * 2, height - border - offset * 2, arcWidth, arcHeight);
    }

    public static Font scaleFont(Font font, double xscale, double yscale) {
        AffineTransform scaleTransform = new AffineTransform();
        scaleTransform.scale(xscale, yscale);
        return font.deriveFont(scaleTransform);
    }

    public static void drawHorizontalCenteredText(Graphics2D graphics2d, int width, int height, String text, Font textFont, int textY) {
        graphics2d.setFont(textFont);
        FontMetrics fontMetrics = graphics2d.getFontMetrics();
        int textWidth = fontMetrics.stringWidth(text);
        graphics2d.drawString(text, width / 2 - textWidth / 2, textY);
    }

    public static void drawHorizontalCenteredText2(Graphics2D graphics2d, int width, int height, String text, Font textFont, int textY, int wordSpacing) {
        if (wordSpacing == 0) {
            drawHorizontalCenteredText(graphics2d, width, height, text, textFont, textY);
        } else {
            graphics2d.setFont(textFont);
            FontMetrics fontMetrics = graphics2d.getFontMetrics();
            char[] charsText = text.toCharArray();
            int textWidth = fontMetrics.stringWidth(text) + wordSpacing * (charsText.length - 1);
            int wordsWidth = 0;

            for(int i = 0; i < charsText.length; ++i) {
                int wordWidth = fontMetrics.charWidth(charsText[i]);
                graphics2d.drawString(String.valueOf(charsText[i]), width / 2 - textWidth / 2 + wordsWidth + i * wordSpacing, textY);
                wordsWidth += wordWidth;
            }

        }
    }

    public static BufferedImage resizeCanvas(BufferedImage bufferedImage, int width, int height, int positionStyle) {
        int oldWidth = bufferedImage.getWidth();
        int oldHeight = bufferedImage.getHeight();
        BufferedImage resizedBufferedIamge = createTransparentBufferedImage(width, height);
        Graphics2D graphics2d = getSmoothGraphics2D(resizedBufferedIamge);
        int topLeftCornerX = 0;
        int topLeftCornerY = 0;
        switch(positionStyle) {
            case 1:
            case 4:
            case 7:
                topLeftCornerX = 0;
                break;
            case 2:
            case 5:
            case 8:
                topLeftCornerX = (width - oldWidth) / 2;
                break;
            case 3:
            case 6:
            case 9:
                topLeftCornerX = width - oldWidth;
        }

        switch(positionStyle) {
            case 1:
            case 2:
            case 3:
                topLeftCornerY = height - oldHeight;
                break;
            case 4:
            case 5:
            case 6:
                topLeftCornerY = (height - oldHeight) / 2;
                break;
            case 7:
            case 8:
            case 9:
                topLeftCornerY = 0;
        }

        graphics2d.drawImage(bufferedImage, topLeftCornerX, topLeftCornerY, (ImageObserver)null);
        graphics2d.dispose();
        return resizedBufferedIamge;
    }

    public static FontMetrics createFontMetrics(Font font) {
        BufferedImage bufferedIamge = new BufferedImage(1, 1, 2);
        Graphics2D graphics2d = getSmoothGraphics2D(bufferedIamge);
        graphics2d.setFont(font);
        return graphics2d.getFontMetrics();
    }

    public static void drawClockwiseFanText(Graphics2D graphics2d, int width, int height, String text, Font fontText, int fanRadiusOfTextBaseline, double angleOfFan) {
        int centerX = width / 2;
        int centerY = height / 2;
        graphics2d.setFont(fontText);
        FontMetrics fontMetrics = graphics2d.getFontMetrics();
        char[] charsFanText = text.toCharArray();
        int wordWidth = fontMetrics.stringWidth(String.valueOf(charsFanText[0]));
        double OE = Math.sqrt((double)(fanRadiusOfTextBaseline * fanRadiusOfTextBaseline - wordWidth * wordWidth / 4));
        double angleBOE = Math.toDegrees(Math.atan2((double)(wordWidth / 2), OE));
        double angleBOC = 2.0D * angleBOE;
        double realAngleFan = angleOfFan + angleBOC;
        double deltaAngleWord = angleOfFan / (double)(text.length() - 1);

        for(int i = 0; i < charsFanText.length; ++i) {
            double angleCoordinateOfWord = 180.0D + deltaAngleWord * (double)i;
            angleCoordinateOfWord += 90.0D - realAngleFan / 2.0D;
            double angleWordToRotate = 270.0D + deltaAngleWord * (double)i;
            angleWordToRotate += angleBOE;
            angleWordToRotate += 90.0D - realAngleFan / 2.0D;
            double wordRelativeX = (double)fanRadiusOfTextBaseline * Math.cos(Math.toRadians(angleCoordinateOfWord));
            double wordRelativeY = (double)fanRadiusOfTextBaseline * Math.sin(Math.toRadians(angleCoordinateOfWord));
            AffineTransform wordTransform = AffineTransform.getRotateInstance(Math.toRadians(angleWordToRotate));
            wordTransform.scale(fontText.getTransform().getScaleX(), fontText.getTransform().getScaleY());
            graphics2d.setFont(fontText.deriveFont(wordTransform));
            graphics2d.drawString(String.valueOf(charsFanText[i]), (int)(wordRelativeX + (double)centerX), (int)((double)centerY + wordRelativeY));
        }

    }

    public static void drawAnticlockwiseFanText(Graphics2D graphics2d, int width, int height, String text, Font fontText, int fanRadiusOfTextBaseline, double angleOfFan) {
        int centerX = width / 2;
        int centerY = height / 2;
        graphics2d.setFont(fontText);
        FontMetrics fontMetrics = graphics2d.getFontMetrics();
        char[] charsText = text.toCharArray();
        int wordWidth = fontMetrics.stringWidth(String.valueOf(charsText[0]));
        double OE = Math.sqrt((double)(fanRadiusOfTextBaseline * fanRadiusOfTextBaseline - wordWidth * wordWidth / 4));
        double angleBOE = Math.toDegrees(Math.atan2((double)(wordWidth / 2), OE));
        double angleBOC = angleBOE * 2.0D;
        double realAngleFan = angleOfFan + angleBOC;
        double deltaAngleWord = angleOfFan / (double)(text.length() - 1);

        for(int i = 0; i < charsText.length; ++i) {
            double angleCoordinateOfWord = 0.0D + deltaAngleWord * (double)i;
            angleCoordinateOfWord += 180.0D + (90.0D - realAngleFan / 2.0D);
            double angleWordToRotate = 270.0D - deltaAngleWord * (double)i;
            angleWordToRotate -= angleBOE;
            angleWordToRotate -= 180.0D + (90.0D - realAngleFan / 2.0D);
            double wordRelativeX = (double)fanRadiusOfTextBaseline * Math.cos(Math.toRadians(angleCoordinateOfWord));
            double wordRelativeY = (double)fanRadiusOfTextBaseline * Math.sin(Math.toRadians(angleCoordinateOfWord));
            AffineTransform wordTransform = AffineTransform.getRotateInstance(Math.toRadians(angleWordToRotate));
            wordTransform.scale(fontText.getTransform().getScaleX(), fontText.getTransform().getScaleY());
            graphics2d.setFont(fontText.deriveFont(wordTransform));
            graphics2d.drawString(String.valueOf(charsText[i]), (int)(wordRelativeX + (double)centerX), (int)((double)centerY - wordRelativeY));
        }

    }

    public static void drawBorderedCircle(Graphics2D graphics2d, int width, int height, int border, int offset) {
        graphics2d.setStroke(new BasicStroke((float)border));
        graphics2d.drawOval(0 + border / 2 + offset, 0 + border / 2 + offset, width - border - offset * 2, height - border - offset * 2);
    }

    public static void drawPentagram(Graphics g, int x0, int y0, int r) {
        int rr = (int)((double)r * (Math.sin(Math.toRadians(54.0D)) - Math.cos(Math.toRadians(54.0D)) * Math.tan(Math.toRadians(36.0D))));
        int x1 = (int)((double)x0 - (double)r * Math.cos(Math.toRadians(18.0D)));
        int y1 = (int)((double)y0 - (double)r * Math.sin(Math.toRadians(18.0D)));
        int y2 = y0 - r;
        int x3 = (int)((double)x0 + (double)r * Math.cos(Math.toRadians(18.0D)));
        int x4 = (int)((double)x0 + (double)r * Math.cos(Math.toRadians(54.0D)));
        int y4 = (int)((double)y0 + (double)r * Math.sin(Math.toRadians(54.0D)));
        int x5 = (int)((double)x0 - (double)r * Math.cos(Math.toRadians(54.0D)));
        int xx1 = (int)((double)x0 + (double)rr * Math.cos(Math.toRadians(18.0D)));
        int yy1 = (int)((double)y0 + (double)rr * Math.sin(Math.toRadians(18.0D)));
        int yy2 = y0 + rr;
        int xx3 = (int)((double)x0 - (double)rr * Math.cos(Math.toRadians(18.0D)));
        int xx4 = (int)((double)x0 - (double)rr * Math.cos(Math.toRadians(54.0D)));
        int yy4 = (int)((double)y0 - (double)rr * Math.sin(Math.toRadians(54.0D)));
        int xx5 = (int)((double)x0 + (double)rr * Math.cos(Math.toRadians(54.0D)));
        Polygon polygon = new Polygon();
        polygon.addPoint(x1, y1);
        polygon.addPoint(xx4, yy4);
        polygon.addPoint(x0, y2);
        polygon.addPoint(xx5, yy4);
        polygon.addPoint(x3, y1);
        polygon.addPoint(xx1, yy1);
        polygon.addPoint(x4, y4);
        polygon.addPoint(x0, yy2);
        polygon.addPoint(x5, y4);
        polygon.addPoint(xx3, yy1);
        g.fillPolygon(polygon);
    }

    public static BufferedImage makeImageTranslucent(BufferedImage bufferedImage, double alpha) {
        BufferedImage target = new BufferedImage(bufferedImage.getWidth(), bufferedImage.getHeight(), 3);
        Graphics2D g = target.createGraphics();
        g.setComposite(AlphaComposite.getInstance(3, (float)alpha));
        g.drawImage(bufferedImage, (BufferedImageOp)null, 0, 0);
        g.dispose();
        return target;
    }

    public static Image makeColorTransparent(BufferedImage bufferedImage, Color color) {
        ImageFilter filter = new RGBImageFilter() {
            public int markerRGB;

            {
//                this.markerRGB = getRGB() | -16777216;
            }

            public final int filterRGB(int x, int y, int rgb) {
                return (rgb | -16777216) == this.markerRGB ? 16777215 & rgb : rgb;
            }
        };
        ImageProducer ip = new FilteredImageSource(bufferedImage.getSource(), filter);
        return Toolkit.getDefaultToolkit().createImage(ip);
    }

    public static BufferedImage imageToBufferedImage(Image image) {
        BufferedImage bufferedImage = new BufferedImage(image.getWidth((ImageObserver)null), image.getHeight((ImageObserver)null), 2);
        Graphics2D g2 = bufferedImage.createGraphics();
        g2.drawImage(image, 0, 0, (ImageObserver)null);
        g2.dispose();
        return bufferedImage;
    }

    public static BufferedImage imageToBufferedImage(Image image, int imageType) {
        BufferedImage bufferedImage = new BufferedImage(image.getWidth((ImageObserver)null), image.getHeight((ImageObserver)null), imageType);
        Graphics2D g2 = bufferedImage.createGraphics();
        g2.drawImage(image, 0, 0, (ImageObserver)null);
        g2.dispose();
        return bufferedImage;
    }

    public static BufferedImage bytesToBufferedImage(byte[] imageBytes) throws IOException {
        ByteArrayInputStream sealImageInputStream = new ByteArrayInputStream(imageBytes);
        BufferedImage bufferedImage = ImageIO.read(sealImageInputStream);
        return bufferedImage;
    }

    public static byte[] bufferedImageToBytes(BufferedImage bufferedImage, String imageType) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ImageIO.write(bufferedImage, imageType, byteArrayOutputStream);
        byte[] imageBytes = byteArrayOutputStream.toByteArray();
        return imageBytes;
    }
}
