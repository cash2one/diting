package com.diting.util;

import com.alibaba.fastjson.JSONObject;
import com.hankcs.hanlp.HanLP;
import com.hankcs.hanlp.dictionary.CustomDictionary;
import com.hankcs.hanlp.seg.common.Term;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;


/**
 * HanLPUtils
 */

public final class HanLPUtils {
    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(HanLPUtils.class);

    private static final String fileP1 = "dic/singer.txt";
    private static final String fileP2 = "dic/songname.txt";
    private static final String fileP3 = "dic/songtype.txt";
    private static final String fileP4 = "dic/album.txt";
    private static final String[] songNames = fileReader(fileP2);
    static {
        String[] singers = fileReader(fileP1);
        for (String singer : singers) {
            CustomDictionary.insert(singer, "artist 1024");
        }


        for (String songName : songNames) {
            CustomDictionary.insert(songName, "name 1024");
        }

        String[] songTypes = fileReader(fileP3);
        for (String songType : songTypes) {
            CustomDictionary.insert(songType, "style 1024");
        }

        String[] albums = fileReader(fileP4);
        for (String album : albums) {
            CustomDictionary.insert(album, "album 1024");
        }
    }

    private HanLPUtils() {
        // private ctor
    }

    public static String getSong(String question,String answer) {
        if(question.equals("我要听音乐")){
            return getRandomSong();
        }
        Map<String, String> map = new HashMap<String, String>();
        if (answer!=null&&answer.startsWith("#DTAI#")) {
            String artist = "";
            String name = "";
            String style = "";
            String album = "";
            String domain = "music";
            String action = "play";
            if (answer.equals("#DTAI#")) {
                name = getRandomSong();
            } else {
                String[] songInfo = answer.split("#");
                for (String info : songInfo) {
                    if (info.startsWith("artist:") | info.startsWith("artist：")) {
                        artist = info.substring(7);
                    }
                    if (info.startsWith("name:") | info.startsWith("name：")) {
                        name = info.substring(5);
                    }
                    if (info.startsWith("style:") | info.startsWith("style：")) {
                        style = info.substring(6);
                    }
                    if (info.startsWith("album:") | info.startsWith("album：")) {
                        album = info.substring(6);
                    }
                    if (info.startsWith("action:") | info.startsWith("action：")) {
                        action = info.substring(7);
                    }
                }
            }
            map.put("domain", domain);
            map.put("artist", artist);
            map.put("name", name);
            map.put("style", style);
            map.put("album", album);
            map.put("action", action);
        } else {
            map = getSongInfo(question);
            if("".equals(map.get("domain"))){
                return null;
            }
        }
        return Utils.toJacksonJSON(map).toString();
    }

    private static Map<String, String> getSongInfo(String question) {
        Map<String, String> map = new HashMap<>();

        try {
            List<Term> termList = HanLP.segment(question);
            String artist = "";
            String name = "";
            String style = "";
            String album = "";
            String domain = "";
            String action = "play";
            for (Term term : termList) {
                if (term.nature.startsWith("artist")) {
                    artist = term.word;
                    domain = "music";
                } else if (term.nature.startsWith("name")) {
                    name = term.word;
                    domain = "music";
                } else if (term.nature.startsWith("style")) {
                    style = term.word;
                    domain = "music";
                } else if (term.nature.startsWith("album")) {
                    album = term.word;
                    domain = "music";
                }
            }
            map.put("domain", domain);
            map.put("artist", artist);
            map.put("name", name);
            map.put("style", style);
            map.put("album", album);
            map.put("action",action);
        } catch (Exception e) {
            LOGGER.info("error occurred during segment" + e.getMessage());
        }
        return map;
    }

    private static String[] fileReader(String path) {
        List list = new ArrayList();
        String[] nums = null;
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(new ClassPathResource(path).getInputStream(), "UTF-8"));
            String line = null;
            //因为不知道有几行数据，所以先存入list集合中
            while ((line = br.readLine()) != null) {
                list.add(line);
            }
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //确定数组长度
        nums = new String[list.size()];
        for (int i = 0; i < list.size(); i++) {
            String s = (String) list.get(i);
            nums[i] = s;
        }
        return nums;
    }
    public static String getRandomSong(){
        return songNames[new Random().nextInt(songNames.length)];
    }
}
