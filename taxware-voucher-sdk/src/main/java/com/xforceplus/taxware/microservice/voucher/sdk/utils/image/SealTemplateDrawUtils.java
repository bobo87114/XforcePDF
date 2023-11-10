package com.xforceplus.taxware.microservice.voucher.sdk.utils.image;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.io.IOUtils;
import org.bouncycastle.util.encoders.Hex;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.*;
import java.util.Map.Entry;

public class SealTemplateDrawUtils {
    public SealTemplateDrawUtils() {
    }

    public static JSONObject getSealTemplateByTypeName(String sealTypeName) throws Exception {
        //        String data = "  {\"version\":1\n" +
        //                "  ,\"name\":\"财务专用章\"\n" +
        //                "  ,\"default\":false\n" +
        //                "  ,\"circleBorder\":8\n" +
        //                "  ,\"offset\":4\n" +
        //                "  ,\"radiusX\":200,\"radiusY\":150,\"color\":\"#E60000\"\n" +
        //                "  ,\"attachedObjects\":\n" +
        //                "  [{\"attachedType\":\"TopText\",\"totalArcAng\":220,\"objectName\":\"orgName\",\"fontFamily\":\"宋体\",\"fontSize\":30,\"fontWeight\":\"bold\",\"xscaleOfTextTop\":0.85,\"yscaleOfTextTop\":1}\n" +
        //                "  ,{\"attachedType\":\"TextBottom\",\"fontFamily\":\"Microsoft YaHei UI\",\"fontSize\":16,\"fontWeight\":\"plain\",\"angleOfFanBottom\":90,\"xscaleOfTextBottom\":0.85,\"yscaleOfTextBottom\":1}\n" +
        //                "  ,{\"attachedType\":\"HorizontalText\",\"fontFamily\":\"宋体\",\"fontSize\":40,\"fontWeight\":\"bold\",\"xscaleOfHorizontalText\":0.8,\"yscaleOfHorizontalText\":1}\n" +
        //                "  ,{\"attachedType\":\"TaxText\",\"fontFamily\":\"Microsoft YaHei UI\",\"fontSize\":28,\"fontWeight\":\"plain\"}\n" +
        //                "  ,{\"attachedType\":\"DateText\",\"fontFamily\":\"华文楷体\",\"fontSize\":40,\"fontWeight\":\"bold\",\"xscaleOfSigningTime\":1,\"yscaleOfSigningTime\":1}\n" +
        //                "  ]}";
        String data ="";
        JSONObject returnObj = JSONObject.parseObject(data);
        if (returnObj != null) {
            return returnObj;
        }

        InputStream inputStream = SealTemplateDrawUtils.class.getResourceAsStream("seal-template-rule.json");
        byte[] bytesJson = IOUtils.toByteArray(inputStream);
        JSONArray jsonArray = JSONObject.parseArray(new String(bytesJson, "UTF-8"));

        for (int i = 0; i < jsonArray.size(); ++i) {
            JSONObject jsonSealTemplate = jsonArray.getJSONObject(i);
            String templateName = jsonSealTemplate.getString("name");
            if (templateName.equals(sealTypeName)) {
                returnObj = jsonSealTemplate;
                break;
            }
        }

        return returnObj;
    }

