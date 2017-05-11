package com.diting.service.impl;


import blade.kit.logging.Logger;
import blade.kit.logging.LoggerFactory;
import com.diting.cache.Cache;
import com.diting.core.Universe;
import com.diting.dao.MailMapper;
import com.diting.dao.TeleOtherMapper;
import com.diting.model.Mail;
import com.diting.model.Mail_phone;
import com.diting.model.Robot;
import com.diting.model.TeleOther;
import com.diting.model.options.MailOptions;
import com.diting.model.result.Flag;
import com.diting.model.result.Results;
import com.diting.service.MailService;
import com.diting.service.RobotService;
import com.diting.weixin.DoSomething;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import redis.clients.jedis.Jedis;

import java.util.ArrayList;
import java.util.List;


/**
 * WalletLotServiceImpl.
 */
@Service("MailService")
@Transactional
public class MailServiceImpl implements MailService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MailServiceImpl.class);


    @Autowired
    private MailMapper mailMapper;

    @Autowired
    private TeleOtherMapper teleOtherMapper;

    @Autowired
    private RobotService robotService;

    @Autowired
    @Qualifier("redisCache")
    private Cache redis;

    @Override
    public List<Mail> search() {
        return mailMapper.search();
    }
    @Override
    public Mail save(Mail mail){
        mailMapper.save(mail);
        return mail;
    }





    @Override
    public Flag find_phone() {
        Mail_phone mail = new Mail_phone();
        String name = Universe.current().getUserName();
        mail.setPhone(name);
        List<Mail> find_mail = mailMapper.find_phone(mail);
        if(find_mail.size() == 0){
            TeleOther teleOther=new TeleOther();
            Robot robot=robotService.getByUserId(Universe.current().getUserId());
            List<TeleOther> teleOthers=new ArrayList<>();
            if(robot!=null){
                teleOther.setReceive_name(robot.getName());
                teleOthers=teleOtherMapper.getCount(teleOther);
            }
            Flag f = new Flag();
            if(teleOthers.size()==0){
                f.setFlg(0);
            }else{
                f.setFlg(1);
            }
            return f;
        }else{
            Flag f = new Flag();
            f.setFlg(1);
            return f;
        }
    }

    @Override
    public Flag findUnreadLetterNum() {
        Flag f = new Flag();
        Mail_phone mail = new Mail_phone();
        String name = Universe.current().getUserName();
        mail.setPhone(name);
        List<Mail> find_mail = mailMapper.find_phone(mail);
        f.setUnreadLetterNum(find_mail.size());

        TeleOther teleOther=new TeleOther();
        Robot robot=robotService.getByUserId(Universe.current().getUserId());
        List<TeleOther> teleOthers=new ArrayList<>();
        if(robot!=null){
            teleOther.setReceive_name(robot.getName());
            teleOthers=teleOtherMapper.getCount(teleOther);
        }
        f.setUnreadMessageNum(teleOthers.size());
        return f;
    }


    @Override
    public Results<Mail> search_phone(MailOptions options) {
        Results results = new Results();
        options.setPhone(Universe.current().getUserName());
        List<Mail> accounts = mailMapper.search_phoneForPage(options);
        results.setItems(accounts);
        results.setTotal(options.getTotalRecord());
        return results;
    }

    @Override
    public Mail_phone save_phone(Mail_phone mail){
        String name = Universe.current().getUserName();
        mail.setPhone(name);
        mail.setFlg("666");
        mailMapper.save_phone(mail);
        return mail;
    }


    @Override
    public Mail_phone find_p(Mail_phone mail) {
        String name = Universe.current().getUserName();
        Jedis jedis = new Jedis("101.201.120.206",6379);
        jedis.auth("diting");
        try {
//            System.out.println("主线程 ： "+Thread.currentThread().getName());
            DoSomething ds1 = new DoSomething(mail.getPhone(),name);
            Thread t1 = new Thread(ds1);
            t1.start();

            Thread.sleep(1000 * 5);
            Mail_phone m = new Mail_phone();
            m.setPhone(name);
            m.setFlg((String)jedis.get(name));
            m.setIds((String)jedis.get(name+"weichat"));
//            System.out.println("service get name "+jedis.get(name));
            LOGGER.info(Thread.currentThread().getName()+"  service get name "+jedis.get(name));
            return m;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
