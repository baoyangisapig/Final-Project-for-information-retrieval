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
        window.onload=function (ev) {
            $.post(
                "http://localhost:8080/demo/searchTop",
                {
                    query:"big data",
                    type: "2",
                    metaScore:40,
                    userScore:4
                },
                function (data) {
                    var data1=JSON.parse(data);
                    $('#biao').html("");
                    for(i=0;i<data1.length;i++){
                        var title=data1[i].title;
                        var director=data1[i].director;
                        var summary=data1[i].summary;
                        var comment=data1[i].comment;
                        var image=data1[i].image;
                        var metaScore=data1[i].metaScore;
                        var userScore=data1[i].userScore;
                        var str= "<tr><td><br/><br/><br/><br/><br/><br/>" + title + "</td><td><br/><br/><br/><br/><br/><br/>" + director + "</td><td>" + comment + "</td><td><img src='" + image + "' width='380px' height='500px'/><br/>" + summary + "</td><td><br/></br><br/></br><br/></br> <div style=\"position:relative;\"><img src=\"https://harperlibrary.typepad.com/.a/6a0105368f4fef970b01b7c8ada1a9970b-800wi\" style=\"width: 100px;height: 100px\" /><div style=\"position:absolute; z-index:2; left:34px; top:25px;font-size: 35px;color: greenyellow\"><strong>"+metaScore+"</strong></div></div></td><td><br/></br><br/></br><br/></br> <div style=\"position:relative;\"><img src=\"https://harperlibrary.typepad.com/.a/6a0105368f4fef970b01b7c8ada1a9970b-800wi\" style=\"width: 100px;height: 100px\" /><div style=\"position:absolute; z-index:2; left:34px; top:25px;font-size: 35px;color: red\"><strong>"+userScore+"</strong></div></div></td></tr>";
                        $('#biao').append(str);


                    }

                }
            ).error(function (msg) {
                alert("无任务状态");
            })
        }
    </script>
    <script>
        $(
            function()
            {

                $("#btn").click(
                    function () {
                        $.post(
                            "http://localhost:8080/demo/searchTop",
                            {
                                query:$("#query").val(),
                                type:$("#test option:selected").val(),
                                metaScore:$("#query1").val(),
                                userScore:$("#query2").val()
                            },
                            function (data) {
                                var data1=JSON.parse(data);//将string类型转换成json类型
                                $('#biao').html("");
                                for(i=0;i<data1.length;i++){
                                          var title=data1[i].title;
                                          var director=data1[i].director;
                                          var summary=data1[i].summary;
                                          var comment=data1[i].comment;
                                          var image=data1[i].image;
                                          var metaScore=data1[i].metaScore;
                                          var userScore=data1[i].userScore;

                                    /*                                var p_name=data1[i].title;
                                                                    var p_section=data1[i].p_section;
                                                                    var d_name=data1[i].d_name;
                                                                    var d_section=data1[i].d_section;
                                                                    var time=data1[i].time;
                                                                    var name=data1[i].name;
                                                                    var room=data1[i].roomnumber;
                                                                    var str="<tr><td>"+p_name+"</td><td>"+p_section+"</td><td>"+d_name+"</td><td>"+d_section+"</td><td>"+time+"</td><td>"+name+"</td><td>"+room+"</td><td>Application accepted and arranged</td><td><td></td><tr>";*/
                                    var str= "<tr><td><br/><br/><br/><br/><br/><br/>" + title + "</td><td><br/><br/><br/><br/><br/><br/>" + director + "</td><td>" + comment + "</td><td><img src='" + image + "' width='380px' height='500px'/><br/>" + summary + "</td><td><br/></br><br/></br><br/></br> <div style=\"position:relative;\"><img src=\"https://harperlibrary.typepad.com/.a/6a0105368f4fef970b01b7c8ada1a9970b-800wi\" style=\"width: 100px;height: 100px\" /><div style=\"position:absolute; z-index:2; left:34px; top:25px;font-size: 35px;color: greenyellow\"><strong>"+metaScore+"</strong></div></div></td><td><br/></br><br/></br><br/></br> <div style=\"position:relative;\"><img src=\"https://harperlibrary.typepad.com/.a/6a0105368f4fef970b01b7c8ada1a9970b-800wi\" style=\"width: 100px;height: 100px\" /><div style=\"position:absolute; z-index:2; left:34px; top:25px;font-size: 35px;color: red\"><strong>"+userScore+"</strong></div></div></td></tr>";
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
    <div class="form-inline">
        <img src="../../../bootstrap/film.png" height="90" width="140" style="margin-left: 50px"/>
        <select class="form-control input-lg" id="test" style="width: 220px" >
            <option value="1">Search by Name</option>
            <option value="2">Search by Comment</option>
            <option value="3">Search by summary</option>
            <option value="4">Combination Search</option>
        </select>
        <input name="query" id="query" size="60" class="form-control input-lg" placeholder="Query with anything" size="20">
        <input name="query" id="query1" size="12" class="form-control input-lg" placeholder="metaScore 0-100">
        <input name="query" id="query2" size="10" class="form-control input-lg" placeholder="userScore 0-10">
        <button type="submit" id="btn" class="btn-info" style="width: 120px;height: 45px">Search</button>
    </div>
    </br>
    <table class="table table-striped">
        <thead>
        <tr>
            <th>Film name</th>
            <th>Director</th>
            <th>Comment</th>
            <th>Movie poster and summary</th>
            <th>metaScore</th>
            <th>userScore</th>

        </tr>
        </thead>
        <tbody id="biao">

        </tbody>
    </table>
</head>
<body>

</body>
</html>
