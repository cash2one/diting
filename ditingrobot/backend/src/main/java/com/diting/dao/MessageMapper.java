package com.diting.dao;

import com.diting.model.Message;
import com.diting.model.options.MessageOptions;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by liufei on 2016/10/11.
 */
public interface MessageMapper {
    void create(Message message);

    List<Message> searchMessageForPage(MessageOptions options);

    List<Message> searchByMessageId(@Param("id") Integer id);

    Message searchMessage(@Param("id") Integer id);

    void batchDelete(List<Message> messages);
}
