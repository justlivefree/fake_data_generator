package com.learning.core.parsers;

import com.learning.core.enums.OutputFormat;
import com.learning.core.fields.base.BaseField;
import com.learning.core.schema.Option;
import com.learning.core.schema.Schema;

import java.util.ArrayList;
import java.util.List;

public class CommandParser extends BaseParser {
    public static Schema getSchema(String text) {
        Schema schema = new Schema();
        char[] arr = text.toCharArray();
        StringBuilder countValue = new StringBuilder();
        for (int i = text.indexOf("--count ") + 8; i < arr.length; i++) {
            if ('0' <= arr[i] && arr[i] <= '9') {
                countValue.append(arr[i]);
            } else {
                break;
            }
        }
        schema.setCount(Long.parseLong(countValue.toString()));

        StringBuilder localeValue = new StringBuilder();
        for (int i = text.indexOf("--locale ") + 9; i < arr.length; i++) {
            if (('a' <= arr[i] && arr[i] <= 'z') || ('A' <= arr[i] && arr[i] <= 'Z') || arr[i] == '-') {
                localeValue.append(arr[i]);
            } else {
                break;
            }
        }
        schema.setLocale(localeValue.toString());

        StringBuilder outputValue = new StringBuilder();
        for (int i = text.indexOf("--output ") + 9; i < arr.length; i++) {
            if (('a' <= arr[i] && arr[i] <= 'z') || ('A' <= arr[i] && arr[i] <= 'Z')) {
                outputValue.append(arr[i]);
            } else {
                break;
            }
        }
        String output = outputValue.toString().toUpperCase();
        System.out.println(outputValue);
        if (output.equals(OutputFormat.JSON.name()) || output.equals(OutputFormat.SQL.name()) || output.equals(OutputFormat.CSV.name())) {
            schema.setFormat(OutputFormat.valueOf(output));
        } else {
            throw new RuntimeException();
        }

        StringBuilder tableNameValue = new StringBuilder();
        for (int i = text.indexOf("--table-name ") + 13; i < arr.length; i++) {
            if (('a' <= arr[i] && arr[i] <= 'z') || ('A' <= arr[i] && arr[i] <= 'Z') || arr[i] == '_') {
                tableNameValue.append(arr[i]);
            } else {
                break;
            }
        }
        schema.setTableName(tableNameValue.toString());

        StringBuilder createTableValue = new StringBuilder();
        for (int i = text.indexOf("--create-table ") + 15; i < arr.length; i++) {
            if (('a' <= arr[i] && arr[i] <= 'z')) {
                createTableValue.append(arr[i]);
            } else {
                break;
            }
        }
        String createTable = createTableValue.toString();
        if (createTable.equals("true") || createTable.equals("false")) {
            schema.setCreateTable(Boolean.parseBoolean(createTable));
        } else {
            schema.setCreateTable(false);
        }


        StringBuilder fieldsValue = new StringBuilder();
        for (int i = text.indexOf("--fields ") + 9; i + 1 <= text.indexOf("]"); i++) {
            if (arr[i] == ' ' || arr[i] == '\n' || arr[i] == '\t' || arr[i] == '[') {
                continue;
            }
            fieldsValue.append(arr[i]);
        }

        String fields = fieldsValue.toString();
        List<BaseField> fieldsList = new ArrayList<>();
        while (!fields.isEmpty()) {
            int sepIndex = fields.indexOf(':');
            String fieldName = fields.substring(0, sepIndex);
            StringBuilder fieldTypeSB = new StringBuilder();
            Option option = null;
            int i = sepIndex + 1;
            for (; i < fields.length(); i++) {
                if (fields.charAt(i) == ',') {
                    break;
                }
                if (fields.charAt(i) == ':') {
                    int openCBIndex = fields.indexOf('{');
                    int closeCBIndex = fields.indexOf('}');
                    option = Option.parseCommand(fields.substring(openCBIndex, closeCBIndex + 1));
                    if (closeCBIndex == fields.length() - 1) {
                        i = closeCBIndex;
                    } else {
                        i = closeCBIndex + 1;
                    }
                    break;
                }
                fieldTypeSB.append(fields.charAt(i));
            }
            if (option == null) {
                option = new Option();
            }
            option.setFieldName(fieldName);
            System.out.println(option);
            if (i == fields.length()) {
                fields = "";
            } else {
                fields = fields.substring(i + 1);
            }
            fieldsList.add(toFieldObject(fieldTypeSB.toString(), option));
        }
        schema.setFields(fieldsList);
        return schema;
    }
}
