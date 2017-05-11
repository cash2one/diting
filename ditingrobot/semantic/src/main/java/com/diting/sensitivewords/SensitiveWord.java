package com.diting.sensitivewords;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;

/**
 * @author liufei
 * @date  2016年12月12日
 **/
public class SensitiveWord {

	private HashSet<String> sensitiwordHashSet;  //保存要查找的敏感词
	private Map sensitiveHashMap;		//敏感词构造的hashmap

	// Constructor
	private SensitiveWord() {
	}

	private static final SensitiveWord SENSITIVE_WORD = new SensitiveWord();

	public static SensitiveWord getInstance() {
		return SENSITIVE_WORD;
	}



	/**
	 * @Description: 读取敏感词文件，存入hashset中
	 * @param
	 * @return void
	 * @throws
	 */
	private void readfile() {
		sensitiwordHashSet = new HashSet<String>();
		String str;
		// FileReader fileReader = null;
		BufferedReader bufferedReader = null;

		File directory  = new File("");
		String courseFile = null;
		String path=null;
		try {

//			courseFile = directory.getCanonicalPath();
			courseFile=this.getClass().getResource("/").getPath();
			InputStreamReader inputStreamReader = new InputStreamReader(
					new FileInputStream(courseFile+"/sensitiveWord.txt"));
			bufferedReader = new BufferedReader(inputStreamReader);
			while ((str = bufferedReader.readLine()) != null) {
				sensitiwordHashSet.add(str);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally { // 关闭流
			if (bufferedReader != null)
				try {
					bufferedReader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
		}


	}

	private void consSensitiveHashMap(HashSet<String> wordsSet) {

		sensitiveHashMap = new HashMap(wordsSet.size());

		Map nowMap;
		Map temhashmap;
		String key;

		Iterator<String> iterator = wordsSet.iterator();

		while (iterator.hasNext()) {
			nowMap = sensitiveHashMap; // 当前处理的map
			key = iterator.next();

			for (int i = 0; i < key.length(); i++) { // 遍历每个词的每个字
				char oneWord_char = key.charAt(i);

				Object wordMap = nowMap.get(oneWord_char);// 查找当前map，看是否有当前的字
				// 若有，则对应的value(对应的hash树)给wordMap

				if (wordMap != null) { // 存在有这个字
					nowMap = (Map) wordMap;
				} else {
					temhashmap = new HashMap<String, String>();
					if (i != key.length() - 1) {
						temhashmap.put("isEnd", 0);
					} else {
						temhashmap.put("isEnd", 1);
					}

					nowMap.put(oneWord_char, temhashmap);
					nowMap = temhashmap;

				}

			}
		}
	}


	/**
	 * @Description: 初始化
	 * @param
	 * @return void
	 * @throws
	 */
	public void init() {
		readfile();
		consSensitiveHashMap(sensitiwordHashSet);

	}

	/**
	 * @return the sensitiwordHashSet
	 */
	public HashSet<String> getSensitiwordHashSet() {
		return sensitiwordHashSet;
	}

	/**
	 * @return the sensitiveHashMap
	 */
	public Map getSensitiveHashMap() {
		return sensitiveHashMap;
	}


}
