package com.xforceplus.taxware.microservice.voucher.sdk;

import com.itextpdf.text.Document;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.*;
import com.itextpdf.text.pdf.qrcode.EncodeHintType;
import com.itextpdf.text.pdf.security.*;

import java.io.*;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.cert.Certificate;
import java.util.ArrayList;
import java.util.Hashtable;

/**
 * pdf板式
 *
 * @author Bobo
 * @create 2011/11/6 15:46
 * @since 1.0.0
 */
public class PdfVoucher {

    public void createSignaturePDF() throws Exception {
        createPDF2();
        // Creating the reader and the stamper，开始pdfreader
        File file = new File(newPDFPath);
        InputStream inputStream = new FileInputStream(file);
        System.out.println("File siz:" + inputStream.available());

        inputStream = inputStream2ResetInputStream(inputStream);

        PdfReader reader = new PdfReader(inputStream);
        // 目标文件输出流
        FileOutputStream os = new FileOutputStream(newPDFPath_s);
        // 创建签章工具PdfStamper ，最后一个boolean参数
        // false的话，pdf文件只允许被签名一次，多次签名，最后一次有效
        // true的话，pdf可以被追加签名，验签工具可以识别出每次签名之后文档是否被修改
        PdfStamper stamper = PdfStamper.createSignature(reader, os, '\0', null, true);

        // 增加省份图片
        PdfContentByte over = stamper.getOverContent(1);// 设置在第几页打印印章
        Image otherImage = Image.getInstance(picPath);// 选择图片
        otherImage.setAlignment(1);
        otherImage.scaleAbsolute(80, 50);// 控制图片大小
        otherImage.setAbsolutePosition(265, 312);// 控制图片位置水平 垂直
        over.addImage(otherImage);

        //证书签名
        // 获取数字签章属性对象
        PdfSignatureAppearance appearance = stamper.getSignatureAppearance();
        appearance.setReason(reason);
        appearance.setLocation(location);
        //设置签名的签名域名称，多次追加签名的时候，签名预名称不能一样，图片大小受表单域大小影响（过小导致压缩）
        appearance.setVisibleSignature(new Rectangle(388, 2, 618, 88), (1), "Signature" + (1));
        //读取图章图片
        Image image = Image.getInstance(companyPath);
        appearance.setSignatureGraphic(image);
        appearance.setCertificationLevel(PdfSignatureAppearance.NOT_CERTIFIED);
        //设置图章的显示方式，如下选择的是只显示图章（还有其他的模式，可以图章和签名描述一同显示）
        appearance.setRenderingMode(PdfSignatureAppearance.RenderingMode.GRAPHIC);
        // 摘要算法
        ExternalDigest digest = new BouncyCastleDigest();
        //将证书文件放入指定路径，并读取keystore ，获得私钥和证书链
        KeyStore ks = KeyStore.getInstance("PKCS12");
        ks.load(new FileInputStream(new File(pxfPath)), certPwd.toCharArray());
        String alias = ks.aliases().nextElement();
        PrivateKey pk = (PrivateKey) ks.getKey(alias, certPwd.toCharArray());
        Certificate[] chain = ks.getCertificateChain(alias);
        // 签名算法
        ExternalSignature signature = new PrivateKeySignature(pk, digestAlgorithm, null);
        MakeSignature.signDetached(appearance, digest, signature, chain, null, null, null, 0, subfilter);
    }

