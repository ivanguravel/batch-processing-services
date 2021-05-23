package com.ivzh.batchprocessing.processors;

import com.ivzh.batchprocessing.daos.HeaderDao;
import com.ivzh.shared.dtos.Header;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Objects;

public class HeadersCalculatingProcessor implements ItemProcessor<Header, Header>  {

    @Autowired
    private HeaderDao dao;

    @Override
    public Header process(Header item) {
        Header headerFromStorage = dao.findByName(item.getName());
        if (Objects.nonNull(headerFromStorage)) {
            item.setCount(item.getCount() + headerFromStorage.getCount());
        }
        return item;
    }
}
