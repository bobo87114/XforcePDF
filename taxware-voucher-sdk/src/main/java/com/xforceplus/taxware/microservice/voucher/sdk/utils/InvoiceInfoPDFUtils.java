package com.xforceplus.taxware.microservice.voucher.sdk.utils;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Image;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfContentByte;

/**
 * 生成发票信息PDF
 *
 * @author Bobo
 * @create 2018/9/29 11:33
 * @since 1.0.0
 */
public class InvoiceInfoPDFUtils {
    public static String content = "01,10,150000045545,15425272,315.33,20180807,13578716668795985910,75F3";
    //length=29
    // String sellerName ="北京我爱我家房地产经纪有限公司北京我爱我家房地产经有限公司";
    //length=34
    //String sellerName ="北京我爱我家房地产经纪有限公司北京我爱我家房地产经有限公司我有限公司";
    //length=39
    //String sellerName ="北京我爱我家房地产经纪有限公司北京我爱我家房地产经有限公司我家房地产经有限公司";
    //length=52
    public static String sellerName = "北京我爱我家房地产经纪有限公司北京我爱我家房地产经有限公司我家房地产经有限经有限公司我家房地产经有限公司";
    //length=43
    //String sellerAddress="上海市浦东新张江高科宜山路2000号利丰广场20幢1601室 021-24165800";
    //length=47
    //String sellerAddress="上海市浦东新区张江镇张江高科宜山路2000号利丰广场20幢1601室 021-24165800";
    //length=53
    public static String sellerAddress = "地球亚洲中国上海市浦东新区张江镇张江高科宜山路2000号利丰广场20幢1601室 021-24165800";
    //length = 39
    // String sellerBankAccount ="上海市浦东新区北京银行东单支行 01090945000120107088888";
    //length=43
    //String sellerBankAccount ="上海市浦东新区张江镇宜北京银行东单支行 01090945000120107088888";
    //length=50
    public static String sellerBankAccount = "上海市浦东新区上海市浦东新区张江镇宜北京银行东单支行 01090945000120107088888";
    //length=86
    //String sellerBankAccount ="上海市浦东新区张江镇宜北京银行东单支行 01090945000120107088888上海市浦东新区张江镇宜北京银行东单支行 01090945000120107088888";
    //length=92
    public static String remark = "门店号:门店号:备注备注备注备注备注备注备注备注备注备注备注备注门店号:备注备注备注备注备注备注备注备注备注备注备注备注门店号:备注备注备注备注备注备注备注备注备注备注备注备注";

    public static void addInvoiceInfo(PdfContentByte over) throws Exception {
        //二维码
        Image codeQrImage = Image.getInstance(QrCodeUtils.createQr(content, QrCodeUtils.FORMAT_PNG, 117, 120));
        codeQrImage.setAlignment(1);
        codeQrImage.setBorder(0);
        codeQrImage.setBorderWidth(0);
        codeQrImage.scaleAbsolute(64, 60);// 控制图片大小
        codeQrImage.setAbsolutePosition(30, 330);// 控制图片位置水平 垂直
        over.addImage(codeQrImage, true);
        //开始写入文本
        over.beginText();
        //设置字体
        BaseFont bf = BaseFont.createFont("STSong-Light", "UniGB-UCS2-H", BaseFont.NOT_EMBEDDED);
        assemblerContentInfo(over, bf);
        over.endText();
    }

