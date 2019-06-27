/**
 * 
 */
package com.ruoyi.cms.oss.utils;

import java.util.Date;
import java.util.UUID;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.text.RandomStringGenerator;

import com.ruoyi.cms.oss.model.enums.OssEnum;

/**
 * @author langhsu
 *
 */
public class FilePathUtils {
	private static final int[]  AVATAR_GRIDS = new int[] {3,3,3};
	private static final int    AVATAR_LENGTH = 9;
	

	private static final String YMDHMS = "/yyyy/MM/dd";

	private static RandomStringGenerator randomString = new RandomStringGenerator.Builder().withinRange('a', 'z').build();
	
	public static String getAvatar(long key) {
		String r = String.format("%09d", key);
		StringBuffer buf = new StringBuffer(32);
		
		int pos = 0;
		for (int t: AVATAR_GRIDS) {
			buf.append(r.substring(pos, pos + t));
			pos += t;
			if (pos < AVATAR_LENGTH) {
				buf.append('/');
			}
		}
        return buf.toString();
	}
	
	

	/**
	 * 生成路径和文件名
	 * 时间路径 /1022/12/3/sfcxvdfgdf.png
	 *
	 * @param originalFilename 原始文件名
	 * @return 16位长度文件名+文件后缀
	 */
	public static String wholePathName(String originalFilename) {
		StringBuilder builder = new StringBuilder(32);
		builder.append(DateFormatUtils.format(new Date(), YMDHMS));
		builder.append("/");
		builder.append(UUID.randomUUID().toString().replaceAll("-", "").substring(3, 13));
		builder.append(FileKit.getSuffix(originalFilename));
		return builder.toString();
	}

	public static String wholePathName(String basePath, String ext) {
		return basePath + wholePathName(ext);
	}
	
	public static String smPathName(String basePath, String ext) {
		StringBuilder builder = new StringBuilder(basePath);
		builder.insert("/myfile".length(), "/sm");
		builder.insert(builder.lastIndexOf("."), "_sm");
		return builder.toString();
	}
	
	public static void main(String[] args) {
		System.out.println(getSmPath("/image/2003/44/09/","gfgfh.png"));
		System.out.println(wholePathName("gfgfh.png"));
		System.out.println(smPathName("adsdsf/sfsfs/fdfsdfsd.png", null));
		String base = FilePathUtils.getAvatar(50);
		System.out.println(String.format("/%s_%d.jpg", base, 100));
		System.out.println(FilePathUtils.wholePathName("a.jpg"));
		System.out.println(getSmPath("/2009/23/4/", "dgdgd.png"));
		System.out.println(getRootPath(3));
	}
   
	/**
	 * 图片文件路径 /xxx/2019/09/24/cccc.png
	 * 缩略图路径 /yyy/2019/09/24/cccc_sm.png
	 * @param fileDir
	 * @param name
	 * @return
	 */
	public static String getSmPath(String fileDir, String name) {
		// TODO Auto-generated method stub/xxx/sm
		StringBuilder builder = new StringBuilder(fileDir);
		builder.replace(1,OssEnum.IMAGE.getValue().length()+1,OssEnum.SM.getValue());
		builder.append(name);		
		builder.insert(builder.lastIndexOf("."), "_sm");	
		return builder.toString();
	}
	
	/**
	 * 获取项目工作空间目录
	 * 形如E:/xxx/peoject
	 * @return
	 */
	public static String getStorageRootPath() {
		return System.getProperties().getProperty("user.dir");
	}
	
	/**
	 * 获取我们存放文件在工作空间的主目录
	 * 形如 /abc
	 * @return
	 */
	public static String getStorageDir() {
		return "/"+OssEnum.STORAGE.getValue();
	}
	/*
	* 获取我们存放文件在工作空间的主目录
	 * 形如 E:/xxx/peoject/abc
	 * @return
	 */
	public static String getMainDir() {
		return getStorageRootPath()+"/"+OssEnum.STORAGE.getValue();
	}
	
	/**
	 * 获取文件主目录/指定村存放主目录
	 * @param ord
	 * @return
	 */
	public static String getRootPath(int ord) {
		
		return "/"+OssEnum.STORAGE.getValue()+"/"+OssEnum.values()[ord].getValue();
	}
	
	
}
