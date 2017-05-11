package com.diting.service.impl;

import com.diting.elasticsearch.ElasticSearch;
import com.diting.model.Knowledge;
import com.diting.model.options.ESSearchOptions;
import com.diting.service.AnswerConstructorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service("answerConstructorService")
@Transactional
public class AnswerConstructorServiceImpl implements AnswerConstructorService {

    private final Logger LOGGER = LoggerFactory.getLogger(AnswerConstructorServiceImpl.class);

    @Autowired
    private ElasticSearch elasticSearchService;

    //第一: 五个关键字加场景
    public String yutou(String keyword, String scene, String isDisable, String indexType) {

        String[] kw = keyword.split(",");
        String wentiji = "";
        List<Knowledge> rs = null;

        //To do 企业机器人
        if (!kw[0].equals("k$z") || !kw[1].equals("k$z") || !kw[2].equals("k$z") || !kw[3].equals("k$z") || !kw[4].equals("k$z")) {
            rs = elasticSearchService.search(scene, kw[0].replace("$", ""), kw[1].replace("$", ""), kw[2].replace("$", ""), kw[3].replace("$", ""), kw[4].replace("$", ""), indexType);
            //企业机器人优先读取企业知识库
            if ((rs == null || rs.size() == 0) && isDisable != null && isDisable.equals("0")) {
                rs = elasticSearchService.search(scene, kw[0].replace("$", ""), kw[1].replace("$", ""), kw[2].replace("$", ""), kw[3].replace("$", ""), kw[4].replace("$", ""), "-1");
            }
        }
        if (rs != null && rs.size() > 0) {
            for (int i = 0; i < rs.size(); i++) {
                Knowledge knowledge = rs.get(i);
                Integer id = knowledge.getId();
                String question = knowledge.getQuestion().replace(",", "，");
                String answer = knowledge.getAnswer().replace(",", "，");
                Integer emotion = knowledge.getEmotion();
                String sight = knowledge.getScene();
                String str_jilus = question + "," + answer + "," + emotion + "," + sight + "," + id;
                if (i == 0) {
                    wentiji = str_jilus;
                } else {
                    wentiji = wentiji + "fen!ge" + str_jilus;
                }
            }
        }
        return wentiji;

    }

    @Override
    public String findByHandleQuestionAndScene(String handleQuestion, String scene, String isDisable, String indexType) {
        String wentiji = "";
        List<Knowledge> rs = null;
        ESSearchOptions esSearchOptions = new ESSearchOptions();
        esSearchOptions.setType(indexType);
        esSearchOptions.setScj(scene);
        esSearchOptions.setHandleQuestion(handleQuestion);
        //To do 企业知识库
        if (null != handleQuestion && handleQuestion.length() > 0) {
            rs = elasticSearchService.searchByHandleQuestion(esSearchOptions);
            if ((rs == null || rs.size() == 0) && isDisable != null && isDisable.equals("0")) {
                esSearchOptions.setType("-1");
                rs = elasticSearchService.searchByHandleQuestion(esSearchOptions);
            }
        }


        if (rs != null && rs.size() > 0) {
            for (int i = 0; i < rs.size(); i++) {
                Knowledge knowledge = rs.get(i);
                Integer id = knowledge.getId();
                String question = knowledge.getQuestion().replace(",", "，");
                String answer = knowledge.getAnswer().replace(",", "，");
                Integer emotion = knowledge.getEmotion();
                String sight = knowledge.getScene();
                String str_jilus = question + "," + answer + "," + emotion + "," + sight + "," + id;
                if (i == 0) {
                    wentiji = str_jilus;
                } else {
                    wentiji = wentiji + "fen!ge" + str_jilus;
                }
            }
        }
        return wentiji;
    }