    public static void assemblerContentInfo(PdfContentByte over, BaseFont bf) {
        //标题
        over.setFontAndSize(bf, 20);
        over.setTextMatrix(194.74F, 355);
        //设置标题颜色
        over.setColorFill(new BaseColor(158, 82, 10));
        over.showText("北京增值税电子普通发票");
        //设置字体和大小
        over.setFontAndSize(bf, 8);
        //设置字体颜色
        over.setColorFill(BaseColor.BLACK);
        //设置字体的输出位置
        //要输出的text
        //设置机器编码
        over.setFontAndSize(bf, 8.5F);
        over.setTextMatrix(73, 321);
        over.showText("499099440254");
        //发票代码 011001800311
        over.setTextMatrix(475, 373);
        over.showText("011001800311");
        //发票号码 10513660
        over.setTextMatrix(475, 356);
        over.showText("10513660");
        //开票日期 2018年07月31日
        over.setTextMatrix(475, 339);
        over.showText("2018 年 07 月 31 日");
        //校 验 码
        over.setTextMatrix(475, 321);
        //over.showText("13118 13934 35525 59210");
        over.showText("13118 13934 35525 88888");
        //购买方======Begin=====
        assemblerSellerInfo(over, bf);
        //购买方======End=====
        //密码区
        over.setFontAndSize(bf, 11F);
        over.setTextMatrix(405, 297);
        over.showText("7*2>445/+>7+4<6<9//941-7769");
        over.setTextMatrix(405, 284);
        over.showText("-91+>5/*+**>+9>4260651>56*/");
        over.setTextMatrix(405, 271);
        over.showText("6>41-<990259+<7+*<><-990/39");
        over.setTextMatrix(405, 259);
        over.showText("3-2<-6<<>5/*+**>+9>42604*9-");
        //设置明细======Begin=====
        assemblerDetailInfo(over, bf);
        //设置明细======End=====
        //销售方======Begin=====
        assemblerPurchaserInfo(over, bf);
        //销售方======End=====
        //备注======Begin=====
        assemblerRemark(over, bf);
        //备注======End=====
        //收款人
        over.setTextMatrix(65, 29);
        over.showText("朱莹");
        //复核
        over.setTextMatrix(215, 29);
        over.showText("何盛霞");
        //开票人
        over.setTextMatrix(345, 29);
        over.showText("沈玮俊");
        //价税合计 (大写）
        over.setTextMatrix(195, 108);
        over.showText("叁拾叁元伍角");
        //（小写）
        over.setTextMatrix(480, 108);
        over.showText("￥ 33.50");
        //合计金额
        over.setTextMatrix(425, 130);
        over.showText("￥31.60");
        //合计税额
        over.setTextMatrix(538, 130);
        over.showText("￥1.90");
    }

    public static void assemblerRemark(PdfContentByte over, BaseFont bf) {
        over.setFontAndSize(bf, 8F);
        if (remark.length() <= 28) {
            over.setTextMatrix(365, 90);
            over.showText(remark);
        } else if (remark.length() > 28 && remark.length() <= 56) {
            String subRemark = remark.substring(0, 28);
            over.setTextMatrix(365, 90);
            over.showText(subRemark);
            over.setTextMatrix(365, 80);
            String twoRemark = remark.substring(28);
            over.showText(twoRemark);
        } else if (remark.length() > 56 && remark.length() <= 84) {
            String subRemark = remark.substring(0, 28);
            over.setTextMatrix(365, 90);
            over.showText(subRemark);
            over.setTextMatrix(365, 80);
            String twoRemark = remark.substring(28, 56);
            over.showText(twoRemark);
            over.setTextMatrix(365, 70);
            String thereRemark = remark.substring(56);
            over.showText(thereRemark);
        } else if (remark.length() > 84) {
            String subRemark = remark.substring(0, 28);
            over.setTextMatrix(365, 90);
            over.showText(subRemark);
            over.setTextMatrix(365, 80);
            String twoRemark = remark.substring(28, 56);
            over.showText(twoRemark);
            over.setTextMatrix(365, 70);
            String thereRemark = remark.substring(56, 84);
            over.showText(thereRemark);
            over.setTextMatrix(365, 60);
            String otherRemark = remark.substring(84);
            over.showText(otherRemark);
        }
    }

    private static void assemblerPurchaserInfo(PdfContentByte over, BaseFont bf) {
        over.setFontAndSize(bf, 8);
        //名 称
        over.setTextMatrix(107, 92);
        //over.showText("汉堡王（北京）餐饮管理有限公司");
        over.showText("测试（北京）餐饮管理有限公司");
        //纳税人识别号
        over.setFontAndSize(bf, 8.5F);
        over.setTextMatrix(107, 77);
        //over.showText("911101136932007849");
        over.showText("888888888888888888");
        //地 址、电 话
        over.setFontAndSize(bf, 8F);
        over.setTextMatrix(107, 62);
        //over.showText("北京市顺义区顺通路25号5幢 010-85110517");
        over.showText("北京市顺义区顺通路88号8幢 010-88888888");
        //开户行及账号
        over.setTextMatrix(107, 48);
        //over.showText("北京银行东单支行 01090945000120107016730");
        over.showText("北京银行东单支行 01090945000120107088888");
    }

