
/**
 * @Title: SensitiveWordFilter.java
 * @Package d
 * @Description: TODO
 * @author liufei
 * @date 2016年12月12日
 */
package com.diting.sensitivewords;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;

import static com.diting.util.Utils.isEmpty;

/**
 * @author liufei
 * @date 2016年12月12日
 **/

public class SensitiveWordFilter {

    private int num; // 保存存在多少个敏感词数量
    private HashSet<String> sensitiwordHashSet;//要查找的敏感词set
    private Map sensitiveHashMap;        //用敏感词构造的hashmap
    private HashSet<String> foundSensitiveWordSet; // 用于保存已经发现的敏感词


    /**
     * @param
     * @return void
     * @throws
     * @Description: TODO
     */
    public void initFilter() {
        SensitiveWord instanceSensitiveWord = SensitiveWord.getInstance();
        instanceSensitiveWord.init();
        sensitiveHashMap = instanceSensitiveWord.getSensitiveHashMap();
        sensitiwordHashSet = instanceSensitiveWord.getSensitiwordHashSet();
        foundSensitiveWordSet = new HashSet<String>();
    }

    /**
     * @param
     * @return void
     * @throws
     * @Description: 遍历待处理文档中的每个字开头到最后，与敏感字构成的hashmap进行匹对
     */
    public void findSensitiveWordInTxt(String txtString) {
        for (int i = 0; i < txtString.length(); i++) {
            if (isContainSensitiveWord(i, txtString))
                num++;
        }
    }

    /**
     * @param
     * @return void
     * @throws
     * @Description: TODO
     */
    public String printResult() {
//        System.out.println("文档中，有" + num + "个敏感词  :  ");
        Iterator<String> iterator = foundSensitiveWordSet.iterator();
        String string = new String();
        while (iterator.hasNext()) {
            String string1 = iterator.next();
            string = string + string1 + ",";
        }
        return !isEmpty(string) ? string.substring(0, string.length() - 1) : null;
    }

    public String printResult(String string) {
//        System.out.println("文档中，有" + num + "个敏感词  :  ");
        Iterator<String> iterator = foundSensitiveWordSet.iterator();
        while (iterator.hasNext()) {
            String str = iterator.next();
            string=string.replaceAll(str, "");
        }
        return string;
    }

    /**
     * @param @param i
     * @param @param str
     * @return 是否存在敏感词
     * @throws
     * @Description:
     */
    private boolean isContainSensitiveWord(int i, String txt) {
        int length = 0; // 已匹配的敏感字长度,每找对一个字，加1
        boolean isExist = false;
        Map nowWorkMap = sensitiveHashMap;
        for (int j = i; j < txt.length(); j++) { // 从开头第一个字开始匹对
            Map wordMap = (Map) nowWorkMap.get(txt.charAt(j));
            if (wordMap == null) {
                break; // 文档中的当前字不匹配敏感词中的字，退出
            } else {
                length++;
//				System.out.println(wordMap.get("isEnd").toString());
                // if (wordMap.get("isEnd").toString() == String.valueOf(1))
                if (wordMap.get("isEnd").toString().equals("1"))
                    isExist = true;
                nowWorkMap = wordMap;
            }

        }

        if (isExist) {
//            System.out.println("添加查找到的敏感词" + txt.substring(i, i + length));
            foundSensitiveWordSet.add(txt.substring(i, i + length));
        }
        return isExist;
    }

    /**
     * @param @param args
     * @return void
     * @throws
     * @Description: TODO
     */
    public static void main(String[] args) {

        SensitiveWordFilter sFilter = new SensitiveWordFilter();
        sFilter.initFilter();
        String txtString = "太多的伤感情怀也许只局限于饲养基地 荧幕中的情节，主人公尝试着去用某种方式渐渐的很潇洒地释自杀指南怀那些自己经历的伤感。"
                + "然后法轮功 我们的扮演的角色就是跟随着主人公的喜红客联盟 怒哀乐而过于牵强的把自己的情感也附加于银幕情节中，然后感动就流泪，"
                + "难过就躺在某一个人的怀里尽情的阐述心扉或者手机卡复制器一个人一杯红酒一部电影在夜三级片深人静的晚上，关上电话静静的发呆着。";
//		String txtString ="日本鬼子";
        sFilter.findSensitiveWordInTxt(txtString);
        System.out.println(sFilter.printResult(txtString));
    }
}
