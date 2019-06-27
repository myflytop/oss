/*
+--------------------------------------------------------------------------
|   Mblog [#RELEASE_VERSION#]
|   ========================================
|   Copyright (c) 2014, 2015 mtons. All Rights Reserved
|   http://www.mtons.com
|
+---------------------------------------------------------------------------
*/
package com.ruoyi.cms.oss;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

/**
 * @author langhsu
 *
 */
public interface Storage {

	/**
	 * 存储图片
	 * @param file
	 * @param basePath
	 * @return
	 * @throws IOException
	 */
	String store(MultipartFile file, String basePath) throws Exception;

	/**
	 * 存储压缩图片
	 * @param file
	 * @param basePath
	 * @return
	 * @throws IOException
	 */
	String storeScale(MultipartFile file, String basePath, int maxWidth) throws Exception;

	/**
	 * 存储压缩图片
	 * @param file
	 * @param basePath
	 * @return
	 * @throws IOException
	 */
	String storeScale(MultipartFile file, String basePath, int width, int height) throws Exception;

	/**
	 * 存储路径
	 * @param storePath
	 */
	void deleteFile(String storePath);
	
	/**
	 * 获取图片列表
	 * @param dir
	 * @param accept
	 * @param exts
	 * @return
	 */
	Map<String, Object> listFile(String dir, String accept, String exts);
    
	/**
	 * 写入图片
	 * @param bytes
	 * @param pathAndFileName
	 * @return
	 * @throws Exception
	 */
	String writeToStore(byte[] bytes, String pathAndFileName) throws Exception;

	Map<String, Object> upload(MultipartFile file);

	String download(String fileDir, HttpServletResponse response);

	Map<String, Object> removeFile(String file);
}
