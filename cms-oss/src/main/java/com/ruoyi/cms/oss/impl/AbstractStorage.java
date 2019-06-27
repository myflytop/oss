/*
+--------------------------------------------------------------------------
|   Mblog [#RELEASE_VERSION#]
|   ========================================
|   Copyright (c) 2014, 2015 mtons. All Rights Reserved
|   http://www.mtons.com
|
+---------------------------------------------------------------------------
*/
package com.ruoyi.cms.oss.impl;


import org.springframework.web.multipart.MultipartFile;

import com.ruoyi.cms.oss.Storage;
import com.ruoyi.cms.oss.exception.StorageException;
import com.ruoyi.cms.oss.utils.FileKit;
import com.ruoyi.cms.oss.utils.FilePathUtils;
import com.ruoyi.cms.oss.utils.ImageUtils;

/**
 * @author langhsu
 * @since 3.0
 */

public abstract class AbstractStorage implements Storage {

    protected void validateFile(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new StorageException("文件不能为空");
        }

       /* if (!FileKit.checkFileType(file.getOriginalFilename())) {
            throw new StorageException("文件格式不支持");
        }*/
    }

    @Override
    public String store(MultipartFile file, String basePath) throws Exception {
        validateFile(file);
        return writeToStore(file.getBytes(), basePath, file.getOriginalFilename());
    }

    @Override
    public String storeScale(MultipartFile file, String basePath, int maxWidth) throws Exception {
        validateFile(file);
        byte[] bytes = ImageUtils.scaleByWidth(file, maxWidth);
        return writeToStore(bytes, basePath, file.getOriginalFilename());
    }

    @Override
    public String storeScale(MultipartFile file, String basePath, int width, int height) throws Exception {
        validateFile(file);
        byte[] bytes = ImageUtils.screenshot(file, width, height);
        return writeToStoreSm(bytes, basePath, file.getOriginalFilename());
    }

    public String writeToStore(byte[] bytes, String src, String originalFilename) throws Exception {
        String path = FilePathUtils.wholePathName(src, originalFilename);
        return writeToStore(bytes, path);
    }
    
    public String writeToStoreSm(byte[] bytes, String src, String originalFilename) throws Exception {
        String path = FilePathUtils.getSmPath(src, "");
       
        return writeToStore(bytes, path);
    }
}
