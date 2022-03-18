import com.orainge.tools.spring_boot.utils.htmlChart.HtmlChartUtils;

import java.util.*;

/**
 * 前端图表数据工具类 Container Demo
 *
 * @author orainge
 * @since 2022/3/17
 */
public class ChartUtilsContainerDemo {
    public static void main(String[] args) {
//        multiCreateTable();
        test();
    }

    public static void test() {
        List<Object> a = new LinkedList<>();
        a.add(1);
        a.add(2);
        a.add(null);
        a.add(4);
        a.add(5);
        a.add(6);

        for (int i = 0; i < a.size(); i++) {
            if (a.get(i) == null) {
                a.remove(i);
                a.add(i, 3);
                break;
            }
        }

        System.out.println(a);
    }

    /**
     * 使用 Container 来创建多个表
     */
    public static void multiCreateTable() {
        // 获取一个图表容器
        HtmlChartUtils.Container<CustomChartDataBean> container = HtmlChartUtils.getChartContainer(CustomChartDataBean.class)
                // 设置所有的图表名称，同时决定创建几个图表 (必须先执行)
                .setChartNameList(Arrays.asList("图表1名称", "图表2名称", "图表3名称"))

                // 设置横坐标名称
                // 添加图表1名称的横坐标名称 (按顺序添加)
                .addChartXAxisNameList(Arrays.asList("图表1-横坐标1名称", "图表1-横坐标2名称"))

                // 设置图表2名称的横坐标名称  (通过指定下标的方式设置)
                .setChartXAxisNameList(2, Arrays.asList("图表2-横坐标1名称", "图表2-横坐标2名称", "图表3-横坐标2名称"))

                // 添加图表3名称的横坐标名称 (按顺序添加，会自动跳过已设置名称的图表2)
                .addChartXAxisNameList(Collections.singletonList("图表3-横坐标1名称"))

                // 设置子类名称（这里的数据列表不会直接应用到 helper 中，需要后续调用 helper 来操作）
                // 添加图表1名称的子类名称 (按顺序添加)
                .addSubDataNameList(Arrays.asList("图表1-A类名称", "图表1-B类名称"))

                // 设置图表2名称的子类名称 (通过指定下标的方式设置)
                .setSubDataNameList(1, Arrays.asList("图表2-A类名称", "图表2-B类名称", "图表2-C类名称"))

                // 添加图表3名称的子类名称 (按顺序添加，会自动跳过已设置名称的图表2)
                .addSubDataNameList(Arrays.asList("图表3-A类名称", "图表3-B类名称", "图表3-C类名称"));

        // 添加数据（这里的数据列表不会直接应用到 helper 中，需要后续调用 helper 来操作）
        // 方式1: 获取每列的数据列表来添加
        List<CustomChartDataBean> table1DataList = container.getChartDataList(0); // 获取图表1的数据列表
        List<CustomChartDataBean> table2DataList = container.getChartDataList(1); // 获取图表2的数据列表
        List<CustomChartDataBean> table3DataList = container.getChartDataList(2); // 获取图表3的数据列表

        // 往列表里添加数据
        table1DataList.add(new CustomChartDataBean());
        table2DataList.add(new CustomChartDataBean());
        table3DataList.add(new CustomChartDataBean());

        // 方式2: 通过 container 来添加数据
        container.addChartData(0, new CustomChartDataBean());
        container.addChartData(1, new CustomChartDataBean());
        container.addChartData(2, new CustomChartDataBean());

        // 获取每个图标的 Helper，为每个图表自定义操作逻辑
        // 方式1: 获取每个 helper 手动操作
        for (int i = 0; i <= container.getChartCount(); i++) {
            HtmlChartUtils.Helper helper = container.getHelper(i);
            List<String> chartXAxisNameList = container.getChartXAxisNameList(i);
            List<String> subDataNameList = container.getSubDataNameList(i);
            List<CustomChartDataBean> chartDataList = container.getChartDataList(i);
            // 完成业务逻辑后执行生成操作
//            Map<String, Object> chartMap = helper.toMap();
        }

        // 方式2: 获取每个表的信息进行操作
        for (HtmlChartUtils.ChartInfo<CustomChartDataBean> chartInfo : container.getChartInfoList()) {
            HtmlChartUtils.Helper helper = chartInfo.getHelper();
            List<String> chartXAxisNameList = chartInfo.getXAxisNameList();
            List<String> subDataNameList = chartInfo.getSubDataNameList();
            List<CustomChartDataBean> chartDataList = chartInfo.getDataList();
        }

        // 方式3: 使用 lambda 方式批量处理 (在每张图表的操作都相同的情况下使用)
        container.batch((helper, xAxisNameList, subDataNameList, dataList) -> {
            // 实现业务逻辑
        });

        // 统一生成所有图表的数据
        List<Map<String, Object>> allChartDataMap = container.generatorAllChart();
        System.out.println(allChartDataMap);
    }

    /**
     * 自定义的图表数据类型 Bean
     */
    public static class CustomChartDataBean {
        public String name = "名称_" + new Random().nextInt(10);
        public String value = "值_" + new Random().nextInt(10);
    }
}
