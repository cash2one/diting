package com.diting.service.yuyi;

import com.diting.model.WordBase;

import java.util.List;

public class YuyijsScene {
    /*static String qiezi(String a,String scene) {
        String jieguo = "";
        String jieguoa = "";
        String jieguob = "";
        String jieguoc = "";
        int jvzizhia = 0;
        int jvzizhib = 0;
        int jvzizhic = 0;
        if (a != "") {
            String reg = "[^\u4e00-\u9fa5\\sa-zA-Z0-9]";
            String d = a.replaceAll(reg, "");
            String d1 = d;
            String d2 = d;
            String c = "";
            String cx = "";
            String cxx = "";
            String jieguo1 = "";
            //正向计算
            while (!d.equals("") && !d.equals(null)) {
                c = shouci(d,scene);
                if (c !=""&&c.length()>0) {
                    d = d.substring(c.length(), d.length());    //这里去掉首词
                    jieguo1 = jieguoa;
                    jieguoa = jieguoa + " " + c;
                } else {
                    cx = shoucibushi(d,scene);
                    if (cx != ""&&cx.length()>0) {
                        if (cx != "" && c != "") {
                            cxx = bushijiaodui(cx, c,scene);
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
                            jieguoa = jieguoa + cx;
                        }
                    }
                    else
                    {
                        d="";
                    }
                }
            }
            jieguoa = jieguoa.trim();
            String[] jieguoA = jieguoa.split(" ");

            for (int i = 0; i < jieguoA.length; i++) {
                jvzizhia += YuyijsScene.cizhi(jieguoA[i], "zhi",scene);
            }

            //反向计算
            while (!d1.equals("") && !d1.equals(null)) {
                c = weici(d1,scene);
                if (!c.equals("") && !c.equals(null)) {
                    d1 = d1.substring(0, d1.length() - c.length());    //这里去掉尾词
                    jieguo1 = jieguob;
                    jieguob = c + " " + jieguob;
                } else {
                    cx = weicibushi(d1,scene);
                    if (cx != "") {
                        if (cx != "" && c != "") {
                            cxx = weicibushijiaodui(cx, c,scene);
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
                            jieguob = cx + jieguob;
                        }
                    }
                    else
                    {
                        d1="";
                    }
                }
            }

            jieguob = jieguob.trim();

            String[] jieguoB = jieguob.split(" ");

            for (int i = 0; i < jieguoB.length; i++) {
                jvzizhib += YuyijsScene.cizhi(jieguoB[i], "zhi",scene);
            }

            //正向简便计算
            while (!d2.equals("") && !d2.equals(null)) {
                c = shouci(d2,scene);
                if (c != "") {
                    d2 = d2.substring(c.length(), d2.length());    //这里去掉首词
                    jieguo1 = jieguoc;
                    jieguoc = jieguoc + " " + c;
                } else {
                    cx = shoucibushi(d2,scene);
                    if (cx != ""&&cx.length()>0)
                    {
                        d2 = d2.substring(cx.length(), d2.length());    //这里去掉首词
                        jieguoc = jieguoc + cx;
                    }
                    else
                    {
                        d2="";
                    }
                }
            }
            jieguoc = jieguoc.trim();
            String[] jieguoC = jieguoc.split(" ");

            for (int i = 0; i < jieguoC.length; i++) {
                jvzizhic += YuyijsScene.cizhi(jieguoC[i], "zhi",scene);
            }


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
    }*/
    static String qiezi(String a, String scene) {
        String jieguo = "";
        String jieguoa = "";
        String jieguob = "";
        String jieguoc = "";
        String jieguod = "";
        int jvzizhia = 0;
        int jvzizhib = 0;
        int jvzizhic = 0;
        int jvzizhid = 0;
        int sizea=0;
        int sizeb=0;
        int sizec=0;
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
            int cizhiResult=0;
            //正向计算
            while (!d.equals("") && !d.equals(null)) {
                c = shouci(d,scene);
                if (c != "" && c.length() > 0) {
                    d = d.substring(c.length(), d.length());    //这里去掉首词
                    jieguo1 = jieguoa;
                    jieguoa = jieguoa + " " + c;
                } else {
                    cx = shoucibushi(d,scene);
                    if (cx != "" && cx.length() > 0) {
                        if (cx != "" && c != "") {
                            cxx = bushijiaodui(cx, c,scene);
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
            for (int i=0;i<jieguoA.length;i++)
            {
                cizhiResult=newCizhi(jieguoA[i],"cixing",scene);
                if 	(cizhiResult<9)//2016/11/13
                {
                    jvzizhia+=newCizhi(jieguoA[i],"zhi",scene);
                    if(cizhiResult==3||cizhiResult==4){
                        jvzizhia+=20;
                    }
                }//2016/11/13
                sizea++;
            }
            jvzizhia=jvzizhia-sizea*30;
            //反向计算
            while (!d1.equals("") && !d1.equals(null)) {
                c = weici(d1,scene);
                if (!c.equals("") && !c.equals(null)) {
                    d1 = d1.substring(0, d1.length() - c.length());    //这里去掉尾词
                    jieguo1 = jieguob;
                    jieguob = c + " " + jieguob;
                } else {
                    cx = weicibushi(d1,scene);
                    if (cx != "") {
                        if (cx != "" && c != "") {
                            cxx = weicibushijiaodui(cx, c,scene);
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
            for (int i=0;i<jieguoB.length;i++)
            {
                cizhiResult=newCizhi(jieguoB[i],"cixing",scene);
                if 	(cizhiResult<9)//2016/11/13
                {
                    jvzizhib+=newCizhi(jieguoB[i],"zhi",scene);
                    if(cizhiResult==3||cizhiResult==4){
                        jvzizhib+=20;
                    }
                }//2016/11/13
                sizeb++;
            }
            jvzizhib=jvzizhib-sizeb*30;
            //正向简便计算
            while (!d2.equals("") && !d2.equals(null)) {
                c = shouci(d2,scene);
                if (c != "") {
                    d2 = d2.substring(c.length(), d2.length());    //这里去掉首词
                    jieguo1 = jieguoc;
                    jieguoc = jieguoc + " " + c;
                } else {
                    cx = shoucibushi(d2,scene);
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
            for (int i=0;i<jieguoC.length;i++)
            {
                cizhiResult=newCizhi(jieguoC[i],"cixing",scene);
                if 	(cizhiResult<9)//2016/11/13
                {
                    jvzizhic+=newCizhi(jieguoC[i],"zhi",scene);
                    if(cizhiResult==3||cizhiResult==4){
                        jvzizhic+=20;
                    }
                }//2016/11/13
                sizec++;
            }
            jvzizhic=jvzizhic-sizec*30;
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
        return jieguo;
    }
    /**
     * 根据场景计算值
     * @param a
     * @param b
     * @param scene
     * @return
     */
    static int cizhi(String a, String b,String scene) {
        List<WordBase> wlist = Yuyijs.mlWordBase.get(a);
        int zhi = 10;
        if (wlist != null) {
            int i1 = 0;
            int i2 = 0;
            /*//输入场景不为空 且 输入场景与切词场景一致 计算词值
            if(scene!=null&&scene!=""&&scene.equals(wlist.get(0).getScene())){
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
            //词的场景为空 计算词值
            if (wlist.get(0).getScene()==null||wlist.get(0).getScene()=="") {
                i1 = wlist.get(0).getWord_character();
                i2 = wlist.get(0).getWord_level();
                if (b == "cixing") {
                    zhi = i1;
                } else if (b == "zhi") {
                    zhi = (11 - i1) * 10 + (11 - i2);
                } else {
                    zhi = i2;
                }
            }*/
            i1 = wlist.get(0).getWord_character();
            i2 = wlist.get(0).getWord_level();
            if (b == "cixing") {
                zhi = i1;
            } else if (b == "zhi") {
                /*zhi = (11 - i1) * 10 + (11 - i2);*///20170511
                zhi=((11-i1)*10+(11-i2))*10+a.length();
            } else {
                zhi = i2;
            }
        }
        return zhi;
    }

    static String shouci(String a,String scene) {
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
               if (YuyijsScene.newCizhi(b, "cixing",scene) < 10) {
                   jieguo = b;
                   break;
               }
           }
       }
       return jieguo;
   }

    static String weici(String a,String scene) {
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
                if (YuyijsScene.newCizhi(b, "cixing",scene) < 10) {
                    jieguo = b;
                    break;
                }
            }
        }
        return jieguo;
    }

    static String shoucibushi(String a,String scene) {//这里输出生词
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
                b = shouci(c,scene);
                if (YuyijsScene.newCizhi(b, "cixing",scene) != 10) {
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

    static String weicibushi(String a,String scene) {//这里输出生词
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
                b = weici(c,scene);
                if (YuyijsScene.newCizhi(b, "cixing",scene) != 10) {
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

    public static String bushijiaodui(String cx, String c,String scene) {
        String d = "";
        String e = "";
        String f = "";
        String jieguo = "";
        for (int i = 1; i < c.length(); i++) {
            d = c.substring(i);
            f = c.substring(0, i);
            e = d + cx;
            if (YuyijsScene.newCizhi(e, "cixing",scene) < 10 && YuyijsScene.newCizhi(f, "cixing",scene) < 10) {
                jieguo = f;
                break;
            }
        }

        return jieguo;
    }

    public static String weicibushijiaodui(String cx, String c,String scene) {
        String d = "";
        String e = "";
        String f = "";
        String jieguo = "";
        for (int i = 1; i < c.length(); i++) {
            d = c.substring(i);
            f = c.substring(0, i);
            e = d + cx;
            if (YuyijsScene.newCizhi(e, "cixing",scene) < 10 && YuyijsScene.newCizhi(f, "cixing",scene) < 10) {
                jieguo = f;
                break;
            }
        }

        return jieguo;
    }

    public static int  newCizhi(String a,String b,String scene){
        int n=10;
        List<WordBase> wlist = Yuyijs.mlWordBase.get(a);
        if(wlist!=null){
            if(wlist.get(0).getScene()!=null&&wlist.get(0).getScene()!=""&&wlist.get(0).getScene().length()>0&&!wlist.get(0).getScene().equals(scene)){
                n=10;
            }else{
                n=cizhi(a,b,scene);
            }
        }
        return n;
    }
}
