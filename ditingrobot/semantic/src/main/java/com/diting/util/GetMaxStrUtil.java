package com.diting.util;

/**
 * Created by Administrator on 2017/3/24.
 */
public class GetMaxStrUtil {

    public static String getMaxString(String[] strs){
        int max=1;
        String result=null;
//        String[] strs={"a","b","f","a","d","c","e","d","a","d"};
        for(int i=0;i<strs.length-1;i++)
        {
            int count=1;
            for(int j=i+1;j<strs.length;j++)
            {
                if(strs[i].equals(strs[j]))
                    count++;
            }
            if(max<count)
                max=count;
        }
        for(int i=0;i<strs.length-1;i++)
        {
            int count=1;
            for(int j=i+1;j<strs.length;j++)
            {
                if(strs[i].equals(strs[j]))
                    count++;
            }
            if(count==max){
                result= strs[i];
                break;
            }
        }
        return result;
    }

    public static void main(String[] args)
    {
        int i=0;
        i++;
        System.out.println(i);
//        int max=1;
//        String[] strs={"a","b","f","a","d","c","e","d","a","d"};
//        for(int i=0;i<strs.length-1;i++)
//        {
//            int count=1;
//            for(int j=i+1;j<strs.length;j++)
//            {
//                if(strs[i].equals(strs[j]))
//                    count++;
//            }
//            if(max<count)
//                max=count;
//        }
//        System.out.println("重复最多次数为："+max);
//        for(int i=0;i<strs.length-1;i++)
//        {
//            int count=1;
//            for(int j=i+1;j<strs.length;j++)
//            {
//                if(strs[i].equals(strs[j]))
//                    count++;
//            }
//            if(count==max)
//                System.out.println("重复最多次("+max+")的字符串为："+strs[i]);
//        }
    }
}
