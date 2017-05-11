package com.diting.service.yuyi;


import com.diting.model.OralTreatment;
import com.diting.model.ReplaceWord;
import com.diting.model.Synonym;
import com.diting.model.WordBase;
import com.diting.service.OralTreatmentService;
import com.diting.service.ReplaceWordService;
import com.diting.service.SynonymService;
import com.diting.service.WordBaseService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
@SuppressWarnings("ALL")
@Service("yuyijs")
public class Yuyijs {
    static Logger logger = LoggerFactory.getLogger(Yuyijs.class);
    static String englishChars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
    static String numbers = "0123456789";
    @Autowired
    private WordBaseService wordBaseService;
    @Autowired
    private SynonymService synonymService;
    @Autowired
    private ReplaceWordService replaceWordService;
    @Autowired
    private OralTreatmentService oralTreatmentService;

    public static Map<String, List<WordBase>> mlWordBase = new LinkedHashMap<>();

    public static Map<String, List<Synonym>> synonymMap = new LinkedHashMap<>();

    public static Map<String, List<ReplaceWord>> replaceWordMap = new LinkedHashMap<>();

    public static Map<String, List<OralTreatment>> oralTreatmentMap = new LinkedHashMap<>();

    static String changjingjs(String a, String shang) {
//        String cj = shang;
//        String a1 = a;
//        a = a.replace(" ", "");
//        a = a.trim();
//        if (a.indexOf("谁爱") == 0 || a.indexOf("谁能") == 0 || a.indexOf("谁会") == 0) {
//            cj = "认识事物";
//        } else if (a.indexOf("男朋友") > -1 || a.indexOf("女朋友") > -1 || a.indexOf("男友") > -1 || a.indexOf("女友") > -1) {
//            cj = "男女朋友";
//        } else if (a.indexOf("你") > -1 && (a.indexOf("干什么") > -1 || a.indexOf("会什么") > -1 || a.indexOf("会做") > -1
//                || a.indexOf("能做") > -1 || a.indexOf("会干") > -1 || a.indexOf("能干") > -1 || a.indexOf("懂") > -1
//                || a.indexOf("功能") > -1)) {
//            cj = "你会什么";
//        } else if (a.indexOf("你") > -1
//                && (a.indexOf("爷") > -1 || a.indexOf("奶") > -1 || a.indexOf("哥") > -1 || a.indexOf("姐") > -1
//                || a.indexOf("弟") > -1 || a.indexOf("妹") > -1 || a.indexOf("爸") > -1 || a.indexOf("妈") > -1)) {
//            cj = "你家庭";
//        } else if (a.indexOf("你") > -1 && (a.indexOf("性别") > -1 || a.indexOf("男") > -1 || a.indexOf("女") > -1)) {
//            cj = "你性别";
//        } else if (a.indexOf("你") > -1 && (a.indexOf("生的") > -1 || a.indexOf("出生") > -1 || a.indexOf("年龄") > -1
//                || a.indexOf("几岁") > -1 || a.indexOf("多大") > -1 || a.indexOf("生日") > -1)) {
//            cj = "你年龄";
//        } else if ((a.indexOf("你") > -1 && a.indexOf("我") > -1) || a.indexOf("我们") > -1) {
//            cj = "我你";
//        } else if (a.indexOf("笑话") > -1 && (a.indexOf("讲") > -1 || a.indexOf("说") > -1 || a.indexOf("听") > -1)) {
//            cj = "讲笑话";
//        } else if (a.indexOf("歌") > -1 || a.indexOf("唱") > -1 || a.indexOf("音乐") > -1 || a.indexOf("播放") > -1) {
//            cj = "音乐播放";
//        } else if (a.indexOf("频道") > -1 || a.indexOf("卫视") > -1 || a.indexOf("中央一套 ") > -1 || a.indexOf("电视") > -1
//                || a.indexOf("画面") > -1 || a.indexOf("有线") > -1) {
//            cj = "看电视";
//        } else if (a.indexOf("你叫什么") > -1 || a.indexOf("你是") > -1) {
//            cj = "你是";
//        } else if (a.equals("嗨") || a.equals("哈喽") || a.equals("hello") || a.equals("你好") || a.equals("你好么")
//                || a.equals("你好啊") || a.equals("你好吧") || a.equals("你好呀") || a.equals("你好吗") || a.equals("你好不")
//                || a.equals("你好不好") || (a.indexOf("见到你") > -1 && ((a.indexOf("开心") > -1) || a.indexOf("高兴") > -1))) {
//            cj = "打招呼";
//        } else if (a.indexOf("前") > -1 || a.indexOf("左") > -1 || a.indexOf("后") > -1 || a.indexOf("右") > -1
//                || a.indexOf("停") > -1) {
//            cj = "运动";
//        } else if (a.indexOf("你") == 0 && a.indexOf("爱") > -1) {
//            cj = "你喜欢";
//        } else {
//            cj = guanjianzijs(a1);
//            String c[] = cj.split(",");
//            cj = "";
//            for (int ii = 0; ii < 2; ii++) {
//                if (!c[ii].equals("k$z")) {
//                    cj = c[ii];
//                    break;
//                }
//            }
//        }

        return null;
    }

    /**
     * 计算词值
     * @param a
     * @param b
     * @return
     */
    static int cizhi(String a, String b) {
        List<WordBase> wlist = mlWordBase.get(a);
        int zhi = 10;
        if (wlist != null) {
                int i1 = 0;
                int i2 = 0;
                i1 = wlist.get(0).getWord_character();
                i2 = wlist.get(0).getWord_level();
                if (b == "cixing") {
                    zhi = i1;
                } else if (b == "zhi") {
                    zhi = (11 - i1) * 10 + (11 - i2);
                } else {
                    zhi = i2;
                }
        }
        return zhi;
    }

    static String shouci(String a) {
        String jieguo = "";
        if (a != "") {
            String b;
            b = "";
            int ic = a.length();
            int ib = ic;
            if (ic >= 10) {
                ib = 10;
            }
            for (int i = ib; i > 0; i--) {
                b = a.substring(0, i);
                if (Yuyijs.cizhi(b, "cixing") < 10) {
                    jieguo = b;
                    break;
                }
            }
        }
        return jieguo;
    }

    static String weici(String a) {
        String jieguo = "";
        if (a != "") {
            String b = "";
            int ic = a.length();
            int ib = ic;
            if (ic >= 10) {
                ib = 10;
            }
            for (int i = ib; i > 0; i--) {
                b = a.substring(ic - i, ic);
                if (Yuyijs.cizhi(b, "cixing") < 10) {
                    jieguo = b;
                    break;
                }
            }
        }
        return jieguo;
    }

