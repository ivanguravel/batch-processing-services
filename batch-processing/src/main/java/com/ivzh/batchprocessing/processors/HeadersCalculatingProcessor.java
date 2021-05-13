package com.ivzh.batchprocessing.processors;

import com.ivzh.batchprocessing.daos.HeaderDao;
import com.ivzh.shared.dtos.Header;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.groupingBy;

public class HeadersCalculatingProcessor implements ItemProcessor<Header, List<Header>>  {

    @Autowired
    private HeaderDao dao;

    @Override
    public List<Header> process(Header item) {
        List<Header> headers = dao.findAll();
        headers.add(item);
        List<Header> headersResult = headers
                .stream()
                .collect(groupingBy(Header::getName))
                .entrySet()
                .stream()
                .map(e ->
                        new Header(e.getKey(), e.getValue().stream().map(header -> header.getCount())
                                .reduce((count1, count2) -> count1 + count2).get()))
                .collect(Collectors.toList());
        return headersResult;
    }
}
