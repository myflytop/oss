package com.ruoyi.cms.oss.config;

import java.util.ArrayList;
import java.util.List;

public class OssConfig {
	//文件根路径
	
	String rootDir="";
	
	//支持文件类型
	List<String> supportType=new ArrayList<>();
	
	//文件类型路径
	List<String> fileDir=new ArrayList<>();
	
	//生成文件名类型
	Byte useName=0;
	
	//生成文件夹路径类型
	Byte useDir=0;
	

}