    static String shoucijiaodui(String a, String shouci) {
        String jieguo;
        jieguo = "";
        String b;
        b = "";
        String c;
        c = "";
        String jiluc = "";
        int jilucc = 0;
        for (int ia = 1; ia <= shouci.length(); ia++) {
            b = shouci.substring(0, ia);
            if (cizhi(b, "cixing") < 10) {
                int ic = 0;
                if (a.length() - b.length() < 5) {
                    ic = (a.length() - b.length());
                } else {
                    ic = 5;
                }
                for (int i = 0; i <= ic; i++) {
                    c = a.substring(b.length(), b.length() + i);
                    int cizic = ((10 - cizhi(c, "cixing")) * 10 - cizhi(c, "jibie"));
                    if (cizic > jilucc) {
                        jiluc = c;
                        jilucc = cizic;
                    }
                }
            }
        }

        if (((10 - cizhi(shouci, "cixing")) * 10 - cizhi(shouci, "jibie")) < jilucc) {
            jieguo = a.substring(0, a.indexOf(jiluc));
        } else {
            jieguo = shouci;
        }
        return jieguo;
    }

    static String shoucibushi(String a) {//这里输出生词
        String jieguo = "";
        if (a != "") {
            String b;
            b = "";
            String c;
            c = "";
            int il = 0;
            int ic = a.length();
            if (a.length() >= 10) {
                ic = 10;
            }
            for (int ib = 1; ib < ic; ib++) {
                c = a.substring(ib, a.length());
                b = shouci(c);
                if (Yuyijs.cizhi(b, "cixing") != 10) {
                    il = ib;
                    break;
                } else {
                    il = ic;
                }
            }
            jieguo = a.substring(0, il);
        }
        return jieguo;
    }

    static String weicibushi(String a) {//这里输出生词
        String jieguo = "";
        if (a != "") {
            String b;
            b = "";
            String c;
            c = "";
            int il = 0;
            int ic = a.length();
            int ib = ic;
            if (ic >= 10) {
                ib = 10;
            }
            for (int i = 0; i < ib; i++) {
                c = a.substring(0, ic - i);
                b = weici(c);
                if (Yuyijs.cizhi(b, "cixing") != 10) {
                    il = i;
                    break;
                } else {
                    il = ib;
                }
            }
            jieguo = a.substring(ic - il, ic);
        }
        return jieguo;
    }

    public static String bushijiaodui(String cx, String c) {
        String d = "";
        String e = "";
        String f = "";
        String jieguo = "";
        for (int i = 1; i < c.length(); i++) {
            d = c.substring(i);
            f = c.substring(0, i);
            e = d + cx;
            if (Yuyijs.cizhi(e, "cixing") < 10 && Yuyijs.cizhi(f, "cixing") < 10) {
                jieguo = f;
                break;
            }
        }

        return jieguo;
    }

    public static String weicibushijiaodui(String cx, String c) {
        String d = "";
        String e = "";
        String f = "";
        String jieguo = "";
        for (int i = 1; i < c.length(); i++) {
            d = c.substring(i);
            f = c.substring(0, i);
            e = d + cx;
            if (Yuyijs.cizhi(e, "cixing") < 10 && Yuyijs.cizhi(f, "cixing") < 10) {
                jieguo = f;
                break;
            }
        }

        return jieguo;
    }

    /**
     * 切词
     * 调用方法 qieci("我想听年度之歌", "{"gjz1":"下雨","gjz2":"听","gjz3":"我想","gjz4":"k$z","gjz5":"k$z"}", "音乐播放")
     * @param localScanner 本句输入
     * @param keyString 上句关键字
     * @param scene 本句场景
     * @return
     */
    static String qieci(String localScanner, String keyString, String scene) {
        //关键字key:{"gjz1":"k$z","gjz2":"k$z","gjz3":"k$z","gjz4":"k$z","gjz5":"你好啊"}
       /* JSONObject jsonObject=JSONObject.fromObject(keyString);
        for (Object key:jsonObject.keySet()) {
            if(jsonObject.get(key)!=null&&jsonObject.get(key)!=""&&!"k$z".equals(jsonObject.get(key)))
            if(localScanner!=null&&localScanner!=""&&localScanner.indexOf(jsonObject.get(key).toString())!=-1){
                localScanner= localScanner.replaceAll(jsonObject.get(key).toString()," "+jsonObject.get(key).toString()+" ");
            }
        }*/
        /*System.out.println("原：" + localScanner);*/
        //根据场景进行切词
       /* if (scene != null && scene != "") {
            localScanner = YuyijsScene.qiezi(localScanner, scene);
        }*/
        localScanner = YuyijsScene.qiezi(localScanner, scene);
        //无场景切词
        /*localScanner = qiezi(localScanner, keyString);*/
        //去除前后空格，将多个空格替换成一个空格
        localScanner = localScanner.trim();
        Pattern p = Pattern.compile("\\s+");
        Matcher m = p.matcher(localScanner);
        localScanner = m.replaceAll(" ");
        return localScanner;
    }

    static String shuruchuli(String wenti, String keyString, String scene) {
        String jieguo = "";
        wenti = kouyu(wenti);
        String reg = "[^\u4e00-\u9fa5\\sa-zA-Z0-9]";
        wenti = wenti.replaceAll(reg, "");
        wenti=wenti.trim();
        wenti = qieci(wenti, keyString, scene);
        wenti = specialchuli(wenti,scene);
        wenti = tongyichuli(wenti);
        jieguo = wenti.trim();
        return jieguo;
    }

    /**
     * 对切词后的问题进行特殊情况处理
     * @param wenti
     * @param scene
     * @return
     */
    static String specialchuli(String wenti,String scene){
        //不是音乐播放场景情况下 拆分‘想听’
        if(!"音乐播放".equals(scene)&&wenti.indexOf("想听")!=-1){
            wenti=wenti.replaceAll("想听","想 听");
        }
        return wenti;
    }

