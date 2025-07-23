package com.learning.core.parsers;

import com.learning.core.fields.base.BaseField;
import com.learning.core.schema.Option;
import com.learning.core.schema.Schema;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class CommandParser extends BaseParser {
    private final String text;
    private final char[] arr;
    private final Predicate<Character> isLowerCase = c -> 'a' <= c && c <= 'z';
    private final Predicate<Character> isUpperCase = c -> 'A' <= c && c <= 'Z';
    private final Predicate<Character> isDigit = c -> '0' <= c && c <= '9';
    private final Predicate<Character> isUnderscore = c -> c == '_';
    private final Predicate<Character> isHyphen = c -> c == '-';


    public CommandParser(String text) {
        this.text = text;
        this.arr = text.toCharArray();
    }

    public StringBuilder getField(String field, Predicate<Character> predicate, boolean throwError) {
        StringBuilder fieldSB = new StringBuilder();
        int fieldValueIndex = text.indexOf(field);
        if (fieldValueIndex >= 0) {
            for (int i = fieldValueIndex + field.length(); i < arr.length; i++) {
                if (predicate.test(arr[i])) {
                    fieldSB.append(arr[i]);
                } else {
                    break;
                }
            }
        }
        return fieldSB;
    }

    public Schema getSchema() {
        Schema schema = new Schema();

        // COUNT
        StringBuilder countSB = getField("--count ", isDigit, true);
        if (!countSB.isEmpty()) {
            schema.setCount(Long.parseLong(countSB.toString()));
        }

        // LOCALE
        StringBuilder localeSB = getField("--locale ", isLowerCase.or(isUpperCase).or(isHyphen), true);
        schema.setLocale(localeSB.toString());

        // OUTPUT
        StringBuilder outputSB = getField("--output ", isLowerCase.or(isUpperCase), true);
        schema.setFormatAsString(outputSB.toString());

        // TABLE NAME
        StringBuilder tableNameSB = getField("--table-name ", isLowerCase.or(isUpperCase).or(isUnderscore), false);
        schema.setTableName(tableNameSB.toString());

        // CREATE TABLE
        StringBuilder createTableSB = getField("--create-table ", isLowerCase, false);
        schema.setCreateTableAsString(createTableSB.toString());

        // FIELDS
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
            option.check();
            if (i == fields.length()) {
                fields = "";
            } else {
                fields = fields.substring(i + 1);
            }
            fieldsList.add(toFieldObject(fieldTypeSB.toString(), option));
        }
        schema.setFields(fieldsList);
        schema.check();
        return schema;
    }
}
