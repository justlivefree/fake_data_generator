package com.learning.core.generator;

import com.github.javafaker.Faker;
import com.learning.core.exceptions.GeneratorError;
import com.learning.core.parsers.JSONParser;
import com.learning.core.schema.Schema;
import lombok.Getter;
import org.json.JSONException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Locale;

public abstract class BaseGenerator {
    @Getter
    protected final Schema schema;
    protected final Faker faker;

    public BaseGenerator(Path jsonTemplatePath) {
        try {
            this.schema = new JSONParser(Files.readString(jsonTemplatePath)).getSchema();
            this.faker = new Faker(Locale.of(schema.getLocale()));
        } catch (JSONException e) {
            throw new GeneratorError("Invalid JSON: " + e.getMessage());
        } catch (IOException e) {
            throw new GeneratorError("Invalid file: " + e.getMessage());
        }
    }

    public BaseGenerator(Schema schema) {
        try {
            this.schema = schema;
            this.faker = new Faker(Locale.of(schema.getLocale()));
        } catch (JSONException e) {
            throw new GeneratorError("Invalid JSON: " + e.getMessage());
        }
    }

    public abstract void save(Path outputPath);

    public abstract String save();

}
