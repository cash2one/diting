package com.diting.service.impl;

import com.diting.service.PinYinHuaService;
import com.diting.util.PinYinUtil;
import org.springframework.stereotype.Service;

/**
 * Created by Administrator on 2017/3/21.
 */
@Service
public class PinYinHuaServiceImpl implements PinYinHuaService{
    /**
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {



        String A="k$z,k$z,k$z,天京,k$z;k$z,k$z,k$z,k$z,天津;k$z,k$z,天津,k$z,k$z;天津,k$z,k$z,k$z,k$z;k$z,k$z,k$z,k$z,k$z";
        String B="k$z,k$z,k$z,k$z,天津";

        System.out.println("相似："+new PinYinHuaServiceImpl().gjzyyxiangsizhi(A,B));

        // TODO 自动生成的方法存根

    }
    //这个函数计算关键字语音相似
    @Override
    public	String gjzyyxiangsizhi(String A,String B) {
        //A="gjz1,gjz2,gjz3,gjz4,gjz5;gjz1,gjz2,gjz3,gjz4,gjz5;gjz1,gjz2,gjz3,gjz4,gjz5";A里面包含最少一个最多不限关键词组合，从数据库里相同的场景离取出所有数据
        //B="gjz1,gjz2,gjz3,gjz4,gjz5";B为本句输入的关键词，词之间用,分开。
        String jl="";
        String aa[]=A.split(";");
        double zuida=0;
        double zuidajl=0;
        for (int i=0;i<aa.length;i++)
        {
            try {
                zuida=	gjzxiangsizhi1(aa[i],B);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (zuidajl<zuida && zuida>0.8)
            {
                zuidajl=zuida;
                jl=aa[i];
            }
        }
        return jl;
    }
    public	static double gjzxiangsizhi1(String A,String B) throws Exception{
        //A="gjz1,gjz2,gjz3,gjz4,gjz5";
        //B="gjz1,gjz2,gjz3,gjz4,gjz5";B最多是两个词，词之间用空格分开，空值用“k$z”表示。
        double xiangsizhi=0;
        A=A.trim();
        B=B.trim();
        String a1[]=A.split(",");
        String b1[]=B.split(",");
        int lll=b1.length;
        if (a1.length<b1.length )
        {
            lll=a1.length;
        }

        double cs=0;
        for (int i=0;i<lll;i++)
        {
            if(!a1[i].equals("k$z") &&  !b1[i].equals("k$z")  )
            {
                xiangsizhi=xiangsizhi+pyxiangsizhi(a1[i],b1[i]);
                cs++;
            }
            else if(i==0 && (!a1[i].equals("k$z") &&  !b1[1].equals("k$z")) )
            {
                xiangsizhi=xiangsizhi+pyxiangsizhi(a1[i],b1[1]);
                cs++;
            }
            else if (i==1 && (!a1[i].equals("k$z") &&  !b1[1].equals("k$z")))
            {
                xiangsizhi=xiangsizhi+pyxiangsizhi(a1[i],b1[2]);
                cs++;
            }
            else if(i==2 && (!a1[i].equals("k$z") &&  !b1[3].equals("k$z")) )
            {
                xiangsizhi=xiangsizhi+pyxiangsizhi(a1[i],b1[3]);
                cs++;
            }
            else if (i==3 && (!a1[i].equals("k$z") &&  !b1[4].equals("k$z")))
            {
                xiangsizhi=xiangsizhi+pyxiangsizhi(a1[i],b1[4]);
                cs++;
            }
            else if (i==3 && (!a1[i].equals("k$z") &&  !b1[2].equals("k$z")))
            {
                xiangsizhi=xiangsizhi+pyxiangsizhi(a1[i],b1[2]);
                cs++;
            }
            else if (i==4 && (!a1[i].equals("k$z") &&  !b1[3].equals("k$z")))
            {
                xiangsizhi=xiangsizhi+pyxiangsizhi(a1[i],b1[3]);
                cs++;
            }
        }
        if (cs!=0){
            xiangsizhi=xiangsizhi/cs;
        }
        return xiangsizhi;
    }

    public	static double pyxiangsizhi(String A,String B) throws Exception{
        double xiangsizhi=0;
        A=pinyin(A);
        B=pinyin(B);
        String L;
        String S;
        L=B;
        S=A;
        if (A.length()>B.length())
        {
            L=A;
            S=B;
        }
        double M=1;
        for (int i=0;i<S.length();i++)
        {
            String P=S.substring(i,i+1);
            if (L.indexOf(P)>0)
            {
                M=M+1;
            }
        }
        xiangsizhi=M/L.length();

        return xiangsizhi;
    }

    public	static String pinyin (String A) throws Exception{
        String shuchu=A;
        PinYinUtil py = new PinYinUtil();
        shuchu=py.getPinyin(shuchu);
        return	shuchu;

    }
}