    @Override
    public String findByQuestionAndScene(String question, String scene, String isDisable, String indexType) {
        String wentiji = "";
        List<Knowledge> rs = null;
        ESSearchOptions esSearchOptions = new ESSearchOptions();
        esSearchOptions.setType(indexType);
        esSearchOptions.setScj(scene);
        esSearchOptions.setQuestion(question);
        //To do 企业知识库
        if (null != question && question.length() > 0) {
            rs = elasticSearchService.searchByQuestionAndScene(esSearchOptions);
            if ((rs == null || rs.size() == 0) && isDisable != null && isDisable.equals("0")) {
                esSearchOptions.setType("-1");
                rs = elasticSearchService.searchByQuestionAndScene(esSearchOptions);
            }
        }


        if (rs != null && rs.size() > 0) {
            for (int i = 0; i < rs.size(); i++) {
                Knowledge knowledge = rs.get(i);
                Integer id = knowledge.getId();
                String str_question = knowledge.getQuestion().replace(",", "，");
                String answer = knowledge.getAnswer().replace(",", "，");
                Integer emotion = knowledge.getEmotion();
                String sight = knowledge.getScene();
                String str_jilus = str_question + "," + answer + "," + emotion + "," + sight + "," + id;
                if (i == 0) {
                    wentiji = str_jilus;
                } else {
                    wentiji = wentiji + "fen!ge" + str_jilus;
                }
            }
        }
        return wentiji;
    }

    //这里进行第二种查找，输入关键字12345，不需要场景
    public String yuer(String keyword, String isDisable, String indexType) {
        String[] kw = keyword.split(",");
        String wentiji = "";

        //判断是否为企业机器人，username为企业账号

        List<Knowledge> rs = new ArrayList<>();
        //To do 企业知识库
        if (!kw[0].equals("k$z") || !kw[1].equals("k$z") || !kw[2].equals("k$z") || !kw[3].equals("k$z") || !kw[4].equals("k$z")) {
            rs = elasticSearchService.search(null, kw[0].replace("$", ""), kw[1].replace("$", ""), kw[2].replace("$", ""), kw[3].replace("$", ""), kw[4].replace("$", ""), indexType);
            if ((rs == null || rs.size() == 0) && isDisable != null && isDisable.equals("0")) {
                rs = elasticSearchService.search(null, kw[0].replace("$", ""), kw[1].replace("$", ""), kw[2].replace("$", ""), kw[3].replace("$", ""), kw[4].replace("$", ""), "-1");
            }
        }
        if (rs != null && rs.size() > 0) {
            for (int i = 0; i < rs.size(); i++) {
                Knowledge knowledge = rs.get(i);
                Integer id = knowledge.getId();
                String question = knowledge.getQuestion().replace(",", "，");
                String answer = knowledge.getAnswer().replace(",", "，");
                Integer emotion = knowledge.getEmotion();
                String sight = knowledge.getScene();
                String str_jilus = question + "," + answer + "," + emotion + "," + sight + "," + id;
                if (i == 0) {
                    wentiji = str_jilus;
                } else {
                    wentiji = wentiji + "fen!ge" + str_jilus;
                }
            }
        }
        return wentiji;
    }

    //这里进行第三种查找，输入关键字123和场景
    public String yusan(String keyword, String scene, String isDisable, String indexType) {
        String[] kw = keyword.split(",");
        String wentiji = "";
        List<Knowledge> rs = null;
        //To do 企业知识库
        if (!kw[0].equals("k$z") || !kw[1].equals("k$z") || !kw[2].equals("k$z")) {
            rs = elasticSearchService.search(scene, kw[0].replace("$", ""), kw[1].replace("$", ""), kw[2].replace("$", ""), null, null, indexType);
            if ((rs == null || rs.size() == 0) && isDisable != null && isDisable.equals("0")) {
                rs = elasticSearchService.search(scene, kw[0].replace("$", ""), kw[1].replace("$", ""), kw[2].replace("$", ""), null, null, "-1");
            }
        }
        if (rs != null && rs.size() > 0) {
            for (int i = 0; i < rs.size(); i++) {
                Knowledge knowledge = rs.get(i);
                Integer id = knowledge.getId();
                String question = knowledge.getQuestion().replace(",", "，");
                String answer = knowledge.getAnswer().replace(",", "，");
                Integer emotion = knowledge.getEmotion();
                String sight = knowledge.getScene();
                String str_jilus = question + "," + answer + "," + emotion + "," + sight + "," + id;
                if (i == 0) {
                    wentiji = str_jilus;
                } else {
                    wentiji = wentiji + "fen!ge" + str_jilus;
                }
            }
        }
        return wentiji;
    }

