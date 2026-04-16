package com.citymall.api.common.properties;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author cqkir
 */
@Data
@Component
@ConfigurationProperties(prefix = "file")
@Schema(description = "文件访问配置")
public class FileUrlProperties {

    @Schema(description = "资源访问域名")
    private String resourceBaseUrl;

    @Schema(description = "附件访问前缀")
    private String annexPrefix = "/files/WebAnnexFile/";
}