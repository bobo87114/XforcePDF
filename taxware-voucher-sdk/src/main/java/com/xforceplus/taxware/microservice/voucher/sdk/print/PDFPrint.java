package com.xforceplus.taxware.microservice.voucher.sdk.print;

import java.io.InputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author Bobo
 * @create 2011/11/10 9:23
 * @since 1.0.0
 */
public class PDFPrint {


    public void testPDFPrint() throws Exception {
        Long beginTime = System.currentTimeMillis();
        InputStream inputStream = ElectronicInvoiceUtils.thinBodyPDFMaking(getElectronicInvoiceInfo());
        System.out.println("耗时:" + (System.currentTimeMillis() - beginTime));
        //System.out.println("File siz:" + ElectronicInvoiceUtils.fileSizeConversion(inputStream.available()));
//        String pdfUrlKey = String.format("file/contract/thinBody/%s/%s.pdf", getDay(), UUID.randomUUID());
//        Long beginTime2 = System.currentTimeMillis();
//        OSSUtils.putObjectByInputStream(pdfUrlKey, inputStream);
//        Long endTime = System.currentTimeMillis();
//        System.out.println("耗时:" + (endTime - beginTime2));
//        System.out.println("总耗时:" + (endTime - beginTime));
//        String allUrl = OSSUtils.OSS_ENDPOINT + OSSUtils.bucketName + "/" +
//                "" + pdfUrlKey;
//        System.out.println("pdfUrlKey:" + pdfUrlKey);
//        System.out.println("allUrl:" + allUrl);
        // 讲pdf输出到本地
        PDFMakingTest.createFileByInputStream(inputStream, pdfPath);
    }

    public static ElectronicInvoiceInfo getElectronicInvoiceInfo() {
        ElectronicInvoiceInfo electronicInvoiceInfo = new ElectronicInvoiceInfo();
        //electronicInvoiceInfo.setTitle("黑龙江电子普通发票");
        electronicInvoiceInfo.setEwmContent("01,04,3700164320,44425925,960.38,20180702,08823726172990860402,70F9");
        electronicInvoiceInfo.setMachineCode("499098866982");
        electronicInvoiceInfo.setInvoiceCode("3700164320");
        electronicInvoiceInfo.setInvoiceNo("44425925");
        electronicInvoiceInfo.setInvoiceDate("2018年07月02日");
        electronicInvoiceInfo.setCheckCode("08823 72617 29908 60402");
        electronicInvoiceInfo.setPurchaserName("威海海依达斯文化产业发展有限公司");
        electronicInvoiceInfo.setPurchaserTaxNo("9137100034910171XF");
        electronicInvoiceInfo.setPurchaserAddressTel("山东省威海市高区古寨西路阮家寺18号 13561890079");
        electronicInvoiceInfo.setPurchaserBankNameAccount("威海市商业银行新威支行 817820001421000646");
        electronicInvoiceInfo.setCipherText("0339/2*6+//9*+10/<1876-/49241-83/173/><54<</>+1/8*->1<-*4>5293<27945138/*5<<2/58*<680468*<->9+018+19062+1-*37053");
        electronicInvoiceInfo.setCipherText("+1*-1-385+135885/6364393535040**4/80/><157*1>1+77242>6/2*4637/5/3*5-13/815>27>/1-71762->8*4/80/><157*1>18<26");
        electronicInvoiceInfo.setSellerName("山东德邦物流有限公司");
        electronicInvoiceInfo.setSellerTaxNo("91370782494393931N");
        electronicInvoiceInfo.setSellerAddressTel("山东省潍坊市诸城市西环路230号 13370934315");
        electronicInvoiceInfo.setSellerBankNameAccount("中国工商银行诸城市支行 1607004109010058144");
        electronicInvoiceInfo.setRemark("");
        electronicInvoiceInfo.setCashierName("尹赵明");
        electronicInvoiceInfo.setCheckerName("徐伟");
        electronicInvoiceInfo.setInvoiceName("宿彩霞");
        List<InvoiceDetailInfo> invoiceDetailInfoList = new ArrayList<>();
        Double sumAmountWithoutTax = 0D;
        Double sumTaxAmount = 0D;
        int num = 1;
        electronicInvoiceInfo.setTemplate("1");
        for (int i = 0; i < num; i++) {
            InvoiceDetailInfo invoiceDetailInfo = new InvoiceDetailInfo();
            invoiceDetailInfo.setCargoName("*经纪代理服务*货运代理服务");
            invoiceDetailInfo.setItemSpec("");
            invoiceDetailInfo.setQuantityUnit("");
            invoiceDetailInfo.setQuantity(NumberToCNUtils.rvZeroAndDot(String.format("%.8f", 1.00)));
            invoiceDetailInfo.setUnitPrice(NumberToCNUtils.rvZeroAndDot("960.38"));
            invoiceDetailInfo.setAmountWithoutTax("960.38");
            invoiceDetailInfo.setTaxRate("6%");
            invoiceDetailInfo.setTaxAmount("57.62");
            if (i == 1) {
                invoiceDetailInfo.setTaxRate("不征税");
                invoiceDetailInfo.setTaxAmount("-8888888.88");
                invoiceDetailInfo.setAmountWithoutTax("-888888888.88");
                invoiceDetailInfo.setQuantity(NumberToCNUtils.rvZeroAndDot(String.format("%.8f", 888888.0123456789922)));
                invoiceDetailInfo.setUnitPrice(NumberToCNUtils.rvZeroAndDot(String.format("%.8f", 888888.0123456789922)));
//            } else if (i == 2) {
//                invoiceDetailInfo.setTaxRate("100%");
//                invoiceDetailInfo.setUnitPrice(NumberToCNUtils.rvZeroAndDot(String.format("%.8f", 1.289123456)));
//            } else if (i == 3) {
//                invoiceDetailInfo.setTaxRate("17%");
            }
            String amountWithoutTax = invoiceDetailInfo.getAmountWithoutTax();
            sumAmountWithoutTax += Double.valueOf(amountWithoutTax);
            String taxAmount = invoiceDetailInfo.getTaxAmount();
            sumTaxAmount += Double.valueOf(taxAmount);

            invoiceDetailInfoList.add(invoiceDetailInfo);
        }

        electronicInvoiceInfo.setInvoiceDetailInfoList(invoiceDetailInfoList);

        electronicInvoiceInfo.setAmountWithTaxLowercase(String.format("%s%.2f", InvoiceConstants.RMB_SYMBOL, (sumAmountWithoutTax + sumTaxAmount)));
        electronicInvoiceInfo.setAmountWithoutTax(String.format("%s%.2f", InvoiceConstants.RMB_SYMBOL, sumAmountWithoutTax));
        electronicInvoiceInfo.setTaxAmount(String.format("%s%.2f", InvoiceConstants.RMB_SYMBOL, sumTaxAmount));

        BigDecimal numberOfMoney = new BigDecimal((sumAmountWithoutTax + sumTaxAmount));
        String amountWithTaxUppercase = NumberToCNUtils.number2CNMontrayUnit(numberOfMoney);
        electronicInvoiceInfo.setAmountWithTaxUppercase(amountWithTaxUppercase);
        System.out.println(electronicInvoiceInfo.getAmountWithTaxUppercase());
//        electronicInvoiceInfo.setProductOil(true);
//        electronicInvoiceInfo.setSalesList(true);
//        electronicInvoiceInfo.setOutputMinus(true);
        return electronicInvoiceInfo;
    }

    public static String getDay() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        Date now = new Date();
        String nowString = sdf.format(now);
        return nowString;
    }
}
