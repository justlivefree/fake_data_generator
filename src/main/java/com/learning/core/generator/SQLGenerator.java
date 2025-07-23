package com.learning.core.generator;

import com.learning.core.exceptions.GeneratorError;
import com.learning.core.fields.base.BaseField;
import com.learning.core.schema.Schema;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.util.StringJoiner;

public class SQLGenerator extends BaseGenerator {


    public SQLGenerator(Path templatePath) {
        super(templatePath);
    }

    public SQLGenerator(Schema schema) {
        super(schema);
    }


    public void save(Path outputPath) {
        try (FileWriter fileWriter = new FileWriter(outputPath.toString()); BufferedWriter bufferedWriter = new BufferedWriter(fileWriter)) {
            // table fields and creation
            StringJoiner tableFields = new StringJoiner(",\n");
            StringJoiner fieldsString = new StringJoiner(", ");
            for (var field : schema.getFields()) {
                fieldsString.add(field.getOption().getFieldName());
                tableFields.add("\t" + field.sqlTableField());
            }
            if (schema.getCreateTable()) {
                bufferedWriter.write("CREATE TABLE IF NOT EXISTS %s (\n%s\n);\n\n".formatted(schema.getTableName(), tableFields.toString()));
            }

            // insert into
            String stringQuery = "INSERT INTO %s (%s) values (%s);\n";
            for (long i = 0; i < schema.getCount(); i++) {
                StringJoiner values = new StringJoiner(", ");
                for (BaseField field : schema.getFields()) {
                    values.add(field.generate(faker, schema.getFormat()));
                }
                bufferedWriter.write(stringQuery.formatted(schema.getTableName(), fieldsString.toString(), values.toString()));
            }
        } catch (IOException e) {
            throw new GeneratorError("Invalid file: " + e.getMessage());
        }
    }

    @Override
    public String save() {
        return "";
    }
}
