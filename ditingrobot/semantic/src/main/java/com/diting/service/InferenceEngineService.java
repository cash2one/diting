package com.diting.service;

import com.diting.model.Chat;
import com.diting.model.CustomerSynonym;
import com.diting.model.Robot;

import java.util.List;
import java.util.Map;

/**
 * InferenceEngineService
 */
public interface InferenceEngineService {
    Map<String, String> tuiliji(Chat chatInfo, String C2, String N2, String R2, String G2, String D2, String G3, String R3);

    Map<String, String> tuilijiByOriginalProblem(Chat chatInfo, String C2, String N2, String R2, String G2, String D2, String G3, String R3);

    String shengluebuquan(String aj, String bj);

    double shengluepanduan(String a, String b);

    boolean shengluepanduan1(String a, String b);

    String wendabuquan(String wenti, String dg1);

    boolean xiangsipd(String d1, String d2);

    boolean shangjvpd(String d2);

    String shangjvgjzjs(String d1);

    String daichichuli(String wenti1, String a10, String a11);

    String zhuyujs(String gjz1);

    String binyujs(String gjz1);

    String bypaixu(String a);

    String suijidaan();

    String suijiwenti();

    String suijifanwen();

    boolean isNum(String str);

    String guanjianzibuquan(String a, String b);

    String buquanjvzi(String aj, String bj, String ag, String bg, int n);

    String changjingjs(String a, String shang);

    String tuili(String gjz1, String isDisable, String userId);

    String tuili1(String gjz1, String isDisable, String userId);

    String tuili2(String gjz1, String isDisable, String userId);
}
