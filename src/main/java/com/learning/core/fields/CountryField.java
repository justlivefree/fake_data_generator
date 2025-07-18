package com.learning.core.fields;

import com.github.javafaker.Faker;
import com.learning.core.enums.OutputFormat;
import com.learning.core.fields.base.BaseField;
import com.learning.core.schema.Option;

public class CountryField extends BaseField {

    public CountryField(Option option) {
        super(option);
    }

    @Override
    public String sqlTableField() {
        return sqlTable("VARCHAR(50)");
    }

    @Override
    public String generate(Faker faker, OutputFormat format) {
        return stringFormater(faker.address().country(), format);
    }
}
