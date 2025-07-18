package com.learning.core.fields.base;

import com.github.javafaker.Faker;
import com.learning.core.enums.OutputFormat;
import com.learning.core.schema.Option;
import lombok.Getter;

import java.util.StringJoiner;

public abstract class BaseField {

    @Getter
    protected Option option;

    protected BaseField(Option option) {
        this.option = option;
    }

    protected String stringFormater(String text, OutputFormat format) {
        if (format == OutputFormat.SQL) {
            return "'%s'".formatted(text);
        }
        if (format == OutputFormat.JSON) {
            return "\"%s\"".formatted(text);
        }
        return text;
    }

    protected String sqlTable(String sqlField) {
        StringJoiner sj = new StringJoiner(" ");
        sj.add(option.getFieldName());
        sj.add(sqlField);
        if (!option.isNullable()) {
            sj.add("NOT NULL");
        }
        return sj.toString();
    }


    public abstract String sqlTableField();

    public abstract String generate(Faker faker, OutputFormat format);
}
