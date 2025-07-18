package com.learning.core.schema;

import com.learning.core.enums.OutputFormat;
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
    private long count;
    private OutputFormat format;

    // sql
    private boolean createTable;
    private String tableName;

    // json
    private int indent;

    // list of fields
    private List<BaseField> fields;
}
