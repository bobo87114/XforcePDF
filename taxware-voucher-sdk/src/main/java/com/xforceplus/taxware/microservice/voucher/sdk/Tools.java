package com.xforceplus.taxware.microservice.voucher.sdk;//package com.bobo.tools;
//
//
//import org.apache.commons.logging.Log;
//import org.apache.commons.logging.LogFactory;
//
//import javax.imageio.stream.FileImageInputStream;
//import javax.servlet.http.HttpServletResponse;
//import java.io.*;
//import java.math.BigDecimal;
//import java.net.URLDecoder;
//import java.security.MessageDigest;
//import java.security.NoSuchAlgorithmException;
//import java.text.DateFormat;
//import java.text.ParseException;
//import java.text.SimpleDateFormat;
//import java.util.*;
//import java.util.regex.Matcher;
//import java.util.regex.Pattern;
//
//public class Tools {
//	private static Log log = LogFactory.getLog(Tools.class);
//	public static void main(String[] args) throws Exception {
////		System.err.println(randomNumber(6));
////		String aa = getTime17();
////		Thread.currentThread().sleep(2000);
////		System.out.println(getTimeSecond(aa, getTime17()));
//		String taxNo = "91110105096066054Y";
//		System.out.println(getProvByTaxNo(taxNo));
//
////		System.out.println(Tools.getTimeDay("20171229104242" ,getTime17()));
//
////		System.out.println(parseLongDateToString("2018-03-26 17:37:00"));
//
////		System.out.println(Long.parseLong(Tools.getTimeSecond("20180403143442", Tools.getTime())));
////		System.out.println(Tools.getTimeSecond("20180403143442", Tools.getTime()));
//
////		String begin = Tools.getTime17();
////		Thread.sleep(1000);
////		String end = Tools.getTime17();
////		System.out.println(begin);
////		System.out.println(end);
////		System.out.println(Tools.getTimeMinus(begin, end));
//	}
//
//	/***
//	 * 获取36位流水号
//	 *
//	 * @return
//	 */
//
//	public static String getUUID() {
//		UUID uuid = UUID.randomUUID();
//		return uuid.toString();
//	}
//
//	/**
//	 * 验证码类型
//	 *
//	 * @param type
//	 * @return
//	 */
//	public static String yzmType(String type) {
//		String msg = "";
//		switch (type) {
//		case "00":
//			msg = "全文";
//			break;
//		case "01":
//			msg = "红色";
//			break;
//		case "02":
//			msg = "黄色";
//			break;
//		case "03":
//			msg = "蓝色";
//			break;
//
//		default:
//			msg = "pass";
//			break;
//		}
//		return msg;
//	}
//
//	/**
//	 * 自定义长度生成密码，同时包含大写字母，数字
//	 *
//	 * @param int
//	 *            (密码长度)
//	 * @return String
//	 */
//	public static String randomNumber(int length) {
//		char[] ss = new char[length];
//		int[] flag = { 0, 0 }; // 0-9
//		int i = 0;
//		while (flag[0] == 0 || flag[1] == 0 || i < length) {
//			i = i % length;
//			int f = (int) (Math.random() * 2 % 2);
//			ss[i] = (char) ('2' + Math.random() * 8);// 随机生成一个0-9之间的数字
//			flag[f] = 1;
//			i++;
//		}
//		String aa = new String(ss);
//		return aa;
//	}
//
//	/**
//	 * 自定义长度生成密码，同时包含大写字母，数字
//	 *
//	 * @param int
//	 *            (密码长度)
//	 * @return String
//	 */
//	public static String randomBigPassword(int length) {
//		char[] ss = new char[length];
//		int[] flag = { 0, 0 }; // A-Z, a-z, 0-9
//		int i = 0;
//		while (flag[0] == 0 || flag[1] == 0 || i < length) {
//			i = i % length;
//			int f = (int) (Math.random() * 2 % 2);
//			if (f == 0)
//				ss[i] = (char) ('A' + Math.random() * 26);// 随机生成一个A-Z之间的字符
//			else
//				ss[i] = (char) ('2' + Math.random() * 8);// 随机生成一个0-9之间的数字
//			flag[f] = 1;
//			i++;
//		}
//		String aa = new String(ss);
//		aa = aa.replace("O", "X");
//		aa = aa.replace("o", "x");
//		aa = aa.replace("l", "a");
//		aa = aa.replace("i", "m");
//		aa = aa.replace("Z", "S");
//		aa = aa.replace("z", "s");
//		return aa;
//	}
//
//	/**
//	 * 自定义长度生成密码，前n大写字母，后m位数字
//	 *
//	 * @param int
//	 *            (密码长度)
//	 * @return String
//	 */
//	public static String randomBigPassword(int n, int m) {
//		String aa = "";
//		char[] ss = new char[n];
//		char[] pp = new char[m];
//		int[] flag = { 0, 0 }; // A-Z, a-z, 0-9
//		int i = 0;
//		while (i < n) {
//			i = i % n;
//			ss[i] = (char) ('A' + Math.random() * 26);// 随机生成一个A-Z之间的字符
//			i++;
//		}
//		int j = 0;
//		while (j < m) {
//			j = j % m;
//			pp[j] = (char) ('2' + Math.random() * 8);// 随机生成一个0-9之间的数字
//			j++;
//		}
//		aa = new String(ss) + new String(pp);
//		aa = aa.replace("O", "X");
//		aa = aa.replace("l", "A");
//		aa = aa.replace("Z", "S");
//		return aa;
//	}
//
//	/**
//	 * 自定义长度生成密码，同时包含大写字母，小写字母，数字
//	 *
//	 * @param int
//	 *            (密码长度)
//	 * @return String
//	 */
//	public static String randomPassword(int length) {
//		char[] ss = new char[length];
//		int[] flag = { 0, 0, 0 }; // A-Z, a-z, 0-9
//		int i = 0;
//		while (flag[0] == 0 || flag[1] == 0 || flag[2] == 0 || i < length) {
//			i = i % length;
//			int f = (int) (Math.random() * 3 % 3);
//			if (f == 0)
//				ss[i] = (char) ('A' + Math.random() * 26);// 随机生成一个A-Z之间的字符
//			else if (f == 1)
//				ss[i] = (char) ('a' + Math.random() * 26);// 随机生成一个a-z之间的字符
//			else
//				ss[i] = (char) ('2' + Math.random() * 8);// 随机生成一个0-9之间的数字
//			flag[f] = 1;
//			i++;
//		}
//		String aa = new String(ss);
//		aa = aa.replace("O", "X");
//		aa = aa.replace("o", "x");
//		aa = aa.replace("l", "a");
//		aa = aa.replace("i", "m");
//		aa = aa.replace("Z", "S");
//		aa = aa.replace("z", "s");
//		return aa;
//	}
//
//	/**
//	 * 将获得的密码MD5加密
//	 *
//	 * @param password
//	 * @return
//	 * @throws NoSuchAlgorithmException
//	 */
//	public static String getPassword(String password) throws NoSuchAlgorithmException {
//		MessageDigest md = MessageDigest.getInstance("MD5");
//		byte[] psd = md.digest(password.getBytes());
//		// 将得到的字节数组变成字符串返回
//		password = byteArrayToHexString(psd);
//		return password;
//	}
//
//	/**
//	 * 转换字节数组为十六进制字符串
//	 *
//	 * @param 字节数组
//	 * @return 十六进制字符串
//	 */
//	private static String byteArrayToHexString(byte[] b) {
//		StringBuffer resultSb = new StringBuffer();
//		for (int i = 0; i < b.length; i++) {
//			resultSb.append(byteToHexString(b[i]));
//		}
//		return resultSb.toString();
//	}
//
//	// 十六进制下数字到字符的映射数组
//	private final static String[] hexDigits = { "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "a", "b", "c", "d",
//			"e", "f" };
//
//	/** 将一个字节转化成十六进制形式的字符串 */
//	private static String byteToHexString(byte b) {
//		int n = b;
//		if (n < 0)
//			n = 256 + n;
//		int d1 = n / 16;
//		int d2 = n % 16;
//		return hexDigits[d1] + hexDigits[d2];
//	}
//
//	/**
//	 * 验证字符串是否为空
//	 *
//	 * @param str
//	 * @return
//	 */
//	public static boolean isEmpty(String str) {
//		if (str == null)
//			return true;
//		if (str.trim().length() == 0)
//			return true;
//		if (str.trim().equalsIgnoreCase("null"))
//			return true;
//		return false;
//	}
//
//	/**
//	 * 功能 根据传入的表名、字段名称、值和条件，判断表中是否存在数据
//	 *
//	 * @param tableName
//	 *            表名 TPGY02
//	 * @param fieldName
//	 *            字段名 INV_PHYSIC
//	 * @param fieldVal
//	 *            字段值 BGSA-01
//	 * @param condition
//	 *            条件
//	 * @return boolean 返回值
//	 */
//	public static boolean isEmpty(String tableName, String fieldName, String fieldVal, String condition) {
//		NamedSqlDao namedSqlDao = (NamedSqlDao) SpringApplicationContext.getBean("namedSqlDao");
//		StringBuffer str = new StringBuffer("SELECT * FROM ");
//		str.append(tableName);
//		str.append(" WHERE ");
//		str.append(fieldName);
//		str.append(" = '");
//		str.append(fieldVal);
//		str.append("'");
//		if (condition != null && !condition.equals("")) {
//			str.append(" AND ");
//			str.append(condition);
//		}
//		List list = namedSqlDao.queryForList(str.toString());
//		return list.size() > 0;
//	}
//
//	/**
//	 * 各种字符的unicode编码的范围： 汉字：[0x4e00,0x9fa5]（或十进制[19968,40869]）
//	 * 数字：[0x30,0x39]（或十进制[48, 57]） 小写字母：[0x61,0x7a]（或十进制[97, 122]）
//	 * 大写字母：[0x41,0x5a]（或十进制[65, 90]） 判断只能是字母或数字
//	 */
//	public static boolean isLetterDigit(String str) {
//		String regex = "^[a-z0-9A-Z0x30-0x39]+$";
//		return str.matches(regex);
//	}
//
//	/***
//	 * 获取当前时间 格式：20100101120000
//	 *
//	 * @return
//	 */
//	public static String getTime() {
//		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
//		Date now = new Date();
//		String nowString = sdf.format(now);
//		return nowString;
//	}
//
//	/***
//	 * 获取当前日期 	格式：20100101
//	 * @return
//	 */
//
//	public static String getDay(){
//		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
//		Date now = new Date();
//		String nowString = sdf.format(now);
//		return nowString;
//	}
//
//	/***
//	 * 获取当前时间 格式：20150101120101000
//	 *
//	 * @return
//	 */
//	public static String getTime17() {
//		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
//		Date now = new Date();
//		String nowString = sdf.format(now);
//		return nowString;
//	}
//
//	/**
//	 * 将List[Map{}]的结构转换为 EiBlockMeta
//	 *
//	 * 用途: 通过dao.query查询后，为结果集生成Meta
//	 *
//	 * @return the ei block meta
//	 */
//	public static EiBlockMeta MapList2BlockMeta(List<Map> list) {
//		EiBlockMeta eiMetadata = new EiBlockMeta();
//
//		if (list.size() > 0) {
//			Object[] names = list.get(0).keySet().toArray();
//
//			EiColumn eiColumn = null;
//
//			for (int i = 0; i < names.length; i++) {
//				eiColumn = new EiColumn(names[i] + "");
//				eiMetadata.addMeta(eiColumn);
//			}
//		}
//
//		return eiMetadata;
//	}
//
//	/**
//	 * 去除字符串中的空格、回车、换行符、制表符
//	 *
//	 * @param str
//	 * @return
//	 */
//	public static String replaceBlank(String str) {
//		String dest = "";
//		if (str != null) {
//			Pattern p = Pattern.compile("\\s*|\t|\r|\n");
//			Matcher m = p.matcher(str);
//			dest = m.replaceAll("");
//		}
//		return dest;
//	}
//
//	/**
//	 * 将半角括号转成圆角括号
//	 *
//	 * @param str
//	 * @return
//	 */
//	public static String converToHslf(String str) {
//		String strq = (str.trim()).replaceAll("\\(", "（").replaceAll("\\)", "）");
//		return strq;
//	}
//
//	/**
//	 * 判断字符串中是否包含特殊字符 包含返回true
//	 *
//	 * @param s
//	 * @return
//	 */
//	public static boolean containSpecial(String s) {
//		boolean flag = false;
//		if (!(s.matches("[A-Z0-9]*"))) {
//			flag = true;
//		}
//		// if(!(s.replaceAll("[a-z]*[A-Z]*\\d*-*_*\\s*", "").length()==0)){
//		// flag=true;
//		// }
//		return flag;
//	}
//
//	/**
//	 * 是否包含中文
//	 *
//	 * @param companyNameKey
//	 * @return
//	 */
//	public static boolean hasChinese(String companyNameKey) {
//		boolean flag = false;
//		String regEx = "[\u4e00-\u9fa5]";
//		Pattern pat = Pattern.compile(regEx);
//		Matcher matcher = pat.matcher(companyNameKey);
//		if (matcher.find()) {
//			flag = true;
//		}
//		return flag;
//	}
//
//	/***
//	 * 日期 格式转换： 传入：2010-01-01 12:00:00 转出：20100101
//	 *
//	 * @param date
//	 * @return
//	 */
//	public static String parseLongDateToDay(String date) {
//		SimpleDateFormat smf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//		SimpleDateFormat smf1 = new SimpleDateFormat("yyyyMMdd");
//		Date date1 = new Date();
//		try {
//			date1 = smf.parse(date);
//		} catch (ParseException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		return smf1.format(date1);
//	}
//
//	/***
//	 * 日期 格式转换： 传入：2010-01-01 12:00:00 转出：20100101
//	 *
//	 * @param date
//	 * @return
//	 */
//	public static String parseLongDateToString(String date) {
//		SimpleDateFormat smf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//		SimpleDateFormat smf1 = new SimpleDateFormat("yyyyMMddHHmmss");
//		Date date1 = new Date();
//		String dataStr = date;
//		try {
//			date1 = smf.parse(date);
//			dataStr = smf1.format(date1);
//		} catch (ParseException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		return dataStr;
//	}
//
//	/**
//	 * 获取登陆用户账号，未取到默认为：系统
//	 *
//	 * @return
//	 */
//	public static String getLoginUserId() {
//		String userId = UserSession.getUserId() + "";
//		if (isEmpty(userId)) {
//			userId = "系统";
//		}
//		return userId;
//	}
//
//	/**
//	 * 字符串补全位数
//	 *
//	 * @param str
//	 * @param length
//	 * @return
//	 */
//	public static String fill(String str, int length) {
//		String backStr = str;
//		while (backStr.length() < length) {
//			backStr = "0" + backStr;
//		}
//		return backStr;
//	}
//
//	/**
//	 * 补全2位字符串
//	 *
//	 * @param str
//	 * @return
//	 */
//	public static String fill2(int i) {
//		return fill(i + "", 2);
//	}
//
//	/**
//	 * 补全3位字符串
//	 *
//	 * @param str
//	 * @return
//	 */
//	public static String fill3(int i) {
//		return fill(i + "", 3);
//	}
//
//	/**
//	 * 补全4位字符串
//	 *
//	 * @param str
//	 * @return
//	 */
//	public static String fill4(int i) {
//		return fill(i + "", 4);
//	}
//
//	/**
//	 * 补全5位字符串
//	 *
//	 * @param str
//	 * @return
//	 */
//	public static String fill5(int i) {
//		return fill(i + "", 5);
//	}
//
//	/**
//	 * 补N位0
//	 *
//	 * @param str
//	 * @return
//	 */
//	public static String fillN(int n) {
//		return fill("0", n);
//	}
//
//	/**
//	 * 日期转换
//	 *
//	 * @param str
//	 * @return
//	 */
//	public static String DateParse(String str) {// 将”01/01/2015“类型的字符串转成”2015/01/01“
//		SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
//		Date date = null;
//		try {
//			date = sdf.parse(str);
//		} catch (ParseException e) {
//			e.printStackTrace();
//		}
//		sdf = new SimpleDateFormat("yyyy/MM/dd");
//		str = sdf.format(date);
//		return str;
//	}
//
//	public static String dateParseToyyyyMMdd(Date date) {
//		if(date == null){
//			return "";
//		}
//		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
//		return sdf.format(date);
//	}
//
//	/**
//	 * 日期转换
//	 *
//	 * @param str
//	 * @return
//	 */
//	public static String DateParseSplit(String str) {// 将”20150101“类型的字符串转成”2015-01-01“
//		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
//		Date date = null;
//		try {
//			date = sdf.parse(str);
//		} catch (ParseException e) {
//			e.printStackTrace();
//		}
//		sdf = new SimpleDateFormat("yyyy-MM-dd");
//		str = sdf.format(date);
//		return str;
//	}
//
//	/**
//	 * 获取时间差
//	 *
//	 * @return
//	 */
//	public static Long getTimeMinus(String str1, String str2) {
//		DateFormat df = null;
//		if (str1.length() == 17) {
//			df = new SimpleDateFormat("yyyyMMddHHmmssSSS");
//		} else {
//			df = new SimpleDateFormat("yyyyMMddHHmmss");
//		}
//		Date date1 = null;
//		Date date2 = null;
//		try {
//			date1 = df.parse(str1);
//			date2 = df.parse(str2);
//		} catch (ParseException e1) {
//			return 0L;
//		}
//		try {
//			long d = (date1.getTime() > date2.getTime()) ? (date1.getTime() - date2.getTime())
//					: (date2.getTime() - date1.getTime());
//			return d;
//		} catch (Exception e) {
//		}
//		return 0L;
//	}
//
//	/**
//	 * 获取时间差:毫秒
//	 *
//	 * @return
//	 */
//	public static String getTimeMillisecond(String str1, String str2) {
//		DateFormat df1 = null;
//		DateFormat df2 = null;
//		if (str1.length() == 17) {
//			df1 = new SimpleDateFormat("yyyyMMddHHmmssSSS");
//		} else {
//			df1 = new SimpleDateFormat("yyyyMMddHHmmss");
//		}
//
//		if (str2.length() == 17) {
//			df2 = new SimpleDateFormat("yyyyMMddHHmmssSSS");
//		} else {
//			df2 = new SimpleDateFormat("yyyyMMddHHmmss");
//		}
//		Date date1 = null;
//		Date date2 = null;
//		try {
//			date1 = df1.parse(str1);
//			date2 = df2.parse(str2);
//		} catch (ParseException e1) {
//			return " ";
//		}
//		try {
//			long d = (date1.getTime() > date2.getTime()) ? (date1.getTime() - date2.getTime())
//					: (date2.getTime() - date1.getTime());
//			long dd = d / 1000;// 转换成秒
//			return (dd+"");
//		} catch (Exception e) {
//			e.printStackTrace();
//			return " ";
//		}
//	}
//
//	/**
//	 * 获取时间差
//	 *
//	 * @return
//	 */
//	public static String getTimeSecond(String str1, String str2) {
//		DateFormat df1 = null;
//		DateFormat df2 = null;
//		if (str1.length() == 17) {
//			df1 = new SimpleDateFormat("yyyyMMddHHmmssSSS");
//		} else {
//			df1 = new SimpleDateFormat("yyyyMMddHHmmss");
//		}
//
//		if (str2.length() == 17) {
//			df2 = new SimpleDateFormat("yyyyMMddHHmmssSSS");
//		} else {
//			df2 = new SimpleDateFormat("yyyyMMddHHmmss");
//		}
//		Date date1 = null;
//		Date date2 = null;
//		try {
//			date1 = df1.parse(str1);
//			date2 = df2.parse(str2);
//		} catch (ParseException e1) {
//			return " ";
//		}
//		try {
//			long d = (date1.getTime() > date2.getTime()) ? (date1.getTime() - date2.getTime())
//					: (date2.getTime() - date1.getTime());
//			long dd = d / 1000;// 转换成秒
//			return (dd+"");
//		} catch (Exception e) {
//			e.printStackTrace();
//			return " ";
//		}
//	}
//
//	/**
//	 * 获取时间差
//	 *
//	 * @return
//	 */
//	public static String getTimeDay(String str1, String str2) {
//		DateFormat df = null;
//		DateFormat df2 = null;
//		if (str1.length() == 17) {
//			df = new SimpleDateFormat("yyyyMMddHHmmssSSS");
//		} else {
//			df = new SimpleDateFormat("yyyyMMddHHmmss");
//		}
//
//		if (str2.length() == 17) {
//			df2 = new SimpleDateFormat("yyyyMMddHHmmssSSS");
//		} else {
//			df2 = new SimpleDateFormat("yyyyMMddHHmmss");
//		}
//		Date date1 = null;
//		Date date2 = null;
//		try {
//			date1 = df.parse(str1);
//			date2 = df2.parse(str2);
//		} catch (ParseException e1) {
//			return " ";
//		}
//		try {
//			long d =  (date1.getTime() - date2.getTime());
//			long dd = d / 1000 / 60 / 60 / 24;// 转换天
//			return (dd+"");
//		} catch (Exception e) {
//			e.printStackTrace();
//			return " ";
//		}
//	}
//
//	/**
//	 * response输入到页面
//	 * @param response
//	 * @param rtnStr
//	 */
//	public static void out(HttpServletResponse response, String responeResult) {
//        System.out.println("客户收到的xml：" + responeResult);
//        response.setContentType("text/plain;charset=gbk");
//
//        try {
//            response.getWriter().write(responeResult);
//        } catch (IOException var3) {
//        	System.out.println("输出流返回客户端失败:"+getStrForLen(var3.getMessage(), 50));
//        }
//
//    }
//
//	/**
//	 * 获取固定长度的字符串
//	 * @param response
//	 * @param rtnStr
//	 */
//	public static String getStrForLen(String str, int len) {
//		str = str+"";
//		if(str.length() >= len) {
//			str = str.substring(0, len-1);
//		}
//		return str;
//	}
//
//	public static String parse(String str) {
//        return str.replaceAll("&", "&amp;").replaceAll(">", "&gt;").replaceAll("<", "&lt;").replaceAll("\"", "&quot;").replaceAll("\'", "apos;");
//    }
//
//	// 含中文字符串的真实长度
//	public static int  realStringLengthGBK(String a) {
//		a = a+"";
//		int num = 0;
//		String b = "";
//		try {
//			b = new String(a.getBytes("GBK"), "ISO8859_1");
//			num = b.length();
//		}
//		catch (Exception ex) {
//			num = a.length();
//		}
//		 return num;
//	}
//
//	// 含中文字符串的真实长度
//	public static int  realStringLengthUTF8(String a) {
//		a = a+"";
//		int num = 0;
//		String b = "";
//		try {
//			b = new String(a.getBytes("UTF-8"), "ISO8859_1");
//			num = b.length();
//		}
//		catch (Exception ex) {
//			num = a.length();
//		}
//		 return num;
//	}
//
//	public static String getProvByTaxNo(String taxNo) {
//
//		List<Map> dt = new ArrayList<Map>();
//	    Map dr = new HashMap();
//	    dr = new HashMap(); dr.put("name", "大连"); dr.put("value","https://fpdk.dlntax.gov.cn"); dr.put("prefix","2102"); dt.add(dr);
//	    dr = new HashMap(); dr.put("name", "宁波"); dr.put("value","https://fpdk.nb-n-tax.gov.cn"); dr.put("prefix","3302"); dt.add(dr);
//	    dr = new HashMap(); dr.put("name", "青岛"); dr.put("value","https://fpdk.qd-n-tax.gov.cn"); dr.put("prefix","3702"); dt.add(dr);
//	    dr = new HashMap(); dr.put("name", "深圳"); dr.put("value","https://fpdk.szgs.gov.cn"); dr.put("prefix","4403"); dt.add(dr);
//	    dr = new HashMap(); dr.put("name", "厦门"); dr.put("value","https://fpdk.xm-n-tax.gov.cn"); dr.put("prefix","3502"); dt.add(dr);
//	    dr = new HashMap(); dr.put("name", "安徽"); dr.put("value","https://fpdk.ah-n-tax.gov.cn"); dr.put("prefix","34"); dt.add(dr);
//	    dr = new HashMap(); dr.put("name", "北京"); dr.put("value","https://fpdk.bjsat.gov.cn"); dr.put("prefix","11"); dt.add(dr);
//	    dr = new HashMap(); dr.put("name", "福建"); dr.put("value","https://fpdk.fj-n-tax.gov.cn"); dr.put("prefix","35"); dt.add(dr);
//	    dr = new HashMap(); dr.put("name", "甘肃"); dr.put("value","https://fpdk.gs-n-tax.gov.cn"); dr.put("prefix","62"); dt.add(dr);
//	    dr = new HashMap(); dr.put("name", "广东"); dr.put("value","https://fpdk.gd-n-tax.gov.cn"); dr.put("prefix","44"); dt.add(dr);
//	    dr = new HashMap(); dr.put("name", "广西"); dr.put("value","https://fpdk.gxgs.gov.cn"); dr.put("prefix","45"); dt.add(dr);
//	    dr = new HashMap(); dr.put("name", "贵州"); dr.put("value","https://fpdk.gz-n-tax.gov.cn"); dr.put("prefix","52"); dt.add(dr);
//	    dr = new HashMap(); dr.put("name", "海南"); dr.put("value","https://fpdk.hitax.gov.cn"); dr.put("prefix","46"); dt.add(dr);
//	    dr = new HashMap(); dr.put("name", "河北"); dr.put("value","https://fpdk.he-n-tax.gov.cn:81"); dr.put("prefix","13"); dt.add(dr);
//	    dr = new HashMap(); dr.put("name", "河南"); dr.put("value","https://fpdk.ha-n-tax.gov.cn"); dr.put("prefix","41"); dt.add(dr);
//	    dr = new HashMap(); dr.put("name", "黑龙江"); dr.put("value","https://fpdk.hl-n-tax.gov.cn"); dr.put("prefix","23"); dt.add(dr);
//	    dr = new HashMap(); dr.put("name", "湖北"); dr.put("value","https://fpdk.hb-n-tax.gov.cn"); dr.put("prefix","42"); dt.add(dr);
//	    dr = new HashMap(); dr.put("name", "湖南"); dr.put("value","https://fpdk.hntax.gov.cn"); dr.put("prefix","43"); dt.add(dr);
//	    dr = new HashMap(); dr.put("name", "吉林"); dr.put("value","https://fpdk.jl-n-tax.gov.cn:4431"); dr.put("prefix","22"); dt.add(dr);
//	    dr = new HashMap(); dr.put("name", "江苏"); dr.put("value","https://fpdk.jsgs.gov.cn:81"); dr.put("prefix","32"); dt.add(dr);
//	    dr = new HashMap(); dr.put("name", "江西"); dr.put("value","https://fpdk.jxgs.gov.cn"); dr.put("prefix","36"); dt.add(dr);
//	    dr = new HashMap(); dr.put("name", "辽宁"); dr.put("value","https://fpdk.tax.ln.cn"); dr.put("prefix","21"); dt.add(dr);
//	    dr = new HashMap(); dr.put("name", "内蒙古"); dr.put("value","https://fpdk.nm-n-tax.gov.cn"); dr.put("prefix","15"); dt.add(dr);
//	    dr = new HashMap(); dr.put("name", "宁夏"); dr.put("value","https://fpdk.nxgs.gov.cn"); dr.put("prefix","64"); dt.add(dr);
//	    dr = new HashMap(); dr.put("name", "青海"); dr.put("value","http://fpdk.qh-n-tax.gov.cn"); dr.put("prefix","63"); dt.add(dr);
//	    dr = new HashMap(); dr.put("name", "山东"); dr.put("value","https://fpdk.sd-n-tax.gov.cn"); dr.put("prefix","37"); dt.add(dr);
//	    dr = new HashMap(); dr.put("name", "山西"); dr.put("value","https://fpdk.tax.sx.cn"); dr.put("prefix","14"); dt.add(dr);
//	    dr = new HashMap(); dr.put("name", "陕西"); dr.put("value","https://fprzweb.sn-n-tax.gov.cn"); dr.put("prefix","61"); dt.add(dr);
//	    dr = new HashMap(); dr.put("name", "上海"); dr.put("value","https://fpdk.tax.sh.gov.cn"); dr.put("prefix","31"); dt.add(dr);
//	    dr = new HashMap(); dr.put("name", "四川"); dr.put("value","https://fpdk.sc-n-tax.gov.cn"); dr.put("prefix","51"); dt.add(dr);
//	    dr = new HashMap(); dr.put("name", "天津"); dr.put("value","https://fpdk.tjsat.gov.cn"); dr.put("prefix","12"); dt.add(dr);
//	    dr = new HashMap(); dr.put("name", "西藏"); dr.put("value","https://fpdk.xztax.gov.cn"); dr.put("prefix","54"); dt.add(dr);
//	    dr = new HashMap(); dr.put("name", "新疆"); dr.put("value","https://fpdk.xj-n-tax.gov.cn"); dr.put("prefix","65"); dt.add(dr);
//	    dr = new HashMap(); dr.put("name", "云南"); dr.put("value","https://fpdk.yngs.gov.cn"); dr.put("prefix","53"); dt.add(dr);
//	    dr = new HashMap(); dr.put("name", "浙江"); dr.put("value","https://fpdk.zjtax.gov.cn"); dr.put("prefix","33"); dt.add(dr);
//	    dr = new HashMap(); dr.put("name", "重庆"); dr.put("value","https://fpdk.cqsw.gov.cn"); dr.put("prefix","50"); dt.add(dr);
//
//        int prefixLen = 4;
//        String comPrefix = "";
//        if (taxNo.length() == 15)
//        {
//            comPrefix = taxNo.substring(0, prefixLen);
//        }
//        else if (taxNo.length() == 18)
//        {
//            comPrefix = taxNo.substring(2, prefixLen+2);
//        }
//        for (int i = 0; i < dt.size(); i++)
//        {
//            Map row = dt.get(i);
//            String prefix = row.get("prefix")+ "";
//            if (!Tools.isEmpty(prefix) && comPrefix.startsWith(prefix))
//            {
//                return row.get("name") + "";
//            }
//        }
//        return "";
//	}
//
//	/**
//	 * 根据省份名称转换标准名称
//	 * @param provinceCode
//	 * @return
//	 */
//	public static String getProv(String provName){
//		if(provName.indexOf("安徽")!=-1){provName="安徽";}
//		if(provName.indexOf("北京")!=-1){provName="北京";}
//		if(provName.indexOf("大连")!=-1){provName="大连";}
//		if(provName.indexOf("福建")!=-1){provName="福建";}
//		if(provName.indexOf("甘肃")!=-1){provName="甘肃";}
//		if(provName.indexOf("广东")!=-1){provName="广东";}
//		if(provName.indexOf("广西")!=-1){provName="广西";}
//		if(provName.indexOf("贵州")!=-1){provName="贵州";}
//		if(provName.indexOf("海南")!=-1){provName="海南";}
//		if(provName.indexOf("河北")!=-1){provName="河北";}
//		if(provName.indexOf("河南")!=-1){provName="河南";}
//		if(provName.indexOf("黑龙江")!=-1){provName="黑龙江";}
//		if(provName.indexOf("湖北")!=-1){provName="湖北";}
//		if(provName.indexOf("湖南")!=-1){provName="湖南";}
//		if(provName.indexOf("吉林")!=-1){provName="吉林";}
//		if(provName.indexOf("江苏")!=-1){provName="江苏";}
//		if(provName.indexOf("江西")!=-1){provName="江西";}
//		if(provName.indexOf("辽宁")!=-1){provName="辽宁";}
//		if(provName.indexOf("内蒙古")!=-1){provName="内蒙古";}
//		if(provName.indexOf("宁波")!=-1){provName="宁波";}
//		if(provName.indexOf("宁夏")!=-1){provName="宁夏";}
//		if(provName.indexOf("青岛")!=-1){provName="青岛";}
//		if(provName.indexOf("青海")!=-1){provName="青海";}
//		if(provName.indexOf("山东")!=-1){provName="山东";}
//		if(provName.indexOf("山西")!=-1){provName="山西";}
//		if(provName.indexOf("陕西")!=-1){provName="陕西";}
//		if(provName.indexOf("上海")!=-1){provName="上海";}
//		if(provName.indexOf("深圳")!=-1){provName="深圳";}
//		if(provName.indexOf("四川")!=-1){provName="四川";}
//		if(provName.indexOf("天津")!=-1){provName="天津";}
//		if(provName.indexOf("西藏")!=-1){provName="西藏";}
//		if(provName.indexOf("厦门")!=-1){provName="厦门";}
//		if(provName.indexOf("新疆")!=-1){provName="新疆";}
//		if(provName.indexOf("云南")!=-1){provName="云南";}
//		if(provName.indexOf("浙江")!=-1){provName="浙江";}
//		if(provName.indexOf("重庆")!=-1){provName="重庆";}
//
//		return provName;
//	}
//
//	public static String decodeRevort(String str) throws UnsupportedEncodingException {
//		return URLDecoder.decode((String) str, "UTF-8");
//	}
//
//	/**
//	 * 根据传入参数平均拆分字符串
//	 * @param content
//	 * @param num
//	 * @return
//	 */
//	public static ArrayList splitByParam(String content, int num){
//		ArrayList list=new ArrayList();
//		if (Tools.isEmpty(content)||num<=0) {
//			return list;
//		}
//		int length=content.length();
//		int size=length/num;
//		for (int i = 0; i < num; i++) {
//			int beginIndex=size*i;
//			int endIndex=size*(i+1);
//			String item=(i==num-1)?(content.substring(beginIndex)):(content.substring(beginIndex,endIndex));
//			System.out.println(item);
//			item=item.replaceAll(">", "&gt");
//			item=item.replaceAll("<", "&lt");
//			list.add(item);
//		}
//		return list;
//	}
//
//	/**
//	 * 转换类型代码
//	 * @param fplx
//	 * @return
//	 */
//	public static String getFplxdm(String fplx) {
//		String fplxdm = "026";
//		switch (fplx.toLowerCase()) {
//		case "ec":
//			fplxdm = "026";
//			break;
//		case "c":
//			fplxdm = "007";
//			break;
//		case "s":
//			fplxdm = "004";
//			break;
//		default:
//			break;
//		}
//		return fplxdm;
//	}
//
//	/**
//	 * 转换类型代码 航信的类型代码
//	 * 发票类型	0专用发票，2普通发票，12机动车票，41卷式发票
//	 * @param fplx
//	 * @return
//	 */
//	public static String getFplxdmHx(String fplx) {
//		String fplxdm = "2";
//		switch (fplx.toLowerCase()) {
//		case "c":
//			fplxdm = "2";
//			break;
//		case "s":
//			fplxdm = "0";
//			break;
//		default:
//			break;
//		}
//		return fplxdm;
//	}
//
//	/**
//	 * 转换类型代码 百望To航信
//	 * @param fplx
//	 * @return
//	 */
//	public static String getFplxdmBwToHx(String fplx) {
//		String fplxdm = "2";
//		switch (fplx.toLowerCase()) {
//		case "007":
//			fplxdm = "2";
//			break;
//		case "004":
//			fplxdm = "0";
//			break;
//		default:
//			break;
//		}
//		return fplxdm;
//	}
//	//图片到byte数组
//	public static byte[] file2byte(String path) {
//		byte[] data = null;
//		FileImageInputStream input = null;
//		try {
//			input = new FileImageInputStream(new File(path));
//			ByteArrayOutputStream output = new ByteArrayOutputStream();
//			byte[] buf = new byte[1024];
//			int numBytesRead = 0;
//			while ((numBytesRead = input.read(buf)) != -1) {
//				output.write(buf, 0, numBytesRead);
//			}
//			data = output.toByteArray();
//			output.close();
//			input.close();
//		} catch (FileNotFoundException ex1) {
//			ex1.printStackTrace();
//		} catch (IOException ex1) {
//			ex1.printStackTrace();
//		}
//		return data;
//	}
//
//	/**
//     * 保留两位小数	格式化金额
//     * @param amount
//     * @return
//     */
//    public static BigDecimal formatAmount(BigDecimal amount, int n){
//        if (null == amount){
//            return new BigDecimal("0");
//        }
//        return amount.setScale(n, BigDecimal.ROUND_HALF_UP);
//    }
//
//    /**
//     * 生成二维码数据
//     * @param map
//     * @return
//     */
//    public static String createEwm(Map map) {
//        StringBuffer ewm = new StringBuffer();
//        String qklbs = map.get("qklbs")+"";
//        ewm.append("01").append(",");
//        String fplx = null;
//        if(map.get("fplxdm") != null && !"".equals(map.get("fplxdm"))) {
//            if(map.get("fplxdm").equals("004")) {
//                fplx = "01";
//            } else if(map.get("fplxdm").equals("009")) {
//                fplx = "02";
//            } else if(map.get("fplxdm").equals("005")) {
//                fplx = "03";
//            } else if(map.get("fplxdm").equals("007")) {
//                fplx = "04";
//            } else if(map.get("fplxdm").equals("026")) {
//                fplx = "10";
//            } else if(map.get("fplxdm").equals("025")) {
//                fplx = "11";
//            }
//        }
//		if(fplx != null) {
//			ewm.append(fplx).append(",");
//		}
//
//		ewm.append(map.get("fpdm")).append(",");
//		ewm.append(map.get("fphm")).append(",");
//		String src = "";
//		if("1".equals(qklbs)) {//区块链标识
//			ewm.append(map.get("xhdwdm")).append(",");
//			ewm.append(map.get("hjje")).append(",");
//			if(map.get("skm") != null && !"".equals(map.get("skm"))) {
//				ewm.append(((String)map.get("kprq")).substring(0, 8)).append(",");
//				ewm.append(map.get("skm"));
//			} else {
//				ewm.append(((String)map.get("kprq")).substring(0, 8));
//			}
//			src = ewm.toString();
//		}else{
//			ewm.append(map.get("hjje")).append(",");
//			if(map.get("jym") != null && !"".equals(map.get("jym"))) {
//				ewm.append(((String)map.get("kprq")).substring(0, 8)).append(",");
//				ewm.append(map.get("jym"));
//			} else {
//				ewm.append(((String)map.get("kprq")).substring(0, 8));
//			}
//
//			src = ewm.toString();
//			if(src.startsWith("01,01")) {
//				src = src + ",";
//			}
//			try {
//				String aa= CRCUtil.getCrc(src);
//				src = src + "," + aa;
//			} catch (Exception var3) {}
//		}
//
//        log.debug("二维码生成的数据为：" + src);
//        return src;
//    }
//
//	/**
//	 *  生成区块链二维码数据
//	 * @param ewmContentInfo
//	 * @return
//	 */
//	public static String createBlockChainEwm(EwmContentInfo ewmContentInfo){
//		// 内容：电子税务局查验域名+发票哈希值+发票号码+价税合计金额（单位是分，如1元用100表示）
//		// 示例：https://bcfp.shenzhen.chinatax.gov.cn/verify/scan?hash=00d6e8118a7973aadf0640525118c126cde5217623aac73a0740eae30d539efa37&bill_num=00420212&total_amount=25017
//		BigDecimal sl = new BigDecimal(ewmContentInfo.getTotalAmountWithTax());
//		sl = sl.multiply(new BigDecimal(100));
//		String totalAmount = String.format("%.0f", sl);
//		return String.format("%s?hash=%s&bill_num=%s&total_amount=%s",ewmContentInfo.getTaxUrl(),ewmContentInfo.getCipherText(),ewmContentInfo.getInvoiceNo(),totalAmount);
//	}
//
//    /**
//	 * 去掉小数点后面无用的零，如小数点后面全是零则去掉小数点
//	 * @param obj
//	 * @return
//	 */
//	public static String subZeroAndDot(Object obj){
//		String str1 = obj+"";
//		if(str1.indexOf(".") > 0){
//		     //正则表达
//			str1 = str1.replaceAll("0+?$", "");//去掉后面无用的零
//			str1 = str1.replaceAll("[.]$", "");//如小数点后面全是零则去掉小数点
//		 }
//		return str1;
//	}
//
//	/**
//	 * 根据发票代码获取地区代码
//	 * @param invoiceCode
//	 * @return
//	 */
//	public static String getProvByInvoiceCode(String invoiceCode) {
//		String prov = "";
//		String dqCode = "";
//		if (invoiceCode.length() == 12) {
//			dqCode = invoiceCode.substring(1, 5);
//		} else {
//			dqCode = invoiceCode.substring(0, 4);
//		}
//		if (!dqCode.equals("2102") && !dqCode.equals("3302") &&! dqCode.equals("3502") && !dqCode .equals("3702") && !dqCode.equals( "4403")) {
//			dqCode = dqCode.substring(0, 2) + "00";
//		}
//
//		if("1100".equals(dqCode)) {prov = "北京";}
//		if("1200".equals(dqCode)) {prov = "天津";}
//		if("1300".equals(dqCode)) {prov = "河北";}
//		if("1400".equals(dqCode)) {prov = "山西";}
//		if("1500".equals(dqCode)) {prov = "内蒙古 ";}
//		if("2100".equals(dqCode)) {prov = "辽宁";}
//		if("2102".equals(dqCode)) {prov = "大连";}
//		if("2200".equals(dqCode)) {prov = "吉林";}
//		if("2300".equals(dqCode)) {prov = "黑龙江 ";}
//		if("3100".equals(dqCode)) {prov = "上海";}
//		if("3200".equals(dqCode)) {prov = "江苏";}
//		if("3300".equals(dqCode)) {prov = "浙江";}
//		if("3302".equals(dqCode)) {prov = "宁波";}
//		if("3400".equals(dqCode)) {prov = "安徽";}
//		if("3500".equals(dqCode)) {prov = "福建";}
//		if("3502".equals(dqCode)) {prov = "厦门";}
//		if("3600".equals(dqCode)) {prov = "江西";}
//		if("3700".equals(dqCode)) {prov = "山东";}
//		if("3702".equals(dqCode)) {prov = "青岛";}
//		if("4100".equals(dqCode)) {prov = "河南";}
//		if("4200".equals(dqCode)) {prov = "湖北";}
//		if("4300".equals(dqCode)) {prov = "湖南";}
//		if("4400".equals(dqCode)) {prov = "广东";}
//		if("4403".equals(dqCode)) {prov = "深圳";}
//		if("4500".equals(dqCode)) {prov = "广西";}
//		if("4600".equals(dqCode)) {prov = "海南";}
//		if("5000".equals(dqCode)) {prov = "重庆";}
//		if("5100".equals(dqCode)) {prov = "四川";}
//		if("5200".equals(dqCode)) {prov = "贵州";}
//		if("5300".equals(dqCode)) {prov = "云南";}
//		if("5400".equals(dqCode)) {prov = "西藏";}
//		if("6100".equals(dqCode)) {prov = "陕西";}
//		if("6200".equals(dqCode)) {prov = "甘肃";}
//		if("6300".equals(dqCode)) {prov = "青海";}
//		if("6400".equals(dqCode)) {prov = "宁夏";}
//		if("6500".equals(dqCode)) {prov = "新疆";}
//
//
//		return prov;
//	}
//
//	public static String replacePartEscape(String aa){
//		if(Tools.isEmpty(aa)) {
//			return aa;
//		}
//		aa = aa.replaceAll("&amp;","&").replaceAll("&lt;","<").replaceAll("&gt;",">").replaceAll("&quot;","\"" ).replaceAll("apos;","\'" );
//
//		return aa;
//	}
//}