    public static BufferedImage drawSeal(BufferedImage bufferedImage, JSONObject templateRule, Map<String, Object> parameters, Properties fontTable) throws ParseException, ScriptException, JSONException {
        int radiusX = templateRule.getIntValue("radiusX");
        int radiusY = templateRule.getIntValue("radiusY");
        String name = templateRule.getString("name");
        String content1 = (String) parameters.get("content1");
        String content2 = (String) parameters.get("content2");
        String content3 = (String) parameters.get("content3");
        int fontType = Integer.parseInt(parameters.get("fontType").toString());
        Color inkpadColor = createColor((String) parameters.get("color"));
        if (bufferedImage == null) {
            bufferedImage = SealUtils.createTransparentBufferedImage(radiusX == 0 ? 1 : radiusX * 2, radiusY == 0 ? 1 : radiusY * 2);
        } else {
            radiusX = bufferedImage.getWidth();
            radiusY = bufferedImage.getHeight();
        }

        Graphics2D graphics2d = getSmoothGraphics2D(bufferedImage);
        int circleBorder = templateRule.getIntValue("circleBorder");
        int offset = templateRule.getIntValue("offset");
        FontMetrics metrics = graphics2d.getFontMetrics();
        JSONArray attachedObjects = templateRule.getJSONArray("attachedObjects");
        String orgName = content1;

        for (int i = 0; i < attachedObjects.size(); ++i) {
            JSONObject attachedObject = attachedObjects.getJSONObject(i);
            String attachedType = attachedObject.getString("attachedType");
            String fontFamily = attachedObject.getString("fontFamily");
            int fontSize = attachedObject.getIntValue("fontSize");
            String fontWeight = attachedObject.getString("fontWeight");
            Font f = null;
            if (fontType == 1) {
                f = new Font("Microsoft YaHei", 0, fontSize);
            } else if (fontType == 2) {
                f = new Font("SimHei", 0, fontSize);
            } else if (fontType == 3) {
                f = new Font("SimSun", 0, fontSize);
            } else if (fontType == 4) {
                f = new Font("华文行楷", 0, fontSize);
            } else if (fontType == 5) {
                f = new Font("方正舒体", 0, fontSize);
            } else {
                f = new Font("Microsoft YaHei", 0, fontSize);
            }

            int var10000;
            if ("TopText".equals(attachedType)) {
                int fontStyle = "bold".equals(fontWeight) ? 1 : 0;
                double totalArcAng;
                if (orgName.length() <= 14) {
                    totalArcAng = 180.0D;
                } else {
                    totalArcAng = attachedObject.getDoubleValue("totalArcAng");
                    if (attachedObject.getBooleanValue("fontItalic")) {
                        var10000 = fontStyle | 2;
                    }
                }

                int a = f.getSize();
                int b = f.getSize();
                double xscaleOfTextTop = attachedObject.getDoubleValue("xscaleOfTextTop");
                double yscaleOfTextTop = attachedObject.getDoubleValue("yscaleOfTextTop");
                graphics2d.setColor(inkpadColor);
                drawBorderedRoundOval(graphics2d, radiusX, radiusY, circleBorder);
                Font fontTextTop = scaleFont(f, xscaleOfTextTop, yscaleOfTextTop);
                drawTopText(graphics2d, radiusX, radiusY, orgName, fontTextTop, totalArcAng, a, b, offset, circleBorder);
            } else {
                Font horizontalTextFont;
                int fontStyle;
                double xscaleOfHorizontalText;
                double yscaleOfHorizontalText;
                if ("TextBottom".equals(attachedType)) {
                    fontStyle = "plain".equals(fontWeight) ? 1 : 0;
                    if (attachedObject.getBooleanValue("fontItalic")) {
                        var10000 = fontStyle | 2;
                    }

                    xscaleOfHorizontalText = attachedObject.getDoubleValue("angleOfFanBottom");
                    yscaleOfHorizontalText = attachedObject.getDoubleValue("xscaleOfTextBottom");
                    double yscaleOfTextBottom = attachedObject.getDoubleValue("yscaleOfTextBottom");
                    horizontalTextFont = scaleFont(f, yscaleOfHorizontalText, yscaleOfTextBottom);
                    drawBottomText(graphics2d, radiusX, radiusY, content3, horizontalTextFont, xscaleOfHorizontalText, offset + circleBorder + metrics.getDescent(), offset + circleBorder + metrics.getDescent());
                } else if ("HorizontalText".equals(attachedType)) {
                    fontStyle = "bold".equals(fontWeight) ? 1 : 0;
                    if (attachedObject.getBooleanValue("fontItalic")) {
                        var10000 = fontStyle | 2;
                    }

                    xscaleOfHorizontalText = attachedObject.getDoubleValue("xscaleOfHorizontalText");
                    yscaleOfHorizontalText = attachedObject.getDoubleValue("yscaleOfHorizontalText");
                    horizontalTextFont = scaleFont(f, xscaleOfHorizontalText, yscaleOfHorizontalText);
                    FontMetrics fontMetrics1 = createFontMetrics(horizontalTextFont);
                    int horizontalTextY = radiusY + (fontMetrics1.getHeight() / 2 - fontMetrics1.getDescent());
                    drawHorizontalCenteredText(graphics2d, radiusX, horizontalTextY + 63, content2, horizontalTextFont);
                }
            }
        }

        graphics2d.dispose();
        return bufferedImage;
    }

