package com.xforceplus.taxware.microservice.voucher.sdk.utils;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * 电子发票工具类
 *
 * @author Bobo
 * @create 2018/9/29 10:44
 * @since 1.0.0
 */
public class ElectronicInvoiceUtils {
    public static final BaseColor INVOICE_COLOR_GOLDEN = new BaseColor(158, 82, 10);

    public static ByteArrayOutputStream createInvoiceInfoToPDF() throws Exception {
        String templatePath = "D:\\testPath\\pdf\\Template_M_9_3.pdf";
        PdfReader reader = new PdfReader(templatePath);// 读取pdf模板
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        PdfStamper stamper = new PdfStamper(reader, bos);
        PdfContentByte over = stamper.getOverContent(1);
        com.bobo.tools.util.InvoiceInfoPDFUtils.addInvoiceInfo(over);
        // 如果为false那么生成的PDF文件还能编辑，一定要设为true
        stamper.setFormFlattening(true);
        stamper.close();
        return bos;
    }

    public static ByteArrayOutputStream createSalesListToPDF() throws DocumentException, IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        Document document = new Document(PageSize.A4, 10F, 10F, 25, 5);
        PdfWriter.getInstance(document, bos);
        document.open();
        com.bobo.tools.util.SalesListPDFUtils.addSalesList(document);
        document.close();
        return bos;
    }
}