    //这里进行第三种查找，输入关键字123和场景
    public String yusanObject(String keyword, String isDisable, String indexType) {
        String[] kw = keyword.split(",");
        String wentiji = "";
        List<Knowledge> rs = null;
        //To do 企业知识库
        if (!kw[0].equals("k$z") || !kw[1].equals("k$z") || !kw[2].equals("k$z")) {
            rs = elasticSearchService.search(null, kw[0].replace("$", ""), kw[1].replace("$", ""), kw[2].replace("$", ""), null, null, indexType);
            if ((rs == null || rs.size() == 0) && isDisable != null && isDisable.equals("0")) {
                rs = elasticSearchService.search(null, kw[0].replace("$", ""), kw[1].replace("$", ""), kw[2].replace("$", ""), null, null, "-1");
            }
        }

        if (rs != null && rs.size() > 0) {
            for (int i = 0; i < rs.size(); i++) {
                Knowledge knowledge = rs.get(i);
                Integer id = knowledge.getId();
                String question = knowledge.getQuestion().replace(",", "，");
                String answer = knowledge.getAnswer().replace(",", "，");
                Integer emotion = knowledge.getEmotion();
                String sight = knowledge.getScene();
                String str_jilus = question + "," + answer + "," + emotion + "," + sight + "," + id;
                if (i == 0) {
                    wentiji = str_jilus;
                } else {
                    wentiji = wentiji + "fen!ge" + str_jilus;
                }
            }
        }
        return wentiji;
    }

    @Override
    public String yuzuObject(String keyword, String isDisable, String indexType) {
        String[] kw = keyword.split(",");
        String wentiji = "";
        List<Knowledge> rs = null;
        //To do 企业知识库
        if (!kw[0].equals("k$z") || !kw[1].equals("k$z") || !kw[2].equals("k$z") || !kw[3].equals("k$z")) {
            rs = elasticSearchService.search(null, kw[0].replace("$", ""), kw[1].replace("$", ""), kw[2].replace("$", ""), kw[3].replace("$", ""), null, indexType);
            if ((rs == null || rs.size() == 0) && isDisable != null && isDisable.equals("0")) {
                rs = elasticSearchService.search(null, kw[0].replace("$", ""), kw[1].replace("$", ""), kw[2].replace("$", ""), kw[3].replace("$", ""), null, "-1");
            }
        }

        if (rs != null && rs.size() > 0) {
            for (int i = 0; i < rs.size(); i++) {
                Knowledge knowledge = rs.get(i);
                Integer id = knowledge.getId();
                String question = knowledge.getQuestion().replace(",", "，");
                String answer = knowledge.getAnswer().replace(",", "，");
                Integer emotion = knowledge.getEmotion();
                String sight = knowledge.getScene();
                String str_jilus = question + "," + answer + "," + emotion + "," + sight + "," + id;
                if (i == 0) {
                    wentiji = str_jilus;
                } else {
                    wentiji = wentiji + "fen!ge" + str_jilus;
                }
            }
        }
        return wentiji;
    }

    //这里进行第四种查找，输入关键字12345
    public String yuzu(String keyword, String isDisable, String indexType) {
        String[] kw = keyword.split(",");
        String wentiji = "";
        List<Knowledge> rs = null;
        //To do 企业知识库
        if (!kw[0].equals("k$z") || !kw[1].equals("k$z") || !kw[2].equals("k$z") || !kw[3].equals("k$z") || !kw[4].equals("k$z")) {
            rs = elasticSearchService.search(null, kw[0].replace("$", ""), kw[1].replace("$", ""), kw[2].replace("$", ""), kw[3].replace("$", ""), kw[4].replace("$", ""), indexType);
            if ((rs == null || rs.size() == 0) && isDisable != null && isDisable.equals("0")) {
                rs = elasticSearchService.search(null, kw[0].replace("$", ""), kw[1].replace("$", ""), kw[2].replace("$", ""), kw[3].replace("$", ""), kw[4].replace("$", ""), "-1");
            }
        }

        if (rs != null && rs.size() > 0) {
            for (int i = 0; i < rs.size(); i++) {
                Knowledge knowledge = rs.get(i);
                Integer id = knowledge.getId();
                String question = knowledge.getQuestion().replace(",", "，");
                String answer = knowledge.getAnswer().replace(",", "，");
                Integer emotion = knowledge.getEmotion();
                String sight = knowledge.getScene();
                String str_jilus = question + "," + answer + "," + emotion + "," + sight + "," + id;
                if (i == 0) {
                    wentiji = str_jilus;
                } else {
                    wentiji = wentiji + "fen!ge" + str_jilus;
                }
            }
        }
        return wentiji;
    }


