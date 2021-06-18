package sorim.f1.slasher.relentless.model;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

public class LiveTimingDataMapForm {

//    @Getter
//    private OperationTypeEnum operationType;
//
//    public void setOperation_type(String operation_type) {
//        this.operationType = OperationTypeEnum.fromString(operation_type);
//    }

    @Getter
    @Setter
    private String schema;


    private Map<String, Object> dataFields = new HashMap<>();

    @JsonAnyGetter
    public Map<String, Object> getDataFields() {
        return dataFields;
    }

    @JsonAnySetter
    public void setDataField(String fieldName, Object value) {
        dataFields.put(fieldName, value);
    }

    public Object getDataField(String fieldName) {
        return dataFields.get(fieldName);
    }
}
