package com.learning.core.parsers;

import com.learning.core.exceptions.JSONTemplateError;
import com.learning.core.fields.base.BaseField;
import com.learning.core.schema.Option;
import com.learning.core.schema.Schema;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class JSONParser extends BaseParser {
    private final String text;

    public JSONParser(String text) {
        this.text = text;
    }

    public Schema getSchema() {
        try {
            JSONObject jsonObject = new JSONObject(text);
            Schema schema = new Schema();
            if (jsonObject.has("locale")) {
                schema.setLocale(jsonObject.getString("locale"));
            }
            if (jsonObject.has("count")) {
                schema.setCount(jsonObject.getLong("count"));
            }
            if (jsonObject.has("format")) {
                schema.setFormatAsString(jsonObject.getString("format"));
            }
            if (jsonObject.has("table_name")) {
                schema.setTableName(jsonObject.getString("table_name"));
            }
            if (jsonObject.has("create_table")) {
                schema.setCreateTable(jsonObject.getBoolean("create_table"));
            }
            if (jsonObject.has("fields")) {
                JSONArray jsonFields = jsonObject.getJSONArray("fields");
                ArrayList<BaseField> fields = new ArrayList<>();
                jsonFields.forEach((field) -> {
                    JSONObject innerJsonObject = (JSONObject) field;
                    String fieldType = innerJsonObject.getString("type");
                    Option option = Option.parseJson(innerJsonObject);
                    option.check();
                    fields.add(toFieldObject(fieldType, option));
                });
                schema.setFields(fields);
            }
            schema.check();
            return schema;
        } catch (JSONException e) {
            throw new JSONTemplateError("Invalid JSON: " + e.getMessage());
        }
    }
}
