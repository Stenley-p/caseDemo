package com.example.exceldemo.controller;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelReader;
import com.alibaba.excel.read.metadata.ReadSheet;
import com.example.exceldemo.entity.FileData;
import com.example.exceldemo.listener.FileDataListener;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

@RestController
@RequestMapping(value = "/File")
public class FileController {

    @RequestMapping(value = "/upload")
    public String upload(@RequestParam("file")MultipartFile file){
        if (file.isEmpty()){
            return "null";
        }
        ExcelReader excelReader = EasyExcel.read((File) file, FileData.class, new FileDataListener()).build();
        ReadSheet readSheet = EasyExcel.readSheet(0).build();
        excelReader.read(readSheet);
        // 这里千万别忘记关闭，读的时候会创建临时文件，到时磁盘会崩的
        excelReader.finish();

        return "ok";
    }
}
