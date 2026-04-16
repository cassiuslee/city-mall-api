package com.citymall.api.common.util;

import com.citymall.api.common.properties.FileUrlProperties;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author cqkir
 */
@Component
public class FileUrlUtils {

    private final FileUrlProperties properties;
    private final ObjectMapper objectMapper;

    public FileUrlUtils(FileUrlProperties properties, ObjectMapper objectMapper) {
        this.properties = properties;
        this.objectMapper = objectMapper;
    }

    public String parseFirstUrl(String rawValue) {
        List<String> urls = parseUrlList(rawValue);
        return urls.isEmpty() ? "" : urls.get(0);
    }

    public List<String> parseUrlList(String rawValue) {
        if (!StringUtils.hasText(rawValue)) {
            return Collections.emptyList();
        }

        String value = rawValue.trim();

        if (isAbsoluteUrl(value)) {
            return Collections.singletonList(value);
        }

        if (value.startsWith("[")) {
            try {
                List<FileItem> items = objectMapper.readValue(value, new TypeReference<List<FileItem>>() {});
                List<String> result = new ArrayList<>();
                for (FileItem item : items) {
                    if (item != null && StringUtils.hasText(item.getFileId())) {
                        result.add(buildUrlByFileId(item.getFileId()));
                    } else if (item != null && StringUtils.hasText(item.getUrl())) {
                        result.add(parseSingleValue(item.getUrl()));
                    }
                }
                return result;
            } catch (Exception e) {
                return Collections.emptyList();
            }
        }

        return Collections.singletonList(parseSingleValue(value));
    }

    public String parseSingleValue(String rawValue) {
        if (!StringUtils.hasText(rawValue)) {
            return "";
        }

        String value = rawValue.trim();

        if (isAbsoluteUrl(value)) {
            return value;
        }

        if (value.contains(",")) {
            return buildUrlByFileId(extractFileId(value));
        }

        if (value.contains("/")) {
            return normalizeBaseUrl(properties.getResourceBaseUrl())
                    + normalizePrefix(properties.getAnnexPrefix())
                    + trimLeadingSlash(value);
        }

        return buildUrlByFileId(value);
    }

    public String buildUrlByFileId(String fileId) {
        if (!StringUtils.hasText(fileId)) {
            return "";
        }

        String normalizedPath = fileId.trim().replace(",", "/");
        return normalizeBaseUrl(properties.getResourceBaseUrl())
                + normalizePrefix(properties.getAnnexPrefix())
                + trimLeadingSlash(normalizedPath);
    }

    public String extractFileId(String path) {
        if (!StringUtils.hasText(path)) {
            return "";
        }

        String value = path.trim();
        int index = value.lastIndexOf('/');
        if (index >= 0 && index < value.length() - 1) {
            return value.substring(index + 1);
        }
        return value;
    }

    private boolean isAbsoluteUrl(String value) {
        return value.startsWith("http://") || value.startsWith("https://");
    }

    private String normalizeBaseUrl(String baseUrl) {
        if (!StringUtils.hasText(baseUrl)) {
            return "";
        }
        return baseUrl.endsWith("/") ? baseUrl.substring(0, baseUrl.length() - 1) : baseUrl;
    }

    private String normalizePrefix(String prefix) {
        if (!StringUtils.hasText(prefix)) {
            return "/";
        }
        String result = prefix.startsWith("/") ? prefix : "/" + prefix;
        return result.endsWith("/") ? result : result + "/";
    }

    private String trimLeadingSlash(String value) {
        if (!StringUtils.hasText(value)) {
            return "";
        }
        return value.startsWith("/") ? value.substring(1) : value;
    }

    @Data
    @Schema(description = "文件对象")
    public static class FileItem {

        @Schema(description = "文件名")
        private String name;

        @Schema(description = "缩略图地址")
        private String thumbUrl;

        @Schema(description = "原始地址")
        private String url;

        @Schema(description = "文件ID")
        private String fileId;
    }
}