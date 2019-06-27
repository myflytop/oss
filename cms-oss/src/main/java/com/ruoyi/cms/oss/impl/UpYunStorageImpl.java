package com.ruoyi.cms.oss.impl;

import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.UpYun;
import com.ruoyi.cms.oss.Storage;
import com.ruoyi.cms.oss.config.SiteOptions;
import com.ruoyi.cms.oss.exception.StorageException;
import com.ruoyi.cms.oss.utils.FileKit;
import com.upyun.UpYunUtils;

/**
 * created by langhsu
 * on 2019/1/20
 *
 * @since 3.0
 */

@Component
public class UpYunStorageImpl extends AbstractStorage implements Storage {
    @Autowired
    private SiteOptions siteOptions;
    
    private Logger log = LoggerFactory.getLogger(AliyunStorageImpl.class);

    @Override
    public String writeToStore(byte[] bytes, String pathAndFileName) throws Exception {
        String domain = siteOptions.getOptions().get("upyun_oss_domain");
        String src = siteOptions.getOptions().get("upyun_oss_src");

        if (StringUtils.isAnyBlank(domain)) {
            throw new StorageException("请先在后台设置又拍云配置信息");
        }

        if (StringUtils.isBlank(src)) {
            src = "";
        } else {
            if (!src.startsWith("/")) {
                src = "/" + src;
            }

            if (!src.endsWith("/")) {
                src = src + "/";
            }
        }

        String key = UpYunUtils.md5(bytes);
        String path = src + key + FileKit.getSuffix(pathAndFileName);
        UpYun upYun = builder();
        upYun.setContentMD5(key);
        upYun.writeFile(path, bytes, true);
        return domain.trim() + path;
    }

    @Override
    public void deleteFile(String storePath) {
        String domain = siteOptions.getOptions().get("upyun_oss_domain");
        String path = StringUtils.remove(storePath, domain.trim());
        UpYun yun = builder();
        try {
            yun.deleteFile(path);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    private UpYun builder() {
        String bucket = siteOptions.getOptions().get("upyun_oss_bucket");
        String operator = siteOptions.getOptions().get("upyun_oss_operator");
        String password = siteOptions.getOptions().get("upyun_oss_password");

        if (StringUtils.isAnyBlank(bucket, operator, password)) {
            throw new StorageException("请先在后台设置又拍云配置信息");
        }
        UpYun yun = new UpYun(bucket, operator, password);
        yun.setTimeout(60);
        yun.setApiDomain(UpYun.ED_AUTO);
        yun.setDebug(true);
        return yun;
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
