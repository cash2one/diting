package com.diting.service;




import com.diting.model.Fans;
import com.diting.model.options.FansOptions;
import com.diting.model.result.Flag;
import com.diting.model.result.Results;

import java.awt.*;
import java.util.*;
import java.util.List;


public interface FansService {

    Flag save_fans(Fans fans);
    Flag del_fans(Fans fans);
    public Flag find_fans(Fans fans);
    List<Fans> search_fans();
    List<Fans> search_count_fans();
    List<Fans> search_concern_list();
    List<Fans> search_my_fans(Fans fans);
    Integer find_fans_count(Fans fans);
    Results<Fans> findFansByLogin(FansOptions options);
    Fans searchFansByUserId(Integer ownUserId,Integer othUserId);

}
