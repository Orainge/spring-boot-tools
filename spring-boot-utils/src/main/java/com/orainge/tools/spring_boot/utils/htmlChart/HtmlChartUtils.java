package com.orainge.tools.spring_boot.utils.htmlChart;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.*;

/**
 * 生成适用于前端图表的数据的工具类
 *
 * @author orainge
 * @since 2022/03/16
 */
public class HtmlChartUtils {
    /**
     * 图表工具 Helper
     */
    public static class Helper {
        /**
         * 数据列数
         */
        private int columnCount;

        /**
         * 表格名称
         */
        private String tableTitle;

        /**
         * 横坐标轴名称列表
         */
        private List<String> xAxisDataNameList;

        /**
         * 每列的数据
         */
        private List<Object> columnData;

        /**
         * 每种数据的数据名称<br>
         * 仅在需要每列数据有多种数据时才使用
         */
        private List<String> subDataNameList = null;

        /**
         * 每种数据的数据列表<br>
         * 仅在设置每种数据的数据名称后才使用
         */
        private List<List<?>> subDataList;

        /**
         * 额外的数据<br>
         * 随生成的表数据一起输出
         */
        private final Map<String, Object> extraDataMap = new HashMap<>();

        public Helper() {
        }

        public Helper(int columnCount) {
            this.columnCount = columnCount;
        }

        public Helper setColumnCount(int columnCount) {
            this.columnCount = columnCount;
            return this;
        }

        /**
         * 设置图表标题
         *
         * @param tableTitle 图表标题
         */
        public Helper setTitle(String tableTitle) {
            this.tableTitle = tableTitle;
            return this;
        }

        /**
         * 设置横坐标轴名称列表
         *
         * @param xAxisDataNameArray 横坐标轴名称列表
         */
        public Helper setXAxisDataNames(String... xAxisDataNameArray) {
            return setXAxisDataNames(Arrays.asList(xAxisDataNameArray));
        }

        /**
         * 设置横坐标轴名称列表
         *
         * @param xAxisDataNameList 横坐标轴名称列表
         */
        public Helper setXAxisDataNames(List<String> xAxisDataNameList) {
            if (columnCount == 0 && xAxisDataNameList != null && !xAxisDataNameList.isEmpty()) {
                columnCount = xAxisDataNameList.size();
            }

            if (this.xAxisDataNameList == null) {
                this.xAxisDataNameList = new LinkedList<>();
            }

            if (this.xAxisDataNameList.size() != columnCount &&
                    xAxisDataNameList != null) {
                for (String xAxisDataName : xAxisDataNameList) {
                    this.xAxisDataNameList.add(xAxisDataName);

                    // 超过列大小
                    if (this.xAxisDataNameList.size() == columnCount) {
                        break;
                    }
                }
            }
            return this;
        }

        /**
         * 为每一列添加数据<br>
         * 如果开启每列多种数据，则该方法调用无效
         *
         * @param dataArray 每列的数据
         */
        public Helper addData(Object... dataArray) {
            return addDataList(Arrays.asList(dataArray));
        }

        /**
         * 为每一列添加数据<br>
         * 如果开启每列多种数据，则该方法调用无效
         *
         * @param dataList 每列的数据
         */
        public Helper addDataList(List<?> dataList) {
            if (columnData == null) {
                columnData = new LinkedList<>();
            }

            if (subDataNameList == null &&
                    columnData.size() != columnCount &&
                    dataList != null) {
                for (Object data : dataList) {
                    columnData.add(data);

                    // 达到列大小
                    if (columnData.size() == columnCount) {
                        break;
                    }
                }
            }

            return this;
        }

        /**
         * 每种数据的数据名称<br>
         * 该方法只能调用一次<br>
         * 仅在每列数据有多种数据时才设置
         *
         * @param subDataNames 每种数据的数据名称
         */
        public Helper setSubDataNameList(String... subDataNames) {
            return setSubDataNameList(Arrays.asList(subDataNames));
        }

