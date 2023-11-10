package com.xforceplus.taxware.microservice.voucher.sdk.utils;

import com.itextpdf.text.Document;
import com.itextpdf.text.pdf.PdfCopy;
import com.itextpdf.text.pdf.PdfImportedPage;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.security.DigestAlgorithms;
import com.itextpdf.text.pdf.security.MakeSignature;

import javax.imageio.stream.FileImageInputStream;
import java.io.*;
import java.util.Map;

/**
 * @author Bobo
 * @create 2011/11/10 9:22
 * @since 1.0.0
 */
public class PDFMaking {

    // 生成的新文件路径
    private String pdfPath = "D:\\testPath\\pdfMakingTest\\MY_PDF_电子发票_min.pdf";
    // 统一监制章
    private String picPath = "D:\\testPath\\tongyijianzhizhang.gif";
    // 销售方签章
    private String companyPath = "D:\\testPath\\AIXDX66257.gif";
    // 证书
    private String pxfPath = "D:\\testPath\\pfx200266.pfx";
    // 签名私钥
    private String certPwd = "200266";
    // 证书
    private String signClearPath = "D:\\testPath\\clear.gif";


    public void createSignaturePDF() throws Exception {
        ByteArrayOutputStream byteArrayOutputStream = pafMakingTest();
        //pdf文件流
        InputStream inputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
        System.out.println("File siz:" + inputStream.available());
        // 签名证书文件流
        InputStream certInputStream = new FileInputStream(new File(pxfPath));
        byte[] signBytes = file2byte(companyPath);
        Map resultMap = SignPDFUtils.signPdf(inputStream, certInputStream, certPwd, signClearPath, signBytes, picPath, DigestAlgorithms.SHA1, null, MakeSignature.CryptoStandard.CMS, "", "");
        String ret = resultMap.get("ret") + "";
        if ("0".equals(ret)) {
            Object obj = resultMap.get("signedFileData");
            if (obj != null) {
                byte[] signedFileData = (byte[]) obj;
                InputStream sbs = new ByteArrayInputStream(signedFileData);
                System.out.println("Done File siz:" + sbs.available());
                createFileByInputStream(sbs,pdfPath);
            }
        }
    }

    public ByteArrayOutputStream pafMakingTest() throws Exception {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        Document doc = new Document();
        PdfCopy copy = new PdfCopy(doc, byteArrayOutputStream);
        doc.open();
        //发票信息
        ByteArrayOutputStream bosInvoiceInfo = ElectronicInvoiceUtils.createInvoiceInfoToPDF();
        PdfReader readerInvoiceInfo = new PdfReader(bosInvoiceInfo.toByteArray());
        copy.addDocument(readerInvoiceInfo);
        //销货清单
        PdfReader readerSalesList = new PdfReader(ElectronicInvoiceUtils.createSalesListToPDF().toByteArray());
        PdfImportedPage importPage = copy.getImportedPage(readerSalesList, 1);
        copy.addPage(importPage);
        doc.close();

        return byteArrayOutputStream;
    }

    //图片到byte数组
    public byte[] file2byte(String path) {
        byte[] data = null;
        FileImageInputStream input = null;
        try {
            input = new FileImageInputStream(new File(path));
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            byte[] buf = new byte[1024];
            int numBytesRead = 0;
            while ((numBytesRead = input.read(buf)) != -1) {
                output.write(buf, 0, numBytesRead);
            }
            data = output.toByteArray();
            output.close();
            input.close();
        } catch (FileNotFoundException ex1) {
            ex1.printStackTrace();
        } catch (IOException ex1) {
            ex1.printStackTrace();
        }
        return data;
    }

    public static boolean createFileByInputStream(InputStream inputStream, String outputFileName) throws IOException {
        OutputStream out = new FileOutputStream(outputFileName);
        // 判断输入或输出是否准备好
        if (inputStream != null && out != null) {
            int temp;
            // 开始拷贝
            while ((temp = inputStream.read()) != -1) {
                out.write(temp);
            }
            // 关闭输入输出流
            inputStream.close();
            out.close();
            return true;
        } else {
            return false;
        }
    }

}