    @Override
    public String yusanOblect(String keyword, String scene, String isDisable, String indexType) {
        String[] kw = keyword.split(",");
        String wentiji = "";
        List<Knowledge> rs = null;
        //To do 企业知识库
        if (!kw[0].equals("k$z") || !kw[1].equals("k$z") || !kw[2].equals("k$z") || !kw[3].equals("k$z")) {
            rs = elasticSearchService.search(scene, kw[0].replace("$", ""), kw[1].replace("$", ""), kw[2].replace("$", ""), kw[3].replace("$", ""), null, indexType);
            if ((rs == null || rs.size() == 0) && isDisable != null && isDisable.equals("0")) {
                rs = elasticSearchService.search(scene, kw[0].replace("$", ""), kw[1].replace("$", ""), kw[2].replace("$", ""), kw[3].replace("$", ""), null, "-1");
            }
        }
        if (rs != null && rs.size() > 0) {
            for (int i = 0; i < rs.size(); i++) {
                Knowledge knowledge = rs.get(i);
                Integer id = knowledge.getId();
                String question = knowledge.getQuestion().replace(",", "，");
                String answer = knowledge.getAnswer().replace(",", "，");
                Integer emotion = knowledge.getEmotion();
                String sight = knowledge.getScene();
                String str_jilus = question + "," + answer + "," + emotion + "," + sight + "," + id;
                if (i == 0) {
                    wentiji = str_jilus;
                } else {
                    wentiji = wentiji + "fen!ge" + str_jilus;
                }
            }
        }
        return wentiji;
    }

    @Override
    public String yuzuAnswer(String keyword, String scene, String isDisable, String indexType) {
        String[] kw = keyword.replace("k$z", "").split(",");
        String wentiji = "";
        List<Knowledge> rs = null;
        ESSearchOptions esSearchOptions = new ESSearchOptions();
        esSearchOptions.setType(indexType);
        esSearchOptions.setScj(scene);
        for (int i = 0; i < kw.length; i++) {
            if (i == 0) {
                esSearchOptions.setSkw1(kw[0]);
            } else if (i == 1) {
                esSearchOptions.setSkw2(kw[1]);
            } else if (i == 2) {
                esSearchOptions.setSkw3(kw[2]);
            } else if (i == 3) {
                esSearchOptions.setSkw4(kw[3]);
            } else if (i == 4) {
                esSearchOptions.setSkw5(kw[4]);
            }
        }
        //To do 企业知识库
        if (null != esSearchOptions.getSkw1()) {
            rs = elasticSearchService.search(esSearchOptions);
            if ((rs == null || rs.size() == 0) && isDisable != null && isDisable.equals("0")) {
                esSearchOptions.setType("-1");
                rs = elasticSearchService.search(esSearchOptions);
            }
        }
        if (rs != null && rs.size() > 0) {
            for (int i = 0; i < rs.size(); i++) {
                Knowledge knowledge = rs.get(i);
                Integer id = knowledge.getId();
                String question = knowledge.getQuestion().replace(",", "，");
                String answer = knowledge.getAnswer().replace(",", "，");
                Integer emotion = knowledge.getEmotion();
                String sight = knowledge.getScene();
                String str_jilus = question + "," + answer + "," + emotion + "," + sight + "," + id;
                if (i == 0) {
                    wentiji = str_jilus;
                } else {
                    wentiji = wentiji + "fen!ge" + str_jilus;
                }
            }
        }
        return wentiji;
    }

