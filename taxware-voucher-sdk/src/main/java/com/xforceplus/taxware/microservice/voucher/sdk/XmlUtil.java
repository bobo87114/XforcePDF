//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.xforceplus.taxware.microservice.voucher.sdk;

import org.apache.log4j.Logger;
import org.dom4j.*;

import java.util.*;

public class XmlUtil {
    static Logger logger = Logger.getLogger(XmlUtil.class.getName());

    public XmlUtil() {
    }

    public static void main(String[] args) throws Exception {
        String xml ="";


        Map zpMap = XmlUtil.getZpFpkjMap(xml);

        zpMap.get("bz");


    }

    public static Map<String, Object> getZpFpkjMap(String xml) throws DocumentException {
        HashMap map = new HashMap();
        Document document = DocumentHelper.parseText(xml);
        Element rootElement = document.getRootElement();
        Element body = rootElement.element("body");
        Element returncode = body.element("returncode");
        Element returnmsg = body.element("returnmsg");
        String returncodes = returncode.getText();
        if ("0".equals(returncodes)) {
            Element returndata = body.element("returndata");
            Element jqbh = returndata.element("jqbh");
            Element fpdm = returndata.element("fpdm");
            Element fphm = returndata.element("fphm");
            Element kprq = returndata.element("kprq");
            Element skm = returndata.element("skm");
            Element jym = returndata.element("jym");
            Element bz;
            Element fpqqlsh = returndata.element("fpqqlsh");

            if (fpqqlsh == null) {
                if (fpqqlsh == null) {
                    Map comMap = xmlToMap(xml);
                    try {
                        Map bodyMap = (Map) comMap.get("body");
                        Map returnDataMap = (Map) bodyMap.get("returndata");
                        Map kpxxMap = (Map) returnDataMap.get("kpxx");
                        Map groupMap = (Map) kpxxMap.get("group");

                        returndata.addElement("jqbh");
                        returndata.addElement("fpdm");
                        returndata.addElement("fphm");
                        returndata.addElement("kprq");
                        returndata.addElement("skm");
                        returndata.addElement("jym");
                        returndata.addElement("fpqqlsh");

                        jqbh = returndata.element("jqbh");
                        fpdm = returndata.element("fpdm");
                        fphm = returndata.element("fphm");
                        kprq = returndata.element("kprq");
                        skm = returndata.element("skm");
                        jym = returndata.element("jym");
                        fpqqlsh = returndata.element("fpqqlsh");

                        fpdm.setText(groupMap.get("fpdm") + "");
                        fphm.setText(groupMap.get("fphm") + "");
                        kprq.setText(groupMap.get("kprq") + "");
                        jqbh.setText(groupMap.get("jqbh") + "");
                        skm.setText(groupMap.get("skm") + "");
                        jym.setText(groupMap.get("jym") + "");
                        fpqqlsh.setText(groupMap.get("jmbbh") + "");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            try {
                bz = returndata.element("ewm");
                if (bz != null) {
                    map.put("ewm", bz.getText());
                } else {
                    map.put("ewm", "");
                }
            } catch (Exception e) {
            }

            bz = returndata.element("bz");
            if (fpqqlsh != null) {
                map.put("fpqqlsh", fpqqlsh.getText());
            } else {
                map.put("fpqqlsh", "");
            }

            if (jqbh != null) {
                map.put("jqbh", jqbh.getText());
            } else {
                map.put("jqbh", "");
            }

            if (fpdm != null) {
                map.put("fpdm", fpdm.getText());
            } else {
                map.put("fpdm", "");
            }

            if (fphm != null) {
                map.put("fphm", fphm.getText());
            } else {
                map.put("fphm", "");
            }

            if (kprq != null) {
                map.put("kprq", kprq.getText());
            } else {
                map.put("kprq", "");
            }

            if (skm != null) {
                map.put("skm", skm.getText());
            } else {
                map.put("skm", "");
            }

            if (jym != null) {
                map.put("jym", jym.getText());
            } else {
                map.put("jym", "");
            }

            if (bz != null) {
                map.put("bz", bz.getText());
            } else {
                map.put("bz", "");
            }

            map.put("returncode", "0000");
            if (returnmsg != null) {
                map.put("returnmsg", returnmsg.getText());
            }
        } else {
            if (returncodes != null) {
                map.put("returncode", "9999");
            }

            if (returnmsg != null) {
                map.put("returnmsg", returnmsg.getText());
            }
        }

        return map;
    }

    public static Map<String, Object> getZfFpKjMap(String xml) throws DocumentException {
        HashMap<String, Object> map = new HashMap();
        Document document = DocumentHelper.parseText(xml);
        Element rootElement = document.getRootElement();
        Element REQUEST_COMMON_FPKJ = rootElement.element("REQUEST_COMMON_FPKJ");
        Element COMMON_FPKJ_FPT = REQUEST_COMMON_FPKJ.element("COMMON_FPKJ_FPT");
        Element FPQQLSH = COMMON_FPKJ_FPT.element("FPQQLSH");
        Element KPLX = COMMON_FPKJ_FPT.element("KPLX");
        Element BMB_BBH = COMMON_FPKJ_FPT.element("BMB_BBH");
        Element ZSFS = COMMON_FPKJ_FPT.element("ZSFS");
        Element TSPZ = COMMON_FPKJ_FPT.element("TSPZ");
        Element XSF_NSRSBH = COMMON_FPKJ_FPT.element("XSF_NSRSBH");
        Element XSF_MC = COMMON_FPKJ_FPT.element("XSF_MC");
        Element XSF_DZDH = COMMON_FPKJ_FPT.element("XSF_DZDH");
        Element XSF_YHZH = COMMON_FPKJ_FPT.element("XSF_YHZH");
        Element GMF_NSRSBH = COMMON_FPKJ_FPT.element("GMF_NSRSBH");
        Element GMF_MC = COMMON_FPKJ_FPT.element("GMF_MC");
        Element GMF_DZDH = COMMON_FPKJ_FPT.element("GMF_DZDH");
        Element GMF_YHZH = COMMON_FPKJ_FPT.element("GMF_YHZH");
        Element KPR = COMMON_FPKJ_FPT.element("KPR");
        Element SKR = COMMON_FPKJ_FPT.element("SKR");
        Element FHR = COMMON_FPKJ_FPT.element("FHR");
        Element YFP_DM = COMMON_FPKJ_FPT.element("YFP_DM");
        Element YFP_HM = COMMON_FPKJ_FPT.element("YFP_HM");
        Element JSHJ = COMMON_FPKJ_FPT.element("JSHJ");
        Element HJJE = COMMON_FPKJ_FPT.element("HJJE");
        Element HJSE = COMMON_FPKJ_FPT.element("HJSE");
        Element KCE = COMMON_FPKJ_FPT.element("KCE");
        Element BZ = COMMON_FPKJ_FPT.element("BZ");
        Element TSBZ = COMMON_FPKJ_FPT.element("TSBZ");
        Element GFKHDH = COMMON_FPKJ_FPT.element("GFKHDH");
        Element GFKHYX = COMMON_FPKJ_FPT.element("GFKHYX");
        Element YFPLX = COMMON_FPKJ_FPT.element("YFPLX");
        Element YKPRQ = COMMON_FPKJ_FPT.element("YKPRQ");
        Element SSLKJLY = COMMON_FPKJ_FPT.element("SSLKJLY");
        Element CHYYDM = COMMON_FPKJ_FPT.element("CHYYDM");
        Element COMMON_FPKJ_XMXXS = REQUEST_COMMON_FPKJ.element("COMMON_FPKJ_XMXXS");
        Attribute attribute = COMMON_FPKJ_XMXXS.attribute("size");
        List COMMON_FPKJ_XMXX = COMMON_FPKJ_XMXXS.elements("COMMON_FPKJ_XMXX");
        List list = new ArrayList();
        int j = Integer.valueOf(attribute.getValue());
        if (j != COMMON_FPKJ_XMXX.size()) {
            throw new DocumentException("size与实际明细信息数量不符");
        }


        for(j = 0; j < COMMON_FPKJ_XMXX.size(); ++j) {
            Map<String, Object> map2 = new HashMap();
            Element FPKJ_XMXX = (Element)COMMON_FPKJ_XMXX.get(j);
            Element FPHXZ = FPKJ_XMXX.element("FPHXZ");
            Element SPBM = FPKJ_XMXX.element("SPBM");
            Element ZXBM = FPKJ_XMXX.element("ZXBM");
            Element YHZCBS = FPKJ_XMXX.element("YHZCBS");
            Element LSLBS = FPKJ_XMXX.element("LSLBS");
            Element ZZSTSGL = FPKJ_XMXX.element("ZZSTSGL");
            Element XMMC = FPKJ_XMXX.element("XMMC");
            Element GGXH = FPKJ_XMXX.element("GGXH");
            Element DW = FPKJ_XMXX.element("DW");
            Element XMSL = FPKJ_XMXX.element("XMSL");
            Element XMDJ = FPKJ_XMXX.element("XMDJ");
            Element XMJE = FPKJ_XMXX.element("XMJE");
            Element SL = FPKJ_XMXX.element("SL");
            Element SE = FPKJ_XMXX.element("SE");
            if (FPHXZ != null) {
                map2.put("FPHXZ", FPHXZ.getText());
            } else {
                map.put("FPHXZ", "");
            }

            if (XMMC != null) {
                map2.put("XMMC", XMMC.getText());
            } else {
                map2.put("XMMC", "");
            }

            if (GGXH != null) {
                map2.put("GGXH", GGXH.getText());
            } else {
                map2.put("GGXH", "");
            }

            if (DW != null) {
                map2.put("DW", DW.getText());
            } else {
                map2.put("DW", "");
            }

            if (XMSL != null) {
                map2.put("XMSL", parse(XMSL.getText()));
            } else {
                map2.put("XMSL", "");
            }

            if (XMDJ != null) {
                map2.put("XMDJ", parse(XMDJ.getText()));
            } else {
                map2.put("XMDJ", "");
            }

            if (XMJE != null) {
                map2.put("XMJE", parse(XMJE.getText()));
            } else {
                map2.put("XMJE", "");
            }

            if (SL != null) {
                map2.put("SL", parse(SL.getText()));
            } else {
                map2.put("SL", "");
            }

            if (SE != null) {
                map2.put("SE", parse(SE.getText()));
            } else {
                map2.put("SE", "");
            }

            if (SPBM != null) {
                map2.put("SPBM", parse(SPBM.getText()));
            } else {
                map2.put("SPBM", "");
            }

            if (ZXBM != null) {
                map2.put("ZXBM", parse(ZXBM.getText()));
            } else {
                map2.put("ZXBM", "");
            }

            if (YHZCBS != null) {
                map2.put("YHZCBS", parse(YHZCBS.getText()));
            } else {
                map2.put("YHZCBS", "");
            }

            if (LSLBS != null) {
                map2.put("LSLBS", parse(LSLBS.getText()));
            } else {
                map2.put("LSLBS", "");
            }

            if (ZZSTSGL != null) {
                map2.put("ZZSTSGL", ZZSTSGL.getText());
            } else {
                map2.put("ZZSTSGL", "");
            }

            list.add(map2);
        }

        map.put("COMMON_FPKJ_XMXXS", list);
        if (FPQQLSH != null) {
            map.put("FPQQLSH", parse(FPQQLSH.getText()));
        } else {
            map.put("FPQQLSH", "");
        }

        if (KPLX != null) {
            map.put("KPLX", parse(KPLX.getText()));
        } else {
            map.put("KPLX", "");
        }

        if (BMB_BBH != null) {
            map.put("BMB_BBH", parse(BMB_BBH.getText()));
        } else {
            map.put("BMB_BBH", "");
        }

        if (ZSFS != null) {
            map.put("ZSFS", parse(ZSFS.getText()));
        } else {
            map.put("ZSFS", "0");
        }

        if (TSPZ != null) {
            map.put("TSPZ", parse(TSPZ.getText()));
        } else {
            map.put("TSPZ", "");
        }

        if (XSF_NSRSBH != null) {
            map.put("XSF_NSRSBH", parse(XSF_NSRSBH.getText()));
        } else {
            map.put("XSF_NSRSBH", "");
        }

        if (XSF_MC != null) {
            map.put("XSF_MC", XSF_MC.getText());
        } else {
            map.put("XSF_MC", "");
        }

        if (XSF_DZDH != null) {
            map.put("XSF_DZDH", XSF_DZDH.getText());
        } else {
            map.put("XSF_DZDH", "");
        }

        if (XSF_YHZH != null) {
            map.put("XSF_YHZH", XSF_YHZH.getText());
        } else {
            map.put("XSF_YHZH", "");
        }

        if (GMF_NSRSBH != null) {
            map.put("GMF_NSRSBH", parse(GMF_NSRSBH.getText()));
        } else {
            map.put("GMF_NSRSBH", "");
        }

        if (GMF_MC != null) {
            map.put("GMF_MC", GMF_MC.getText());
        } else {
            map.put("GMF_MC", "");
        }

        if (GMF_DZDH != null) {
            map.put("GMF_DZDH", GMF_DZDH.getText());
        } else {
            map.put("GMF_DZDH", "");
        }

        if (GMF_YHZH != null) {
            map.put("GMF_YHZH", GMF_YHZH.getText());
        } else {
            map.put("GMF_YHZH", "");
        }

        if (KPR != null) {
            map.put("KPR", KPR.getText());
        } else {
            map.put("KPR", "");
        }

        if (SKR != null) {
            map.put("SKR", SKR.getText());
        } else {
            map.put("SKR", "");
        }

        if (FHR != null) {
            map.put("FHR", FHR.getText());
        } else {
            map.put("FHR", "");
        }

        if (YFP_DM != null) {
            map.put("YFP_DM", parse(YFP_DM.getText()));
        } else {
            map.put("YFP_DM", "");
        }

        if (YFP_HM != null) {
            map.put("YFP_HM", parse(YFP_HM.getText()));
        } else {
            map.put("YFP_HM", "");
        }

        if (JSHJ != null) {
            map.put("JSHJ", parse(JSHJ.getText()));
        } else {
            map.put("JSHJ", "");
        }

        if (HJJE != null) {
            map.put("HJJE", parse(HJJE.getText()));
        } else {
            map.put("HJJE", "");
        }

        if (HJSE != null) {
            map.put("HJSE", parse(HJSE.getText()));
        } else {
            map.put("HJSE", "");
        }

        if (KCE != null) {
            map.put("KCE", parse(KCE.getText()));
        } else {
            map.put("KCE", "");
        }

        if (BZ != null) {
            map.put("BZ", BZ.getText());
        } else {
            map.put("BZ", "");
        }

        if (TSBZ != null) {
            map.put("TSBZ", TSBZ.getText());
        } else {
            map.put("TSBZ", "");
        }

        if (GFKHDH != null) {
            map.put("GFKHDH", GFKHDH.getText());
        } else {
            map.put("GFKHDH", "");
        }

        if (GFKHYX != null) {
            map.put("GFKHYX", GFKHYX.getText());
        } else {
            map.put("GFKHYX", "");
        }

        if (YFPLX != null) {
            map.put("YFPLX", YFPLX.getText());
        } else {
            map.put("YFPLX", "");
        }

        if (YKPRQ != null) {
            map.put("YKPRQ", YKPRQ.getText());
        } else {
            map.put("YKPRQ", "");
        }

        if (SSLKJLY != null) {
            map.put("SSLKJLY", SSLKJLY.getText());
        } else {
            map.put("SSLKJLY", "");
        }

        if (CHYYDM != null) {
            map.put("CHYYDM", CHYYDM.getText());
        } else {
            map.put("CHYYDM", "");
        }

        return map;
    }

    /**
     * xml转Map
     *
     * @param xml
     * @return
     * @throws DocumentException
     */
    public static Map<String, Object> xmlToMap(String xml) throws DocumentException {
        LinkedHashMap mso = new LinkedHashMap();
        Document doc = DocumentHelper.parseText(xml);
        Element root = doc.getRootElement();
        generateMap(root, mso);
        return (Map) mso.get(root.getName());
    }

    private static void generateMap(Element parent, Map<String, Object> mso) {
        List le = parent.elements();
        List la = parent.attributes();
        if (le.size() == 0 && la.size() == 0) {
            mso.put(parent.getName(), parent.getText());
        } else {
            LinkedHashMap childMap = new LinkedHashMap();
            String parentName = parent.getName();
            Object alreadyIn = mso.get(parentName);
            if (alreadyIn != null) {
                if (alreadyIn instanceof Collection) {
                    ((Collection) alreadyIn).add(childMap);
                } else {
                    ArrayList e = new ArrayList();
                    e.add(alreadyIn);
                    e.add(childMap);
                    mso.put(parentName, e);
                }
            } else {
                mso.put(parent.getName(), childMap);
            }

            Iterator var8 = la.iterator();

            while (var8.hasNext()) {
                Attribute e1 = (Attribute) var8.next();
                childMap.put(e1.getName(), e1.getValue());
            }

            var8 = le.iterator();

            while (var8.hasNext()) {
                Element e2 = (Element) var8.next();
                generateMap(e2, childMap);
            }

        }
    }

    public static String parse(String str)
        /*      */   {
        /* 2981 */     return str.replaceAll("&", "&amp;").replaceAll(">", "&gt;").replaceAll("<", "&lt;")
/* 2982 */       .replaceAll("\"", "&quot;").replaceAll("'", "apos;");
        /*      */   }
}


