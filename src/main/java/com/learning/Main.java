package com.learning;

import com.learning.core.generator.JSONGenerator;

import java.nio.file.Path;

public class Main {
    public static void main(String[] args) {
        JSONGenerator jg = new JSONGenerator(Path.of("template.json"));
        System.out.println(jg.save());
    }
}
