<%@ page import="java.io.PrintWriter" %>
<%@ page import="java.io.StringWriter" %>
<%@ page contentType="text/html; ISO-8859-1" pageEncoding="UTF-8" language="java" isErrorPage="true" %>
<h3>Вызвано исключение</h3>
<p>Трасировачная информация:<br/>
    <%
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        exception.printStackTrace(pw);
        out.write(sw.toString());
    %>
</p>