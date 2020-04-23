package com.example.exceldemo;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelReader;
import com.alibaba.excel.read.metadata.ReadSheet;
import com.example.exceldemo.entity.FileData;
import com.example.exceldemo.listener.FileDataListener;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;

@SpringBootTest
class ExceldemoApplicationTests {

    @Test
    void contextLoads() {
        // 有个很重要的点 DemoDataListener 不能被spring管理，要每次读取excel都要new,然后里面用到spring可以构造方法传进去
        // 写法1：
        //String fileName = new String("C:\\Users\\Administrator\\Desktop\\ALL_KIDXDATA.xlsx");
        // 这里 需要指定读用哪个class去读，然后读取第一个sheet 文件流会自动关闭
        //EasyExcel.read(fileName, FileData.class, new FileDataListener()).sheet().doRead();

        // 写法2：
        File fileName = new File("C:\\Users\\Administrator\\Desktop\\ALL_KIDXDATA.xlsx");
        ExcelReader excelReader = EasyExcel.read(fileName, FileData.class, new FileDataListener()).build();
        ReadSheet readSheet = EasyExcel.readSheet(0).build();
        excelReader.read(readSheet);
        // 这里千万别忘记关闭，读的时候会创建临时文件，到时磁盘会崩的
        excelReader.finish();
    }

}
