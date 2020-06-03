package com.jascola.welcome.web.entity;

import com.github.pagehelper.PageHelper;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
@Getter
@Setter
@ToString
abstract class BaseEntity implements Serializable {

    private static final long serialVersionUID = -1;

    public void setPageParam(int pageNum,int pageSize){
        PageHelper.startPage(pageNum,pageSize);
    }

}
