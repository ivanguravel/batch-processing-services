package com.ivzh.batchprocessing.writters;

import com.ivzh.batchprocessing.daos.HeaderDao;
import com.ivzh.shared.dtos.Header;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class HeadersWriter implements ItemWriter<List<Header>> {

    @Autowired
    private HeaderDao dao;

    @Override
    public void write(List<? extends List<Header>> items) throws Exception {
        for (List<Header> item : items) {
            dao.add(item);
        }
    }
}
