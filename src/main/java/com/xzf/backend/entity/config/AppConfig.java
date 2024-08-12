package com.xzf.backend.entity.config;

import com.xzf.backend.utils.StringTools;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component("appConfig")
public class AppConfig {

    /**
     * 文件目录
     */
    @Value("${project.folder:}")
    private String projectFolder;

    @Value("${image.ffmpegCompress:false}")
    private Boolean ffmpegCompress;



    public String getProjectFolder() {
        if (!StringTools.isEmpty(projectFolder) && !projectFolder.endsWith("/")) {
            projectFolder = projectFolder + "/";
        }
        return projectFolder;
    }

    public Boolean getFfmpegCompress() {
        return ffmpegCompress;
    }

}
