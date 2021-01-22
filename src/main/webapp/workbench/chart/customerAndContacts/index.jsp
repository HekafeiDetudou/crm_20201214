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
                    var myChart = echarts.init(document.getElementById('main'));

                    // 指定图表的配置项和数据
                    option = {
                        backgroundColor: '#2c343c',

                        title: {
                            text: 'Customized Pie',
                            left: 'center',
                            top: 20,
                            textStyle: {
                                color: '#ccc'
                            }
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
                                name: '访问来源',
                                type: 'pie',
                                radius: '55%',
                                center: ['50%', '50%'],
                                data: [
                                    {value: 335, name: '直接访问'},
                                    {value: 310, name: '邮件营销'},
                                    {value: 274, name: '联盟广告'},
                                    {value: 235, name: '视频广告'},
                                    {value: 400, name: '搜索引擎'}
                                ].sort(function (a, b) { return a.value - b.value; }),
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
                    };


                    // 使用刚指定的配置项和数据显示图表。
                    myChart.setOption(option);


                }
            })



        }


    </script>

</head>
<body>

<div style="margin:0 auto;width:1200px;height:600px;background-color: #0f0f0f">

    <!-- 为 ECharts 准备一个具备大小（宽高）的 DOM -->
    <div id="main" style="width: 600px;height:600px;" ></div>

</div>


</body>
</html>









