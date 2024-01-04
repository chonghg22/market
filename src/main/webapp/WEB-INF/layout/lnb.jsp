<%@ page language="java" contentType="text/html; charset=utf-8"
		 pageEncoding="utf-8" %>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<script>
$( function() {
	$( "#menu" ).menu();

	$("#mini").click(function() {
		$(".main-sidebar").css("width", "40px");
		$(".content-wrapper").css("margin-left", "40px");
		$(".maxiDiv").show();
		$(".miniDiv").hide();
	});
	$("#maxi").click(function() {
		$(".main-sidebar").css("width", "200px");
		$(".content-wrapper").css("margin-left", "200px");
		$(".miniDiv").show();
		$(".maxiDiv").hide();
	});
});
</script>