        /**
         * 每种数据的数据名称<br>
         * 该方法只能调用一次<br>
         * 仅在每列数据有多种数据时才设置
         *
         * @param subDataNameList 每种数据的数据名称
         */
        public Helper setSubDataNameList(List<String> subDataNameList) {
            if (this.subDataNameList == null && subDataNameList != null && !subDataNameList.isEmpty()) {
                this.subDataNameList = new LinkedList<>(subDataNameList);
                this.subDataList = new LinkedList<>();
            }
            return this;
        }

        /**
         * 为每一列添加多种数据<br>
         * 仅在设置每种数据的数据名称后才有效<br>
         * 按列顺序添加，每调用一次就添加一列数据，超出列数的数据无效
         *
         * @param subDataArray 每一列的多个数据，按照设置的每种数据的数据名称排序
         */
        public Helper addSubData(Object... subDataArray) {
            return addSubDataList(Arrays.asList(subDataArray));
        }

        /**
         * 为每一列添加多种数据<br>
         * 仅在设置每种数据的数据名称后才有效<br>
         * 按列顺序添加，每调用一次就添加一列数据，超出列数的数据无效
         *
         * @param subDataList 每一列的多个数据，按照设置的每种数据的数据名称排序
         */
        public Helper addSubDataList(List<?> subDataList) {
            if (this.subDataList == null) {
                this.subDataList = new LinkedList<>();
            }

            if (columnData == null &&
                    subDataNameList != null &&
                    this.subDataList.size() != columnCount &&
                    subDataList != null &&
                    subDataList.size() == subDataNameList.size() // 数据长度要与数据种类匹配
            ) {
                this.subDataList.add(subDataList);
            }
            return this;
        }

        /**
         * 添加额外的数据<br>
         * 添加的额外数据将随结果一起输出
         *
         * @param extraDataList 额外的数据 [{"name":"value"}]
         */
        public Helper addExtraData(List<Map<String, Object>> extraDataList) {
            if (extraDataList != null) {
                for (Map<String, Object> map : extraDataList) {
                    extraDataMap.putAll(map);
                }
            }
            return this;
        }

        /**
         * 添加额外的数据<br>
         * 添加的额外数据将随结果一起输出
         *
         * @param name  额外的数据名称
         * @param value 额外的数据值
         */
        public Helper addExtraData(String name, Object value) {
            extraDataMap.put(name, value);
            return this;
        }

        /**
         * 将表格数据生成为 map
         */
        public Map<String, Object> toMap() {
            Map<String, Object> dataMap = new LinkedHashMap<>();

            // 添加标题
            dataMap.put("title", tableTitle);

            // 添加横坐标轴名称
            if (xAxisDataNameList != null) {
                dataMap.put("xAxisData", xAxisDataNameList);
            }

            // 判断是否为多种数据
            if (subDataNameList != null) {
                // 多种数据
                // 设置每种数据名称（图例名称）
                dataMap.put("legendData", subDataNameList);

                // 拼装数据
                columnData = new LinkedList<>();
                for (int i = 0; i < subDataNameList.size(); i++) {
                    List<Object> data = new LinkedList<>();

                    // 每种数据都要取 1 个
                    for (int j = 0; j < columnCount; j++) {
                        data.add(subDataList.get(j).get(i));
                    }

                    Map<String, Object> subDataMap = new LinkedHashMap<>();
                    subDataMap.put("name", subDataNameList.get(i));
                    subDataMap.put("data", data);
                    columnData.add(subDataMap);
                }
            }

            // 单种数据，不做任何操作
            // 放入数据列表
            if (columnData != null) {
                dataMap.put("dataList", columnData);
            }

            // 添加额外数据
            if (!extraDataMap.isEmpty()) {
                dataMap.putAll(extraDataMap);
            }

            return dataMap;
        }

    }

    /**
     * 多图表容器
     *
     * @param <T> 图标数据类型
     */
    public static class Container<T> {
        /**
         * 图表个数
         */
        private int chartCount = 0;

        /**
         * 每个图表的名字
         */
        private List<String> chartNameList = null;