    public static Color createColor(String colorStr) {
        Color color = null;
        if (colorStr != null) {
            if (colorStr.startsWith("#")) {
                colorStr = colorStr.substring(1);
                byte fontR = Hex.decode(colorStr.substring(0, 2))[0];
                int fontR1 = fontR < 0 ? fontR + 256 : fontR;
                byte fontG = Hex.decode(colorStr.substring(2, 4))[0];
                int fontG1 = fontG < 0 ? fontG + 256 : fontG;
                byte fontB = Hex.decode(colorStr.substring(4, 6))[0];
                int fontB1 = fontB < 0 ? fontB + 256 : fontB;
                color = new Color(fontR1, fontG1, fontB1);
            } else {
                switch (colorStr.hashCode()) {
                    case 49:
                        if (colorStr.equals("1")) {
                            color = new Color(230, 0, 18);
                            return color;
                        }
                        break;
                    case 50:
                        if (colorStr.equals("2")) {
                            color = new Color(18, 230, 18);
                            return color;
                        }
                        break;
                    case 51:
                        if (colorStr.equals("3")) {
                            color = new Color(18, 0, 230);
                            return color;
                        }
                        break;
                    case 52:
                        if (colorStr.equals("4")) {
                            color = new Color(0, 0, 0);
                            return color;
                        }
                }

                color = new Color(230, 0, 18);
            }
        }

        return color;
    }

    public static Graphics2D getSmoothGraphics2D(BufferedImage bufferedImage) {
        Graphics2D graphics2d = (Graphics2D) bufferedImage.getGraphics();
        graphics2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        return graphics2d;
    }

    public static void drawBorderedRoundOval(Graphics2D graphics2d, int radiusX, int radiusY, int border) {
        graphics2d.setStroke(new BasicStroke((float) border));
        graphics2d.drawOval(border, border, (radiusX - border) * 2, (radiusY - border) * 2);
    }

    public static Font scaleFont(Font font, double xscale, double yscale) {
        AffineTransform scaleTransform = new AffineTransform();
        scaleTransform.scale(xscale, yscale);
        return font.deriveFont(scaleTransform);
    }

    public static void drawTopText(Graphics2D graphics2d, int radiusX, int radiusY, String companyName, Font font, double totalArcAng, int a, int b, int border, int circleborder) {
        double startAng = 90.0D + totalArcAng / 2.0D;
        int count = companyName.length();
        int w = radiusX - a - border - circleborder;
        int d = radiusY - b - border - circleborder;
        char[] texts = companyName.toCharArray();

        for (int i = 0; i < count; ++i) {
            List<Double> list = rerturnAngleList(count, totalArcAng, startAng, w, d, true);
            Point point = returnPoint((Double) list.get(i), w, d);
            double α = Obliquity(point, w, d);
            graphics2d.setFont(font);
            FontMetrics metrics = graphics2d.getFontMetrics();
            Point p = returnNewPosition(point, metrics, texts[i], α, true);
            AffineTransform transform;
            if (point.getY() < 0.0D) {
                transform = AffineTransform.getRotateInstance(3.141592653589793D - α);
            } else {
                transform = AffineTransform.getRotateInstance(-α);
            }

            Font thisFont = font.deriveFont(transform);
            graphics2d.setFont(thisFont);
            graphics2d.drawString(String.valueOf(texts[i]), p.x + radiusX, -p.y + radiusY);
        }

    }