    /**
     * 切词
     * 调用方法：qiezi(原句，上句关键字)
     * @param a
     * @param keyString
     * @return
     */
    static String qiezi(String a, String keyString) {
        String jieguo = "";
        String jieguoa = "";
        String jieguob = "";
        String jieguoc = "";
        String jieguod = "";
        int jvzizhia = 0;
        int jvzizhib = 0;
        int jvzizhic = 0;
        int jvzizhid = 0;
        if (a != "") {
            String reg = "[^\u4e00-\u9fa5\\sa-zA-Z0-9]";
            String d = a.replaceAll(reg, "");
            String d1 = d;
            String d2 = d;
            String d3 = d;
            String c = "";
            String cx = "";
            String cxx = "";
            String jieguo1 = "";
            //正向计算
            while (!d.equals("") && !d.equals(null)) {
                c = shouci(d);
                if (c != "" && c.length() > 0) {
                    d = d.substring(c.length(), d.length());    //这里去掉首词
                    jieguo1 = jieguoa;
                    jieguoa = jieguoa + " " + c;
                } else {
                    cx = shoucibushi(d);
                    if (cx != "" && cx.length() > 0) {
                        if (cx != "" && c != "") {
                            cxx = bushijiaodui(cx, c);
                            if (cxx != "") {
                                String g = (c + d);
                                d = g.substring(cxx.length(), g.length());    //这里去掉首词
                                jieguoa = jieguo1 + " " + cxx;
                            } else {
                                d = d.substring(cx.length(), d.length());    //这里去掉首词
                                jieguoa = jieguoa + " " + cx;
                            }
                        } else {
                            d = d.substring(cx.length(), d.length());    //这里去掉首词
                            jieguoa = jieguoa + " " + cx;
                        }
                    } else {
                        d = "";
                    }
                }
            }
            jieguoa = jieguoa.trim();


            String[] jieguoA = jieguoa.split(" ");

            /*for (int i = 0; i < jieguoA.length; i++) {
                jvzizhia += Yuyijs.cizhi(jieguoA[i], "zhi");
            }*/
            for (int i=0;i<jieguoA.length;i++)
            {
                if 	(Yuyijs.cizhi(jieguoA[i],"cixing")<9)//2016/11/13
                {
                    jvzizhia+=Yuyijs.cizhi(jieguoA[i],"zhi");
                }//2016/11/13
            }
            //反向计算
            while (!d1.equals("") && !d1.equals(null)) {
                c = weici(d1);
                if (!c.equals("") && !c.equals(null)) {
                    d1 = d1.substring(0, d1.length() - c.length());    //这里去掉尾词
                    jieguo1 = jieguob;
                    jieguob = c + " " + jieguob;
                } else {
                    cx = weicibushi(d1);
                    if (cx != "") {
                        if (cx != "" && c != "") {
                            cxx = weicibushijiaodui(cx, c);
                            if (cxx != "") {
                                String g = (c + d1);
                                d1 = g.substring(0, g.length() - cxx.length());    //这里去掉尾词
                                jieguob = cxx + " " + jieguo1;
                            } else {
                                d1 = d1.substring(0, d1.length() - cx.length());    //这里去掉尾词
                                jieguob = cx + " " + jieguob;
                            }
                        } else {
                            d1 = d1.substring(0, d1.length() - cx.length());    //这里去掉尾词
                            jieguob = cx + " " + jieguob;
                        }
                    } else {
                        d1 = "";
                    }
                }
            }

            jieguob = jieguob.trim();

            String[] jieguoB = jieguob.split(" ");

            /*for (int i = 0; i < jieguoB.length; i++) {
                jvzizhib += Yuyijs.cizhi(jieguoB[i], "zhi");
            }*/
            for (int i=0;i<jieguoB.length;i++)
            {
                if 	(Yuyijs.cizhi(jieguoB[i],"cixing")<9)//2016/11/13
                {
                    jvzizhib+=Yuyijs.cizhi(jieguoB[i],"zhi");
                }//2016/11/13
            }
            //正向简便计算
            while (!d2.equals("") && !d2.equals(null)) {
                c = shouci(d2);
                if (c != "") {
                    d2 = d2.substring(c.length(), d2.length());    //这里去掉首词
                    jieguo1 = jieguoc;
                    jieguoc = jieguoc + " " + c;
                } else {
                    cx = shoucibushi(d2);
                    if (cx != "" && cx.length() > 0) {
                        d2 = d2.substring(cx.length(), d2.length());    //这里去掉首词
                        jieguoc = jieguoc + " " + cx;
                    } else {
                        d2 = "";
                    }
                }
            }
            jieguoc = jieguoc.trim();


            String[] jieguoC = jieguoc.split(" ");

            /*for (int i = 0; i < jieguoC.length; i++) {
                jvzizhic += Yuyijs.cizhi(jieguoC[i], "zhi");
            }*/
            for (int i=0;i<jieguoC.length;i++)
            {
                if 	(Yuyijs.cizhi(jieguoC[i],"cixing")<9)//2016/11/13
                {
                    jvzizhic+=Yuyijs.cizhi(jieguoC[i],"zhi");
                }//2016/11/13
            }
            //结果D 根据上句关键字计算
            //关键字key:{"gjz1":"k$z","gjz2":"k$z","gjz3":"k$z","gjz4":"k$z","gjz5":"你好啊"}
            /*if (keyString != null && keyString != "") {
                Object object= JSONValue.parse(keyString);
                JSONObject jo=(JSONObject)object;
                for (Object key : jo.keySet()) {
                    if (jo.get(key) != null && jo.get(key) != "" && !"k$z".equals(jo.get(key)))
                        if (d3 != null && d3 != "" && d3.indexOf(jo.get(key).toString()) != -1) {
                            d3 = d3.replaceAll(jo.get(key).toString(), " " + jo.get(key).toString() + " ");
                        }
                }
            }
            //正向简便计算
            while (!d3.equals("") && !d3.equals(null)) {
                c = shouci(d3);
                if (c != "") {
                    d3 = d3.substring(c.length(), d3.length());    //这里去掉首词
                    jieguo1 = jieguod;
                    jieguod = jieguod + " " + c;
                } else {
                    cx = shoucibushi(d3);
                    if (cx != "" && cx.length() > 0) {
                        d3 = d3.substring(cx.length(), d3.length());    //这里去掉首词
                        jieguod = jieguod + " " + cx;
                    } else {
                        d3 = "";
                    }
                }
            }
            jieguod = jieguod.trim();
            String[] jieguoD = jieguod.split(" ");

            for (int i = 0; i < jieguoD.length; i++) {
                jvzizhid += Yuyijs.cizhi(jieguoD[i], "zhi");
            }
            int B = 0;
            int A[] = {jvzizhia, jvzizhib, jvzizhic, jvzizhid};
            String As[] = {jieguoa, jieguob, jieguoc, jieguod};
            */
            /*jvzizhid+=100;*/
            int B = 0;
            int A[] = {jvzizhia, jvzizhib, jvzizhic};
            String As[] = {jieguoa, jieguob, jieguoc};
            int az = 0;
            for (int i = 0; i < 3; i++) {
                if (A[i] > az) {
                    az = A[i];
                    B = i;
                }
            }
            jieguo = As[B];
        }
        //	jieguo=jvzizhia+"|"+jvzizhib;
        return jieguo;
    }

