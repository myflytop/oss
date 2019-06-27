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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.tika.Tika;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.ruoyi.cms.oss.model.enums.OssEnum;
import com.ruoyi.cms.oss.utils.FileKit;
import com.ruoyi.cms.oss.utils.FilePathUtils;



/**
 * @author langhsu
 * @since  3.0
 */

@Component
public class NativeStorageImpl extends AbstractStorage {
	private Logger log = LoggerFactory.getLogger(NativeStorageImpl.class);
	@Override
	public void deleteFile(String storePath) {
		File file = new File(FilePathUtils.getStorageRootPath()+ storePath);
		// 文件存在, 且不是目录
		if (file.exists() && !file.isDirectory()) {
			file.delete();
			log.info("fileRepo delete " + storePath);
		}
	}

	@Override
	public String writeToStore(byte[] bytes, String pathAndFileName) throws Exception {
		String dest = FilePathUtils.getMainDir() + pathAndFileName;
		FileKit.writeByteArrayToFile(bytes, dest);
		return pathAndFileName;
	}

	
	
	
	
	private String getRealPath() {
		return "/myfile";
	}

	@Override
	public Map<String, Object> listFile(String dir, String accept, String exts) {
   	    //支持的扩展名
   	    String[] mExts = FileKit.getExts(exts);
        Map<String, Object> rs = new HashMap<>();
        //获取文件的基本路径
        dir=FileKit.getAccetpDir(dir);
		File file = new File(FilePathUtils.getStorageRootPath(),FilePathUtils.getStorageDir()+dir);
		File[] listFiles = file.listFiles();

		List<Map> dataList = new ArrayList<>();
		if (listFiles != null) {
			for (File f : listFiles) {
				if (OssEnum.SM.getValue().equals(f.getName())) {
					continue;
				}
				Map<String, Object> m = new HashMap<>();
				m.put("name", f.getName()); // 文件名称
				m.put("updateTime", f.lastModified()); // 修改时间
				m.put("isDir", f.isDirectory()); // 是否是目录
				if (f.isDirectory()) {
					m.put("type", "dir"); // 文件类型
				} else {
					String type;
					// 文件地址
					m.put("url", (dir.isEmpty() ? dir : (dir + "/")) + f.getName()); 
					// 获取文件类型
					String contentType = FileKit.getContentType(f);
					//获取后缀
					String suffix = f.getName().substring(f.getName().lastIndexOf(".") + 1);
					
					// 筛选文件类型
					if (accept != null && !accept.trim().isEmpty() && !accept.equals("file")) {
						if (contentType == null || !contentType.startsWith(accept + "/")) {
							continue;
						}
						if (mExts != null) {
							for (String ext : mExts) {
								if (!f.getName().endsWith("." + ext)) {
									continue;
								}
							}
						}
					}
					// 获取文件图标
					type=FileKit.getFileType(suffix, contentType);
					
					m.put("type", type);
					// 是否有缩略图 源文件地址/xxx/2019/08/04/xxx.png
					//缩略图地址 /xxx/sm/2019/08/04/xxx_sm.png
					
					//判断是否是图片					
					 if (contentType != null && contentType.startsWith("image/"))
					 {   String fileDir=dir.isEmpty() ? dir : (dir + "/");
					     //缩略图基本路径
						 String smUrl=FilePathUtils.getSmPath(fileDir,f.getName());
                         //判断缩略图书是否存在
							if (new File(FilePathUtils.getStorageRootPath(), FilePathUtils.getStorageDir()+"/" + smUrl).exists()) {
								// 缩略图是否
								m.put("hasSm", true);
								// 缩略图地址
								m.put("smUrl", smUrl); 
							}
					 }
					
					
				}
				dataList.add(m);
			}
		}
		// 根据上传时间排序
		Collections.sort(dataList, new Comparator<Map>() {
			@Override
			public int compare(Map o1, Map o2) {
				Long l1 = (long) o1.get("updateTime");
				Long l2 = (long) o2.get("updateTime");
				return l1.compareTo(l2);
			}
		});
		// 把文件夹排在前面
		Collections.sort(dataList, new Comparator<Map>() {
			@Override
			public int compare(Map o1, Map o2) {
				Boolean l1 = (boolean) o1.get("isDir");
				Boolean l2 = (boolean) o2.get("isDir");
				return l2.compareTo(l1);
			}
		});
		rs.put("code", 200);
		rs.put("msg", "查询成功");
		rs.put("data", dataList);
		return rs;
	}

	@Override
	public Map<String, Object> upload(MultipartFile file) {
		// TODO Auto-generated method stub
		Map<String, Object> rs=new HashMap<>();
		String fileRelativePath="";
		 rs.put("code", 200);
	     rs.put("msg", "Success");
		try {
			String oName=file.getOriginalFilename();
			
			String fload=FileKit.getFileDir(oName.substring(oName.lastIndexOf(".") + 1));
			
			fileRelativePath=store(file,fload);
			
			rs.put("url", fileRelativePath);
			if(!"".equals(fileRelativePath)&&new Tika().detect(new File(FilePathUtils.getMainDir()+fileRelativePath)).startsWith("image/")) {
			rs.put("smUrl", storeScale(file,fileRelativePath,100,100));
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			
			e.printStackTrace();
		}
		
			
		
		return rs;
	}

	@Override
	public String download(String fileDir, HttpServletResponse response) {
		// TODO Auto-generated method stub
		outputFile(fileDir, response);
		return null;
	}
	// 输出文件流
    private void outputFile(String fileDir, HttpServletResponse response) {
        // 判断文件是否存在
        File inFile = new File(FilePathUtils.getMainDir(), fileDir);
        if (!inFile.exists()) {
            PrintWriter writer = null;
            try {
                response.setContentType("text/html;charset=UTF-8");
                writer = response.getWriter();
                writer.write("<!doctype html><title>404 Not Found</title><h1 style=\"text-align: center\">404 Not Found</h1><hr/><p style=\"text-align: center\">Easy File Server</p>");
                writer.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return;
        }
        // 获取文件类型
        String contentType = null;
        try {
            // Path path = Paths.get(inFile.getName());
            // contentType = Files.probeContentType(path);
            contentType = new Tika().detect(inFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (contentType != null) {
            response.setContentType(contentType);
        } else {
            response.setContentType("application/force-download");
            String newName;
            try {
                newName = URLEncoder.encode(inFile.getName(), "utf-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
                newName = inFile.getName();
            }
            response.setHeader("Content-Disposition", "attachment;fileName=" + newName);
        }
        // 输出文件流
        OutputStream os = null;
        FileInputStream is = null;
        try {
            is = new FileInputStream(inFile);
            os = response.getOutputStream();
            byte[] bytes = new byte[1024];
            int len;
            while ((len = is.read(bytes)) != -1) {
                os.write(bytes, 0, len);
            }
            os.flush();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
                os.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

	@Override
	public Map<String, Object> removeFile(String filePath) {
		File file = new File(FilePathUtils.getMainDir()+ filePath);
		// 文件存在, 且不是目录
		 Map<String, Object> map = new HashMap<>();
	        map.put("code", 200);
	        map.put("msg", "删除成功");
		
		if (file.exists() && !file.isDirectory()) {
			
			try {
				if(new Tika().detect(file).startsWith("image/"))
				{
					File smFile=new File(FilePathUtils.getMainDir()+FilePathUtils.getSmPath(filePath, ""));
					if(smFile.exists()&&!smFile.isDirectory())
					{
						smFile.delete();
						
					}
				}
				
				
				file.delete();
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			log.info("fileRepo delete " + file);
		}
		
		return map;
	}
}
