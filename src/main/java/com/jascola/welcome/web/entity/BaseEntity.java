package com.jascola.welcome.web.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
@Getter
@Setter
@ToString
public class BaseEntity implements Serializable {
    private Integer  pageNum;
    private Integer  pageSize;
}