    /**
     * 口语处理
     * 切词前对原句进行处理
     * @param a
     * @return
     */
    public static String kouyu(String a) {
        String jieguo = "";
        /*绝对替换开始*/
        Set keySet = replaceWordMap.keySet();
        //获取所有key值
        for (Object keyName : keySet) {
            //判断a是否在数据库列表中存在
            if (a.indexOf(keyName.toString()) > -1) {
                List<ReplaceWord> words = new ArrayList<>();
                words = replaceWordMap.get(keyName);
                if (words != null && words.size() > 0) {
                    ReplaceWord word = new ReplaceWord();
                    word = words.get(0);
                    a = a.replace(word.getPre_replace_word(), word.getReplace_word());
                    break;
                }
            }
        }
        if (a.indexOf("有什么用") > -1) {
            if (a.indexOf("有什么用途") == -1 && a.indexOf("有什么用处") == -1) {
                a = a.replace("有什么用", "有什么用处");
            }
        }
        /*绝对替换结束*/

        /*口语处理开始*/
        Set oralTreatmentSet = oralTreatmentMap.keySet();
        //获取所有key值
        for (Object keyName : oralTreatmentSet) {
            //判断a是否在数据库列表中存在
            if (a.indexOf(keyName.toString()) > -1) {
                List<OralTreatment> words = new ArrayList<>();
                words = oralTreatmentMap.get(keyName);
                if (words != null && words.size() > 0) {
                    for (OralTreatment word:words) {
                        String[] args=null;
                        if(word.getEliminate()!=null&&word.getEliminate()!=""&&word.getEliminate().length()>0){
                            args=word.getEliminate().split("，");
                        }
                        boolean flag=true;
                        if("left".equals(word.getWord_place())){
                            flag=true;
                            if (a.length() >= word.getWord_size()&& a.substring(0, word.getWord().length()).equals(word.getWord())) {
                                if(args!=null&&args.length>0){
                                    for (String arg:args) {
                                        if(a.indexOf(arg)!=-1){
                                            flag=false;
                                        }
                                    }
                                }
                                if (flag){
                                    a = a.substring(word.getWord().length(), a.length());
                                }
                            }
                        }
                        if("right".equals(word.getWord_place())){
                            flag=true;
                            if (a.length() >= word.getWord_size()&& a.substring(a.length()-word.getWord().length(), a.length()).equals(word.getWord())) {
                                if(args!=null) {
                                    for (String arg : args) {
                                        if (a.indexOf(arg) != -1) {
                                            flag=false;
                                        }
                                    }
                                }
                                if (flag){
                                    a = a.substring(0, a.length() - word.getWord().length());
                                }
                            }
                        }
                    }
                }
            }
        }
        if (a.length() > 3) {
            if (a.length() > 3 && a.substring(0, 3).equals("你好你") && a.length() > 3) {
                a = a.replace("你好你", "你");
            }//7-14
            if (a.length() > 3 && a.substring(a.length() - 3, a.length()).equals("是不是")) {
                a = a.substring(0, a.length() - 3);
                a = a + "吗";
            }
            if (a.length() > 3 && a.substring(a.length() - 3, a.length()).equals("有没有")) {
                a = a.substring(0, a.length() - 3);
                a = a + "吗";
            }
            if (a.length() > 3 && a.substring(a.length() - 3, a.length()).equals("有么有")) {
                a = a.substring(0, a.length() - 3);
                a = a + "吗";
            }
        }

        if (a.length() > 2) {
            if (a.length() > 2 && a.substring(0, 2).equals("你说") && a.length() > 2 && a.indexOf("你说话") < -1 && a.indexOf("你说的") < -1) {
                a = a.substring(2, a.length());
            }
            if (a.length() > 1 && a.substring(a.length() - 1, a.length()).equals("耶")) {
                a = a.replace("耶", "呀");
            }
            if (a.length() > 1 && a.substring(a.length() - 1, a.length()).equals("呵")) {
                a = a.replace("呵", "啊");
            }
            if (a.length() > 1 && a.substring(a.length() - 1, a.length()).equals("捏")) {
                a = a.replace("捏", "呀");
            }
            if (a.length() > 1 && a.substring(a.length() - 1, a.length()).equals("涅")) {
                a = a.replace("涅", "呀");
            }
            if (a.length() > 1 && a.substring(a.length() - 1, a.length()).equals("吆")) {
                a = a.replace("吆", "呀");
            }
            if (a.length() > 1 && a.substring(a.length() - 1, a.length()).equals("不")) {
                a = a.replace("不", "吗");
            }
            if (a.length() > 1 && a.substring(a.length() - 1, a.length()).equals("么") && a.indexOf("什么") < -1) {
                a = a.replace("么", "吗");
            }
            if (a.length() > 1 && a.substring(a.length() - 1, a.length()).equals("没")) {
                a = a.replace("没", "吗");
            }
            if (a.length() > 1 && a.substring(a.length() - 1, a.length()).equals("幺")) {
                a = a.replace("幺", "吗");
            }
            if (a.length() > 2 && a.substring(a.length() - 1, a.length()).equals("么")
                    && !a.substring(a.length() - 2, a.length()).equals("什么")) {
                a = a.replace("么", "吗");
            }
        }
        if (a.length() > 1) {
            if (a.length() > 1 && a.substring(a.length() - 2, a.length()).equals("在哪")) {
                a = a.replace("在哪", "在哪里");
            }//8-25
            if (a.length() > 1 && a.substring(a.length() - 2, a.length()).equals("是社")) {
                a = a.replace("是社", "是什么");
            }//8-25
        }
        if (a.length() > 5 && a.substring(a.length() - 4, a.length()).equals("怎么处理")) {
            a = a.substring(0, a.length() - 4) + "怎么办";
        }
        if (a.length() > 5 && a.substring(a.length() - 4, a.length()).equals("怎么解决")) {
            a = a.substring(0, a.length() - 4) + "怎么办";
        }
        /*口语处理结束*/

        /*去除多余符号开始*/
        Pattern patPunc = Pattern.compile("[`~!@#$^&()=|{}':;',\\[\\].<>?~！@#￥……&*（）——|{}【】‘；：”“'。，、？]");
        //如果a最后一位是符号，则截取掉该符号
        Matcher m = null;
        if (a.length() > 1) {
            m = patPunc.matcher(a.substring(a.length() - 1));
        }
        if (m != null && m.matches()) {
            a = a.substring(0, a.length() - 1);
        }
        /*去除多余符号结束*/
        jieguo = a;
        return jieguo;
    }

    /**
     * 语义相似计算
     * 调用方法：xiangcai("有 客 云 的 产品价格 是 多少","有客云的价格")
     * @param a
     * @param b
     * @return
     */
    static int xiangcai(String a, String b) {
        int s = 0;
        a = a.trim();
        b = b.trim();
        if (a.equals(b)) {
            s = 100000;
        } else {
            String al = a;
            String as = b;
            if (a.length() > b.length()) {
                al = a;
                as = b;
            } else {
                al = b;
                as = a;
            }

            if (al.indexOf(" ") > -1) {
                String qiezia[] = al.split(" ");
                int ig = 0;
                int mc = 100000 / cishu(al);
                for (ig = 0; ig < qiezia.length; ig++) {
                    if (cizhi(qiezia[ig], "cixing") < 10) {

                        if (as.indexOf(qiezia[ig]) > -1) {
                            int weizhizhi = 0;
                            weizhizhi = mc / 2000 * (Math.abs(as.indexOf(qiezia[ig]) - al.indexOf(qiezia[ig])));
                            s = s + mc + 20 * (10 - cizhi(qiezia[ig], "cixing")) + (10 - cizhi(qiezia[ig], "paixu"))
                                    - weizhizhi;
                        } else {
                            s = s - mc - 20 * (10 - cizhi(qiezia[ig], "cixing")) + (10 - cizhi(qiezia[ig], "paixu"));
                        }
                    }
                }
            } else {
                if (a.indexOf(b) > -1) {
                    s = 100000;
                }
            }
        }
        return s;
    }

    static int cishu(String a) {
        int jieguo = 0;
        String c[] = a.split(" ");
        for (int n = 0; n < c.length; n++) {
            if (cizhi(c[n], "cixing") <= 9) {
                jieguo++;
            }
        }
        if (jieguo == 0) {
            jieguo = 1;
        }
        return jieguo;
    }

    static String shuruchuli(String wenti) {
        String jieguo = "";
        wenti = kouyu(wenti);
        String reg = "[^\u4e00-\u9fa5\\sa-zA-Z0-9]";
        wenti = wenti.replaceAll(reg, "");
        /***************************************************************************************************************************************************/
        /*wenti = qiezi(wenti);*/
        wenti = tongyichuli(wenti);
        jieguo = wenti.trim();
        return jieguo;
    }