    public static void assemblerSellerInfo(PdfContentByte over, BaseFont bf) {
        over.setFontAndSize(bf, 8);
        //名 称
        over.setTextMatrix(110, 298.5F);
        //over.showText("北京我爱我家房地产经纪有限公司");
        if (sellerName.length() < 30) {
            over.setFontAndSize(bf, 8);
            over.showText(sellerName);
        } else if (sellerName.length() >= 30 && sellerName.length() <= 34) {
            over.setFontAndSize(bf, 7);
            over.showText(sellerName);
        } else if (sellerName.length() >= 35 && sellerName.length() <= 39) {
            over.setFontAndSize(bf, 6);
            over.showText(sellerName);
        } else if (sellerName.length() >= 40) {
            over.setFontAndSize(bf, 6);
            String subSellerName = sellerName.substring(0, 39);
            over.setTextMatrix(110, 302.5F);
            over.showText(subSellerName);
            over.setTextMatrix(110, 294.5F);
            String otherSellerName = sellerName.substring(39);
            over.showText(otherSellerName);

        } else {
            over.showText(sellerName);
        }
        //纳税人识别号
        over.setFontAndSize(bf, 8.5F);
        over.setTextMatrix(110, 283.5F);
        //over.showText("911101157001735358");
        over.showText("888888888888888888");
        //地 址、电 话
        over.setFontAndSize(bf, 8F);
        over.setTextMatrix(110, 268.5F);
        if (sellerAddress.length() < 36) {
            over.setFontAndSize(bf, 8);
            over.showText(sellerAddress);
        } else if (sellerAddress.length() >= 36 && sellerAddress.length() <= 43) {
            over.setFontAndSize(bf, 7);
            over.showText(sellerAddress);
        } else if (sellerAddress.length() > 45 && sellerAddress.length() <= 48) {
            over.setFontAndSize(bf, 6);
            over.showText(sellerAddress);
        } else if (sellerAddress.length() > 48) {
            over.setFontAndSize(bf, 6);
            String subSellerAddress = sellerAddress.substring(0, 48);
            over.setTextMatrix(110, 272.5F);
            over.showText(subSellerAddress);
            over.setTextMatrix(110, 264.5F);
            String otherSellerAddress = sellerAddress.substring(48);
            over.showText(otherSellerAddress);
        } else {
            over.showText(sellerAddress);
        }
        // over.showText("北京市顺义区顺通路25号5幢 010-85110517");
        //开户行及账号
        over.setFontAndSize(bf, 8);
        over.setTextMatrix(110, 253.5F);
        if (sellerBankAccount.length() <= 40) {
            over.setFontAndSize(bf, 8);
            over.showText(sellerBankAccount);
        } else if (sellerBankAccount.length() > 40 && sellerBankAccount.length() <= 46) {
            over.setFontAndSize(bf, 7);
            over.showText(sellerBankAccount);
        } else if (sellerBankAccount.length() > 46 && sellerBankAccount.length() <= 50) {
            over.setFontAndSize(bf, 6);
            over.showText(sellerBankAccount);
        } else if (sellerBankAccount.length() > 50) {
            over.setFontAndSize(bf, 6);
            String subSellerBankAccount = sellerBankAccount.substring(0, 50);
            over.setTextMatrix(110, 257.5F);
            over.showText(subSellerBankAccount);
            over.setTextMatrix(110, 250F);
            String otherSellerBankAccount = sellerBankAccount.substring(50);
            over.showText(otherSellerBankAccount);
        } else {
            over.showText(sellerBankAccount);
        }
    }

    public static void assemblerDetailInfo(PdfContentByte over, BaseFont bf) {
        int num = 8;
        float size = 0;
        for (int i = 1; i <= num; i++) {
            over.setFontAndSize(bf, 7);
            over.setTextMatrix(27, 225 - size);
            over.showText("货名汉字个汉字货名汉字个汉字货名汉字名汉字");
//            over.setTextMatrix(27, 217);
//            over.showText("46个汉字货名46个汉字货名46个汉字货名4");
            //over.setFontAndSize(bf, 4.8F);
            over.setTextMatrix(176, 225 - size);
            over.showText("规格型号型");
//            over.setTextMatrix(176, 220);
//            over.showText("格型号32个汉字规格");
//            over.setTextMatrix(176, 215);
//            over.showText("型号32个汉字规格型号");
            over.setFontAndSize(bf, 8);
            over.setTextMatrix(221, 225 - size);
            over.showText("单位单次");
            over.setTextMatrix(283, 225 - size);
            over.showText("1");
            over.setTextMatrix(342, 225 - size);
            over.showText("31.603774");
            over.setTextMatrix(425, 225 - size);
            over.showText("31.60");
            over.setTextMatrix(487, 225 - size);
            over.showText("6%");
            over.setTextMatrix(538, 225 - size);
            over.showText("1.90");
            size += 11;
        }
    }
}
