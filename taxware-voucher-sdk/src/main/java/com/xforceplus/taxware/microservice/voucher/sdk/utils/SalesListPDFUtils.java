package com.xforceplus.taxware.microservice.voucher.sdk.utils;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;

import java.io.IOException;

/**
 * 生成销货清单工具类
 *
 * @author Bobo
 * @create 2018/9/29 11:27
 * @since 1.0.0
 */
public class SalesListPDFUtils {
    private static String SALES_LIST_TITLE = "销售货物或者提供应税劳务清单";
    private static float TABLE_WIDTH = 530;
    private static float TABLE_HEIGHT = 22;

    public static void addSalesList(Document document) throws IOException, DocumentException {
        BaseFont bfChinese = BaseFont.createFont("STSong-Light", "UniGB-UCS2-H", BaseFont.NOT_EMBEDDED);
        Font fontGolden9 = new Font(bfChinese, 9, Font.NORMAL, ElectronicInvoiceUtils.INVOICE_COLOR_GOLDEN);
        Font fontBlack9 = new Font(bfChinese, 9, Font.NORMAL, BaseColor.BLACK);
        //设置标题
        addTitle(document, bfChinese);
        //设置发票信息
        addInvoiceInfo(document, fontGolden9, fontBlack9);
        //设置明细信息
        addDetailInfo(document, fontGolden9, fontBlack9);
        //设置底部信息
        addBottom(document, fontGolden9, fontBlack9);
    }


