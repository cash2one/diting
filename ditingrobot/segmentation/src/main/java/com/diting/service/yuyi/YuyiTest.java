package com.diting.service.yuyi;

import com.diting.dao.ThesaurusMapper;
import com.diting.model.Thesaurus;
import com.diting.model.options.ThesaurusOptions;
import com.diting.service.OralTreatmentService;
import com.diting.service.ReplaceWordService;
import com.diting.service.SynonymService;
import com.diting.service.WordBaseService;
import org.apache.commons.lang.StringUtils;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
@SuppressWarnings("ALL")
@Service("yuyiTest")
public class YuyiTest {

    @Autowired
    private ThesaurusMapper thesaurusMapper;
    @Autowired
    private WordBaseService wordBaseService;
    @Autowired
    private SynonymService synonymService;
    @Autowired
    private ReplaceWordService replaceWordService;
    @Autowired
    private OralTreatmentService oralTreatmentService;
    @Autowired
    private Yuyijs yuyijs;

    public JSONObject shuruchuliJason(String str, String keyString, String scene) {
        if (Yuyijs.mlWordBase == null || Yuyijs.mlWordBase.size() == 0) {
            Yuyijs.mlWordBase = wordBaseService.getWordBaseMap();
        }
        if (Yuyijs.synonymMap == null || Yuyijs.synonymMap.size() == 0) {
            Yuyijs.synonymMap = synonymService.getSynonymMap();
        }
        if (Yuyijs.replaceWordMap == null || Yuyijs.replaceWordMap.size() == 0) {
            Yuyijs.replaceWordMap = replaceWordService.getReplaceWordMap();
        }
        if (Yuyijs.oralTreatmentMap == null || Yuyijs.oralTreatmentMap.size() == 0) {
            Yuyijs yuyijs = new Yuyijs();
            Yuyijs.oralTreatmentMap =oralTreatmentService.getOralTreatmentMap();
        }
        JSONObject shuruJson = new JSONObject();
        if(str == null || str.trim().length() == 0){
            return shuruJson;
        }
        String[] s = yuyijs.shuruchuli(str,keyString,scene).split(" ");
        List<String> list = new ArrayList<String>();
        Map<String, Integer> map = new IdentityHashMap<>();
        for (int i = 0; i < s.length; i++) {
            if (s[i].contains("!")) {
                s[i] = s[i].replace("!", "");
                if (map.containsKey(s[i])) {
                    map.put(s[i], map.get(s[i]));
                } else {
                    map.put(s[i], 1);
                }
            }
            list.add(s[i]);
        }
        shuruJson.put("qieci", StringUtils.join(list.toArray(), " "));
        Boolean bl = saveOrUpdate(map);
        return shuruJson;
    }

    public JSONObject guanjianzijsJason(String a) {
        JSONObject gjzJson = new JSONObject();
        String[] str = Yuyijs.guanjianzijs(a).split(",");
        StringBuffer sb = new StringBuffer();

        for (int i = 1; i <= str.length; ++i) {
            gjzJson.put("gjz" + i, str[i - 1]);
        }
        return gjzJson;
    }

    public JSONObject yuyixiangsijsJason(String a, String b) throws Exception {
        JSONObject xiangshiJson = new JSONObject();
        xiangshiJson.put("xiangsijieguo", Yuyijs.yuyixiangsijs(a, b));
        return xiangshiJson;
    }

    public static JSONObject changjingjsJason(String a, String b) {
        JSONObject changjingJson = new JSONObject();
        changjingJson.put("changjing", Yuyijs.changjingjs(a, b));
        return changjingJson;
    }

    public Boolean saveOrUpdate(Map<String, Integer> map) {
        ThesaurusOptions options=new ThesaurusOptions();
        List<Thesaurus> thesaurusList=new ArrayList<>();
        for (String key : map.keySet()) {
            Thesaurus thesaurus = new Thesaurus();
            options.setKeyword(key);
            thesaurusList = thesaurusMapper.searchForPage(options);
            if (thesaurusList.size()>0) {
                thesaurus=thesaurusList.get(0);
                thesaurus.setWord_frequency(thesaurus.getWord_frequency() + 1);
                thesaurusMapper.update(thesaurus);
            } else {
                thesaurus.setWord(key);
                thesaurus.setWord_frequency(Integer.valueOf(map.get(key)));
                thesaurusMapper.create(thesaurus);
            }
            options.setKeyword(null);
            thesaurusList=new ArrayList<>();
        }
        return true;
    }
}
