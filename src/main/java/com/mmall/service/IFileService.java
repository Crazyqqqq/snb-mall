package com.mmall.service;

import org.springframework.web.multipart.MultipartFile;

/**
 * @program: mmall
 * @description: 文件处理的接口
 * @author: snb
 * @create: 2019-02-03 16:15
 */
public interface IFileService {

    //上传之后的文件名返回回去
    String upload(MultipartFile file, String path);
}
