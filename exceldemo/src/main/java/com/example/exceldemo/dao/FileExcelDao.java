package com.example.exceldemo.dao;

import com.example.exceldemo.entity.FileData;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class FileExcelDao {

    public void save(List<FileData> lists) {
        for (FileData list : lists) {
            System.out.println(list);
        }
    }

    public void saveName(HashSet<String> filenames) {
        for (String list : filenames) {
            System.out.println(list);
        }
    }
}
