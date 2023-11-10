package com.xforceplus.taxware.microservice.voucher.sdk.mode;

import java.util.List;

/**
 * 电子发票信息
 *
 * @author Bobo
 * @create 2018/9/29 17:35
 * @since 1.0.0
 */
public class ElectronicInvoiceInfo {

    /**
     * 发票头
     */
    private String title;
    /**
     * 二维码内容
     */
    private String ewmContent;
    /**
     * 机器编号
     */
    private String machineCode;
    /**
     * 发票代码
     */
    private String invoiceCode;
    /**
     * 发票号码
     */
    private String invoiceNo;
    /**
     * 开票日期
     */
    private String invoiceDate;
    /**
     * 校验码
     */
    private String checkCode;

    /**
     * 发票密文
     */
    private String cipherText;

    /**
     * 购方名称
     */
    private String purchaserName;

    /**
     * 购方税号
     */
    private String purchaserTaxNo;

    /**
     * 购方地址、电话
     */
    private String purchaserAddressTel;

    /**
     * 购方银行及账号
     */
    private String purchaserBankNameAccount;

    /**
     * 销方名称
     */
    private String sellerName;

    /**
     * 销方税号
     */
    private String sellerTaxNo;

    /**
     * 销方地址、电话
     */
    private String sellerAddressTel;

    /**
     * 销方银行及账号
     */
    private String sellerBankNameAccount;

    /**
     * 备注
     */
    private String remark;

    /**
     * 收款人
     */
    private String cashierName;

    /**
     * 复核人
     */
    private String checkerName;

    /**
     * 开票人
     */
    private String invoiceName;

    /**
     * 税额
     */
    private String taxAmount;

    /**
     * 不含税金额
     */
    private String amountWithoutTax;

    /**
     * 含税金额（小写）
     */
    private String amountWithTaxLowercase;

    /**
     * 含税金额（大写）
     */
    private String amountWithTaxUppercase;

    /**
     * 模板类型：0：5行(默认)；1：8行。
     */
    private String template;

    /**
     * 区块链标识 1：区块链发票
     */
    private boolean blockChain;

    /**
     * 成品油标识
     */
    private boolean productOil;

    /**
     * 销项负数标识
     */
    private boolean outputMinus;

    /**
     * 销货清单标识
     */
    private boolean salesList;

    /**
     * 销货清单页数
     */
    private String pageNo;

    /**
     * 销货清单总页数
     */
    private String pageSize;

    private List<InvoiceDetailInfo> invoiceDetailInfoList;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getEwmContent() {
        return ewmContent;
    }

    public void setEwmContent(String ewmContent) {
        this.ewmContent = ewmContent;
    }

    public String getMachineCode() {
        return machineCode;
    }

    public void setMachineCode(String machineCode) {
        this.machineCode = machineCode;
    }

    public String getInvoiceCode() {
        return invoiceCode;
    }

    public void setInvoiceCode(String invoiceCode) {
        this.invoiceCode = invoiceCode;
    }

    public String getInvoiceNo() {
        return invoiceNo;
    }

    public void setInvoiceNo(String invoiceNo) {
        this.invoiceNo = invoiceNo;
    }

    public String getInvoiceDate() {
        return invoiceDate;
    }

    public void setInvoiceDate(String invoiceDate) {
        this.invoiceDate = invoiceDate;
    }

    public String getCheckCode() {
        return checkCode;
    }

    public void setCheckCode(String checkCode) {
        this.checkCode = checkCode;
    }

    public String getCipherText() {
        return cipherText;
    }

    public void setCipherText(String cipherText) {
        this.cipherText = cipherText;
    }

    public String getPurchaserName() {
        return purchaserName;
    }

    public void setPurchaserName(String purchaserName) {
        this.purchaserName = purchaserName;
    }

    public String getPurchaserTaxNo() {
        return purchaserTaxNo;
    }

    public void setPurchaserTaxNo(String purchaserTaxNo) {
        this.purchaserTaxNo = purchaserTaxNo;
    }