        /**
         * 每个图表的数据
         */
        private List<List<T>> chartDataList = null;

        /**
         * 每个图表的横坐标名称
         */
        private List<List<String>> chartXAxisNameList = null;

        /**
         * 每个图表的多种数据名称
         */
        private List<List<String>> chartSubDataNameList = null;

        /**
         * 每个图表的 Helper
         */
        private List<Helper> helperList = null;

        /**
         * 获取图表数量
         */
        public int getChartCount() {
            return chartCount;
        }

        /**
         * 设置所有的图表名称
         *
         * @param chartNameList 所有的图表名称列表
         */
        public Container<T> setChartNameList(List<String> chartNameList) {
            if (this.chartNameList == null &&
                    chartNameList != null &&
                    !chartNameList.isEmpty()) {

                // 初始化操作
                this.chartNameList = chartNameList; // 设置图表名称
                this.chartCount = chartNameList.size(); // 设置图表数量
                this.chartDataList = new LinkedList<>(); // 初始化 [每个图表的数据] 列表
                this.chartXAxisNameList = new LinkedList<>();// 初始化 [每个图表的横坐标名称] 列表
                this.chartSubDataNameList = new LinkedList<>(); // 初始化 [每个图表的多种数据名称] 列表
                this.helperList = new LinkedList<>(); // 初始化 [每个图表的 Helper] 列表

                for (int i = 0; i < chartCount; i++) {
                    // 初始化 [每个图表的数据] 列表
                    this.chartDataList.add(new LinkedList<>());

                    // 初始化 [每个图表的多种数据名称] 列表
                    this.chartSubDataNameList.add(null);

                    // 初始化 [每个图表的 Helper] 列表
                    this.helperList.add(HtmlChartUtils.buildTable());
                }
            }
            return this;
        }

        /**
         * 获取图表名称
         *
         * @param chartIndex 图标索引 (从 0 开始计数)
         */
        public String getChartName(int chartIndex) {
            return chartNameList.get(chartIndex);
        }

        /**
         * 将图表数据放入到指定索引的图表中
         *
         * @param index 要添加到的图标索引 (从 0 开始)
         * @param data  要添加的数据
         */
        public void addChartData(int index, T data) {
            if (0 <= index && index < chartCount) {
                chartDataList.get(index).add(data);
            }
        }

        /**
         * 获取指定序号的图表的数据
         *
         * @param index 图表序号索引 (从0开始)
         */
        public List<T> getChartDataList(int index) {
            return chartDataList.get(index);
        }

        /**
         * 获取所有图表的数据
         */
        public List<List<T>> getAllChartDataList() {
            return chartDataList;
        }

        /**
         * 添加横坐标名称<br>
         * 按照图表顺序依次添加，直到添加满为止
         *
         * @param xAxisNameList 横坐标名称列表
         */
        public Container<T> addChartXAxisNameList(List<String> xAxisNameList) {
            if (chartXAxisNameList != null) {
                for (int i = 0; i < chartXAxisNameList.size(); i++) {
                    if (chartXAxisNameList.get(i) == null) {
                        chartXAxisNameList.remove(i);
                        chartXAxisNameList.add(i, xAxisNameList);
                        break;
                    }
                }
            }

            return this;
        }

        /**
         * 设置横坐标名称<br>
         *
         * @param index         要为第几个表添加名称 (索引从0开始)
         * @param xAxisNameList 横坐标名称列表
         */
        public Container<T> setChartXAxisNameList(int index, List<String> xAxisNameList) {
            if (chartXAxisNameList != null) {
                chartXAxisNameList.remove(index);
                chartXAxisNameList.add(index, xAxisNameList);
            }

            return this;
        }

        /**
         * 获取指定图表的横坐标名称
         *
         * @param index 图表序号索引 (从0开始)
         */
        public List<String> getChartXAxisNameList(int index) {
            return chartXAxisNameList.get(index);
        }

        /**
         * 获取所有图表的横坐标名称
         */
        public List<List<String>> getAllChartXAxisNameList() {
            return chartXAxisNameList;
        }

