package com.orainge.tools.spring_boot.bean.http;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.github.pagehelper.PageHelper;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * 超类: 请求参数
 *
 * @author orainge
 * @since 2022/1/10
 */
@Data
@Accessors(chain = true)
public abstract class RequestBean {
    /**
     * 排序列<br>
     * 请在子类中重写该变量并添加该注解后实现 API 文档的生成
     */
    //    @ApiModelProperty("排序列")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @ApiModelProperty(hidden = true)
    private String orderColumn;

    /**
     * 排序方式<br>
     * 请在子类中重写该变量并添加该注解后实现 API 文档的生成
     */
    //    @ApiModelProperty("排序方式 (当且仅当参数 orderColumn 非空时生效) 升序: 0; 降序: 1; 默认值: 0")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @ApiModelProperty(hidden = true)
    private String orderDir;

    @ApiModelProperty(value = "第几页 默认值: 1; 取值范围: 1 <= pageNo", required = true)
    private Integer pageNo;

    @ApiModelProperty(value = "每页大小 默认值: 10; 取值范围: 1 <= pageSize <= 100", required = true)
    private Integer pageSize;

    /**
     * 每页最多能查询多少数据<br>
     * 默认值: 10<br>
     * 可以在子类中修改
     */
    @JsonIgnore
    protected Integer maxPerPageCount = 10;

    /**
     * 创建并获取 SQL 查询通用参数<br>
     * 如果已经配置，则此方法会自动执行 PageHelper 分页操作
     */
    public Map<String, Object> createSqlParams() {
        Map<String, Object> map = new HashMap<>();

        // 处理 SQL 查询参数
        processSqlParams(map);

        // 添加默认的 SQL 查询参数
        addDefaultSQLParams(map);

        // 返回参数
        return map;
    }

    /**
     * 处理 SQL 查询参数<br>
     * 可以通过重载实现自定义 SQL 查询参数的添加
     *
     * @param sqlParams 默认的 SQL 查询参数
     */
    protected void processSqlParams(Map<String, Object> sqlParams) {
    }

    /**
     * 设置前端提交的参数与数据库表列名的对应关系<br>
     * map: {"前端提交的字段": "数据库里要查询的列名"}<br>
     * 默认不开启排序功能，继承此类重载此方法以开启排序功能
     */
    protected Map<String, String> getAcceptableOrderColumn() {
        return null;
    }

    /**
     * 是否开启 PageHelper 的分页<br>
     * 默认开启<br>
     * 可以通过重载此方法关闭分页
     */
    protected boolean enablePageHelper() {
        return true;
    }

    /**
     * [内部方法] 添加默认的 SQL 查询参数<br>
     * 将会添加的参数:<br>
     * {"orderColumn": 数据表中要排序的列名}<br>
     * {"orderDir": 排序方式}<br>
     * <br>
     * 将会检查的参数:<br>
     * pageNo: 查询第几页<br>
     * pageSize: 每页条数<br>
     * <br>
     * 可选操作:<br>
     * (a) 开启分页 PageHelper<br>
     *
     * @param sqlParams 已有的数据库 SQL 参数 Map
     */
    private void addDefaultSQLParams(Map<String, Object> sqlParams) {
        // 由子类重载方法 getAcceptableOrderColumn() 决定哪些列可以进行排序
        // 避免由前端决定后端哪些列进行排序，防止可能的 SQL 注入攻击
        Map<String, String> acceptableOrderColumn = getAcceptableOrderColumn();

        if (acceptableOrderColumn != null && !acceptableOrderColumn.isEmpty()) {
            // 处理前端提交的排序参数
            String orderColumn = getOrderColumn();
            String orderDir = getOrderDir();

            // 检查排序方式参数是否正确
            if (!StringUtils.isEmpty(orderColumn) && !StringUtils.isEmpty(orderDir)) {
                if (StringUtils.isEmpty(orderDir) || "0".equals(orderDir) || "1".equals(orderDir)) {
                    if (StringUtils.isEmpty(orderDir) || "0".equals(orderDir)) {
                        // 升序 (默认值)
                        orderDir = "asc";
                    } else if ("1".equals(orderDir)) {
                        // 降序
                        orderDir = "desc";
                    }

                    // 检查排序列参数是否合法
                    for (Map.Entry<String, String> item : acceptableOrderColumn.entrySet()) {
                        if (getOrderColumn().equals(item.getKey())) {
                            sqlParams.put("orderColumn", item.getValue());
                            sqlParams.put("orderDir", orderDir);
                            break;
                        }
                    }
                }

                // 排序方式提交其它值将不开启排序
            }
        }

        // 如果开启分页
        if (enablePageHelper()) {
            // 设置页码
            Integer pageNo = getPageNo();
            if (pageNo == null || pageNo <= 0) {
                // 如果页码数为空/页码数 <= 0，重置为第 1 页
                pageNo = 1;
            }

            // 设置每页条数
            Integer pageSize = getPageSize();
            if (pageSize == null || pageSize <= 0) {
                // 如果每页条数为空/页码数 <= 0，重置为每页 10 条
                pageSize = 10;
            } else if (pageSize > maxPerPageCount) {
                // 如果每页条数 >= 0，重置为每页 100 条
                pageSize = maxPerPageCount;
            }

            PageHelper.startPage(pageNo, pageSize);
        }
    }
}