    public String getPurchaserAddressTel() {
        return purchaserAddressTel;
    }

    public void setPurchaserAddressTel(String purchaserAddressTel) {
        this.purchaserAddressTel = purchaserAddressTel;
    }

    public String getPurchaserBankNameAccount() {
        return purchaserBankNameAccount;
    }

    public void setPurchaserBankNameAccount(String purchaserBankNameAccount) {
        this.purchaserBankNameAccount = purchaserBankNameAccount;
    }

    public String getSellerName() {
        return sellerName;
    }

    public void setSellerName(String sellerName) {
        this.sellerName = sellerName;
    }

    public String getSellerTaxNo() {
        return sellerTaxNo;
    }

    public void setSellerTaxNo(String sellerTaxNo) {
        this.sellerTaxNo = sellerTaxNo;
    }

    public String getSellerAddressTel() {
        return sellerAddressTel;
    }

    public void setSellerAddressTel(String sellerAddressTel) {
        this.sellerAddressTel = sellerAddressTel;
    }

    public String getSellerBankNameAccount() {
        return sellerBankNameAccount;
    }

    public void setSellerBankNameAccount(String sellerBankNameAccount) {
        this.sellerBankNameAccount = sellerBankNameAccount;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getCashierName() {
        return cashierName;
    }

    public void setCashierName(String cashierName) {
        this.cashierName = cashierName;
    }

    public String getCheckerName() {
        return checkerName;
    }

    public void setCheckerName(String checkerName) {
        this.checkerName = checkerName;
    }

    public String getInvoiceName() {
        return invoiceName;
    }

    public void setInvoiceName(String invoiceName) {
        this.invoiceName = invoiceName;
    }

    public String getTaxAmount() {
        return taxAmount;
    }

    public void setTaxAmount(String taxAmount) {
        this.taxAmount = taxAmount;
    }

    public String getAmountWithoutTax() {
        return amountWithoutTax;
    }

    public void setAmountWithoutTax(String amountWithoutTax) {
        this.amountWithoutTax = amountWithoutTax;
    }

    public String getAmountWithTaxLowercase() {
        return amountWithTaxLowercase;
    }

    public void setAmountWithTaxLowercase(String amountWithTaxLowercase) {
        this.amountWithTaxLowercase = amountWithTaxLowercase;
    }

    public String getAmountWithTaxUppercase() {
        return amountWithTaxUppercase;
    }

    public void setAmountWithTaxUppercase(String amountWithTaxUppercase) {
        this.amountWithTaxUppercase = amountWithTaxUppercase;
    }

    public List<InvoiceDetailInfo> getInvoiceDetailInfoList() {
        return invoiceDetailInfoList;
    }

    public void setInvoiceDetailInfoList(List<InvoiceDetailInfo> invoiceDetailInfoList) {
        this.invoiceDetailInfoList = invoiceDetailInfoList;
    }

    public String getTemplate() {
        return template;
    }

    public void setTemplate(String template) {
        this.template = template;
    }

    public boolean isBlockChain() {
        return blockChain;
    }

    public void setBlockChain(boolean blockChain) {
        this.blockChain = blockChain;
    }

    public boolean isProductOil() {
        return productOil;
    }

    public void setProductOil(boolean productOil) {
        this.productOil = productOil;
    }

    public boolean isOutputMinus() {
        return outputMinus;
    }

    public void setOutputMinus(boolean outputMinus) {
        this.outputMinus = outputMinus;
    }

    public boolean isSalesList() {
        return salesList;
    }

    public void setSalesList(boolean salesList) {
        this.salesList = salesList;
    }

    public String getPageNo() {
        return pageNo;
    }

    public void setPageNo(String pageNo) {
        this.pageNo = pageNo;
    }

    public String getPageSize() {
        return pageSize;
    }

    public void setPageSize(String pageSize) {
        this.pageSize = pageSize;
    }
}
