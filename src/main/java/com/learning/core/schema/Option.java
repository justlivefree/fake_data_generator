package com.learning.core.schema;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.json.JSONObject;

@Getter
@Setter
@NoArgsConstructor
public class Option {
    private String fieldName;
    private boolean nullable;
    private long rangeFrom = 0;
    private long rangeTo = 100;
    private String format;

    public Option(String fieldName, boolean nullable) {
        this.fieldName = fieldName;
        this.nullable = nullable;
    }

    public static Option parseJson(JSONObject jsonObject) {
        Option option = new Option();
        // field name
        option.setFieldName(jsonObject.getString("name"));
        // nullable
        boolean nullable = false;
        if (jsonObject.has("nullable")) {
            nullable = jsonObject.getBoolean("nullable");
        }
        option.setNullable(nullable);
        // ranges
        if (jsonObject.has("from") && jsonObject.has("to")) {
            option.setRangeFrom(jsonObject.getLong("from"));
            option.setRangeTo(jsonObject.getLong("to"));
        }
        // format
        if (jsonObject.has("format")) {
            option.setFormat(jsonObject.getString("format"));
        }
        return option;
    }
}
