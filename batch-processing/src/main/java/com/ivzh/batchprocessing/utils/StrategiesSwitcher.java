package com.ivzh.batchprocessing.utils;

import org.springframework.util.CollectionUtils;

import java.util.Map;
import java.util.Objects;

public abstract class StrategiesSwitcher<T> {

    protected Map<String, T> strategyMap;

    public T switchStrategy(final String strategyName) {
        T strategy = strategyMap.get(strategyName);
        if (Objects.nonNull(strategy)) {
            return strategy;
        } else if(!CollectionUtils.isEmpty(strategyMap)) {
            return (T) strategyMap.values().toArray()[0];
        } else {
            throw new UnsupportedOperationException("Can't find any strategies");
        }
    }
}
