<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE HTML>
<html>
<head>
    <tiles:insertAttribute name="header"/>
</head>
<body class="hold-transition skin-purple-light sidebar-mini">
<div class="wrapper">

    <header class="main-header">
        <!-- Gnb -->
        <tiles:insertAttribute name="gnb"/>
    </header>

<%--    <!-- Left side column. contains the logo and sidebar -->--%>
<%--    <aside class="main-sidebar">--%>
<%--        <!-- sidebar: style can be found in sidebar.less -->--%>
<%--        <section class="sidebar">--%>

<%--            <tiles:insertAttribute name="lnb"/>--%>

<%--        </section>--%>
<%--        <!-- /.sidebar -->--%>
<%--    </aside>--%>

    <!-- Content Wrapper. Contains page content -->
    <div>
        <!-- Main -->
        <tiles:insertAttribute name="body"/>
    </div>
    <!-- /.content-wrapper -->

</div>
<!-- ./wrapper -->

</body>
</html>