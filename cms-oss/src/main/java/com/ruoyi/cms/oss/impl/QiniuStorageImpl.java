package com.ruoyi.cms.oss.impl;

import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.qiniu.common.QiniuException;
import com.qiniu.common.Zone;
import com.qiniu.http.Response;
import com.qiniu.storage.BucketManager;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.UploadManager;
import com.qiniu.util.Auth;
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
public class QiniuStorageImpl extends AbstractStorage implements Storage {
    @Autowired
    private SiteOptions siteOptions;
    private Logger log = LoggerFactory.getLogger(AliyunStorageImpl.class);
    @Override
    public String writeToStore(byte[] bytes, String pathAndFileName) throws Exception {
        String accessKey = siteOptions.getOptions().get("qiniu_oss_key");
        String secretKey = siteOptions.getOptions().get("qiniu_oss_secret");
        String domain = siteOptions.getOptions().get("qiniu_oss_domain");
        String bucket = siteOptions.getOptions().get("qiniu_oss_bucket");
        String src = siteOptions.getOptions().get("qiniu_oss_src");

        if (StringUtils.isAnyBlank(accessKey, secretKey, domain, bucket)) {
            throw new StorageException("请先在后台设置阿里云配置信息");
        }

        if (StringUtils.isNotBlank(src)) {
            if (src.startsWith("/")) {
                src = src.substring(1);
            }

            if (!src.endsWith("/")) {
                src = src + "/";
            }
        } else {
            src = "";
        }

        String key = UpYunUtils.md5(bytes);
        String path = src + key + FileKit.getSuffix(pathAndFileName);

        Zone z = Zone.autoZone();
        Configuration configuration = new Configuration(z);
        Auth auth = Auth.create(accessKey, secretKey);
        String upToken = auth.uploadToken(bucket, path);

        UploadManager uploadManager = new UploadManager(configuration);
        Response response = uploadManager.put(bytes, path, upToken);

        if (!response.isOK()) {
            throw new StorageException(response.bodyString());
        }
        return domain.trim() + "/" + path;
    }

    @Override
    public void deleteFile(String storePath) {
        String accessKey = siteOptions.getOptions().get("qiniu_oss_key");
        String secretKey = siteOptions.getOptions().get("qiniu_oss_secret");
        String domain = siteOptions.getOptions().get("qiniu_oss_domain");
        String bucket = siteOptions.getOptions().get("qiniu_oss_bucket");

        if (StringUtils.isAnyBlank(accessKey, secretKey, domain, bucket)) {
            throw new StorageException("请先在后台设置阿里云配置信息");
        }

        String path = StringUtils.remove(storePath, domain.trim());

        Zone z = Zone.autoZone();
        Configuration configuration = new Configuration(z);
        Auth auth = Auth.create(accessKey, secretKey);

        BucketManager bucketManager = new BucketManager(auth, configuration);
        try {
            bucketManager.delete(bucket, path);
        } catch (QiniuException e) {
            Response r = e.response;
            log.error(e.getMessage(), r.toString());
        }
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
