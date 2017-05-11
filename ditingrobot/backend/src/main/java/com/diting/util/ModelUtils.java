package com.diting.util;

import com.alibaba.fastjson.JSONObject;
import com.diting.core.Universe;
import com.diting.error.AppErrors;
import com.diting.model.ModelEnums;
import com.diting.model.SecuredModel;
import com.diting.model.enums.UserType;
import org.apache.shiro.SecurityUtils;
import org.springframework.util.Assert;


import java.util.*;

import static com.diting.util.Utils.hash;


/**
 * ModelUtils.
 */
public final class ModelUtils {
    private static final String[] SECURED_MODEL_FILTERS = new String[]{
            "id", "owner", "ownerType", "deleted", "createdBy", "createdTime", "updatedBy", "updatedTime", "salt", "sign"};

    private ModelUtils() {
        // private ctor
    }
//
//    public static String getEligibleName(Account account) {
//        if (account.getUserProfile() != null)
//            return account.getUserProfile().getRealName();
//
//        if (account.getUsername() != null)
//            return account.getUsername();
//
//        if (account.getMobile() != null)
//            return account.getMobile();
//
//        if (account.getEmail() != null)
//            return account.getEmail();
//
//        return null;
//    }
//
//    public static String getEligibleMobile(Account account) {
//        if (account.getUserProfile() != null)
//            return account.getUserProfile().getMobile();
//
//        return account.getMobile();
//    }
//
//    public static List<Integer> getAllInterviewers(Participant participant) {
//        Set<Integer> interviewers = new HashSet<>();
//
//        if (participant.getInterviewResults() == null) {
//            return Collections.EMPTY_LIST;
//        }
//
//        for (InterviewResult result : participant.getInterviewResults()) {
//            for (Integer interviewer : result.getInterviewers()) {
//                interviewers.add(interviewer);
//            }
//        }
//
//        return new ArrayList<>(interviewers);
//    }
//
//    public static <T extends BaseModel> List<T> difference(List<T> a, List<T> b) {
//        List<T> results = new ArrayList<>();
//
//        if (a == null) {
//            return null;
//        }
//
//        if (b == null) {
//            return a;
//        }
//
//        Set<Integer> ids = new HashSet<>();
//        for (T model : b) {
//            ids.add(model.getId());
//        }
//
//        for (T model : a) {
//            if (!ids.contains(model.getId())) {
//                results.add(model);
//            }
//        }
//
//        return results;
//    }
//
//    //获取邮箱验证激活的最后有效时间
//    public static Date getEmailLastActivateTime(EnterpriseAccount account) {
//        if (account.getCreatedTime() == null)
//            return null;
//
//        Calendar calendar = Calendar.getInstance();
//        calendar.setTime(account.getCreatedTime());
//        calendar.add(Calendar.HOUR_OF_DAY, 2);
//
//        return calendar.getTime();
//    }
//
    public static String genSecuredSign(SecuredModel securedModel) {
        Assert.notNull(securedModel, "securedModel");
        Assert.notNull(securedModel.getSalt(), "salt");

        // use DES algorithm in the future
        // don't check-in cipher key
        // store cipher key on server protected place
        return hash(Utils.toJson(securedModel, SECURED_MODEL_FILTERS) + securedModel.getSalt());
    }
//
//    public static UserType parseUserType(String input) {
//        if (StringUtils.isEmpty(input))
//            return null;
//
//        return UserType.valueOf(input);
//    }
//
//    public static boolean isOwner(BaseModel model) {
//        if(model == null) return true;
//        if(SecurityUtils.getSubject().hasRole(ModelEnums.ROLE_SYSTEM)) return true;
//
//        Integer userId = Universe.current().getUserId();
//        UserType userType = Universe.current().getUserType();
//
//        return model.getOwnerType() == userType
//                && Utils.equals(model.getOwner(), userId);
//    }
//
//    public static boolean isOwner(Integer userId, UserType userType) {
//        if (SecurityUtils.getSubject().hasRole(ModelEnums.ROLE_SYSTEM))
//            return true;
//
//        Integer currentId = Universe.current().getUserId();
//        UserType currentType = Universe.current().getUserType();
//
//        return currentType == userType && Utils.equals(currentId, userId);
//    }
//
    public static void checkOwner(Integer userId, Integer userType) {
//        if (SecurityUtils.getSubject().hasRole(ModelEnums.ROLE_SYSTEM))
//            return;

        Integer currentId = Universe.current().getUserId();
        Integer currentType = Universe.current().getUserType();

        if (currentType != 3){
            if (currentType != userType || !Utils.equals(currentId, userId)) {
                throw AppErrors.INSTANCE.accessDenied().exception();
            }
        }
    }
//
//    public static void checkOwner(BaseModel model) {
//        if(model == null) return;
//        if(SecurityUtils.getSubject().hasRole(ModelEnums.ROLE_SYSTEM)) return;
//
//        Integer userId = Universe.current().getUserId();
//        UserType userType = Universe.current().getUserType();
//
//        if (model.getOwnerType() != userType || !Utils.equals(model.getOwner(), userId)) {
//            throw AppErrors.INSTANCE.accessDenied().exception();
//        }
//    }

    public static String json2lines(String json) {
        return json.replaceAll("\"", "").replaceAll(",", "\n").replaceAll("(\\{|\\})", "");
    }

    public static String lines2Json(String lines) {
        Map<String, String> jsonMap = new LinkedHashMap<>();

        for (String line : lines.split("\n")) {
            String[] parts = line.split(":");
            if (parts.length == 2) {
                jsonMap.put(parts[0].trim(), parts[1].trim());
            }
        }

        return JSONObject.toJSONString(jsonMap);
    }
}
