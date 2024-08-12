package com.xzf.backend.entity.po;

import lombok.Data;

import java.io.Serializable;
import java.util.List;


@Data
public class ForumBoard implements Serializable {

    private Integer boardId;

    private String boardName;

    private String cover;

    private String boardDesc;


    private Integer sort;

    private Integer joinedCount;

    private String createUserId;

    private List<ForumBoard> children;

}
