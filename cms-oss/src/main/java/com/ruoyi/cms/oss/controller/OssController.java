package com.ruoyi.cms.oss.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.tika.Tika;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.ruoyi.cms.oss.StorageFactory;
import com.ruoyi.cms.oss.utils.FileKit;

@CrossOrigin
@Controller
@RequestMapping("cms/oss")
public class OssController {
	
	private final String prefix="/oss/";
	
	@Autowired
	private StorageFactory ossService;
	
    // 首页
    @RequestMapping
    public String index() {
        return "redirect:"+prefix+"index.html";
    }

    /**
     * 上传文件
     * 如果是图片同时上传缩略图
     * @throws Exception 
     */
    @ResponseBody
    @PostMapping("/file/upload")
    public Map<String,Object> upload(@RequestParam MultipartFile file) throws Exception {
    	
    	
       return ossService.get().upload(file);
    }

    
   
    /**
     * 查看原文件
     */
    @GetMapping("/file/{fileBold}/{y}/{m}/{d}/{file:.+}")
    public String file(@PathVariable("fileBold") String fileBold,@PathVariable("y") String y, @PathVariable("m") String m, @PathVariable("d") String d, @PathVariable("file") String filename, HttpServletResponse response) {
    	String fileDir =fileBold+"/"+ y + "/" + m + "/" + d + "/" + filename;
    	
        return ossService.get().download(fileDir,response);
    }
    
   
    /**
     * 获取全部文件
     * 返回路径 正常路径 /xxx/2009/23/10/2343424.png
     * 缩略图路径 /yy/2009/23/10/2343424_sm.png
     */
    @ResponseBody
    @RequestMapping("/api/list")
    public Map<String,Object> list(String dir, String accept, String exts) {
           
        return ossService.get().listFile(dir, accept, exts);
       
    }

    /**
     * 删除 文件
     * 如果是缩略图一起删除
     */
    @ResponseBody
    @RequestMapping("/api/del")
    public Map<String,Object> del(String file) {
    
        return ossService.get().removeFile(file);
    }

    // 获取当前日期
    private String getDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd/");
        return sdf.format(new Date());
    }

    // 封装返回结果
    private Map<String,Object> getRS(int code, String msg, String url) {
        Map<String, Object> map = new HashMap<>();
        map.put("code", code);
        map.put("msg", msg);
        if (url != null) {
            map.put("url", url);
        }
        return map;
    }

    // 封装返回结果
    private Map<String,Object> getRS(int code, String msg) {
        return null;
    }

}
