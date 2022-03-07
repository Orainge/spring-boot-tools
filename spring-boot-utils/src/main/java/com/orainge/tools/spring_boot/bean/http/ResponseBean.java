package com.orainge.tools.spring_boot.bean.http;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * HTTP 请求结果
 *
 * @author orainge
 * @since 2022/1/10
 */
@Data
@Accessors(chain = true)
public abstract class ResponseBean<T> {
    @ApiModelProperty("数据")
    private List<T> data;

    @ApiModelProperty("总条数")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Long total;

    @ApiModelProperty("总页数")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Integer totalPage;

    public ResponseBean() {
    }

    /**
     * 设置数据并创建分页信息
     *
     * @param mapperResult Mapper 方法执行结果
     */
    public ResponseBean(List<T> mapperResult) {
        buildData(mapperResult);
    }

    public void buildData(List<T> mapperResult) {
        // 创建分页信息
        PageInfo<T> pageInfo = new PageInfo<>(mapperResult);

        // 设置总条数
        setTotal(pageInfo.getTotal());

        // 设置总页数
        setTotalPage(pageInfo.getPages());

        // 设置数据
        setData(mapperResult);
    }
}
