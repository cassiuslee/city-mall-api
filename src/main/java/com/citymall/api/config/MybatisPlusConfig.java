package com.citymall.api.config;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDateTime;

/**
 * @author cqkir
 */
@Configuration
public class MybatisPlusConfig {

  @Bean
  public MetaObjectHandler metaObjectHandler() {
    return new MetaObjectHandler() {
      @Override
      public void insertFill(MetaObject metaObject) {
        this.strictInsertFill(metaObject, "fCreatorTime", LocalDateTime.class, LocalDateTime.now());
        this.strictInsertFill(metaObject, "fLastModifyTime", LocalDateTime.class, LocalDateTime.now());
        this.strictInsertFill(metaObject, "fDeleteMark", Integer.class, 0);
        this.strictInsertFill(metaObject, "fVersion", Integer.class, 0);
      }

      @Override
      public void updateFill(MetaObject metaObject) {
        this.strictUpdateFill(metaObject, "fLastModifyTime", LocalDateTime.class, LocalDateTime.now());
      }
    };
  }
}
