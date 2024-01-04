<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%
    request.setCharacterEncoding("utf-8");
    // 한글 깨짐 방지
    int currentPage = Integer.parseInt(request.getParameter("currentPage") == null || request.getParameter("currentPage") == "" ? "0" : request.getParameter("currentPage"));
    int totalCount = Integer.parseInt(request.getParameter("totalCount") == null || request.getParameter("totalCount") == "" ? "0" : request.getParameter("totalCount"));
    int listCount = Integer.parseInt(request.getParameter("listCount") == null || request.getParameter("listCount") == "" ? "0" : request.getParameter("listCount"));
%>
<script>
    function fnPaging(num){
        console.log(num);
        if(num > 0){
            $("#currentPage").val(num);
            $("#searchForm").submit();
        }

    }
</script>
<div class="paging">
    <table  style="width: 0px; text-align: center; margin: auto">
        <tr>
            <!-- 현제페이지 -->
            <c:set var="page" value="<%=currentPage%>"/> <!-- 폼을 통해 넘겨받은 현재페이지 값을 불러온다. -->
            <!-- 게시글 수 -->
            <c:set var="listCount" value="<%=listCount%>"/>
            <!-- 페이지 수 -->
            <c:set var="listPage" value="10"/>
            <!-- 전체게시글 수 -->
            <c:set var="totalCount" value="<%=totalCount%>"/> <!-- 총 게시글 리스트 값을 불러온다. -->
            <!-- 전체페이지 수 -->
            <c:set var="totalPage" value="${totalCount / listCount}"/>
            <!-- 페이지 생성 시작값 -->
            <c:set var="startPage" value="1"/>
            <!-- 페이지 생성 종료값 -->
            <c:set var="endPage" value="${totalPage+(1-(totalPage%1))%1}"/>

            <!-- 전체 페이지 수가 페이지 수보다 클 때 -->
            <c:if test="${totalPage gt listPage}">
                <c:set var="endPage" value="${listPage}"/>
            </c:if>

            <!-- 현재페이지가 페이지 수보다 크거나 같을 때 -->
            <c:if test="${page ge listPage}">
                <c:set var="startPage" value="${(page / listPage - (page / listPage) % 1) * listPage}"/>
                <c:set var="endPage" value="${startPage + listPage}"/>

                <!-- 현재페이지가 끝 페이지일 때 -->
                <c:if test="${page ge ((totalPage / listPage - (totalPage / listPage) % 1) * listPage)}">
                    <c:set var="endPage" value="${totalPage}"/>
                </c:if>
            </c:if>

            <!-- 현재페이지가 마지막 페이지일 때 -->
            <c:if test="${page mod listPage eq 0}">
                <c:set var="startPage" value="${page}"/>
                <c:set var="endPage" value="${page + listPage}"/>

                <c:if test="${(page + listPage) ge totalPage}">
                    <c:set var="endPage" value="${totalPage}"/>
                </c:if>
            </c:if>
            <td><label style="margin: 0"><p style="margin: 0; background-color: rgb(239, 239, 239); padding: 6px 12px 6px 12px; font-size: 14px; font-weight: normal; cursor: pointer" ><a href="#" class="p_skip first" onclick="fnPaging(1);"> << </a></p></label></td>
            <td><label style="margin: 0"><p style="margin: 0; background-color: rgb(239, 239, 239); padding: 6px 12px 6px 12px; font-size: 14px; font-weight: normal; cursor: pointer" > <a href="#" class="p_skip prev" <c:if test="${page gt '1'}">onclick="fnPaging(${page - 1});"</c:if>> < </a> </p></label></td>
            <c:forEach begin="${startPage}" end="${endPage}" step="1" var="nowPage">
                <c:choose>
                    <c:when test="${nowPage eq page}">
                        <td><label style="margin: 0"><p style="margin: 0; background-color: rgb(54, 138, 255); padding: 6px 12px 6px 12px; font-size: 14px; font-weight: bold; color:white;" >${nowPage}</p></label></td>
                    </c:when>
                    <c:otherwise>
                        <td><label style="margin: 0"><p style="margin: 0; background-color: rgb(239, 239, 239); padding: 6px 12px 6px 12px; font-size: 14px; font-weight: normal; cursor: pointer" ><a href="#" onclick="fnPaging(${nowPage});">${nowPage}</a></p></label></td>
                    </c:otherwise>
                </c:choose>
            </c:forEach>
            <td><label style="margin: 0"><p style="margin: 0; background-color: rgb(239, 239, 239); padding: 6px 12px 6px 12px; font-size: 14px; font-weight: normal; cursor: pointer" ><a href="#" class="p_skip next" <c:if test="${page lt totalPage}">onclick="fnPaging(${page + 1});"</c:if>> > </a></label></td>
            <td><label style="margin: 0"><p style="margin: 0; background-color: rgb(239, 239, 239); padding: 6px 12px 6px 12px; font-size: 14px; font-weight: normal; cursor: pointer" ><a href="#" class="p_skip end" onclick="fnPaging(${totalPage});"> >> </a></p></label></td>
        </tr>
    </table>
</div>