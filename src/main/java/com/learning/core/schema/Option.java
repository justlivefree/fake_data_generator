package com.learning.core.schema;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.json.JSONObject;


@Getter
@Setter
@NoArgsConstructor
@ToString
public class Option {
    private String fieldName;
    private boolean nullable;
    private long rangeFrom = 0;
    private long rangeTo = 100;

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
        // todo: add exception message
        return option;
    }

    public static Option parseCommand(String text) {
        if (text.charAt(0) != '{' || text.charAt(text.length() - 1) != '}') {
            throw new RuntimeException();
        }
        Option option = new Option();
        text = text.substring(1, text.length() - 1);
        System.out.println(text);
        String[] values = text.split(",");
        for (String value : values) {
            String[] temp = value.split("=");
            String key = temp[0].toLowerCase();
            String val = temp[1].toLowerCase();
            if (key.equals("nullable")) {
                if (val.equals("true") || val.equals("false")) {
                    option.setNullable(Boolean.parseBoolean(val));
                } else {
                    throw new RuntimeException(); // todo: add exception message
                }
            } else if (key.equals("to")) {
                if (val.matches("\\d+")) {
                    option.setRangeTo(Long.parseLong(val));
                } else {
                    throw new RuntimeException();// todo: add exception message
                }
            } else if (key.equals("from")) {
                if (val.matches("\\d+")) {
                    option.setRangeFrom(Long.parseLong(val));
                } else {
                    throw new RuntimeException();// todo: add exception message
                }
            }
        }
        // todo: check from and to ranges
        return option;
    }
}