    @Override
    public String yuzuObjectAnswer(String keyword, String scene, String isDisable, String indexType) {
        String[] kw = keyword.split(",");
        List<String> stringList = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            if (!kw[i].equals("k$z")) {
                stringList.add(kw[i]);
            }
        }
        String wentiji = "";
        List<Knowledge> rs = null;
        ESSearchOptions esSearchOptions = new ESSearchOptions();
        esSearchOptions.setType(indexType);
        esSearchOptions.setScj(scene);
        if (null != stringList) {
            for (int i = 0; i < stringList.size(); i++) {
                if (i == 0) {
                    esSearchOptions.setSkw1(stringList.get(0));
                } else if (i == 1) {
                    esSearchOptions.setSkw2(stringList.get(1));
                } else if (i == 2) {
                    esSearchOptions.setSkw3(stringList.get(2));
                } else if (i == 3) {
                    esSearchOptions.setSkw4(stringList.get(3));
                }
            }
            //To do 企业知识库
            if (null != stringList && stringList.size() > 0) {
                rs = elasticSearchService.search(esSearchOptions);
                if ((rs == null || rs.size() == 0) && isDisable != null && isDisable.equals("0")) {
                    esSearchOptions.setType("-1");
                    rs = elasticSearchService.search(esSearchOptions);
                }
            }
        }