    /**
     * 同义词替换
     * 切词后进行同义词替换
     * @param wenti
     * @return
     */
    static String tongyichuli(String wenti) {
        String jieguo = "";
        String A = "";
        String B = "";
        String wt[] = wenti.split(" ");
        for (int i = 0; i < wt.length; i++) {
            A = wt[i];
            List<Synonym> synonyms = synonymMap.get(A);
            if (synonyms != null)//A在被替换词列表里，这个需要在数据库里查询
            {
                // A=B;//B代表标准词，B需要在数据库里查询
                A = synonyms.get(0).getWord_new();
            }
            jieguo = jieguo + " " + A;
        }
        return jieguo;
    }

    /**
     * 找出几句中与原句最匹配的句子
     * 调用方法：yuyixiangsijs("有客云的产品价格是多少","有客云的价格fen!ge有客云价格")
     * @param wenti
     * @param ku
     * @return
     * @throws Exception
     */
    public static String yuyixiangsijs(String wenti, String ku) throws Exception {
        String da = "";
        if (wenti != null && ku != null) {
            try {
                da = jicai(wenti, ku);
            } catch (Exception e) {
                // TODO 自动生成的 catch 块
                e.printStackTrace();
            }
        }
        // long begin=System.currentTimeMillis();
        // String sDT="12/31/2015 21:08:00";        // SimpleDateFormat sdf=new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
        // Date dt2 = null;
        // try {
        // dt2 = sdf.parse(sDT);
        // } catch (ParseException e) {
        // TODO 自动生成的 catch 块
        // e.printStackTrace();
        // }
        // long end = dt2.getTime();
        // if (begin>=end){da="您的授权已过期！";}

        return da;
    }

    static String jicai(String shuru, String ku) throws Exception{
        String wentiji3 = "";
        String w[] = ku.split("fen!ge");
        if (w.length == 1) {
            wentiji3 = ku;
        } else if (w.length >= 10) {
            wentiji3 = muer(shuru, ku);
        } else {
            wentiji3 = ku;
        }
        int xiangcaiz = 0;
        int xiangcaijilu = -100000;
        String da = "";
        String ww[] = wentiji3.split("fen!ge");
        Random r = new Random();
        int xx = r.nextInt(ww.length);
        for (int i = 0; i < ww.length; i++) {
            int ia = xx + i;
            if (ia >= ww.length) {
                ia = ia - ww.length;
            }
            String iyv = ww[ia];
            String wentijilu = shuruchuli(iyv.split(",")[0], null, null);
            if (shuru.equals(wentijilu))//
            {
                da = iyv;
                break;
            } else {
                xiangcaiz = xiangcai(shuru, wentijilu);
                if (xiangcaiz < 1100000) {
//                    if (xiangcaiz >= 0) {//2016/10/27大禹治水
                        if (xiangcaiz > xiangcaijilu) {
                            xiangcaijilu = xiangcaiz;
                            da = iyv;
                        }
//                    }
                } else {
                    da = iyv;
                    break;
                }
            }

        }

        return da;
    }

    static String muer(String shuru, String wentiku) {
        String shuchu = "";
        int j = 0;
        String wentij[] = wentiku.split("fen!ge");
        for (int i = 0; i < wentij.length && j < 100; i++) {
            if (wentij[i] == " " || wentij[i].isEmpty()) {
                continue;
            }
            String str_shuru = wentij[i].split(",")[0];
            String wentijilu = shuruchuli(str_shuru, null, null);
            String wentiqc[] = wentijilu.split(" ");
            int n = 0;
            int d = 0;
            for (int c = 0; c < wentiqc.length; c++) {
                if (cizhi(wentiqc[c], "cixing") < 9) {
                    n++;
                    if (shuru.indexOf(wentiqc[c]) > -1) {
                        d++;
                    }
                }
            }
            if (n != 0 && n >= d && wentijilu.replace(" ", "").length() < shuru.replace(" ", "").length() * 1.5)// 从这里第一次找到的句子要包含输入所有的实词，因此找到的句子必然比输入句子所有的词的长度要大。但是有可能短于输入句子总长度，因为有虚词。
            {
                shuchu = shuchu + "fen!ge" + wentij[i];
                j++;
            } else if (shuru.equals(wentijilu)) {
                shuchu = shuchu + "fen!ge" + wentij[i];
                j++;
            }
        }
        if (shuchu.indexOf("fen!ge") > -1) {
            shuchu = shuchu.substring(6);
        }
        return shuchu;
    }

    static String suijidaan() {
        String da = "";
        int i = (int) Math.rint(Math.random() * 40);
        switch (i) {
            case 2:
                da = "也许我们可以聊点别的！比如，讲笑话？|5/";
                break;
            case 3:
                da = "要不我讲笑话给你听？可以跟我说“讲笑话”。|2/";
                break;
            case 10:
                da = "跟我说“播放音乐”看看。|2/";
                break;
            case 11:
                da = "这是什么意思？能换个说法吗？。|1/";
                break;
            case 12:
                da = "也许你换个说法我就懂了。|5/";
                break;
            case 14:
                da = "抱歉！也许你再说一遍我就听清了。|1/";
                break;
            case 15:
                da = "我是真的没听懂！也许你能换个说法。|1/";
                break;
            case 16:
                da = "不懂你在说什么？能不能换个说法！|2/";
                break;
            case 22:
                da = "我还是个小朋友，懂的不多。能不能换个说法？|9/";
                break;
            case 23:
                da = "我快被你搞崩溃了！|9/";
                break;
            case 24:
                da = "你想不想看我哭！我快被你搞哭了！|9/";
                break;
            case 27:
                da = "你是在故意为难我吗？我还是个孩子！|9/";
                break;
            default:
                da = "没听清。你能说清楚点吗？|2/";
        }
        return da;
    }

    static String suijiwenti() {
        String da = "";
        int i = (int) Math.rint(Math.random() * 40);
        switch (i) {
            case 1:
                da = "你是男生女生";
                break;
            case 2:
                da = "你好";
                break;
            case 3:
                da = "讲笑话";
                break;
            case 4:
                da = "性格内向的男生怎样找女朋友";
                break;
            case 5:
                da = "我喜欢你";
                break;
            case 6:
                da = "你结婚没";
                break;
            case 7:
                da = "你从哪里来";
                break;
            case 8:
                da = "你有几个男朋友";
                break;
            case 9:
                da = "你几岁了";
                break;
            case 10:
                da = "你多大了";
                break;
            case 11:
                da = "你是谁";
                break;
            case 12:
                da = "你都会什么";
                break;
            case 13:
                da = "你吃了吗";
                break;
            case 14:
                da = "你漂亮吗";
                break;
            case 15:
                da = "我是男人";
                break;
            case 16:
                da = "做我女朋友好吗";
                break;
            case 17:
                da = "怎么生个宝宝";
                break;
            case 18:
                da = "你妈妈是谁";
                break;
            case 19:
                da = "爸爸的爸爸是谁";
                break;
            case 20:
                da = "打你";
                break;
            case 21:
                da = "你有谈过男朋友吗";
                break;
            case 22:
                da = "你愿做我的女朋友吗";
                break;
            case 23:
                da = "你真笨";
                break;
            case 24:
                da = "你真聪明";
                break;
            case 25:
                da = "你是个傻子";
                break;
            case 26:
                da = "我讨厌你";
                break;
            case 27:
                da = "可以做我女友吗";
                break;
            case 30:
                da = "你有小三吗";
                break;
            default:
                da = "细痣疣螈是什么";

        }
        return da;
    }