    public void createPDF() {
        testInvoiceMap();
        PdfReader reader;
        FileOutputStream out;
        ByteArrayOutputStream bos;
        PdfStamper stamper;
        try {
            out = new FileOutputStream(newPDFPath);// 输出流
            reader = new PdfReader(templatePath);// 读取pdf模板
            bos = new ByteArrayOutputStream();
            stamper = new PdfStamper(reader, bos);

            AcroFields form = stamper.getAcroFields();

            BaseFont bf = BaseFont.createFont("STSong-Light", "UniGB-UCS2-H", BaseFont.NOT_EMBEDDED);
            ArrayList<BaseFont> substitutionFonts = new ArrayList<>();
            substitutionFonts.add(bf);
            form.setSubstitutionFonts(substitutionFonts);
            java.util.Iterator<String> it = form.getFields().keySet().iterator();
            while (it.hasNext()) {
                String name = it.next();
                String value = invoiceMap.get(name) == null ? "" : invoiceMap.get(name);
                if (value.length() > 25) {
                    Font font = new Font(bf, 6, Font.NORMAL);
                    //set the field to bold
                    // form.setFieldProperty(name, "textsize",6, null);

                    //set the text of the form field
                    //pdfFormFields.SetField(nameOfField, "This:  Will Be Displayed In The Field");
                }
                form.setField(name, value, true);
                System.out.println(name + ":" + value);
            }

            PdfContentByte over = stamper.getOverContent(1);// 设置在第几页打印印章
            //二维码
            Hashtable<EncodeHintType, Object> hints = new Hashtable<>();
            hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
            //hints.put(EncodeHintType.MARGIN, 1);
            BarcodeQRCode barcodeQRCode = new BarcodeQRCode(content, width, height, hints);
            Image codeQrImage = barcodeQRCode.getImage();
            codeQrImage.setAlignment(1);
            codeQrImage.setBorder(0);
            codeQrImage.setBorderWidth(0);
            codeQrImage.scaleAbsolute(80, 50);// 控制图片大小
            codeQrImage.setAbsolutePosition(30, 320);// 控制图片位置水平 垂直
            over.addImage(codeQrImage);
            //添加监制章
            Image otherImage = Image.getInstance(picPath);// 选择图片
            otherImage.setAlignment(2);
            otherImage.scaleAbsolute(80, 50);// 控制图片大小
            otherImage.setAbsolutePosition(265, 312);// 控制图片位置水平 垂直
            over.addImage(otherImage);
            //销售方签章
            Image otherImage2 = Image.getInstance(companyPath);// 选择图片
            otherImage2.setAlignment(3);
            otherImage2.scaleAbsolute(108.8f, 81.3f);// 控制图片大小
            otherImage2.setAbsolutePosition(498, 4.5f);// 控制图片位置水平 垂直 //477, 8, 607, 94;488, 2, 618, 88
            over.addImage(otherImage2);


            stamper.setFormFlattening(true);// 如果为false那么生成的PDF文件还能编辑，一定要设为true
            stamper.close();

            Document doc = new Document();
            PdfCopy copy = new PdfCopy(doc, out);
            doc.open();
            PdfImportedPage importPage = copy.getImportedPage(new PdfReader(bos.toByteArray()), 1);
            copy.addPage(importPage);
            doc.resetPageCount();
            doc.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 根据pdf模板填充相应的值： 1，如果是根据excel填充的话，在用Acrobat生成PDF模板前，
     * Excel单元格格式最好设置成文本，否则pdf填充值时可能中文无法显示
     */
    public static void fromPDFTempletToPdfWithValue() {
        String fileName = "d:\\学生信息模板.pdf";
        try {
            PdfReader reader = new PdfReader(fileName);
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            PdfStamper ps = new PdfStamper(reader, bos);
            /**
             * 使用中文字体 如果是利用 AcroFields填充值的不需要在程序中设置字体，在模板文件中设置字体为中文字体就行了
             */
            BaseFont bf = BaseFont.createFont("STSong-Light", "UniGB-UCS2-H",
                    BaseFont.NOT_EMBEDDED);
            Font font = new Font(bf, 12, Font.NORMAL);
            AcroFields s = ps.getAcroFields();
            System.out.println("s: " + s);
            System.out.println("AcroFields: " + s.getFields());
            System.out.println("AcroFields.class: " + s.getFields().getClass());
            System.out.println("getSignatureNames: " + s.getSignatureNames());
            System.out.println("getSignatureNames: " + s.getTotalRevisions());
            System.out.println("s: " + s.getBlankSignatureNames());
            System.out.println("s: " + s.getFieldCache());
            System.out.println("s: " + s.getSubstitutionFonts());
            /*
             * int i = 1; for (Iterator it = s.getFields().keySet().iterator();
             * it.hasNext(); i++) { String name = (String) it.next(); String
             * value = s.getField(name); System.out.println("[" + i + "- name:" +
             * name + ", value: "+value+"]"); s.setField(""+name.trim(),
             * "aaa一二三"); }
             *
             * s.setField("Text1", "NOHI"); s.setField("Text2", "2011-04-05");//
             * 注意pdf中域的大小，这里设置的值太长，pdf中会显示不全
             */
            // 设置为true/false在点击生成的pdf文档的填充域时有区别，

            s.setField("xh", "201001");
            s.setField("xm", "姓名");
            s.setField("xb", "男");
            s.setField("cssj", "1989-02-15");
            s.setField("zy_id", "001");
            s.setField("zxf", "123");
            s.setField("bz", "三好学生");

            //Document document = new Document();
            //document.open();
            Image gif = Image.getInstance("d:\\图片.jpg");
            gif.setDpi(100, 100);
            gif.setBorderWidth(200);
            gif.scaleAbsolute(80, 100);
            gif.setAbsolutePosition(400, 700);
            PdfContentByte over = ps.getOverContent(1);
            over.addImage(gif);

            ps.setFormFlattening(true);
            ps.close();
            FileOutputStream fos = new FileOutputStream("d:\\学生信息导出.pdf");
            fos.write(bos.toByteArray());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // doc.close();
        }
    }
}
