package com.fileupdate.fileupdatedemo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.util.List;

@Controller
public class FileUpdateController {

    //单个文件
    @PostMapping("/upload")
    @ResponseBody
    public String upload(@RequestParam("File") MultipartFile file){

        if (file.isEmpty()){
            return "文件上传失败 文件不能为空 ";
        }

        String filename = file.getOriginalFilename();//名字
        String filePath = "F:\\zyp\\filePath\\";//文件路径

        File file1 = new File(filePath+filename);
        try {
            file.transferTo(file1);
            return "文件上传成功";
        }catch (IOException e){
            System.out.println(e.toString());
        }
        return "上传文件失败";

    }

    //多个文件上传
    @PostMapping("/uploads")
    @ResponseBody
    public String multiUpload(HttpServletRequest request) {
        List<MultipartFile> files = ((MultipartHttpServletRequest) request).getFiles("file");
        String filePath = "F:\\zyp\\filePath\\";//文件路径
        for (int i = 0; i < files.size(); i++) {
            MultipartFile file = files.get(i);
            if (file.isEmpty()) {
                return "上传第" + (++i) + "个文件失败";
            }
            String fileName = file.getOriginalFilename();

            File dest = new File(filePath + fileName);
            try {
                file.transferTo(dest);
                System.out.println("第" + (i + 1) + "个文件上传成功");
            } catch (IOException e) {
                return "上传第" + (++i) + "个文件失败";
            }
        }

        return "上传成功";

    }




}
