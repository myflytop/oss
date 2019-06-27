package com.ruoyi.cms.oss.utils;

import org.apache.commons.io.FileUtils;
import org.apache.tika.Tika;

import com.ruoyi.cms.oss.model.enums.OssEnum;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/**
 * @author - langhsu
 * @create - 2018/3/9
 */
public class FileKit {
    // 文件允许格式
    private static List<String> allowFiles = Arrays.asList(".gif", ".png", ".jpg", ".jpeg", ".bmp");
    private final static String PREFIX_VIDEO="video/";
    private final static String PREFIX_IMAGE="image/";
    
   

    /**
     * 文件类型判断
     *
     * @param fileName
     * @return
     */
    public static boolean checkFileType(String fileName) {
        Iterator<String> type = allowFiles.iterator();
        while (type.hasNext()) {
            String ext = type.next();
            if (fileName.toLowerCase().endsWith(ext)) {
                return true;
            }
        }
        return false;
    }

    public static String getFilename(String path) {
        int pos = path.lastIndexOf(File.separator);
        return path.substring(pos + 1);
    }

    public static String getSuffix(String filename) {
        int pos = filename.lastIndexOf(".");
        return filename.substring(pos);
    }

    public static void writeByteArrayToFile(byte[] bytes, String dest) throws IOException {
        FileUtils.writeByteArrayToFile(new File(dest), bytes);
    }
    
    public static String getFileType(String suffix,String contentType) {
    	String type;
    	// 获取文件图标
		if ("ppt".equalsIgnoreCase(suffix) || "pptx".equalsIgnoreCase(suffix)) {
			type = "ppt";
		} else if ("doc".equalsIgnoreCase(suffix) || "docx".equalsIgnoreCase(suffix)) {
			type = "doc";
		} else if ("xls".equalsIgnoreCase(suffix) || "xlsx".equalsIgnoreCase(suffix)) {
			type = "xls";
		} else if ("pdf".equalsIgnoreCase(suffix)) {
			type = "pdf";
		} else if ("html".equalsIgnoreCase(suffix) || "htm".equalsIgnoreCase(suffix)) {
			type = "htm";
		} else if ("txt".equalsIgnoreCase(suffix)) {
			type = "txt";
		} else if ("swf".equalsIgnoreCase(suffix)) {
			type = "flash";
		} else if ("zip".equalsIgnoreCase(suffix) || "rar".equalsIgnoreCase(suffix)
				|| "7z".equalsIgnoreCase(suffix)) {
			type = "zip";
		} else if ("zip".equalsIgnoreCase(suffix) || "rar".equalsIgnoreCase(suffix)
				|| "7z".equalsIgnoreCase(suffix)) {
			type = "zip";
		} else if (contentType != null && contentType.startsWith("audio/")) {
			type = "mp3";
		} else if (contentType != null && contentType.startsWith("video/")) {
			type = "mp4";
		} /*
			 * else if (contentType != null && contentType.startsWith("image/")) { type =
			 * "file"; m.put("hasSm", true); m.put("smUrl", m.get("url")); // 缩略图地址 }
			 */ else {
			type = "file";
		}
		
		return type;
    }
    
    
    
    public static String getFileDir(String suffix) {
    	String typeFload;
    	// 获取文件图标
		if ("ppt".equalsIgnoreCase(suffix) || "pptx".equalsIgnoreCase(suffix)) {
			typeFload = OssEnum.DOUCMENT.getValue();
		} else if ("doc".equalsIgnoreCase(suffix) || "docx".equalsIgnoreCase(suffix)) {
			typeFload = OssEnum.DOUCMENT.getValue();
		} else if ("xls".equalsIgnoreCase(suffix) || "xlsx".equalsIgnoreCase(suffix)) {
			typeFload = OssEnum.DOUCMENT.getValue();
		} else if ("pdf".equalsIgnoreCase(suffix)) {
			typeFload = OssEnum.DOUCMENT.getValue();
		} else if ("html".equalsIgnoreCase(suffix) || "htm".equalsIgnoreCase(suffix)) {
			typeFload = OssEnum.DOUCMENT.getValue();
		} else if ("txt".equalsIgnoreCase(suffix)) {
			typeFload = OssEnum.DOUCMENT.getValue();
		} else if ("swf".equalsIgnoreCase(suffix)) {
			typeFload = OssEnum.OTHER.getValue();
		} else if ("zip".equalsIgnoreCase(suffix) || "rar".equalsIgnoreCase(suffix)
				|| "7z".equalsIgnoreCase(suffix)) {
			typeFload = OssEnum.REDUCE.getValue();
		} else if ("zip".equalsIgnoreCase(suffix) || "rar".equalsIgnoreCase(suffix)
				|| "7z".equalsIgnoreCase(suffix)) {
			typeFload = OssEnum.REDUCE.getValue();
		} else if ("mp3".equalsIgnoreCase(suffix)) {
			typeFload = OssEnum.MEDIA.getValue();
		} else if ("mp4".equalsIgnoreCase(suffix)) {
			typeFload = OssEnum.MEDIA.getValue();
		} /*
			 * else if (contentType != null && contentType.startsWith("image/")) { type =
			 * "file"; m.put("hasSm", true); m.put("smUrl", m.get("url")); // 缩略图地址 }
			 */ else {
				 typeFload = OssEnum.IMAGE.getValue();;
		}
		
		return "/"+typeFload;
    }
    
    public static String getContentType(File file) {
    	String contentType=null;
    	try {
			// Path path = Paths.get(f.getName());
			// contentType = Files.probeContentType(path);
			contentType = new Tika().detect(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
    	
    	return contentType;
    }
    
    /**
     * 获取斜杠路径
     * @param fileDir
     * @return
     */
    public static String endWidth(String fileDir) {
		if (fileDir == null) {
            fileDir = "/";
        }
        if (!fileDir.endsWith("/")) {
            fileDir += "/";
        }
        return fileDir;
	}
    
    /**
     * 按照逗号分隔
     * @param fileDir
     * @return
     */
    public static String[] getExts(String exts) {
    	String[] mExts=null;
    	 if (exts != null && !exts.trim().isEmpty()) {
             mExts = exts.split(",");
         }
    	 return mExts;
	}
    
    public static String getAccetpDir(String dir) {
    	 if (dir == null || "/".equals(dir)) {
             dir = "";
         } 
    	return dir;
    }

    public static void main(String[] args) {
		System.out.println(getSuffix("abc.png"));
	}
    
}
