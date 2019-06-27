package com.ruoyi.cms.oss.impl;

import java.io.ByteArrayInputStream;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.aliyun.oss.OSSClient;

import com.ruoyi.cms.oss.Storage;
import com.ruoyi.cms.oss.config.SiteOptions;
import com.ruoyi.cms.oss.exception.StorageException;
import com.ruoyi.cms.oss.utils.FileKit;
import com.upyun.UpYunUtils;


/**
 * @author langhsu
 * @since  3.0
 */

@Component
public class AliyunStorageImpl extends AbstractStorage implements Storage {
	
	private Logger log = LoggerFactory.getLogger(AliyunStorageImpl.class);
	
    @Autowired
    private SiteOptions siteOptions;

    @Override
    public String writeToStore(byte[] bytes, String pathAndFileName) throws Exception {
        String endpoint = siteOptions.getOptions().get("aliyun_oss_endpoint");
        String bucket = siteOptions.getOptions().get("aliyun_oss_bucket");
        String src = siteOptions.getOptions().get("aliyun_oss_src");

        if (StringUtils.isAnyBlank(endpoint, bucket)) {
            throw new StorageException("请先在后台设置阿里云配置信息");
        }

        if (StringUtils.isBlank(src)) {
            src = "";
        } else {
            if (src.startsWith("/")) {
                src = src.substring(1);
            }

            if (!src.endsWith("/")) {
                src = src + "/";
            }
        }

        String key = UpYunUtils.md5(bytes);
        String path = src + key + FileKit.getSuffix(pathAndFileName);
        OSSClient client = builder();
        client.putObject(bucket, path, new ByteArrayInputStream(bytes));
        return "//" + bucket.trim() + "." + endpoint.trim() + "/" + path;
    }

    @Override
    public void deleteFile(String storePath) {
        String bucket = siteOptions.getOptions().get("aliyun_oss_bucket");
        String endpoint = siteOptions.getOptions().get("aliyun_oss_endpoint");
        String path = StringUtils.remove(storePath, "//" + bucket.trim() + "." + endpoint.trim() + "/");
        OSSClient client = builder();
        try {
            client.doesObjectExist(bucket, path);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    private OSSClient builder() {
        String endpoint = siteOptions.getOptions().get("aliyun_oss_endpoint");
        String accessKeyId = siteOptions.getOptions().get("aliyun_oss_key");
        String accessKeySecret = siteOptions.getOptions().get("aliyun_oss_secret");

        if (StringUtils.isAnyBlank(endpoint, accessKeyId, accessKeySecret)) {
            throw new StorageException("请先在后台设置阿里云配置信息");
        }
        return new OSSClient(endpoint, accessKeyId, accessKeySecret);
    }

	@Override
	public Map<String, Object> listFile(String dir, String accept, String exts) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<String, Object> upload(MultipartFile file) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String download(String fileDir, HttpServletResponse response) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<String, Object> removeFile(String file) {
		// TODO Auto-generated method stub
		return null;
	}
}
