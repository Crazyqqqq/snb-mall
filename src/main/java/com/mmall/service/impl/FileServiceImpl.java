package com.mmall.service.impl;

import com.google.common.collect.Lists;
import com.mmall.service.IFileService;
import com.mmall.util.FTPUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

/**
 * @program: mmall
 * @description: 文件处理实现类
 * @author: snb
 * @create: 2019-02-03 16:16
 */
@Service("iFileService")
public class FileServiceImpl implements IFileService {

    private Logger logger=LoggerFactory.getLogger(FileServiceImpl.class);

    /**
     * 上传之后的文件名返回回去
     * @param file
     * @param path
     * @return
     */
    public String upload(MultipartFile file,String path){
        //得到上传时的文件名
        String fileName=file.getOriginalFilename();
        //获取扩展名
        String fileExtensionName=fileName.substring(fileName.lastIndexOf(".")+1);
        String uploadFileName=UUID.randomUUID().toString()+"."+fileExtensionName;
        logger.info("开始上传文件，上传文件的文件名:{},上传的路径:{},新文件名:{}",fileName,path,uploadFileName);

        //创建目录
        File fileDir=new File(path);
        if(!fileDir.exists()){
            //允许写访问权限
            fileDir.setWritable(true);
            //创建文件夹
            fileDir.mkdirs();
        }

        //创建文件
        File targetFile=new File(path,uploadFileName);

        try {
            //transferto()方法，是springmvc封装的方法，用于图片上传时，把内存中图片写入磁盘
            file.transferTo(targetFile);
            //文件已经上传成功，传到了upload文件夹下

            //将targetFile上传到我们的FTP服务器上
            FTPUtil.uploadFile(Lists.newArrayList(targetFile));
            //已经上传到FTP服务器上

            //上传完之后，删除upload下面的文件
            targetFile.delete();


        } catch (IOException e) {
            logger.error("上传文件异常",e);
            return null;
        }

        return targetFile.getName();

    }



}
