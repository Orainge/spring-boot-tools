package com.orainge.tools.spring_boot.bean.http;

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
    private Long total;

    @ApiModelProperty("总页数")
    private Integer totalPage;

    public ResponseBean() {
    }

    /**
     * 设置数据并创建分页信息
     *
     * @param data Mapper 方法
     */
    public ResponseBean(List<T> data) {
        // 创建分页信息
        PageInfo<T> pageInfo = new PageInfo<>(data);

        // 设置总条数
        setTotal(pageInfo.getTotal());

        // 设置总页数
        setTotalPage(pageInfo.getPages());

        // 设置数据
        setData(data);
    }
}