    public static void drawBottomText(Graphics2D graphics2d, int radiusX, int radiusY, String text, Font font, double totalArcAng, int a, int b) {
        double startAng = -90.0D - totalArcAng / 2.0D;
        int count = text.length();
        int w = radiusX - a;
        int d = radiusY - b;
        char[] texts = text.toCharArray();

        for (int i = 0; i < count; ++i) {
            List<Double> list = rerturnAngleList(count, totalArcAng, startAng, w, d, false);
            Point point = returnPoint((Double) list.get(i), w, d);
            double α = Obliquity(point, w, d);
            graphics2d.setFont(font);
            FontMetrics metrics = graphics2d.getFontMetrics();
            Point p = returnNewPosition(point, metrics, texts[i], α, false);
            AffineTransform transform = AffineTransform.getRotateInstance(-α);
            Font thisFont = font.deriveFont(transform);
            graphics2d.setFont(thisFont);
            graphics2d.drawString(String.valueOf(texts[i]), p.x + radiusX, -p.y + radiusY);
        }

    }

    public static List<Double> rerturnAngleList(int count, double totalArcAng, double startAng, int w, int d, boolean top) {
        List<Double> list = new ArrayList();
        double step = top ? 0.5D : -0.5D;
        int alCount = (int) Math.ceil(totalArcAng / Math.abs(step)) + 1;
        double[] angArr = new double[alCount];
        double[] arcLenArr = new double[alCount];
        int num = 0;
        double accArcLen = 0.0D;
        angArr[num] = startAng;
        arcLenArr[num] = accArcLen;
        num = num + 1;
        Point point = returnPoint(startAng, w, d);
        double lastX = point.getX();
        double lastY = point.getY();

        double arcPer;
        double arcL;
        double ang;
        for (arcPer = startAng - step; num < alCount; arcPer -= step) {
            Point p = returnPoint(arcPer, w, d);
            arcL = p.getX();
            ang = p.getY();
            accArcLen += Math.sqrt((lastX - arcL) * (lastX - arcL) + (lastY - ang) * (lastY - ang));
            angArr[num] = arcPer;
            arcLenArr[num] = accArcLen;
            lastX = arcL;
            lastY = ang;
            ++num;
        }

        arcPer = accArcLen / (double) count;

        for (int i = 0; i < count; ++i) {
            arcL = (double) (count - i - 1) * arcPer + arcPer / 2.0D;
            ang = 0.0D;

            for (int p = 0; p < arcLenArr.length - 1; ++p) {
                if (arcLenArr[p] <= arcL && arcL <= arcLenArr[p + 1]) {
                    ang = arcL >= (arcLenArr[p] + arcLenArr[p + 1]) / 2.0D ? angArr[arcLenArr.length - p - 1] : angArr[arcLenArr.length - p];
                    break;
                }
            }

            list.add(ang);
        }

        return list;
    }

    public static Point returnPoint(double angle, int w, int d) {
        Point point = new Point();
        double α = Math.toRadians(angle);
        if (angle <= 180.0D && angle >= 0.0D) {
            point.y = (int) Math.sqrt(Math.pow((double) w, 2.0D) * Math.pow((double) d, 2.0D) / (Math.pow((double) w, 2.0D) - Math.pow((double) d, 2.0D) * (1.0D - 1.0D / Math.pow(Math.sin(α), 2.0D))));
        } else {
            point.y = -((int) Math.sqrt(Math.pow((double) w, 2.0D) * Math.pow((double) d, 2.0D) / (Math.pow((double) w, 2.0D) - Math.pow((double) d, 2.0D) * (1.0D - 1.0D / Math.pow(Math.sin(α), 2.0D)))));
        }

        if (angle <= 90.0D && angle >= -90.0D) {
            point.x = (int) Math.sqrt(Math.pow((double) w, 2.0D) * Math.pow((double) d, 2.0D) / (Math.pow((double) d, 2.0D) - Math.pow((double) w, 2.0D) * (1.0D - 1.0D / Math.pow(Math.cos(α), 2.0D))));
        } else {
            point.x = -((int) Math.sqrt(Math.pow((double) w, 2.0D) * Math.pow((double) d, 2.0D) / (Math.pow((double) d, 2.0D) - Math.pow((double) w, 2.0D) * (1.0D - 1.0D / Math.pow(Math.cos(α), 2.0D)))));
        }

        return point;
    }

