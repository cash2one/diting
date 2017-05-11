package com.diting.service;


import com.diting.model.Mail;
import com.diting.model.Mail_phone;
import com.diting.model.WalletLot;
import com.diting.model.options.MailOptions;
import com.diting.model.result.Flag;
import com.diting.model.result.Results;

import java.util.List;

/**
 * WalletLotService
 */
public interface MailService {
    List<Mail> search();
    public Mail save(Mail mail);
    Results<Mail> search_phone(MailOptions options);
    public Mail_phone save_phone(Mail_phone mail);
    Flag find_phone();
    Flag findUnreadLetterNum();
    Mail_phone find_p(Mail_phone mail);


}
