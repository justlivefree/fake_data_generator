package com.learning.core.fields;

import com.github.javafaker.Faker;
import com.learning.core.enums.OutputFormat;
import com.learning.core.fields.base.BaseField;
import com.learning.core.schema.Option;

public class NumberField extends BaseField {

    public NumberField(Option option) {
        super(option);
    }

    @Override
    public String sqlTableField() {
        return sqlTable("INT");
    }

    @Override
    public String generate(Faker faker, OutputFormat format) {
        return String.valueOf(faker.number().numberBetween(option.getRangeFrom(), option.getRangeTo()));
    }
}
