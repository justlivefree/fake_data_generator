package com.learning.core.fields;

import com.github.javafaker.Faker;
import com.learning.core.enums.OutputFormat;
import com.learning.core.fields.base.BaseField;
import com.learning.core.schema.Option;

public class RowNumberField extends BaseField {
    private int rowNumberIndex = 1;

    public RowNumberField(Option option) {
        super(option);
    }

    @Override
    public String sqlTableField() {
        return sqlTable("INT");
    }

    @Override
    public String generate(Faker faker, OutputFormat format) {
        return String.valueOf(rowNumberIndex++);
    }
}
