package com.diting.service.impl;

import com.diting.core.Universe;
import com.diting.dao.AccountMapper;
import com.diting.dao.PersonalApplicationMapper;
import com.diting.model.ApiPersonalStore;
import com.diting.model.ApiStore;
import com.diting.model.PersonalApplication;
import com.diting.model.options.ExternalOptions;
import com.diting.service.ApiPersonalStoreService;
import com.diting.service.ApiStoreService;
import com.diting.service.ExternalApplicationService;
import com.diting.util.APIUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.diting.util.Utils.*;

@SuppressWarnings("ALL")
@Service("externalApplicationService")
@Transactional
public class ExternalApplicationServiceImpl implements ExternalApplicationService {

    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(ExternalApplicationServiceImpl.class);

    @Autowired
    ApiStoreService apiStoreService;

    @Autowired
    PersonalApplicationMapper personalApplicationMapper;

    @Autowired
    AccountMapper accountMapper;

    @Autowired
    ApiPersonalStoreService apiPersonalStoreService;

    @Override
    public String invoke(ExternalOptions options) {
        String answer = null;
        String result = null;
        ApiPersonalStore apiPersonalStore = new ApiPersonalStore();
        if (null != Universe.current().getUserId()) {
            apiPersonalStore = apiPersonalStoreService.findApiPersonalStoreBySceneAndUserId(options.getScene(), Universe.current().getUserId());
        }
        if (null != apiPersonalStore) {
            options.setUrl(apiPersonalStore.getUrl());
            result = APIUtils.post(options);
            if (!isEmpty(result)) {
                try {
                    if (result.contains("object") && result.contains("送书")) {
                        JSONObject jsonObject = str2json(result);
                        String str_object = (String) jsonObject.get("object");
                        JSONArray jsonArray = new JSONArray(str_object);
                    }
                    answer = str(str2json(result).get("retData"));
                    System.out.println("应用："+answer);
                } catch (Exception e) {
                    LOGGER.error("error occurred during get answer..." + e.getMessage());
                }
            }
        }
        if (isEmpty(answer)){
            ApiStore apiStore = apiStoreService.getByScene(options.getScene(), options.getUserId());
            if (apiStore != null && apiStore.getApprovalEnable() == 1) {
                options.setUrl(apiStore.getUrl());
                result = APIUtils.post(options);
                if (!isEmpty(result)) {
                    try {
                        if (result.contains("object") && result.contains("送书")) {
                            JSONObject jsonObject = str2json(result);
                            String str_object = (String) jsonObject.get("object");
                            JSONArray jsonArray = new JSONArray(str_object);
                        }
                        answer = str(str2json(result).get("retData"));
                        System.out.println("应用："+answer);
                    } catch (Exception e) {
                        LOGGER.error("error occurred during get answer..." + e.getMessage());
                    }
                }
            }
        }
        return result;
    }

    @Override
    public void setting(ExternalOptions options) {
        Integer userId = Universe.current().getUserId();
        Integer companyId = accountMapper.get(userId).getCompanyId();

        List<Integer> openIds = options.getOpenIds();
        List<Integer> closedIds = options.getClosedIds();
        PersonalApplication personalApplication = new PersonalApplication();

        for (Integer closedId : closedIds) {
            personalApplicationMapper.delete(userId, closedId);
        }

        for (Integer openId : openIds) {
            personalApplication.setCompanyId(companyId);
            personalApplication.setAppId(openId);
            personalApplication.setUserId(userId);
            personalApplicationMapper.create(personalApplication);
        }
    }

    @Override
    public List<Integer> searchForChecked() {
        return personalApplicationMapper.searchForChecked(Universe.current().getUserId());
    }
}