    public static void addDetailInfo(Document document, Font fontGolden9, Font fontBlack9) throws DocumentException {
        //设置字体
        PdfPCell cell1 = new PdfPCell(new Paragraph("序号", fontGolden9));
        PdfPCell cell2 = new PdfPCell(new Paragraph("货物（劳务）名称", fontGolden9));
        PdfPCell cell3 = new PdfPCell(new Paragraph("规格型号", fontGolden9));
        PdfPCell cell4 = new PdfPCell(new Paragraph("单位", fontGolden9));
        PdfPCell cell5 = new PdfPCell(new Paragraph("数  量", fontGolden9));
        PdfPCell cell6 = new PdfPCell(new Paragraph("单  价", fontGolden9));
        PdfPCell cell7 = new PdfPCell(new Paragraph("金  额", fontGolden9));
        PdfPCell cell8 = new PdfPCell(new Paragraph("税率", fontGolden9));
        PdfPCell cell9 = new PdfPCell(new Paragraph("税  额", fontGolden9));
        //水平居中
        cell1.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell2.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell3.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell4.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell5.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell6.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell7.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell8.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell9.setHorizontalAlignment(Element.ALIGN_CENTER);
        //垂直居中
        cell1.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell2.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell3.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell4.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell5.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell6.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell7.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell8.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell9.setVerticalAlignment(Element.ALIGN_MIDDLE);
        //边框颜色
        cell1.setBorderColor(ElectronicInvoiceUtils.INVOICE_COLOR_GOLDEN);
        cell2.setBorderColor(ElectronicInvoiceUtils.INVOICE_COLOR_GOLDEN);
        cell3.setBorderColor(ElectronicInvoiceUtils.INVOICE_COLOR_GOLDEN);
        cell4.setBorderColor(ElectronicInvoiceUtils.INVOICE_COLOR_GOLDEN);
        cell5.setBorderColor(ElectronicInvoiceUtils.INVOICE_COLOR_GOLDEN);
        cell6.setBorderColor(ElectronicInvoiceUtils.INVOICE_COLOR_GOLDEN);
        cell7.setBorderColor(ElectronicInvoiceUtils.INVOICE_COLOR_GOLDEN);
        cell8.setBorderColor(ElectronicInvoiceUtils.INVOICE_COLOR_GOLDEN);
        cell9.setBorderColor(ElectronicInvoiceUtils.INVOICE_COLOR_GOLDEN);
        PdfPTable detailInfoTable = new PdfPTable(9);
        //设置每列宽度比例
        float width11[] = {0.05F, 0.36F, 0.17F, 0.09F, 0.05F, 0.07F, 0.10F, 0.05F, 0.06F};
        detailInfoTable.setWidths(width11);
        // 总宽度
        detailInfoTable.setTotalWidth(TABLE_WIDTH);
        detailInfoTable.setLockedWidth(true);

        detailInfoTable.addCell(cell1);
        detailInfoTable.addCell(cell2);
        detailInfoTable.addCell(cell3);
        detailInfoTable.addCell(cell4);
        detailInfoTable.addCell(cell5);
        detailInfoTable.addCell(cell6);
        detailInfoTable.addCell(cell7);
        detailInfoTable.addCell(cell8);
        detailInfoTable.addCell(cell9);


        for (int i = 1; i <= 28; i++) {
            Paragraph paragraph1 = new Paragraph();
            Paragraph paragraph2 = new Paragraph();
            Paragraph paragraph3 = new Paragraph();
            Paragraph paragraph4 = new Paragraph();
            Paragraph paragraph5 = new Paragraph();
            Paragraph paragraph6 = new Paragraph();
            Paragraph paragraph7 = new Paragraph();
            Paragraph paragraph8 = new Paragraph();
            Paragraph paragraph9 = new Paragraph();
            if (i == 26) {
                paragraph1.add(new Chunk("小计", fontBlack9));
                paragraph7.add(new Chunk("8888888.88", fontBlack9));
                paragraph9.add(new Chunk("8888.88", fontBlack9));
            } else if (i == 27) {
                paragraph1.add(new Chunk("总计", fontBlack9));
                paragraph7.add(new Chunk("8888888.88", fontBlack9));
                paragraph9.add(new Chunk("8888.88", fontBlack9));
            } else if (i == 28) {
                paragraph1.add(new Chunk("备注", fontBlack9));
                paragraph2.add(new Chunk(" 门店号:备注备注备注备注备注备注备注备注备注备注备注备注门店号:备注备注备注备注备注备注备注备注备注备注备注备注门店号:备注备注备注备注备注备注备注备注备注备注备注备注", fontBlack9));
            } else if (i == 1) {
                paragraph1.add(new Chunk(i + "", fontBlack9));
                paragraph2.add(new Chunk("*日用杂品*屈臣氏WATER360矿泉水活保湿旅行套装(卸妆水99毫升+水凝露99毫升", fontBlack9));
                paragraph3.add(new Chunk("屈臣氏14版中号塑料购物袋", fontBlack9));
                paragraph4.add(new Chunk("千克加顿", fontBlack9));
                paragraph5.add(new Chunk("1000", fontBlack9));
                paragraph6.add(new Chunk("8888888.88", fontBlack9));
                paragraph7.add(new Chunk("56.6", fontBlack9));
                paragraph8.add(new Chunk("16%", fontBlack9));
                paragraph9.add(new Chunk("8888.88", fontBlack9));
            } else {
                paragraph1.add(new Chunk(i + "", fontBlack9));
                paragraph2.add(new Chunk("*日用杂品*屈臣氏WATER360矿泉水活保湿旅行套装(卸妆水99毫升+水凝露99毫升", fontBlack9));
                paragraph3.add(new Chunk("屈臣氏14版中号塑料购物袋", fontBlack9));
                paragraph4.add(new Chunk("千克加顿", fontBlack9));
                paragraph5.add(new Chunk("1000", fontBlack9));
                paragraph6.add(new Chunk("8888888.88", fontBlack9));
                paragraph7.add(new Chunk("56.6", fontBlack9));
                paragraph8.add(new Chunk("免税", fontBlack9));
                paragraph9.add(new Chunk("8888.88", fontBlack9));
            }
            PdfPCell detail1 = new PdfPCell(paragraph1);
            //水平居中
            detail1.setHorizontalAlignment(Element.ALIGN_CENTER);
            PdfPCell detail2 = new PdfPCell(paragraph2);
            PdfPCell detail3 = new PdfPCell(paragraph3);
            PdfPCell detail4 = new PdfPCell(paragraph4);
            PdfPCell detail5 = new PdfPCell(paragraph5);
            PdfPCell detail6 = new PdfPCell(paragraph6);
            PdfPCell detail7 = new PdfPCell(paragraph7);
            PdfPCell detail8 = new PdfPCell(paragraph8);
            PdfPCell detail9 = new PdfPCell(paragraph9);

            if (i != 28 && i != 27) {
                //去掉底部边框
                detail1.disableBorderSide(Rectangle.BOTTOM);
                detail2.disableBorderSide(Rectangle.BOTTOM);
                detail3.disableBorderSide(Rectangle.BOTTOM);
                detail4.disableBorderSide(Rectangle.BOTTOM);
                detail5.disableBorderSide(Rectangle.BOTTOM);
                detail6.disableBorderSide(Rectangle.BOTTOM);
                detail7.disableBorderSide(Rectangle.BOTTOM);
                detail8.disableBorderSide(Rectangle.BOTTOM);
                detail9.disableBorderSide(Rectangle.BOTTOM);
            }
            //去掉顶部边框
            detail1.disableBorderSide(Rectangle.TOP);
            detail2.disableBorderSide(Rectangle.TOP);
            detail3.disableBorderSide(Rectangle.TOP);
            detail4.disableBorderSide(Rectangle.TOP);
            detail5.disableBorderSide(Rectangle.TOP);
            detail6.disableBorderSide(Rectangle.TOP);
            detail7.disableBorderSide(Rectangle.TOP);
            detail8.disableBorderSide(Rectangle.TOP);
            detail9.disableBorderSide(Rectangle.TOP);

            //边框颜色
            detail1.setBorderColor(ElectronicInvoiceUtils.INVOICE_COLOR_GOLDEN);
            detail2.setBorderColor(ElectronicInvoiceUtils.INVOICE_COLOR_GOLDEN);
            detail3.setBorderColor(ElectronicInvoiceUtils.INVOICE_COLOR_GOLDEN);
            detail4.setBorderColor(ElectronicInvoiceUtils.INVOICE_COLOR_GOLDEN);
            detail5.setBorderColor(ElectronicInvoiceUtils.INVOICE_COLOR_GOLDEN);
            detail6.setBorderColor(ElectronicInvoiceUtils.INVOICE_COLOR_GOLDEN);
            detail7.setBorderColor(ElectronicInvoiceUtils.INVOICE_COLOR_GOLDEN);
            detail8.setBorderColor(ElectronicInvoiceUtils.INVOICE_COLOR_GOLDEN);
            detail9.setBorderColor(ElectronicInvoiceUtils.INVOICE_COLOR_GOLDEN);
            //设置单元格高度
            detail1.setMinimumHeight(TABLE_HEIGHT);
            detail2.setMinimumHeight(TABLE_HEIGHT);
            detail3.setMinimumHeight(TABLE_HEIGHT);
            detail4.setMinimumHeight(TABLE_HEIGHT);
            detail5.setMinimumHeight(TABLE_HEIGHT);
            detail6.setMinimumHeight(TABLE_HEIGHT);
            detail7.setMinimumHeight(TABLE_HEIGHT);
            detail8.setMinimumHeight(TABLE_HEIGHT);
            detail9.setMinimumHeight(TABLE_HEIGHT);

            if (i == 28) {
                detailInfoTable.addCell(detail1);
                detail2.setColspan(8);
                detailInfoTable.addCell(detail2);
            } else {
                detailInfoTable.addCell(detail1);
                detailInfoTable.addCell(detail2);
                detailInfoTable.addCell(detail3);
                detailInfoTable.addCell(detail4);
                detailInfoTable.addCell(detail5);
                detailInfoTable.addCell(detail6);
                detailInfoTable.addCell(detail7);
                detailInfoTable.addCell(detail8);
                detailInfoTable.addCell(detail9);
            }

        }
        document.add(detailInfoTable);
    }

