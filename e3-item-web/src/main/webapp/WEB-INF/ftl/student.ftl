<html>
<head>
    <title>Title</title>
</head>
<body>
<table border="1">
    <tr>

        <td>序号</td>
        <td>学号</td>
        <td>姓名</td>
        <td>年龄</td>
    </tr>
        <#list studentList as studnet>
            <tr>
            <#if studnet_index % 2 ==0>
            <tr bgcolor="#a52a2a"/>
            <#else>
        <tr bgcolor="#008b8b"/>
            </#if>
                <td>${studnet_index}</td>
                <td>${studnet.id}</td>
                <td>${studnet.name}</td>
                <td>${studnet.age}</td>
            </tr>
        </#list>
</table>
<br>
<br>
当前日期:${date?date}</br>
当前日期:${date?time}</br>
当前日期:${date?datetime}</br>
当前日期:${date?string("yyyy/MM/dd HH:mm:ss")}</br>
<br>
<br>
null值处理:${myval!"myval为null"}<br>
null值处理:
<#if myval2??>
    myval2不为空时.....
<#else>
myval2为空时######
</#if>
<br>
<br>
<#include "hello.ftl"/>
</body>
</html>