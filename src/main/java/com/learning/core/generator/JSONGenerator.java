package com.learning.core.generator;

import com.learning.core.enums.OutputFormat;
import com.learning.core.exceptions.GeneratorError;
import com.learning.core.fields.base.BaseField;
import com.learning.core.schema.Schema;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.util.StringJoiner;

public class JSONGenerator extends BaseGenerator {
    public JSONGenerator(Path templatePath) {
        super(templatePath);
    }

    public JSONGenerator(Schema schema) {
        super(schema);
    }

    @Override
    public void save(Path outputPath) {
        try (FileWriter fileWriter = new FileWriter(outputPath.toString());
             BufferedWriter bufferedWriter = new BufferedWriter(fileWriter)
        ) {
            bufferedWriter.write("[\n");
            for (long i = 0; i < schema.getCount(); i++) {
                StringJoiner jsonString = new StringJoiner(",\n", "\t{\n", "\n\t}");
                for (BaseField field : schema.getFields()) {
                    jsonString.add("\t\t\"%s\":%s".formatted(
                            field.getOption().getFieldName(),
                            field.generate(faker, OutputFormat.JSON)
                    ));
                }
                if (i == schema.getCount() - 1) {
                    bufferedWriter.write(jsonString + "\n");
                } else {
                    bufferedWriter.write(jsonString + ",\n");
                }
            }
            bufferedWriter.write("\n]");
        } catch (IOException e) {
            throw new GeneratorError(e.getMessage(), "Error occurred while saving file");
        }
    }

    @Override
    public String save() {
        StringJoiner result = new StringJoiner(",\n", "[\n", "\n]");
        for (long i = 0; i < schema.getCount(); i++) {
            StringJoiner jsonString = new StringJoiner(",\n", "\t{\n", "\n\t}");
            for (BaseField field : schema.getFields()) {
                jsonString.add("\t\t\"%s\":%s".formatted(
                        field.getOption().getFieldName(),
                        field.generate(faker, OutputFormat.JSON)
                ));
            }
            result.add(jsonString.toString());
        }
        return result.toString();
    }
}
