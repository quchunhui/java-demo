package pojo;

import annotation.Tag;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.influxdb.annotation.Column;
import org.influxdb.annotation.Measurement;

abstract class PojoBase {
    public static String querySql(Class clazz, String extent) {
        Measurement measurementAnnotation = (Measurement) clazz.getAnnotation(Measurement.class);
        String measurement = measurementAnnotation.name();

        Field[] fields = clazz.getDeclaredFields();
        StringBuilder buffer = new StringBuilder("");
        buffer.append(" SELECT ");
        for (int i = 0; i < fields.length; i++) {
            Field field = fields[i];
            Column columnAnnotation = field.getAnnotation(Column.class);
            if (columnAnnotation == null) {
                continue;
            }
            buffer.append(" last(").append(field.getName()).append(") as ").append(field.getName());
            if (i < fields.length - 1) {
                buffer.append(", ");
            }
        }
        buffer.append(" FROM ").append(measurement);
        buffer.append(" ");
        if (extent != null) {
            buffer.append(extent);
        }
        buffer.append(" ");
        return buffer.toString();
    }

    public static Map<String, List<String>> getFields(Class clazz) {
        Field[] fields = clazz.getDeclaredFields();

        Map<String, List<String>> fieldList = new HashMap<>();
        for (Field field : fields) {
            Tag tagAnnotation = field.getAnnotation(Tag.class);
            if (tagAnnotation == null) {
                continue;
            }

            String label = tagAnnotation.value();
            if (!fieldList.containsKey(label)) {
                fieldList.put(label, new ArrayList<>());
            }
            fieldList.get(label).add(field.getName());
        }

        return fieldList;
    }
}
