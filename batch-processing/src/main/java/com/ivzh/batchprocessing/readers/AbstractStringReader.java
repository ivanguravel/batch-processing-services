package com.ivzh.batchprocessing.readers;

import java.util.Objects;

public abstract class AbstractStringReader extends AbstractMqReader<String> {

    private static final String EMPTY = "";

    @Override
    public String read() throws Exception {
        String read = super.read();
        if (Objects.isNull(read)) {
            read = EMPTY;
        }
        return read;
    }
}
