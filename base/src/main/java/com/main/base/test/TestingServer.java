package com.main.base.test;

import java.io.Closeable;
import java.io.IOException;
import java.util.concurrent.Callable;

public class TestingServer implements Closeable{
    private String connectionString = "192.168.3.140:2181";
    @Override
    public void close() throws IOException {

    }

    public String getConnectString() {
        return connectionString;
    }
}