package com.learning.core.fields;

import com.github.javafaker.Faker;
import com.learning.core.enums.OutputFormat;
import com.learning.core.fields.base.BaseField;
import com.learning.core.schema.Option;
import lombok.Getter;

@Getter
public class PhoneNumberField extends BaseField {

    public PhoneNumberField(Option option) {
        super(option);
    }

    public String sqlTableField() {
        return sqlTable("VARCHAR(50)");
    }


    public String generate(Faker faker, OutputFormat format) {
        return stringFormater(faker.phoneNumber().cellPhone(), format);
    }
}
