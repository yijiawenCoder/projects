package com.yijiawen.userSystem.model.dto.request;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
@Data
public class TeamDeleteRequest implements Serializable {
    @Serial
    private static final long serialVersionUID = 8213506752482064006L;
    private String teamId;
}
