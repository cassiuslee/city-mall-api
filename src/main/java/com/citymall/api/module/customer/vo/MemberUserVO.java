package com.citymall.api.module.customer.vo;


import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author cqkir
 */
@Data
public class MemberUserVO {

    private String fId;

    private String cStatus;

    private String userId;

    private String mobile;

    private String nickname;

    private String avatar;

    private String gender;

    private String openid;

    private String unionId;

    private LocalDateTime fCreatorTime;
}