    static String suijifanwen() {
        String da = "";
        int i = (int) Math.rint(Math.random() * 100);
        switch (i) {
            case 1:
                da = "要放音乐吗？";
                break;
            case 2:
                da = "要讲笑话吗？";
                break;
            case 3:
                da = "要看电视吗？";
                break;
            case 4:
                da = "你是女生吗？";
                break;
            case 5:
                da = "你是男生吗？";
                break;
            case 6:
                da = "你是中国人吗？";
                break;
            case 7:
                da = "你是机器人吗？";
                break;
            case 8:
                da = "什么动物喜欢吃鱼？";
                break;
            case 9:
                da = "什么动物喜欢吃老鼠？";
                break;
            case 10:
                da = "什么动物喜欢吃羊？";
                break;
            case 11:
                da = "什么动物喜欢吃骨头？";
                break;
            case 12:
                da = "什么动物喜欢吃谷物？";
                break;
            case 13:
                da = "什么动物喜欢吃草？";
                break;
            case 14:
                da = "什么动物喜欢吃虫子？";
                break;
            default:
                da = "";

        }
        return da;
    }

    private static boolean isNum(String str) {

        try {
            new BigDecimal(str);
            return true;
        } catch (Exception e) {
            return false;
        }
        // TODO 自动生成的方法存根

    }

    /**
     * 切此后进行关键字计算 对应到五个关键字中
     * @param a
     * @return
     */
    public static String guanjianzijs(String a){
        String fanhui = null;
        String a1 = "k$z";
        String a2 = "k$z";
        String a3 = "k$z";
        String a4 = "k$z";
        String b1 = "k$z";
        String b2 = "k$z";
        String b3 = "k$z";
        String b4 = "k$z";
        String b5 = "k$z";
        boolean qingtaidongci=true;
        b3=g(a,3);//谓语动词
        if (b3.equals("k$z"))
        {
            b3=g(a,5);//情态动词做谓语
        }
        if (b3.equals("k$z"))
        {
            b1=g(a,1);//主语名词人名
            if (b1.equals("k$z"))
            {
                b1=g(a,2);//主语名词事物
            }
            a1=a.replace(b1,"");
            b2=g(a1,2);//宾语名词
            a3=a.replace(b2,"");
        }
        else
        {
            a1=a.substring(0,a.indexOf(b3));
            a1=a1.trim();
            a2=a.substring(a.indexOf(b3)+b3.length());
            if (qingtaidongci)
            {
                a2=b3+a2;
                b3="";
            }
            //2017年5月10新加
            a2=a2.trim();
            b1=g(a1,1);//主语名词人名
            if (b1.equals("k$z"))
            {
                b1=g(a1,2);//主语名词事物
            }
            if (!a2.equals(""))
            {
                b2=g(a2,1);//宾语名词人名
                if (b2.equals("k$z"))
                {
                    b2=g(a2,2);//宾语名词事物
                }
            }
            else
            {
                b2 = "k$z";
            }
            a3=a1.replace(b1,"")+ " " +a2.replace(b2,"");
            a3=a3.trim();
        }
        b4=g(a3,3);
        if (b4 == "k$z")
        {b4=g(a3,4);}
        a4=a3.replace(b4,"");
        a4=a4.trim();
        b5=g(a4,5);

        if (b1==""){b1 = "k$z";}
        if (b2==""){b2 = "k$z";}
        if (b3==""){b3 = "k$z";}
        if (b4==""){b4 = "k$z";}
        if (b5==""){b5 = "k$z";}
        fanhui=b1+","+b2+","+b3+","+b4+","+b5;
        return fanhui;
    }
    public static String  g(String a ,int n){
        String guanjianzifanhui="k$z";
        String gjz="k$z";
        String aa[]=a.split(" ");
        int cizhi=0;
        int cixing=0;
        int cizhijilu=0;
        if (n==1)//找主语名词
        {
            for (int k=aa.length-1;k>-1;k--){
                cixing=cizhi(aa[k],"cixing");
                if (cixing==1)
                {
                    gjz=aa[k];
                    break;
                }
            }
        }
        else if (n==2)//找宾语名词
        {
            int cizhiA=0;
            int cizhiB=0;
            for (int k=0;k<aa.length;k++){//5月10日修改
                cixing=cizhi(aa[k],"cixing");
                cizhiA=cizhi(aa[k],"zhi");
                if (cixing<3 && cizhiA>cizhiB )
                {
                    gjz=aa[k];
                    cizhiB=cizhiA;
                }
            }
        }
        else if (n==3)//找谓语动词
        {
            for (int k=0;k<aa.length;k++){
                cixing=cizhi(aa[k],"cixing");
                if  (cixing==3)
                {
                    gjz=aa[k];
                    break;
                }
            }
        }
        else if (n==4)//找形容词，副词等
        {
            for (int k=0;k<aa.length;k++){
                cizhi=cizhi(aa[k],"zhi");
                cixing=cizhi(aa[k],"cixing");
                if (cizhi>cizhijilu && cixing<9 )
                {
                    gjz=aa[k];
                    cizhijilu=cizhi;
                }
            }
        }
        else if (n==5)//找情态动词2017年5月10新加
        {
            for (int k=0;k<aa.length;k++){
                cixing=cizhi(aa[k],"cixing");
                if  (cixing==4)
                {
                    gjz=aa[k];
                    break;
                }
            }//找情态动词2017年5月10新加
        }
        else//找句子的其他成分
        {
            for (int k=0;k<aa.length;k++){
                cizhi=cizhi(aa[k],"zhi");
                cixing=cizhi(aa[k],"cixing");
                if (cizhi>cizhijilu )
                {
                    gjz=aa[k];
                    cizhijilu=cizhi;
                }
            }
        }
        if (!gjz.equals(""))
        {guanjianzifanhui=gjz;}
        else
        {guanjianzifanhui="k$z";}
        return guanjianzifanhui;
    }
/*    public static String guanjianzijs(String a) {//20170511
        String fanhui = null;
        String a1 = "k$z";
        String a2 = "k$z";
        String a3 = "k$z";
        String a4 = "k$z";
        String b1 = "k$z";
        String b2 = "k$z";
        String b3 = "k$z";
        String b4 = "k$z";
        String b5 = "k$z";
        b3 = g(a, 3);//谓语动词
        if (b3.equals("k$z"))
        {
            b1=g(a,1);//主语名词
            if (b1.equals("k$z"))
            {
                b1=g(a1,2);//主语名词事物
            }
            a1=a.replace(b1,"");
            b2=g(a1,2);//宾语名词
            a3=a1.replace(b2,"");
        }else {
            a1 = a.substring(0, a.indexOf(b3));
            a1 = a1.trim();
            a2 = a.substring(a.indexOf(b3) + b3.length());
            a2 = a2.trim();
            b1 = g(a1, 1);//主语名词人名
            if (b1.equals("k$z")) {
                b1 = g(a1, 2);//主语名词事物
            }
            if (!a2.equals("")) {

                b2 = g(a2, 1);//宾语名词人名
                if (b2.equals("k$z")) {
                    b2 = g(a2, 2);//宾语名词事物
                }
            } else {
                b2 = "k$z";
            }
            a3 = a1.replace(b1, "") + " " + a2.replace(b2, "");
            a3 = a3.trim();
        }
        b4 = g(a3, 3);
        if (b4 == "k$z") {
            b4 = g(a3, 4);
        }
        a4 = a3.replace(b4, "");
        a4 = a4.trim();
        b5 = g(a4, 5);

        if (b1 == "") {
            b1 = "k$z";
        }
        if (b2 == "") {
            b2 = "k$z";
        }
        if (b3 == "") {
            b3 = "k$z";
        }
        if (b4 == "") {
            b4 = "k$z";
        }
        if (b5 == "") {
            b5 = "k$z";
        }
        fanhui = b1 + "," + b2 + "," + b3 + "," + b4 + "," + b5;
        return fanhui;
    }
    public static String  g(String a ,int n){
        String guanjianzifanhui="k$z";
        String gjz="k$z";
        String aa[]=a.split(" ");
        int cizhi=0;
        int cixing=0;
        int cizhijilu=0;
        if (n==1)//找主语名词
        {
            for (int k=aa.length-1;k>-1;k--){
                cixing=cizhi(aa[k],"cixing");
                if (cixing==1)
                {
                    gjz=aa[k];
                    break;
                }
            }
        }
        else if (n==2)//找宾语名词
        {
            int cizhiA=0;
            int cizhiB=0;
            for (int k=aa.length-1;k>-1;k--){
                cixing=cizhi(aa[k],"cixing");
                cizhiA=cizhi(aa[k],"zhi");
                if (cixing<3 && cizhiA>cizhiB )
                {
                    gjz=aa[k];
                    cizhiB=cizhiA;
                }
            }
        }
        else if (n==3)//找谓语动词
        {
            for (int k=0;k<aa.length;k++){
                cixing=cizhi(aa[k],"cixing");
                if  (cixing==3)
                {
                    gjz=aa[k];
                    break;
                }
            }
        }
        else if (n==4)//找形容词，副词等
        {
            for (int k=0;k<aa.length;k++){
                cizhi=cizhi(aa[k],"zhi");
                cixing=cizhi(aa[k],"cixing");
                if (cizhi>cizhijilu && cixing<9 )
                {
                    gjz=aa[k];
                    cizhijilu=cizhi;
                }
            }
        }
        else//找句子的其他成分
        {
            for (int k=0;k<aa.length;k++){
                cizhi=cizhi(aa[k],"zhi");
                cixing=cizhi(aa[k],"cixing");
                if (cizhi>cizhijilu )
                {
                    gjz=aa[k];
                    cizhijilu=cizhi;
                }
            }
        }
        if (!gjz.equals(""))
        {guanjianzifanhui=gjz;}
        else
        {guanjianzifanhui="k$z";}
        return guanjianzifanhui;
    }*/