    public static void addInvoiceInfo(Document document, Font fontGolden9, Font fontBlack9) throws DocumentException {
        PdfPTable invoiceInfoTable = new PdfPTable(1);
        Paragraph tPhrase = new Paragraph();
        tPhrase.add(new Chunk("购买方名称：", fontGolden9));
        tPhrase.add(new Chunk("北京湘隆得力机械设备有限公司", fontBlack9));
        tPhrase.add(Chunk.NEWLINE);
        tPhrase.add(Chunk.NEWLINE);
        tPhrase.add(new Chunk("销售方名称：", fontGolden9));
        tPhrase.add(new Chunk("北京出入境检验检疫局动物隔离场", fontBlack9));
        tPhrase.add(Chunk.NEWLINE);
        tPhrase.add(Chunk.NEWLINE);
        PdfPCell invoiceInfoCell = new PdfPCell(tPhrase);
        invoiceInfoCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        invoiceInfoCell.setHorizontalAlignment(Element.ALIGN_LEFT);
        invoiceInfoCell.setBorder(0);
        //设置每列宽度比例
        int width11[] = {100};
        // 总宽度
        invoiceInfoTable.setTotalWidth(TABLE_WIDTH);
        invoiceInfoTable.setLockedWidth(true);
        invoiceInfoTable.setWidths(width11);
        invoiceInfoTable.getDefaultCell().setBorder(0);
        invoiceInfoTable.addCell(invoiceInfoCell);
        document.add(invoiceInfoTable);

        PdfPTable invoiceCodeTable = new PdfPTable(3);
        Paragraph tPhrase1 = new Paragraph();
        tPhrase1.add(new Chunk("所属增值税普通发票代码：", fontGolden9));
        tPhrase1.add(new Chunk("011001800311", fontBlack9));
        PdfPCell cell1 = new PdfPCell(tPhrase1);
        cell1.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell1.setHorizontalAlignment(Element.ALIGN_LEFT);
        cell1.setBorder(0);
        Paragraph tPhrase2 = new Paragraph();
        tPhrase2.add(new Chunk("号码：", fontGolden9));
        tPhrase2.add(new Chunk("06005823", fontBlack9));
        PdfPCell cell2 = new PdfPCell(tPhrase2);
        cell2.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell2.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell2.setBorder(0);
        Paragraph tPhrase3 = new Paragraph();
        tPhrase3.add(new Chunk("共", fontGolden9));
        tPhrase3.add(new Chunk(" 1 ", fontBlack9));
        tPhrase3.add(new Chunk("页 第", fontGolden9));
        tPhrase3.add(new Chunk(" 1 ", fontBlack9));
        tPhrase3.add(new Chunk("页", fontGolden9));
        PdfPCell cell3 = new PdfPCell(tPhrase3);
        cell3.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell3.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cell3.setBorder(0);

        invoiceCodeTable.addCell(cell1);
        invoiceCodeTable.addCell(cell2);
        invoiceCodeTable.addCell(cell3);
        //设置每列宽度比例
        int widths[] = {45, 30, 25};
        // 总宽度
        invoiceCodeTable.setTotalWidth(TABLE_WIDTH);
        invoiceCodeTable.setLockedWidth(true);
        invoiceCodeTable.setWidths(widths);
        document.add(invoiceCodeTable);

        //加入空行
        Paragraph blankRow1 = new Paragraph(18f, " ", fontGolden9);
        document.add(blankRow1);
    }

