package com.learning.core.parsers;

import com.learning.core.enums.FieldType;
import com.learning.core.fields.*;
import com.learning.core.fields.base.BaseField;
import com.learning.core.schema.Option;

public abstract class BaseParser {


    public static BaseField toFieldObject(String fieldType, Option option) {
        if (fieldType.equalsIgnoreCase(FieldType.FULL_NAME.toString())) {
            return new FullNameField(option);
        }
        if (fieldType.equalsIgnoreCase(FieldType.FIRST_NAME.toString())) {
            return new FirstNameField(option);
        }
        if (fieldType.equalsIgnoreCase(FieldType.LAST_NAME.toString())) {
            return new LastNameField(option);
        }
        if (fieldType.equalsIgnoreCase(FieldType.PHONE_NUMBER.toString())) {
            return new PhoneNumberField(option);
        }
        if (fieldType.equalsIgnoreCase(FieldType.EMAIL.toString())) {
            return new EmailField(option);
        }
        if (fieldType.equalsIgnoreCase(FieldType.USERNAME.toString())) {
            return new UsernameField(option);
        }
        if (fieldType.equalsIgnoreCase(FieldType.COUNTRY.toString())) {
            return new CountryField(option);
        }
        if (fieldType.equalsIgnoreCase(FieldType.CITY.toString())) {
            return new CityField(option);
        }
        if (fieldType.equalsIgnoreCase(FieldType.STREET.toString())) {
            return new StreetField(option);
        }
        if (fieldType.equalsIgnoreCase(FieldType.ADDRESS.toString())) {
            return new AddressField(option);
        }
        if (fieldType.equalsIgnoreCase(FieldType.GUID.toString())) {
            return new GUIDField(option);
        }
        if (fieldType.equalsIgnoreCase(FieldType.ROW_NUMBER.toString())) {
            return new RowNumberField(option);
        }
        if (fieldType.equalsIgnoreCase(FieldType.NUMBER.toString())) {
            return new NumberField(option);
        }
        if (fieldType.equalsIgnoreCase(FieldType.IPV4_ADDRESS.toString())) {
            return new IPv4Address(option);
        }
        if (fieldType.equalsIgnoreCase(FieldType.IPV6_ADDRESS.toString())) {
            return new IPv6Address(option);
        }
        if (fieldType.equalsIgnoreCase(FieldType.MAC_ADDRESS.toString())) {
            return new MACAddress(option);
        }
        if (fieldType.equalsIgnoreCase(FieldType.CREDIT_CARD.toString())) {
            return new CreditCardField(option);
        }
        return null;
    }
}