    public static String guanjianzibuquan(String a, String b) {
        String jieguo = "";
        String ga[] = a.split(",");// 关键字分析
        String gb[] = b.split(",");// 关键字分析
        if (ga[0].equals("k$z") && !gb[0].equals("k$z")) {
            a = gb[0];
        } else {
            a = ga[0];
        }
        if (ga[1].equals("k$z") && !gb[1].equals("k$z")) {
            a = a + "," + gb[1];
        } else {
            a = a + "," + ga[1];
        }
        if (ga[2].equals("k$z") && !gb[2].equals("k$z")) {
            a = a + "," + gb[2];
        } else {
            a = a + "," + ga[2];
        }
        a = a + "," + ga[3] + "," + ga[4];
        jieguo = a;
        return jieguo;
    }

    public static String buquanjvzi(String aj, String bj, String ag, String bg, int n) {
        String jieguo = "";
        String jieguo1 = aj;
        String ga[] = ag.split(",");// 关键字分析
        String gb[] = bg.split(",");// 关键字分析
        if (n == 1) {
            if (!gb[1].equals("k$z") && ga[1].equals("k$z")) {
                jieguo1 = gb[1] + " " + jieguo1;
            }
        } else if (n == 0) {
            if (!gb[0].equals("k$z") && ga[0].equals("k$z")) {
                jieguo1 = gb[0] + " " + jieguo1;
            }
        } else if (n == 2) {
            if (!gb[0].equals("k$z") && !gb[1].equals("k$z") && ga[0].equals("k$z")) {
                jieguo1 = gb[0] + " " + gb[1] + " " + jieguo1;
            }
        } else {
            jieguo = jieguo1;
        }
        jieguo = jieguo1;
        jieguo = jieguo.trim();
        return jieguo;
    }

    public static void main(String[] args) throws Exception {
        // TODO 自动生成的方法存根
        /* System.out.println(cizhi("打","zhi"));*/
        /*System.out.print(kouyu("你们公司在哪儿能告诉我!"));请你给我介绍下*/
        /*System.out.println(yuyixiangsijs("有客云的产品价格是多少","有客云的价格fen!ge有客云价格"));*/
//        System.out.println(tongyichuli("你好"));
        /*System.out.println(qiezi("我的哥哥是的哥"));*/
        /*System.out.println("结果：" + qieci("我想听年度之歌", "{\"gjz1\":\"下雨\",\"gjz2\":\"听\",\"gjz3\":\"我想\",\"gjz4\":\"k$z\",\"gjz5\":\"k$z\"}", "音乐播放"));*/
//        System.out.println(shuruchuli("我想听年度之歌", "{\"gjz1\":\"下雨\",\"gjz2\":\"听\",\"gjz3\":\"我想\",\"gjz4\":\"k$z\",\"gjz5\":\"k$z\"}", "音乐播放"));
//        System.out.println(qieci("有客云的产品价格是多少",null,null));
        System.out.println(xiangcai("有 客 云 的 产品价格 是 多少","有客云价格"));
    }

    public static String shengluebuquan(String aj, String bj) {
        String jieguo = "";

        String gb[] = bj.split(",");// 关键字分析
        String ga[] = aj.split(",");// 关键字分析
        for (int g = 0; g < 5; g++) {
            if (!ga[g].equals("k$z")) {
                jieguo = jieguo + " " + ga[g];
            } else {
                if (!gb[g].equals("k$z")) {
                    jieguo = jieguo + " " + gb[g];
                }
            }

        }
        jieguo = jieguo.trim();
        return jieguo;
    }

    public static double shengluepanduan(String a, String b) {
        // TODO 自动生成的方法存根
        double jieguo = 0;
        String ga[] = a.split(",");// 关键字分析
        String gb[] = b.split(",");// 关键字分析
        for (int ab = 0; ab < ga.length; ab++) {
            if ((ga[0].equals("k$z") && gb[0].equals("k$z")) || (!ga[0].equals("k$z") && !gb[0].equals("k$z"))) {
                jieguo = jieguo + 1;
            }
        }

        jieguo = jieguo / ga.length;
        return jieguo;
    }

