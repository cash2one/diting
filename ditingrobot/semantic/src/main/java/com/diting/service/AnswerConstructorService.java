package com.diting.service;

/**
 * AnswerConstructorService
 */
public interface AnswerConstructorService {
    String yutou(String keyword, String scene, String isDisable, String indexType);

    String findByHandleQuestionAndScene(String handleQuestion, String scene, String isDisable, String indexType);

    String findByQuestionAndScene(String question, String scene, String isDisable, String indexType);

    String yuer(String keyword, String isDisable, String indexType);

    String yusan(String keyword, String scene, String isDisable, String indexType);

    String yusanObject(String keyword, String isDisable, String indexType);

    String yuzu(String keyword, String isDisable, String indexType);

    String yuzuObject(String s, String s1, String indexType);

    String yusanOblect(String gjz1, String c2, String s, String indexType);

    String yuzuAnswer(String keyword,String scene, String isDisable, String indexType);

    String yuzuObjectAnswer(String s,String scene, String s1, String indexType);

    String yusanOblectAnswer(String keyword,String scene, String isDisable, String indexType);

    String yuzuQuestion(String keyword,String scene, String isDisable, String indexType);

    String yuerQuestion(String keyword,String scene, String isDisable, String indexType);

    String findKnowledgeByScene(String scene, String isDisable, String indexType);
}