        /**
         * 添加多种数据列表名称<br>
         * 按照图表顺序依次添加，直到添加满为止
         *
         * @param subDataNameList 多种数据列表名称列表
         */
        public Container<T> addSubDataNameList(List<String> subDataNameList) {
            if (chartSubDataNameList != null) {
                for (int i = 0; i < chartSubDataNameList.size(); i++) {
                    if (chartSubDataNameList.get(i) == null) {
                        chartSubDataNameList.remove(i);
                        chartSubDataNameList.add(i, subDataNameList);
                        break;
                    }
                }
            }

            return this;
        }

        /**
         * 设置多种数据列表名称<br>
         *
         * @param index           要为第几个表添加名称 (索引从0开始)
         * @param subDataNameList 多种数据列表名称列表
         */
        public Container<T> setSubDataNameList(int index, List<String> subDataNameList) {
            if (chartSubDataNameList != null) {
                chartSubDataNameList.remove(index);
                chartSubDataNameList.add(index, subDataNameList);
            }

            return this;
        }

        /**
         * 获取指定图表的多种数据列表名称
         *
         * @param index 图表序号索引 (从0开始)
         */
        public List<String> getSubDataNameList(int index) {
            return chartSubDataNameList.get(index);
        }

        /**
         * 获取所有图表的多种数据列表名称
         */
        public List<List<String>> getAllSubDataNameList() {
            return chartSubDataNameList;
        }

        /**
         * 获取 Helper
         *
         * @param index 图表序号索引 (从0开始)
         */
        public Helper getHelper(int index) {
            if (0 <= index && index < chartCount) {
                return helperList.get(index);
            } else {
                return null;
            }
        }

        /**
         * 获取每个图表的 Helper 列表
         */
        public List<Helper> getHelperList() {
            return helperList;
        }

        /**
         * 生成所有图表的数据
         */
        public List<Map<String, Object>> generatorAllChart() {
            List<Map<String, Object>> list = new LinkedList<>();
            for (Helper helper : helperList) {
                list.add(helper.toMap());
            }
            return list;
        }

        /**
         * 获取图表信息
         *
         * @param index 图表序号索引 (从0开始)
         */
        public ChartInfo<T> getChartInfo(int index) {
            return new ChartInfo<T>(helperList.get(index), chartXAxisNameList.get(index), chartSubDataNameList.get(index), chartDataList.get(index));
        }

        /**
         * 获取所有的图表信息
         */
        public List<ChartInfo<T>> getChartInfoList() {
            List<ChartInfo<T>> list = new LinkedList<>();
            for (int i = 0; i < chartCount; i++) {
                list.add(new ChartInfo<T>(helperList.get(i), chartXAxisNameList.get(i), chartSubDataNameList.get(i), chartDataList.get(i)));
            }
            return list;
        }

        /**
         * 批量处理数据 (Lambda 方式)
         */
        public Container<T> batch(IBatch<T> batchInterface) {
            for (int i = 0; i < chartCount; i++) {
                batchInterface.batch(helperList.get(i), chartXAxisNameList.get(i), chartSubDataNameList.get(i), chartDataList.get(i));
            }
            return this;
        }
    }

    /**
     * 图表信息 Bean
     */
    @Data
    @AllArgsConstructor
    public static class ChartInfo<T> {
        private Helper helper;
        private List<String> xAxisNameList;
        private List<String> subDataNameList;
        private List<T> dataList;
    }

    /**
     * 创建图表
     */
    public static Helper buildTable() {
        return new Helper();
    }

    /**
     * 创建图表
     *
     * @param columnCount 图表列数
     */
    public static Helper buildTable(int columnCount) {
        return new Helper(columnCount);
    }

    /**
     * 获取一个图表容器
     *
     * @param clazz 图表数据类型
     */
    public static <T> Container<T> getChartContainer(Class<T> clazz) {
        return new Container<>();
    }

    public interface IBatch<T> {
        void batch(Helper helper, List<String> xAxisNameList, List<String> subDataNameList, List<T> dataList);
    }
}