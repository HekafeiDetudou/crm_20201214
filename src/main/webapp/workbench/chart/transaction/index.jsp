<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() + "/";

    /*
    * 需求：
    *       根据交易表中不同的阶段的数量进行一个统计，最终形成一个漏斗图
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

            //在页面加载完毕后，绘制统计图表
            getCharts2();

        })

        function getCharts(){

            $.ajax({
                url:"workbench/transaction/getCharts.do",
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
                    option = {
                        toolbox: {
                            feature: {
                                dataView: {readOnly: false},
                                restore: {},
                                saveAsImage: {}
                            }
                        },
                        tooltip: {
                            trigger: 'item',
                            formatter: '{a} <br/>{b} : {c} ({d}%)'
                        },
                        /*backgroundColor:'#00FA9A',*/
                        title: {
                            text: '交易漏斗图',
                            subtext: '统计交易阶段数量的漏斗图'
                        },

                        series: [
                            {
                                name:'交易漏斗图',
                                type:'funnel',
                                left: '10%',
                                top: 60,
                                //x2: 80,
                                bottom: 60,
                                width: '80%',
                                // height: {totalHeight} - y - y2,
                                min: 0,
                                max: data.total,
                                minSize: '0%',
                                maxSize: '100%',
                                sort: 'descending',
                                gap: 2,
                                label: {
                                    show: true,
                                    position: 'inside'
                                },
                                labelLine: {
                                    length: 10,
                                    lineStyle: {
                                        width: 1,
                                        type: 'solid'
                                    }
                                },
                                itemStyle: {
                                    borderColor: '#fff',
                                    borderWidth: 1
                                },
                                emphasis: {
                                    label: {
                                        fontSize: 20
                                    }
                                },
                                data: data.dataList
                                /*
                                    [
                                        {value: 60, name: '访问'},
                                        {value: 40, name: '咨询'},
                                        {value: 20, name: '订单'},
                                        {value: 80, name: '点击'},
                                        {value: 100, name: '展现'}
                                    ]
                                */
                            }
                        ]
                    };




                    // 使用刚指定的配置项和数据显示图表。
                    myChart.setOption(option);


                }
            })



        }


        function getCharts2(){

            $.ajax({
                url:"workbench/transaction/getCharts2.do",
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
                        title: {
                            text: '交易柱状图',
                            subtext: '统计交易阶段数量的柱状图'
                        },
                        toolbox: {
                            feature: {
                                dataView: {readOnly: false},
                                restore: {},
                                saveAsImage: {}
                            }
                        },
                        xAxis: {
                            type: 'category',
                            data:data.xList
                            /*['Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat', 'Sun']*/
                        },
                        yAxis: {
                            type: 'value'
                        },
                        series: [{
                            data:data.yList /*[120, 200, 150, 80, 70, 110, 130]*/,
                            type: 'bar',
                            showBackground: false,
                            backgroundStyle: {
                                color: 'rgba(145, 958, 325, 0.8)'
                            }
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