    public static boolean shengluepanduan1(String a, String b) {
        // TODO 自动生成的方法存根
        boolean jieguo = false;
        String ga[] = a.split(",");// 关键字分析
        String gb[] = b.split(",");// 关键字分析
        for (int aa = 0; aa < 4; aa++) {
            if (ga[aa].equals(gb[aa])) {
                jieguo = true;
            } else {
                if (ga[aa].equals("k$z")) {
                    jieguo = true;
                } else {
                    if (aa == 1 && !gb[aa].equals("k$z")) {
                        jieguo = true;
                    } else {
                        jieguo = false;
                        break;
                    }
                }
            }
        }
        return jieguo;
    }

    public static String wendabuquan(String wenti, String dg1) {
        // TODO 自动生成的方法存根
        String jieguo1 = "";
        jieguo1 = wenti + dg1;
        jieguo1 = jieguo1.replace(",", "");
        jieguo1 = jieguo1.replace("k$z", "");
        jieguo1 = jieguo1.replace(" ", "");
        jieguo1 = shuruchuli(jieguo1, null, null);
        jieguo1 = guanjianzijs(jieguo1);
        jieguo1 = jieguo1.replace(",", "");
        jieguo1 = jieguo1.replace("k$z", "");
        jieguo1 = shuruchuli(jieguo1, null, null);
        return jieguo1;
    }

    public static boolean xiangsipd(String d1, String d2) {
        boolean bbb = false;
        String g1[] = d1.split(",");
        String g2[] = d2.split(",");
        for (int i = 0; i < 4; i++) {
            if (g1[i] == g2[i] && !g2[i].equals("k$z")) {
                bbb = true;
                break;
            }
        }
        return bbb;
    }

    public static boolean shangjvpd(String d2) {
        boolean bbb = false;
        String g2[] = d2.split(",");
        if (!g2[0].equals("k$z") || !g2[1].equals("k$z") && !g2[2].equals("k$z")) {
            bbb = true;
        } else {
            bbb = false;
        }
        return bbb;
    }

    public static String shangjvgjzjs(String d1) {
        String bbb = "";
        String[] ccc1 = new String[]{"。", "？", "！"};

        String d2 = "";
        for (int in = 0; in < ccc1.length; in++) {
            d1 = d1.replace(ccc1[in], "as1df");

        }
        d1 = d1.trim();
        if (d1.length() > 0 && d1.substring(d1.length() - 1).equals("as1df")) {
            d1 = d1.substring(0, d1.length() - 1);
        }
        if (d1.indexOf("as1df") > -1) {
            String ccc2[] = d1.split("as1df");
            bbb = ccc2[ccc2.length - 1];
        } else {
            bbb = d1;
        }
        if (!bbb.equals("")) {
            d2 = bbb.replace("你", "wo1");
            d2 = d2.replace("我", "ni1");
            d2 = d2.replace("wo1", "我");
            d2 = d2.replace("ni1", "你");
            d2 = shuruchuli(d2, null, null);
            d2 = guanjianzijs(d2);
        }
        return d2;
    }

    public static String daichichuli(String wenti1, String a10, String a11) {
        // TODO 自动生成的方法存根
        if (wenti1.indexOf("他 ") > -1 || wenti1.indexOf("她 ") > -1 || wenti1.indexOf(" 他") > -1
                || wenti1.indexOf(" 她") > -1) {
            if (wenti1.indexOf(a10) > -1) {
                wenti1 = wenti1.replace(a10, "");
            }
            wenti1 = wenti1.replace("他 ", a10 + " ");
            wenti1 = wenti1.replace("她 ", a10 + " ");
            wenti1 = wenti1.replace(" 他", " " + a10);
            wenti1 = wenti1.replace(" 她 ", " " + a10);
        } else if (wenti1.indexOf("它 ") > -1 || wenti1.indexOf(" 它") > -1) {
            if (wenti1.indexOf(a10) > -1) {
                wenti1 = wenti1.replace(a10, "");
            }
            wenti1 = wenti1.replace("它 ", a11 + " ");
            wenti1 = wenti1.replace(" 它", " " + a11);
        } else if (wenti1.indexOf("这 ") > -1 || wenti1.indexOf("那 ") > -1) {
            if (wenti1.indexOf(a11) > -1) {
                wenti1 = wenti1.replace(a11, "");
            }
            wenti1 = wenti1.replace("这 ", a11 + " ");
            wenti1 = wenti1.replace("那 ", a11 + " ");
        }

        return wenti1;
    }

    public static String zhuyujs(String gjz1) {
        String g1[] = gjz1.split(",");
        if (!g1[0].equals("k$z") && g1[1].equals("k$z")) {
            gjz1 = g1[0];
        } else if (g1[0].equals("k$z") && !g1[1].equals("k$z")) {
            gjz1 = g1[1];
        } else if (!g1[0].equals("k$z") && !g1[1].equals("k$z")) {
            gjz1 = g1[0] + g1[1];
        } else {
            gjz1 = "";
        }
        return gjz1;
    }

    public static String binyujs(String gjz1) {
        String g1[] = gjz1.split(",");
        if (!g1[0].equals("k$z") && !g1[1].equals("k$z")) {
            gjz1 = g1[1];
        } else if (!g1[1].equals("k$z") && g1[0].equals("k$z")) {
            if (cizhi(g1[3], "cixing") < 3) {
                gjz1 = g1[3];
            } else if (cizhi(g1[4], "cixing") < 3) {
                gjz1 = g1[4];
            } else {
                gjz1 = "";
            }
        } else {
            gjz1 = "";
        }
        return gjz1;
    }

    public static String bypaixu(String a) {
        String da = "";
        String jilus[] = a.split("fen!ge");
        String jilupx = "";
        String ps = "";
        String psg = "";
        String psb = "";
        int pbyz = 0;
        for (int lk = 0; lk < jilus.length; lk++) {
            ps = jilus[lk];
            psg = shuruchuli(ps, null, null);
            psg = guanjianzijs(psg);
            psb = binyujs(psg);
            pbyz = cizhi(psb, "zhi");
            jilupx = jilupx + "f！g" + ps + "," + pbyz;
        }
        jilupx = jilupx.substring(3);
        jilupx = jilupx.trim();
        if (!jilupx.equals("")) {
            String[] jilupx1 = jilupx.split("f！g");
            int niu = 0;
            int niu1 = 0;
            int niu2 = 200;
            for (int px = 0; px < jilupx1.length; px++) {
                String[] chongxin = jilupx1[px].split(",");
                niu = Integer.parseInt(chongxin[1]);
                if (niu > niu1) {
                    niu1 = niu;
                }
            }
            for (int px = 0; px < jilupx1.length; px++) {
                String[] chongxin = jilupx1[px].split(",");
                niu = Integer.parseInt(chongxin[1]);
                if (niu < niu2) {
                    niu2 = niu;
                }
            }
            niu = niu1;
            while (niu > niu2 - 1) {

                for (int px = 0; px < jilupx1.length; px++) {
                    String[] chongxin = jilupx1[px].split(",");
                    int niu3 = Integer.parseInt(chongxin[1]);
                    if (niu3 == niu) {
                        da = da + "fen!ge" + chongxin[0];

                    }
                }
                niu--;
            }

        }
        return da;
    }
}