        if (rs != null && rs.size() > 0) {
            for (int i = 0; i < rs.size(); i++) {
                Knowledge knowledge = rs.get(i);
                Integer id = knowledge.getId();
                String question = knowledge.getQuestion().replace(",", "，");
                String answer = knowledge.getAnswer().replace(",", "，");
                Integer emotion = knowledge.getEmotion();
                String sight = knowledge.getScene();
                String str_jilus = question + "," + answer + "," + emotion + "," + sight + "," + id;
                if (i == 0) {
                    wentiji = str_jilus;
                } else {
                    wentiji = wentiji + "fen!ge" + str_jilus;
                }
            }
        }
        return wentiji;
    }

    @Override
    public String yusanOblectAnswer(String keyword, String scene, String isDisable, String indexType) {
        String[] kw = keyword.split(",");
        List<String> stringList = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            if (!kw[i].equals("k$z")) {
                stringList.add(kw[i]);
            }
        }
        String wentiji = "";
        List<Knowledge> rs = null;
        ESSearchOptions esSearchOptions = new ESSearchOptions();
        esSearchOptions.setType(indexType);
        esSearchOptions.setScj(scene);
        for (int i = 0; i < stringList.size(); i++) {
            if (i == 0) {
                esSearchOptions.setSkw1(stringList.get(0));
            } else if (i == 1) {
                esSearchOptions.setSkw2(stringList.get(1));
            } else if (i == 2) {
                esSearchOptions.setSkw3(stringList.get(2));
            }
        }
        //To do 企业知识库
        if (null != stringList && stringList.size() > 0) {
            rs = elasticSearchService.search(esSearchOptions);
            if ((rs == null || rs.size() == 0) && isDisable != null && isDisable.equals("0")) {
                esSearchOptions.setType("-1");
                rs = elasticSearchService.search(esSearchOptions);
            }
        }

        if (rs != null && rs.size() > 0) {
            for (int i = 0; i < rs.size(); i++) {
                Knowledge knowledge = rs.get(i);
                Integer id = knowledge.getId();
                String question = knowledge.getQuestion().replace(",", "，");
                String answer = knowledge.getAnswer().replace(",", "，");
                Integer emotion = knowledge.getEmotion();
                String sight = knowledge.getScene();
                String str_jilus = question + "," + answer + "," + emotion + "," + sight + "," + id;
                if (i == 0) {
                    wentiji = str_jilus;
                } else {
                    wentiji = wentiji + "fen!ge" + str_jilus;
                }
            }
        }
        return wentiji;
    }

    @Override
    public String yuzuQuestion(String keyword, String scene, String isDisable, String indexType) {
        String[] kw = keyword.split(",");
        List<String> stringList = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            if (!kw[i].equals("k$z")) {
                stringList.add(kw[i]);
            }
        }
        String wentiji = "";
        List<Knowledge> rs = null;
        ESSearchOptions esSearchOptions = new ESSearchOptions();
        esSearchOptions.setType(indexType);
        esSearchOptions.setScj(scene);
        if (null != stringList) {
            for (int i = 0; i < stringList.size(); i++) {
                if (i == 0) {
                    esSearchOptions.setSkw1(stringList.get(0));
                } else if (i == 1) {
                    esSearchOptions.setSkw2(stringList.get(1));
                } else if (i == 2) {
                    esSearchOptions.setSkw3(stringList.get(2));
                } else if (i == 3) {
                    esSearchOptions.setSkw4(stringList.get(3));
                }
            }
            //To do 企业知识库
            if (null != stringList && stringList.size() > 0) {
                rs = elasticSearchService.searchByQuestion(esSearchOptions);
                if ((rs == null || rs.size() == 0) && isDisable != null && isDisable.equals("0")) {
                    esSearchOptions.setType("-1");
                    rs = elasticSearchService.searchByQuestion(esSearchOptions);
                }
            }
        }

        if (rs != null && rs.size() > 0) {
            for (int i = 0; i < rs.size(); i++) {
                Knowledge knowledge = rs.get(i);
                Integer id = knowledge.getId();
                String question = knowledge.getQuestion().replace(",", "，");
                String answer = knowledge.getAnswer().replace(",", "，");
                Integer emotion = knowledge.getEmotion();
                String sight = knowledge.getScene();
                String str_jilus = question + "," + answer + "," + emotion + "," + sight + "," + id;
                if (i == 0) {
                    wentiji = str_jilus;
                } else {
                    wentiji = wentiji + "fen!ge" + str_jilus;
                }
            }
        }
        return wentiji;
    }

    @Override
    public String yuerQuestion(String keyword, String scene, String isDisable, String indexType) {
        String[] kw = keyword.split(",");
        List<String> stringList = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            if (!kw[i].equals("k$z")) {
                stringList.add(kw[i]);
            }
        }
        String wentiji = "";
        List<Knowledge> rs = null;
        ESSearchOptions esSearchOptions = new ESSearchOptions();
        esSearchOptions.setType(indexType);
        esSearchOptions.setScj(scene);
        if (null != stringList) {
            for (int i = 0; i < stringList.size(); i++) {
                if (i == 0) {
                    esSearchOptions.setSkw1(stringList.get(0));
                } else if (i == 1) {
                    esSearchOptions.setSkw2(stringList.get(1));
                } else if (i == 2) {
                    esSearchOptions.setSkw3(stringList.get(2));
                } else if (i == 3) {
                    esSearchOptions.setSkw4(stringList.get(3));
                }
            }
            //To do 企业知识库
            if (null != stringList && stringList.size() > 0) {
                rs = elasticSearchService.searchByQuestion(esSearchOptions);
                if ((rs == null || rs.size() == 0) && isDisable != null && isDisable.equals("0")) {
                    esSearchOptions.setType("-1");
                    rs = elasticSearchService.searchByQuestion(esSearchOptions);
                }
            }
        }

        if (rs != null && rs.size() > 0) {
            for (int i = 0; i < rs.size(); i++) {
                Knowledge knowledge = rs.get(i);
                Integer id = knowledge.getId();
                String question = knowledge.getQuestion().replace(",", "，");
                String answer = knowledge.getAnswer().replace(",", "，");
                Integer emotion = knowledge.getEmotion();
                String sight = knowledge.getScene();
                String str_jilus = question + "," + answer + "," + emotion + "," + sight + "," + id;
                if (i == 0) {
                    wentiji = str_jilus;
                } else {
                    wentiji = wentiji + "fen!ge" + str_jilus;
                }
            }
        }
        return wentiji;
    }

    @Override
    public String findKnowledgeByScene(String scene, String isDisable, String indexType) {
        String wentiji = "";

        //判断是否为企业机器人，username为企业账号

        List<Knowledge> rs = new ArrayList<>();
        //To do 企业知识库
        rs = elasticSearchService.search(scene, null, null, null, null, null, indexType);
        if ((rs == null || rs.size() == 0) && isDisable != null && isDisable.equals("0")) {
            rs = elasticSearchService.search(scene, null, null, null, null, null, "-1");
        }
        if (rs != null && rs.size() > 0) {
            for (int i = 0; i < rs.size(); i++) {
                Knowledge knowledge = rs.get(i);
                String kw1=knowledge.getKw1();
                String kw2=knowledge.getKw2();
                String kw3=knowledge.getKw3();
                String kw4=knowledge.getKw4();
                String kw5=knowledge.getKw5();
                String kws=kw1+","+kw2+","+kw3+","+kw4+","+kw5;
                if (i==0){
                    wentiji=kws;
                }else {
                    wentiji=wentiji+";"+kws;
                }
            }
        }
        return wentiji;
    }

}
