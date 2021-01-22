<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() + "/";

    /*
     * 需求：
     *       根据市场活动表中的cost进行统计，最终形成一个漏斗图
     *
     *       将统计出来的阶段的数量比较多的，向上排列
     *       将统计出来的阶段的数量比较少的，向下排列
     *
     * */

%>

<html>
<head>
    <base href="<%=basePath%>">
    <title>Title</title>
    <script src="ECharts/echarts.min.js"></script>
    <script src="jquery/jquery-1.11.1-min.js"></script>

    <script>

        $(function (){

            //在页面加载完毕后，绘制统计图表
            getCharts();
            getCharts2();

        })

        function getCharts(){

            $.ajax({
                url:"workbench/activity/getCharts.do",
                type:"post",
                dataType:"json",
                success : function (data){

                    /*
                    * data
                    *       {""}
                    * */

                    //alert("123");
                    // 基于准备好的dom，初始化echarts实例
                    var myChart = echarts.init(document.getElementById('main2'));

                    // 指定图表的配置项和数据
                    /*option = {
                        /!*backgroundColor: '#2c343c',*!/

                        toolbox: {
                            feature: {
                                dataView: {readOnly: false},
                                restore: {},
                                saveAsImage: {}
                            }
                        },
                        /!*backgroundColor:'#00FA9A',*!/
                        title: {
                            text: '市场活动饼图',
                            subtext: '统计市场活动名称和花费的饼状图'
                        },

                        tooltip: {
                            trigger: 'item',
                            formatter: '{a} <br/>{b} : {c} ({d}%)'
                        },

                        visualMap: {
                            show: false,
                            min: 80,
                            max: 600,
                            inRange: {
                                colorLightness: [0, 1]
                            }
                        },
                        series: [
                            {
                                name: '市场活动',
                                type: 'pie',
                                radius: '55%',
                                center: ['50%', '50%'],
                                data:data.sort(function (a, b) { return a.value - b.value; }),
                                    /!*[
                                        {value: 335, name: '直接访问'},
                                        {value: 310, name: '邮件营销'},
                                        {value: 274, name: '联盟广告'},
                                        {value: 235, name: '视频广告'},
                                        {value: 400, name: '搜索引擎'}
                                    ].sort(function (a, b) { return a.value - b.value; }),*!/
                                roseType: 'radius',
                                label: {
                                    color: 'rgba(255, 255, 255, 0.3)'
                                },
                                labelLine: {
                                    lineStyle: {
                                        color: 'rgba(255, 255, 255, 0.3)'
                                    },
                                    smooth: 0.2,
                                    length: 10,
                                    length2: 20
                                },
                                itemStyle: {
                                    color: '#c23531',
                                    shadowBlur: 200,
                                    shadowColor: 'rgba(0, 0, 0, 0.5)'
                                },

                                animationType: 'scale',
                                animationEasing: 'elasticOut',
                                animationDelay: function (idx) {
                                    return Math.random() * 200;
                                }
                            }
                        ]
                    };*/

                    option = {
                        toolbox: {
                            feature: {
                                dataView: {readOnly: false},
                                restore: {},
                                saveAsImage: {}
                            }
                        },
                        title: {
                            text: '市场活动饼图',
                            subtext: '统计市场活动名称和花费的饼状图'
                        },
                        tooltip: {
                            trigger: 'item',
                            formatter: '{a} <br/>{b} : {c} ({d}%)'
                        },
                        series: [
                            {
                                name: '市场活动',
                                type: 'pie',
                                radius: '55%',
                                center: ['50%', '50%'],
                                data: data
                                /*[
                                    {value: 335, name: '直接访问'},
                                    {value: 310, name: '邮件营销'},
                                    {value: 234, name: '联盟广告'},
                                    {value: 135, name: '视频广告'},
                                    {value: 1548, name: '搜索引擎'}
                                ]*/,
                                emphasis: {
                                    itemStyle: {
                                        shadowBlur: 10,
                                        shadowOffsetX: 0,
                                        shadowColor: 'rgba(0, 0, 0, 0.5)'
                                    }
                                }
                            }
                        ]
                    };

                    // 使用刚指定的配置项和数据显示图表。
                    myChart.setOption(option);


                }
            })



        }

        //左侧的表
        function getCharts2(){

            $.ajax({
                url:"workbench/activity/getCharts2.do",
                type:"post",
                dataType:"json",
                success : function (data){

                    /*
                    * data
                    *       {""}
                    * */

                    //alert("123");
                    // 基于准备好的dom，初始化echarts实例
                    var myChart = echarts.init(document.getElementById('main'));

                    // 指定图表的配置项和数据
                    option = {

                        toolbox: {
                            feature: {
                                dataView: {readOnly: false},
                                restore: {},
                                saveAsImage: {}
                            }
                        },

                        /*backgroundColor:'#00FA9A',*/
                        title: {
                            text: '市场活动折线图',
                            subtext: '统计交易阶段数量的漏斗图'
                        },

                        /*backgroundColor:'#00FA9A',*/
                        xAxis: {
                            type: 'category',
                            data:data.xList /*['Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat', 'Sun']*/
                        },
                        yAxis: {
                            type: 'value'
                        },
                        series: [{
                            data: data.yList/*[820, 932, 901, 934, 1290, 1330, 1320]*/,
                            type: 'line'
                        }]
                    };



                    // 使用刚指定的配置项和数据显示图表。
                    myChart.setOption(option);


                }
            })



        }


    </script>

</head>
<body>

<div style="margin:0 auto;width:1200px;height:600px;">

    <table cellspacing="0px"; border="0px"; width="1000px">
        <tr>
            <th style="text-align: left">
                <!-- 为 ECharts 准备一个具备大小（宽高）的 DOM -->
                <div id="main" style="width: 600px;height:600px; " ></div>
            </th>

            <th style="text-align: right">
                <!-- 为 ECharts 准备一个具备大小（宽高）的 DOM -->
                <div id="main2" style="width: 600px;height:600px; " ></div>
            </th>
        </tr>
    </table>

</div>


</body>
</html>









