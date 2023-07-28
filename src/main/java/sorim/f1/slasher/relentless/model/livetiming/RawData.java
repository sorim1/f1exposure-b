package sorim.f1.slasher.relentless.model.livetiming;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;

import java.util.HashMap;
import java.util.Map;

public class RawData {
    private final Map<String, Object> dataFields = new HashMap<>();

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
