package com.fileupdate.fileupdatedemo.controller;

import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.*;

@Controller
public class DownLoadController {

    /**
    * 1.通过ResponseEntity<InputStreamResource>实现
    * */

    @RequestMapping("/download/{fileName}")
    public ResponseEntity<Object> downloadFile(@PathVariable("fileName")String fileName) throws FileNotFoundException {


        String filePath = "F:\\zyp\\filePath\\";

        File file = new File(filePath + fileName);

        InputStreamResource resource = new InputStreamResource(new FileInputStream((file)));

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition",String.format("attachment;filename=\"%s\"",file.getName()));
        headers.add("Cache-Control","no-cache,no-store,must-revalidate");
        headers.add("Pragma","no-cache");
        headers.add("Expires","0");

        return ResponseEntity.ok()
                .headers(headers)
                .contentLength(file.length())
                .contentType(MediaType.parseMediaType("application/text"))
                .body(resource);

    }

    /**
    * 2.通过写HttpServletResponse的OutputStream实现
    * */
    @RequestMapping("/download2/{fileName}")
    public void getDownLoad(HttpServletResponse response, @PathVariable("fileName")String fileName){

        String filePath = "F:\\zyp\\filePath\\";
        File file = new File(filePath + fileName);

        if (file.exists()){//检查目录是否存在
            response.setContentType("application/octet-stream");
            response.setHeader("content-type", "application/octet-stream");
            response.setHeader("Content-Disposition", "attachment;fileName=" + fileName);// 设置文件名

            byte[] buffer = new byte[1024];

            FileInputStream fileInputStream = null;

            BufferedInputStream bufferedInputStream = null;

            try {
                fileInputStream = new FileInputStream(file);
                bufferedInputStream = new BufferedInputStream(fileInputStream);
                ServletOutputStream outputStream = response.getOutputStream();
                int i = bufferedInputStream.read(buffer);

                while (i != -1){
                    outputStream.write(buffer,0,i);
                    i=bufferedInputStream.read(buffer);

                }

                System.out.println("success");
            } catch (Exception e) {
                e.printStackTrace();
            }finally {
                if (bufferedInputStream != null){
                    try {
                        bufferedInputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                if (fileInputStream != null){
                    try {
                        fileInputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

    }

}
