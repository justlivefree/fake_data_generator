package com.learning.core.schema;

import com.learning.core.enums.OutputFormat;
import com.learning.core.exceptions.SchemaError;
import com.learning.core.fields.base.BaseField;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
public class Schema {
    // general
    private String locale;
    private Long count;
    private OutputFormat format;

    // sql
    private Boolean createTable;
    private String tableName;

    // list of fields
    private List<BaseField> fields;

    public void check() {
        if (locale == null || locale.isEmpty() || count == null || count <= 0L || format == null || fields == null || fields.isEmpty()) {
            throw new SchemaError("General fields are not complete.");
        }
        if (format == OutputFormat.SQL && (tableName == null || tableName.isEmpty())) {
            throw new SchemaError("Table name not found");
        }
    }

    public void setFormatAsString(String format) {
        try {
            this.format = OutputFormat.valueOf(format.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new SchemaError(e.getMessage(), "Invalid output format");
        }
    }

    public void setCreateTableAsString(String createTable) {
        try {
            this.createTable = Boolean.parseBoolean(createTable);
        } catch (IllegalArgumentException e) {
            throw new SchemaError(e.getMessage(), "Invalid argument for create table field");
        }
    }
}
