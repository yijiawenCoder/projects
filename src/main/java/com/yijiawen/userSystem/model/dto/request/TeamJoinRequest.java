package com.yijiawen.userSystem.model.dto.request;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
@Data
public class TeamJoinRequest implements Serializable {
    @Serial
    private static final long serialVersionUID = -2231869829004772956L;
    private String teamId;
    private String password;
}
