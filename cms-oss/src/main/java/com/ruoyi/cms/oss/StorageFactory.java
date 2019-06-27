package com.ruoyi.cms.oss;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import com.ruoyi.cms.oss.config.SiteOptions;
import com.ruoyi.cms.oss.impl.AliyunStorageImpl;
import com.ruoyi.cms.oss.impl.NativeStorageImpl;
import com.ruoyi.cms.oss.impl.QiniuStorageImpl;
import com.ruoyi.cms.oss.impl.UpYunStorageImpl;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

/**
 * created by langhsu
 * on 2019/1/21
 */
@Component
public class StorageFactory {
    @Autowired
    private ApplicationContext applicationContext;
    @Autowired
    private SiteOptions siteOptions;

    private Map<String, Storage> fileRepoMap = new HashMap<>();

    @PostConstruct
    public void init() {
        fileRepoMap.put("native", applicationContext.getBean(NativeStorageImpl.class));
        fileRepoMap.put("upyun", applicationContext.getBean(UpYunStorageImpl.class));
        fileRepoMap.put("aliyun", applicationContext.getBean(AliyunStorageImpl.class));
        fileRepoMap.put("qiniu", applicationContext.getBean(QiniuStorageImpl.class));
    }

    public Storage get() {
        String scheme = siteOptions.getOptions().get("storage_scheme");
        if (StringUtils.isBlank(scheme)) {
            scheme = "native";
        }
        return fileRepoMap.get(scheme);
    }
}
