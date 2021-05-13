package com.ivzh.batchprocessing.daos;

import com.ivzh.shared.dtos.Header;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Repository
public class HeaderDao {

    // TODO: should be replaced with something like redis :)
    private final Map<String, Long> holder = new ConcurrentHashMap<>();

    public List<Header> findAll() {
        return holder.entrySet().stream().map(e -> new Header(e.getKey(), e.getValue())).collect(Collectors.toList());
    }

    public Header findByName(String name) {
        return new Header(name, holder.getOrDefault(name, 0L));
    }

    public void add(Header header) {
        Long value = holder.get(header.getName());
        holder.merge(header.getName(), Optional.of(holder.get(header.getName())).orElse(0L) + header.getCount(), Long::sum);
    }

    public void add(List<Header> headers) {
        for (Header h : headers) {
            add(h);
        }
    }
}
