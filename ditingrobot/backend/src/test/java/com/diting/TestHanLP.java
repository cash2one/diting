package com.diting;

import com.hankcs.hanlp.HanLP;
import com.hankcs.hanlp.collection.AhoCorasick.AhoCorasickDoubleArrayTrie;
import com.hankcs.hanlp.dictionary.BaseSearcher;
import com.hankcs.hanlp.dictionary.CoreDictionary;
import com.hankcs.hanlp.dictionary.CustomDictionary;
import org.springframework.core.io.ClassPathResource;
import org.testng.annotations.Test;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * TestHanLP.
 */
public class TestHanLP extends BaseTest {

    //https://github.com/hankcs/HanLP

    @Test(enabled = false)
    public void testBVT() {
        System.out.println(HanLP.segment("你好，欢迎使用HanLP汉语处理包！"));
        System.out.println("hahahahahahah");
    }

    @Test(enabled = false)
    public void testDemoCustomDictionary() {
        String[] names = parse("singer.txt");
        for(String name:names){
            System.out.println(name);
        }
//         动态增加
        CustomDictionary.add("攻城狮");
//         强行插入
        CustomDictionary.insert("范特西","album 1024");
        CustomDictionary.insert("周杰伦", "nr 1024");
        CustomDictionary.insert("双节棍", "songname 1024");
        CustomDictionary.insert("快歌", "songtype 1024");
//         删除词语（注释掉试试）
        CustomDictionary.remove("攻城狮");
        System.out.println(CustomDictionary.add("单身狗", "nz 1024 n 1"));
        System.out.println(CustomDictionary.get("单身狗"));

        String text = "放一首范特西里面周杰伦的双节棍";  // 怎么可能噗哈哈！

        // AhoCorasickDoubleArrayTrie自动机分词
        final char[] charArray = text.toCharArray();
        CustomDictionary.parseText(charArray, new AhoCorasickDoubleArrayTrie.IHit<CoreDictionary.Attribute>()
        {
            @Override
            public void hit(int begin, int end, CoreDictionary.Attribute value)
            {
                System.out.printf("[%d:%d]=%s %s\n", begin, end, new String(charArray, begin, end - begin), value);
            }
        });
//        // trie树分词
        BaseSearcher searcher = CustomDictionary.getSearcher(text);
        Map.Entry entry;
        while ((entry = searcher.next()) != null)
        {
            System.out.println(entry);
        }

        // 标准分词
        System.out.println(HanLP.segment(text));

        System.out.println("gao shen me gao");
    }

    private String[] parse(String path){
            List list = new ArrayList();
            String[] nums = null;
            try {
                BufferedReader br = new BufferedReader(new InputStreamReader(new ClassPathResource(path).getInputStream(), "UTF-8"));
                String line = null;
                //因为不知道有几行数据，所以先存入list集合中
                while((line = br.readLine()) != null){
                    list.add(line);
                }
                br.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            //确定数组长度
            nums = new String[list.size()];
            for(int i=0;i<list.size();i++){
                String s = (String) list.get(i);
                nums[i] = s;
            }
            return nums;

    }

}
