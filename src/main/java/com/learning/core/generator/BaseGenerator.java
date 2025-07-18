package com.learning.core.generator;

import com.github.javafaker.Faker;
import com.learning.core.exceptions.InvalidTemplateFile;
import com.learning.core.parsers.JSONTemplateParser;
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

    public BaseGenerator(Path templatePath) {
        try {
            JSONTemplateParser jsonParser = new JSONTemplateParser(Files.readString(templatePath));
            this.schema = jsonParser.getSchema();
            this.faker = new Faker(Locale.of(schema.getLocale()));
        } catch (JSONException e) {
            throw new InvalidTemplateFile(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public BaseGenerator(Schema schema) {
        try {
            this.schema = schema;
            this.faker = new Faker(Locale.of(schema.getLocale()));
        } catch (JSONException e) {
            throw new InvalidTemplateFile(e);
        }
    }

    public abstract void save(Path outputPath);

    public abstract String save();

}
