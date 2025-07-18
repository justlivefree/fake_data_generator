package com.learning.core.parsers;

import com.learning.core.enums.FieldType;
import com.learning.core.enums.OutputFormat;
import com.learning.core.fields.*;
import com.learning.core.fields.base.BaseField;
import com.learning.core.schema.Option;
import com.learning.core.schema.Schema;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class JSONTemplateParser {
    private JSONObject jsonObject;

    public JSONTemplateParser(String jsonString) {
        jsonObject = new JSONObject(jsonString);
    }

    public Schema getSchema() {
        Schema schema = new Schema();
        schema.setLocale(jsonObject.getString("locale"));
        schema.setCount(jsonObject.getLong("count"));
        schema.setTableName(jsonObject.getString("table_name"));
        schema.setCreateTable(jsonObject.getBoolean("create_table"));
        schema.setFormat(OutputFormat.valueOf(jsonObject.getString("format").toUpperCase()));

        // include create table
        boolean createTable = false;
        if (jsonObject.has("create_table")) {
            createTable = jsonObject.getBoolean("create_table");
        }
        schema.setCreateTable(createTable);

        // check fields
        JSONArray jsonFields = jsonObject.getJSONArray("fields");
        ArrayList<BaseField> fields = new ArrayList<>();
        jsonFields.forEach((field) -> {
            fields.add(fromJsonToFieldObject((JSONObject) field));
        });

        schema.setFields(fields);
        return schema;
    }

    public static BaseField fromJsonToFieldObject(JSONObject jsonObject) {
        String type = jsonObject.getString("type");
        Option option = Option.parseJson(jsonObject);
        if (type.equalsIgnoreCase(FieldType.FULL_NAME.toString())) {
            return new FullNameField(option);
        }
        if (type.equalsIgnoreCase(FieldType.FIRST_NAME.toString())) {
            return new FirstNameField(option);
        }
        if (type.equalsIgnoreCase(FieldType.LAST_NAME.toString())) {
            return new LastNameField(option);
        }
        if (type.equalsIgnoreCase(FieldType.PHONE_NUMBER.toString())) {
            return new PhoneNumberField(option);
        }
        if (type.equalsIgnoreCase(FieldType.EMAIL.toString())) {
            return new EmailField(option);
        }
        if (type.equalsIgnoreCase(FieldType.USERNAME.toString())) {
            return new UsernameField(option);
        }
        if (type.equalsIgnoreCase(FieldType.COUNTRY.toString())) {
            return new CountryField(option);
        }
        if (type.equalsIgnoreCase(FieldType.CITY.toString())) {
            return new CityField(option);
        }
        if (type.equalsIgnoreCase(FieldType.STREET.toString())) {
            return new StreetField(option);
        }
        if (type.equalsIgnoreCase(FieldType.ADDRESS.toString())) {
            return new AddressField(option);
        }
        if (type.equalsIgnoreCase(FieldType.GUID.toString())) {
            return new GUIDField(option);
        }
        if (type.equalsIgnoreCase(FieldType.ROW_NUMBER.toString())) {
            return new RowNumberField(option);
        }
        if (type.equalsIgnoreCase(FieldType.NUMBER.toString())) {
            return new NumberField(option);
        }
        if (type.equalsIgnoreCase(FieldType.IPV4_ADDRESS.toString())) {
            return new IPv4Address(option);
        }
        if (type.equalsIgnoreCase(FieldType.IPV6_ADDRESS.toString())) {
            return new IPv6Address(option);
        }
        if (type.equalsIgnoreCase(FieldType.MAC_ADDRESS.toString())) {
            return new MACAddress(option);
        }
        if (type.equalsIgnoreCase(FieldType.CREDIT_CARD.toString())) {
            return new CreditCardField(option);
        }
        return null;
    }

    public JSONObject toJson() {
        return null;
    }

}
