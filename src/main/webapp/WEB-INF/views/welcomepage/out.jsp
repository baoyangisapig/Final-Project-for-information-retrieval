<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <style>
        a img{border:none} .testdiv *{ vertical-align:middle; }
    </style>
    <!-- 新 Bootstrap 核心 CSS 文件 -->
    <link href="https://cdn.bootcss.com/bootstrap/3.3.7/css/bootstrap.min.css" rel="stylesheet">

    <!-- 可选的Bootstrap主题文件（一般不使用） -->
    <script src="https://cdn.bootcss.com/bootstrap/3.3.7/css/bootstrap-theme.min.css"></script>

    <!-- jQuery文件。务必在bootstrap.min.js 之前引入 -->
    <script src="https://cdn.bootcss.com/jquery/2.1.1/jquery.min.js"></script>

    <!-- 最新的 Bootstrap 核心 JavaScript 文件 -->
    <script src="https://cdn.bootcss.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
    <script>
        $(
            function()
            {
                $("#btn").click(
                    function () {
                        $.post(
                            "http://localhost:8080/demo/searchTop",
                            {
                                query:"love"
                            },
                            function (data) {
                                var data1=JSON.parse(data);//将string类型转换成json类型
                                $('#biao').html("");
                                for(i=0;i<data1.length;i++){
                              /*      var p_name=data1[i].director;*/
    /*                                var p_name=data1[i].title;
                                    var p_section=data1[i].p_section;
                                    var d_name=data1[i].d_name;
                                    var d_section=data1[i].d_section;
                                    var time=data1[i].time;
                                    var name=data1[i].name;
                                    var room=data1[i].roomnumber;
                                    var str="<tr><td>"+p_name+"</td><td>"+p_section+"</td><td>"+d_name+"</td><td>"+d_section+"</td><td>"+time+"</td><td>"+name+"</td><td>"+room+"</td><td>Application accepted and arranged</td><td><td></td><tr>";*/
                                   var str="<tr><td>"+p_name+"</td></tr>";
                                    $('#biao').append(str);


                                }

                            }
                        ).error(function (msg) {
                            alert("无任务状态");
                        })
                    }
                );

            }
        );
    </script>
    <title>Title</title>
<%--    <form action="get" style="margin-left: 530px">
        <input type="file" name="file">
        <button type="submit">upload</button>
    </form>--%>
<%--    <form action="searchTop" class="testdiv" style="margin-left: 280px">--%>

<%--    </form>--%>
</head>
<body>
<img src="../../../bootstrap/google.png" height="70" width="120"/>
<input name="query" size="80">
<button type="submit" id=btn">Search</button>
<a style="text-align: center">Top 20 Searching result for  "
    ${word} ":<br/><br/> ${username}</a>
<tbody id="biao">

</tbody>
</body>
</html>