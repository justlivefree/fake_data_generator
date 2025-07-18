package com.learning.core.fields;

import com.github.javafaker.Faker;
import com.learning.core.enums.OutputFormat;
import com.learning.core.fields.base.BaseField;
import com.learning.core.schema.Option;

import java.util.UUID;

public class GUIDField extends BaseField {
    public GUIDField(Option option) {
        super(option);
    }

    @Override
    public String sqlTableField() {
        return sqlTable("UUID");
    }

    @Override
    public String generate(Faker faker, OutputFormat format) {
        return stringFormater(UUID.randomUUID().toString(), format);
    }
}
