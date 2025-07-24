package com.learning.core.schema;


import com.learning.core.exceptions.SchemaError;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.json.JSONException;
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
        try {
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
            return option;
        } catch (JSONException e) {
            throw new SchemaError("Invalid schema: " + e.getMessage(), "Invalid values in options");
        }

    }

    public static Option parseCommand(String text) {
        if (text.charAt(0) != '{' || text.charAt(text.length() - 1) != '}') {
            throw new SchemaError("Invalid schema: {} are not correct");
        }
        Option option = new Option();
        text = text.substring(1, text.length() - 1);
        String[] values = text.split(",");
        for (String value : values) {
            String[] temp = value.split("=");
            String key = temp[0].toLowerCase();
            String val = temp[1].toLowerCase();
            if (key.equals("nullable")) {
                if (val.equals("true") || val.equals("false")) {
                    option.setNullable(Boolean.parseBoolean(val));
                } else {
                    throw new SchemaError("Invalid schema: illegal argument given for nullable");
                }
            } else if (key.equals("to")) {
                if (val.matches("\\d+")) {
                    option.setRangeTo(Long.parseLong(val));
                } else {
                    throw new SchemaError("Invalid schema: illegal argument given for rangeTo");
                }
            } else if (key.equals("from")) {
                if (val.matches("\\d+")) {
                    option.setRangeFrom(Long.parseLong(val));
                } else {
                    throw new SchemaError("Invalid schema: illegal argument given for rangeFrom");
                }
            }
        }
        return option;
    }

    public void check() {
        if (fieldName == null || !fieldName.matches("^[a-zA-Z_]+$")) {
            throw new SchemaError("Invalid field name");
        }
        if (rangeFrom > rangeTo) {
            throw new SchemaError("Invalid range values");
        }
    }
}