    public static Point returnNewPosition(Point point, FontMetrics metrics, char ch, double α, boolean top) {
        Point p = new Point();
        if (top) {
            if (point.getY() < 0.0D && point.getX() > 0.0D) {
                p.x = (int) ((double) point.x + (double) (metrics.charWidth(ch) / 2) * Math.cos(α));
                p.y = (int) ((double) point.y + (double) (metrics.charWidth(ch) / 2) * Math.sin(α));
            } else if (point.getY() < 0.0D && point.getX() > 0.0D) {
                p.x = (int) ((double) point.x + (double) (metrics.charWidth(ch) / 2) * Math.cos(α));
                p.y = (int) ((double) point.y - (double) (metrics.charWidth(ch) / 2) * Math.sin(α));
            } else if (point.getY() < 0.0D && point.getX() < 0.0D) {
                p.x = (int) ((double) point.x + (double) (metrics.charWidth(ch) / 2) * Math.cos(α));
                p.y = (int) ((double) point.y + (double) (metrics.charWidth(ch) / 2) * Math.sin(α));
            } else {
                p.x = (int) ((double) point.x - (double) (metrics.charWidth(ch) / 2) * Math.cos(α));
                p.y = (int) ((double) point.y - (double) (metrics.charWidth(ch) / 2) * Math.sin(α));
            }
        } else {
            p.x = (int) ((double) point.x - (double) (metrics.charWidth(ch) / 2) * Math.cos(α));
            p.y = (int) ((double) point.y - (double) (metrics.charWidth(ch) / 2) * Math.sin(α));
        }

        return p;
    }

    public static double Obliquity(Point point, int w, int d) {
        double k = 0.0D;
        double α = 0.0D;
        double x = point.getX();
        double y = point.getY();
        k = -Math.pow((double) d, 2.0D) * x / Math.pow((double) w, 2.0D) / y;
        α = Math.atan(k);
        return α;
    }

    public static FontMetrics createFontMetrics(Font font) {
        BufferedImage bufferedIamge = new BufferedImage(1, 1, 2);
        Graphics2D graphics2d = getSmoothGraphics2D(bufferedIamge);
        graphics2d.setFont(font);
        return graphics2d.getFontMetrics();
    }

    public static void drawHorizontalCenteredText(Graphics2D graphics2d, int radiusX, int textY, String text, Font textFont) {
        graphics2d.setFont(textFont);
        FontMetrics fontMetrics = graphics2d.getFontMetrics();
        int textWidth = fontMetrics.stringWidth(text);
        graphics2d.drawString(text, radiusX - textWidth / 2, textY);
    }

