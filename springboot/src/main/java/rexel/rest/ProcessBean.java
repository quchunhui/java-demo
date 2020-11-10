package rexel.rest;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;

@Data
public class ProcessBean {
    private String aluGrade;
    private String aluState;
    private float thickness;
    private int width;
    private int length;
    private int isFilm;
    private int cnt;
    private String id;

    ProcessBean(JSONObject jsonObject) {
        if (jsonObject == null) {
            return;
        }
        aluGrade = jsonObject.getString("aluGrade");
        aluState = jsonObject.getString("aluState");
        thickness = jsonObject.getFloat("thickness");
        width = jsonObject.getInteger("width");
        length = jsonObject.getInteger("length");
        isFilm = jsonObject.getInteger("isFilm");
        cnt = jsonObject.getInteger("cnt");
        id = jsonObject.getString("id");
    }
}
