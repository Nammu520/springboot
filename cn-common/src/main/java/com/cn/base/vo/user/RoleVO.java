package com.cn.base.vo.user;

import com.cn.base.util.DateUtils;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.IOException;
import java.util.Date;

@Data
@ApiModel("角色Vo")
public class RoleVO {

    @ApiModelProperty(value = "角色id", example = "1")
    private Integer id;

    @ApiModelProperty(value = "角色名", example = "技术支持")
    private String name;

    @ApiModelProperty(value = "角色创建时间", example = "2019-02-12 23:59:12")
    @JsonSerialize(using = RoleVO.IntDate2LongDate.class)
    @JsonProperty(value = "create_time")
    private Date createTime;

    public static class IntDate2LongDate extends JsonSerializer<Date> {
        @Override
        public void serialize(Date value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
            if (value != null) {
                gen.writeObject(DateUtils.dateFormat(new Date(value.getTime()), DateUtils.DATE_TIME_PATTERN));
            }
        }
    }
}