    static BufferedImage drawSeal(BufferedImage bufferedImage, JSONObject templateRule, Map parameters, Properties fontTable, String fontPath) throws ParseException, ScriptException {
        int width = templateRule.getIntValue("width");
        int height = templateRule.getIntValue("height");
        if (bufferedImage == null) {
            bufferedImage = SealUtils.createTransparentBufferedImage(width != 0 ? width : 1, height != 0 ? height : 1);
        } else {
            width = bufferedImage.getWidth();
            height = bufferedImage.getHeight();
        }

        Color color = createColor(templateRule.getString("color"));
        double alpha = templateRule.getDoubleValue("alpha");
        Graphics2D graphics2d = SealUtils.getSmoothGraphics2D(bufferedImage);
        JSONArray attachedObjects = templateRule.getJSONArray("attachedObjects");
        if (attachedObjects != null) {
            for (int i = 0; i < attachedObjects.size(); ++i) {
                JSONObject attachedObject = attachedObjects.getJSONObject(i);
                String attachedType = attachedObject.getString("attachedType");
                String colorStr = attachedObject.getString("color");
                if (colorStr != null) {
                    color = createColor(colorStr);
                }

                graphics2d.setColor(color);
                int w;
                int h;
                int border;
                int offset;
                if ("Circle".equals(attachedType)) {
                    w = attachedObject.getIntValue("width");
                    h = attachedObject.getIntValue("height");
                    border = attachedObject.getIntValue("border");
                    offset = attachedObject.getIntValue("offset");
                    SealUtils.drawBorderedCircle(graphics2d, w, h, border, offset);
                } else if ("RoundRect".equals(attachedType)) {
                    w = attachedObject.getIntValue("width");
                    h = attachedObject.getIntValue("height");
                    border = attachedObject.getIntValue("border");
                    offset = attachedObject.getIntValue("offset");
                    int arcWidth = attachedObject.getIntValue("arcWidth");
                    int arcHeight = attachedObject.getIntValue("arcHeight");
                    SealUtils.drawBorderedRoundRect(graphics2d, w, h, border, arcWidth, arcHeight, offset);
                } else if ("Pentagram".equals(attachedType)) {
                    w = attachedObject.getIntValue("width");
                    h = attachedObject.getIntValue("height");
                    border = attachedObject.getIntValue("pentagramRadius");
                    SealUtils.drawPentagram(graphics2d, w / 2, h / 2, border);
                } else if ("Rectangle".equals(attachedType)) {
                    w = attachedObject.getIntValue("width");
                    h = attachedObject.getIntValue("height");
                    border = attachedObject.getIntValue("border");
                    offset = attachedObject.getIntValue("offset");
                    graphics2d.setStroke(new BasicStroke((float) border));
                    graphics2d.drawRect(width / 2 + border / 2 - w / 2 + offset, height / 2 + border / 2 - h / 2 + offset, w - border - offset * 2, h - border - offset * 2);
                } else if ("Text".equals(attachedType)) {
                    String objectClass = attachedObject.getString("objectClass");
                    String objectName = attachedObject.getString("objectName");
                    Object objectValue = attachedObject.get("objectValue");
                    if (objectValue == null && parameters != null) {
                        objectValue = parameters.get(objectName);
                    }

                    String objectText = null;
                    String fontFamily;
                    if ("Date".equals(objectClass)) {
                        Date dateObjectValue = null;
                        SimpleDateFormat simpleDateFormat;
                        String dateFormat;
                        if (objectValue instanceof Date) {
                            dateObjectValue = (Date) objectValue;
                        } else if (objectValue instanceof String) {
                            dateFormat = "yyyy-MM-dd'T'HH:mm:ssZ";
                            simpleDateFormat = new SimpleDateFormat(dateFormat);
                            dateObjectValue = simpleDateFormat.parse((String) objectValue);
                        } else if (objectValue instanceof Integer) {
                            dateObjectValue = new Date((Long) objectValue);
                        }

                        dateFormat = attachedObject.getString("dateFormat");
                        if (dateFormat == null) {
                            dateFormat = "yyyy-MM-dd";
                        }

                        simpleDateFormat = new SimpleDateFormat(dateFormat);
                        objectText = simpleDateFormat.format(dateObjectValue);
                    } else {
                        if (!"String".equals(objectClass)) {
                            continue;
                        }

                        objectText = (String) objectValue;
                        fontFamily = attachedObject.getString("stringTransform");
                        if ("lowerCase".equals(fontFamily)) {
                            objectText = objectText.toLowerCase();
                        } else if ("upperCase".equals(fontFamily)) {
                            objectText = objectText.toUpperCase();
                        }
                    }

                    if (objectText != null) {
                        fontFamily = attachedObject.getString("fontFamily");
                        String fontWeight;
                        if (fontTable != null) {
                            fontWeight = fontTable.getProperty(fontFamily);
                            fontFamily = fontWeight == null ? fontFamily : fontWeight;
                        }

                        fontWeight = attachedObject.getString("fontWeight");
                        int fontSize = attachedObject.getIntValue("fontSize");
                        int fontStyle = "bold".equals(fontWeight) ? 1 : 0;
                        fontStyle = attachedObject.getBooleanValue("fontItalic") ? fontStyle | 2 : fontStyle;
                        Font textFont = fontPath == null ? new Font(fontFamily, fontStyle, fontSize) : getFont(fontPath, fontFamily, fontStyle, fontSize);
                        String textStyle = attachedObject.getString("textStyle");
                        if (!"VerticalHorizontalCentered".equals(textStyle) && !"BelowHorizontalCentered".equals(textStyle) && !"HorizontalCentered".equals(textStyle) && !"SquareCentered".equals(textStyle)) {
                            double fanAngle;
                            JSONObject fanAngleFunc;
                            String script;
                            Object result;
                            int fanRadius;
                            double yscale;
                            if ("ClockwiseFan".equals(textStyle)) {
                                fanRadius = attachedObject.getIntValue("fanRadius");
                                fanAngle = attachedObject.getDoubleValue("fanAngle");
                                fanAngleFunc = attachedObject.getJSONObject("fanAngleFunc");
                                if (fanAngleFunc != null) {
                                    script = fanAngleFunc.getString("script");
                                    result = eval(script, parameters);
                                    fanAngle = Double.parseDouble(result.toString());
                                }

                                double xscale = attachedObject.getDoubleValue("xscale");
                                yscale = attachedObject.getDoubleValue("yscale");
                                if (xscale != 1.0D || yscale != 1.0D) {
                                    textFont = SealUtils.scaleFont(textFont, xscale, yscale);
                                }

                                SealUtils.drawClockwiseFanText(graphics2d, width, height, objectText, textFont, fanRadius, fanAngle);
                            } else if ("AnticlockwiseFan".equals(textStyle)) {
                                fanRadius = attachedObject.getIntValue("fanRadius");
                                fanAngle = attachedObject.getDoubleValue("fanAngle");
                                fanAngleFunc = attachedObject.getJSONObject("fanAngleFunc");
                                if (fanAngleFunc != null) {
                                    script = fanAngleFunc.getString("script");
                                    result = eval(script, parameters);
                                    fanAngle = Double.parseDouble(result.toString());
                                }

                                double xscale = attachedObject.getDoubleValue("xscale");
                                yscale = attachedObject.getDoubleValue("yscale");
                                if (xscale != 1.0D || yscale != 1.0D) {
                                    textFont = SealUtils.scaleFont(textFont, xscale, yscale);
                                }

                                SealUtils.drawAnticlockwiseFanText(graphics2d, width, height, objectText, textFont, fanRadius, fanAngle);
                            }
                        } else {
                            double xscale = attachedObject.getDoubleValue("xscale");
                            double yscale = attachedObject.getDoubleValue("yscale");
                            int wordSpacing = attachedObject.getIntValue("wordSpacing");
                            int textY = 0;
                            int offsetY = attachedObject.getIntValue("offsetY");
                            if (xscale != 1.0D || yscale != 1.0D) {
                                textFont = SealUtils.scaleFont(textFont, xscale, yscale);
                            }

                            FontMetrics fontMetrics = SealUtils.createFontMetrics(textFont);
                            if (width == 0 && height == 0 && bufferedImage.getWidth() == 1 && bufferedImage.getHeight() == 1) {
                                width = fontMetrics.stringWidth(objectText);
                                height = fontMetrics.getHeight();
                                bufferedImage = SealUtils.resizeCanvas(bufferedImage, width, height, 5);
                                graphics2d = SealUtils.getSmoothGraphics2D(bufferedImage);
                                graphics2d.setColor(color);
                            }

                            if ("VerticalHorizontalCentered".equals(textStyle)) {
                                textY = height / 2 + (fontMetrics.getHeight() / 2 - fontMetrics.getDescent());
                            } else if ("BelowHorizontalCentered".equals(textStyle)) {
                                textY = height + fontMetrics.getAscent();
                            } else if ("HorizontalCentered".equals(textStyle)) {
                                textY = attachedObject.getIntValue("textY");
                                if (textY == 0) {
                                    textY = fontMetrics.getAscent();
                                }
                            }

                            textY += offsetY;
                            if (!"SquareCentered".equals(textStyle) && (fontMetrics.stringWidth(objectText) > width || textY + fontMetrics.getDescent() > height)) {
                                width = fontMetrics.stringWidth(objectText) <= width ? width : fontMetrics.stringWidth(objectText);
                                height = textY + fontMetrics.getDescent() <= height ? height : textY + fontMetrics.getDescent();
                                bufferedImage = SealUtils.resizeCanvas(bufferedImage, width, height, 8);
                                graphics2d = SealUtils.getSmoothGraphics2D(bufferedImage);
                                graphics2d.setColor(color);
                            }

                            if ("SquareCentered".equals(textStyle)) {
                                drawHorizontalCenteredText3(graphics2d, width, height, objectText, textFont, textY, wordSpacing);
                            } else {
                                drawHorizontalCenteredText2(graphics2d, width, height, objectText, textFont, textY, wordSpacing);
                            }
                        }
                    }
                }
            }
        }

        graphics2d.dispose();
        if (alpha >= 0.0D) {
            bufferedImage = SealUtils.makeImageTranslucent(bufferedImage, alpha);
        }

        return bufferedImage;
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

            for (int i = 0; i < charsText.length; ++i) {
                int wordWidth = fontMetrics.charWidth(charsText[i]);
                graphics2d.drawString(String.valueOf(charsText[i]), width / 2 - textWidth / 2 + wordsWidth + i * wordSpacing, textY);
                wordsWidth += wordWidth;
            }

        }
    }

    public static void drawHorizontalCenteredText3(Graphics2D graphics2d, int width, int height, String text, Font textFont, int textY, int wordSpacing) {
        if (wordSpacing == 0) {
            drawHorizontalCenteredText(graphics2d, width, height, text, textFont, textY);
        } else {
            graphics2d.setFont(textFont);
            if (text.length() < 3) {
                text = text + "之印";
            }

            if (text.length() < 4) {
                text = text + "印";
            }

            char[] charsText = text.toCharArray();

            for (int i = 0; i < charsText.length; ++i) {
                int widths;
                if (i < 2) {
                    widths = width / 2 - 2;
                } else {
                    widths = width / 2 - 45;
                }

                int heights;
                if (i % 2 == 0) {
                    heights = height / 2 - 5;
                } else {
                    heights = height / 2 + 32;
                }

                graphics2d.drawString(String.valueOf(charsText[i]), widths, heights);
            }

        }
    }

    public static Font getFont(String fontPath, String fontName, int fontStyle, int fontSize) {
        Font font = null;
        if ("宋体".equals(fontName)) {
            font = getFile(fontPath, "simsun.ttc", fontStyle, fontSize);
        } else if ("华文楷体".equals(fontName)) {
            font = getFile(fontPath, "STKAITI.TTF", fontStyle, fontSize);
        }

        return font;
    }

    public static Font getFile(String fontPath, String fileName, int fontStyle, int fontSize) {
        File file = new File(fontPath, fileName);

        try {
            FileInputStream fi = new FileInputStream(file);
            BufferedInputStream bi = new BufferedInputStream(fi);
            Font font = Font.createFont(0, bi);
            font = font.deriveFont(fontStyle, (float) fontSize);
            return font;
        } catch (Exception var8) {
            var8.printStackTrace();
            return null;
        }
    }

    public static Object eval(String script, Map parameters) throws ScriptException {
        ScriptEngineManager manager = new ScriptEngineManager();
        ScriptEngine engine = manager.getEngineByName("js");
        if (parameters != null) {
            Iterator iterator = parameters.entrySet().iterator();

            while (iterator.hasNext()) {
                Entry entry = (Entry) iterator.next();
                if (entry.getValue() != null) {
                    script = script.replaceAll("\\$\\{" + (String) entry.getKey() + "\\}", entry.getValue().toString());
                }
            }
        }

        Object result = engine.eval(script);
        return result;
    }
}
