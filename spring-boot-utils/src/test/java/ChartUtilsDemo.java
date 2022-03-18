import com.orainge.tools.spring_boot.utils.htmlChart.HtmlChartUtils;

import java.util.Map;

/**
 * 前端图表数据工具类 Demo
 *
 * @author orainge
 * @since 2022/3/17
 */
public class ChartUtilsDemo {
    public static void main(String[] args) {
        singleData();
        multiData();
    }

    /**
     * 生成单种数据表
     * <p>
     * {
     * "title": "图表标题",
     * "xAxisData": ["横坐标轴列1名称", "横坐标轴列2名称"],
     * "dataList": [120, 132, 101, 134],
     * "额外的参数名称": "额外的参数值"
     * }
     */
    public static void singleData() {
        Map<String, Object> obj = HtmlChartUtils.buildTable() // 如果不指定列数，则由横坐标名称个数决定
                .setTitle("图表标题")
                .setXAxisDataNames("横坐标轴列1名称", "横坐标轴列2名称")
                .addData("横坐标轴列1的值", "横坐标轴列2的值")
                .addExtraData("额外的参数名称", "额外的参数值")
                .toMap(); // 生成图表
        System.out.println(obj);
    }

    /**
     * 生成多种数据表
     * <p>
     * {
     * "title": "图表标题",
     * "xAxisData": ["横坐标轴列1名称", "横坐标轴列2名称", "横坐标轴列3名称"],
     * "legendData": ["子类A名称", "子类B名称"],
     * "dataList": [{
     * "name": "子类A名称",
     * "data": ["横坐标轴列1-子类A的值", "横坐标轴列2-子类A的值", "横坐标轴列3-子类A的值"]
     * }, {
     * "name": "子类B名称",
     * "data": ["横坐标轴列1-子类B的值", "横坐标轴列2-子类B的值", "横坐标轴列3-子类B的值"]
     * }],
     * "额外的参数名": "额外的参数值"
     * }
     */
    public static void multiData() {
        Map<String, Object> obj = HtmlChartUtils.buildTable()// 如果不指定列数，则由横坐标名称个数决定
                .setTitle("图表标题")
                .setXAxisDataNames("横坐标轴列1名称", "横坐标轴列2名称", "横坐标轴列3名称")
                .setSubDataNameList("子类A名称", "子类B名称")
                .addSubData("横坐标轴列1-子类A的值", "横坐标轴列1-子类B的值") // 添加横坐标轴列1数据
                .addSubData("横坐标轴列2-子类A的值", "横坐标轴列2-子类B的值") // 添加横坐标轴列2数据
                .addSubData("横坐标轴列3-子类A的值", "横坐标轴列3-子类B的值") // 添加横坐标轴列3数据
                .addExtraData("额外的参数名", "额外的参数值")
                .toMap();
        System.out.println(obj);
    }
}
