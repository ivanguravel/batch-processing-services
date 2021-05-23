package com.ivzh.batchprocessing.writters;

import com.ivzh.batchprocessing.daos.HeaderDao;
import com.ivzh.shared.dtos.Header;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class HeadersWriter implements ItemWriter<Header> {

    @Autowired
    private HeaderDao dao;

    @Override
    public void write(List<? extends Header> items) throws Exception {
        for (Header header : items) {
            dao.add(header);
        }
    }
}
