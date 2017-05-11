package com.diting.service;

/**
 * SemanticsService
 */

public interface SemanticsService {
    String inputHandling(String word,String scene);

    String keywordProcessing(String keyword);

    String semanticMyopia(String a, String b) ;

    String scene(String a, String b);

    String wordValue(String a, String b);
}
