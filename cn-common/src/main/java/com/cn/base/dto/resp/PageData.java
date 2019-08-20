package com.cn.base.dto.resp;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

@Data
public class PageData<T> implements Serializable {

    @ApiModelProperty(name = "count", value = "数据条数")
    private long count;
    @ApiModelProperty(name = "results", value = "响应对象")
    private List<T> content;


    public static <T> PageData<T> getEmptyPage() {
        PageData<T> p = new PageData<>();
        p.setCount(0);
        p.setContent(Collections.emptyList());
        return p;
    }
}
