package com.citymall.api.common.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author cqkir
 */
@Data
public class BaseEntity {

    @TableId(value = "f_id")
    private String fId;

    @TableField("f_tenant_id")
    private String fTenantId;

    @TableField("f_delete_mark")
    private Integer fDeleteMark;

    @TableField("f_delete_time")
    private LocalDateTime fDeleteTime;

    @TableField("f_delete_user_id")
    private String fDeleteUserId;

    @TableField("f_version")
    private Integer fVersion;

    @TableField("f_flow_id")
    private String fFlowId;

    @TableField("f_flow_task_id")
    private String fFlowTaskId;

    @TableField("f_flow_state")
    private Integer fFlowState;

    @TableField("f_creator_user_id")
    private String fCreatorUserId;

    @TableField("f_last_modify_user_id")
    private String fLastModifyUserId;

    @TableField(value = "f_creator_time", fill = FieldFill.INSERT)
    private LocalDateTime fCreatorTime;

    @TableField(value = "f_last_modify_time", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime fLastModifyTime;
}