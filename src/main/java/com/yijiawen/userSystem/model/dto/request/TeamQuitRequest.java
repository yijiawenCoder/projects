package com.yijiawen.userSystem.model.dto.request;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
public class TeamQuitRequest implements Serializable {
    @Serial
    private static final long serialVersionUID = 8111943924442725229L;
    private String teamId;
}
