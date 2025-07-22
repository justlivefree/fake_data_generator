package com.learning.core.parsers;

import com.learning.core.enums.OutputFormat;
import com.learning.core.fields.base.BaseField;
import com.learning.core.schema.Option;
import com.learning.core.schema.Schema;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class JSONTemplateParser extends BaseParser {

    public static Schema getSchema(String text) {
        JSONObject jsonObject = new JSONObject(text);
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
            JSONObject innerJsonObject = (JSONObject) field;
            String fieldType = innerJsonObject.getString("type");
            Option option = Option.parseJson(innerJsonObject);
            fields.add(toFieldObject(fieldType, option));
        });

        schema.setFields(fields);
        return schema;
    }
}
