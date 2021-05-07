package com.ivzh.batchprocessing.readers;

import org.springframework.batch.item.ItemReader;

import java.util.Deque;
import java.util.concurrent.ConcurrentLinkedDeque;

public abstract class AbstractMqReader<T> implements ItemReader<T> {

    protected final Deque<T> deque;

    public AbstractMqReader() {
        this.deque = new ConcurrentLinkedDeque<>();
    }

    @Override
    public T read() throws Exception {
        return deque.isEmpty() ? null : deque.poll();
    }

    public abstract void processQueue(T message);
}
