package com.example.exceldemo.entity;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

@Data
public class FileData {

    private String id;
    @ExcelProperty(value = "classification",index = 0)
    private String classification;
    @ExcelProperty(value = "subCategory",index = 1)
    private String subCategory;
    @ExcelProperty(value = "intent",index = 2)
    private String intent;
    @ExcelProperty(value = "keyword",index = 3)
    private String keyword;
    @ExcelProperty(value = "userSpeech",index = 4)
    private String userSpeech;
    @ExcelProperty(value = "botAnwser1",index = 5)
    private String botAnwser1;
    @ExcelProperty(value = "botanwser2",index = 6)
    private String botanwser2;
    private String fileName;
}
