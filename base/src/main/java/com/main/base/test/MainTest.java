package com.main.base.test;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class MainTest {
    public static void main(String[] args) {
        //这是要替代for循环吗
        List<Integer> list = IntStream.range(1, 10)
                .mapToObj(__ -> ThreadLocalRandom.current().nextInt())
                .collect(Collectors.toList ());
        list.forEach(System.out::println);
    }
}
