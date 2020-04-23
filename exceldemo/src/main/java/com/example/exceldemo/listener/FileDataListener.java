package com.example.exceldemo.listener;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.example.exceldemo.dao.FileExcelDao;
import com.example.exceldemo.entity.FileData;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class FileDataListener extends AnalysisEventListener<FileData> {

    /**
     * 每隔10000条存储数据库，然后清理list ，方便内存回收
     */
    private static final int BATCH_COUNT = 5;
    //数据
    List<FileData> list = new ArrayList<>();
    //放文件名
    HashSet<String> filenames = new HashSet<>();
    //超过1w数量后迭代文件名
    private static int i = 0;

    /**
     * 这个每一条数据解析都会来调用
     *
     * @param data
     *            one row value. Is is same as {@link AnalysisContext#readRowHolder()}
     * @param context
     */
    @Override
    public void invoke(FileData data, AnalysisContext context) {
        //获取文件名
        String name = context.readWorkbookHolder().getFile().getName();
        //设置默认文件名
        String filename = name.substring(0, name.lastIndexOf("."))+"_"+ i;
        data.setFileName(filename);
        list.add(data);
        filenames.add(filename);
        // 达到BATCH_COUNT了，需要去存储一次数据库，防止数据几万条数据在内存，容易OOM
        if (list.size() >= BATCH_COUNT) {
            saveData();
            // 存储完成清理 list
            list.clear();
            i++;
        }
    }

    /**
     * 所有数据解析完成了 都会来调用
     *
     * @param context
     */
    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {
        // 这里也要保存数据，确保最后遗留的数据也存储到数据库
        saveData();
        //LOGGER.info("所有数据解析完成！");
    }

    private FileExcelDao fileExcelDao;

    public FileDataListener() {
        fileExcelDao = new FileExcelDao();
    }

    /**
     * 加上存储数据库
     */
    private void saveData() {
        //数据
        fileExcelDao.save(list);
        //库名
        fileExcelDao.saveName(filenames);
        //LOGGER.info("存储数据库成功！");
    }
}
