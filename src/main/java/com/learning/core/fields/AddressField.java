package com.learning.core.fields;

import com.github.javafaker.Faker;
import com.learning.core.enums.OutputFormat;
import com.learning.core.fields.base.BaseField;
import com.learning.core.schema.Option;

public class AddressField extends BaseField {

    public AddressField(Option option) {
        super(option);
    }

    @Override
    public String sqlTableField() {
        return sqlTable("VARCHAR(50)");
    }

    @Override
    public String generate(Faker faker, OutputFormat format) {
        return stringFormater(faker.address().fullAddress(), format);
    }
}