    public static void addTitle(Document document, BaseFont bfChinese) throws DocumentException {
        //设置字体
        Font fontGolden21 = new Font(bfChinese, 21, Font.NORMAL, ElectronicInvoiceUtils.INVOICE_COLOR_GOLDEN);
        PdfPTable titleTable = new PdfPTable(1);
        PdfPCell title = new PdfPCell(new Paragraph(SALES_LIST_TITLE, fontGolden21));
        title.setVerticalAlignment(Element.ALIGN_MIDDLE);
        title.setHorizontalAlignment(Element.ALIGN_CENTER);
        title.setBorder(0);
        //设置每列宽度比例
        int width11[] = {100};
        titleTable.setWidths(width11);
        titleTable.getDefaultCell().setBorder(0);
        titleTable.addCell(title);
        document.add(titleTable);
        //加入空行
        Paragraph blankRow1 = new Paragraph(18f, " ", fontGolden21);
        document.add(blankRow1);
    }

    public static void addBottom(Document document, Font fontGolden9, Font fontBlack9) throws DocumentException {
        //加入空行
        Paragraph blankRow1 = new Paragraph(18f, " ", fontGolden9);
        document.add(blankRow1);

        PdfPTable bottomTable = new PdfPTable(2);
        PdfPCell cell1 = new PdfPCell(new Paragraph("销售单位（章）：", fontGolden9));
        cell1.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell1.setHorizontalAlignment(Element.ALIGN_LEFT);
        cell1.setBorder(0);
        Paragraph paragraph = new Paragraph();
        paragraph.add(new Chunk("填开日期：", fontGolden9));
        paragraph.add(new Chunk("2018年9月28日", fontBlack9));
        PdfPCell cell2 = new PdfPCell(paragraph);
        cell2.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell2.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cell2.setBorder(0);
        //设置每列宽度比例
        int width11[] = {50, 50};
        bottomTable.setWidths(width11);
        // 总宽度
        bottomTable.setTotalWidth(TABLE_WIDTH);
        bottomTable.setLockedWidth(true);

        bottomTable.addCell(cell1);
        bottomTable.addCell(cell2);
        document.add(bottomTable);
    }
}
