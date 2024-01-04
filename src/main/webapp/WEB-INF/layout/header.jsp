<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no" name="viewport">
<title>MARKET</title>

<%--<link rel="stylesheet" href="/css/cp.css">--%>
<link rel="stylesheet" href="//code.jquery.com/ui/1.12.1/themes/base/jquery-ui.css" />
<script src="https://code.jquery.com/jquery-3.5.1.js"></script>
<script src="https://code.jquery.com/ui/1.12.1/jquery-ui.js"></script>


<link rel="stylesheet" type="text/css" href="https://cdn.datatables.net/v/dt/dt-1.10.21/datatables.min.css"/>
<script type="text/javascript" src="https://cdn.datatables.net/v/dt/dt-1.10.21/datatables.min.js"></script>
<link href="//cdn.jsdelivr.net/gh/gitbrent/bootstrap4-toggle@3.4.0/css/bootstrap4-toggle.min.css" rel="stylesheet">
<script src="//cdn.jsdelivr.net/gh/gitbrent/bootstrap4-toggle@3.4.0/js/bootstrap4-toggle.min.js"></script>


<link rel = "stylesheet" href="${pageContext.request.contextPath}/bootstrap/css/bootstrap.css" />
<link rel = "stylesheet" href="${pageContext.request.contextPath}/bootstrap/js/bootstrap.js" />

<link rel="shortcut icon" type="image/png/ico" href="${pageContext.request.contextPath}/img/favicon.ico">
<style>
	div.container {
		width: 100%;
		height: 100%;
		overflow: auto;
	}
</style>
<script>
    $(function(){
        $("input:text[numberOnly]").on("focus", function() {
            var x = $(this).val();
            $(this).val(x);
        }).on("focusout", function() {
            var x = $(this).val();
            if(x && x.length > 0) {
                if(!$.isNumeric(x)) {
                    x = x.replace(/[^0-9]/g,"");
                }
                $(this).val(x);
            }
        }).on("keyup", function() {
            $(this).val($(this).val().replace(/[^0-9]/g,""));
        });
    });
    function timestampToFormat(val) {
        if(val == undefined){
            return '';
        }
        var date =  new Date(Date.parse(val));

        var day = ('0' + date.getDate()).slice(-2);
        var month = ('0' + (date.getMonth() + 1)).slice(-2);
        var year = date.getFullYear();

        var hh = ('0' + date.getHours()).slice(-2);
        var mm = ('0' + date.getMinutes()).slice(-2);
        var ss = ('0' + date.getSeconds()).slice(-2);

        return year + '-' + month + '-' + day + ' ' + hh + ':' + mm + ':' + ss;
    }

</script>
