<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="sec" %>
<script src="https://code.jquery.com/jquery-3.6.4.min.js"></script>
<script>
    $(document).ready(function () {
        $("#userid").focus();
        $.ajax({
            url: '/searchProduct', // 실제 서버 URL로 변경해야 합니다.
            type: 'GET',
            dataType: 'json',
            success: function (data) {
                // 성공적으로 데이터를 받았을 때 처리
                handleResponse(data);
            },
            error: function (xhr, status, error) {
                console.error('Ajax request failed:', status, error);
            }
        });
    });

    function chkForm(formobj) {
        formobj = formobj || $('#loginForm');
        with (formobj) {
            if (userid.value.split(' ').join('') == '') {
                alert('ID를 입력해 주시기 바랍니다.');
                userid.focus();
                return false;
            }
            if (password.value.split(' ').join('') == '') {
                alert('PW를 입력해 주시기 바랍니다.');
                password.focus();
                return false;
            }
        }
        return true;
    }

    function doLogin(formobj) {
        if (chkForm(formobj)) {
            formobj.submit();
        }
    }

    function goMain() {
        location.replace('/cp');
        return;
    }

    function showLoginCode() {
        popupmainCenter();
        $("#popmain").show();
    }

    function popupmainCenter() {
        var width = $('.popmain').width();
        var height = $('.popmain').height();

        $('.popmain').css({'left': ($(window).width() - width) / 2, 'top': ($(window).height() - height) / 6});
    }

    function senderLoginCode() {
        alert("폰봐주세요?");
    }

    function checkLoginCode() {
        $("#pw").val($("#str").val());
        $('#loginForm').submit();
    }

    function pressEnter() {
        checkLoginCode();
    }



    function handleResponse(data) {
        // itemList를 이용하여 원하는 작업 수행
        for (var i = 0; i < data.length; i++) {
            var item = data[i];
            console.log("Title: " + item.title);
            console.log("Link: " + item.link);
            console.log("Image: " + item.image);
            console.log("Lowest Price: " + item.lprice);
            console.log("Mall Name: " + item.mallName);
            // 추가 필요한 정보들에 대한 처리
        }
    }
</script>
<style>
  .popmain {
        display: none;
        width: 40%;
        height: 20%;
        background: white;
        color: black;
        position: fixed;
        z-index: 1;
        border: 1px solid #333333;
    }
</style>


<%--<sec:authorize access="isAuthenticated()">--%>
<%--    <script>goMain();--%>
<%--    alert("?")</script>--%>
<%--</sec:authorize>--%>

<sec:authorize access="isAnonymous()">

    <div class="row">
        <div class="col-xs-6 col-sm-4"></div>
        <div class="col-xs-6 col-sm-4">
            <br/><br/><br/><br/>
            <p style="text-align: center; font-size: 40px; font-weight: bolder">MARKET_ADMIN</p>
            <form id='loginForm' action="/loginProcess" method="post" onsubmit="return chkForm(this);">
                <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                <div class="form-group">
                    <input type="text" id="userid" name="userid" class="form-control" placeholder="Enter your id" value="">
                </div>
                <div class="form-group">
                    <input type="password" id="password" name="password" class="form-control" placeholder="Password" value="" onkeyup="if(event.keyCode == 13) checkLoginCode()">
                </div>
                <div class="form-group" style="text-align: center;">
                    <input type="text" id="otpSecret" name="otpSecret" placeholder="인증번호" value="" autocomplete="off" />
                    <input type="button" class="btn btn-default" value="인증번호 받기" onclick="senderLoginCode();"/>
                </div>
                <input type="hidden" class="form-control" id="pw" name="pw" value=""/>
                <c:if test="${not empty SPRING_SECURITY_LAST_EXCEPTION}">
                    <div>
                        <pre>${ sessionScope["SPRING_SECURITY_LAST_EXCEPTION"].message }</pre>
                        <c:remove var="SPRING_SECURITY_LAST_EXCEPTION" scope="session"/>
                    </div>
                </c:if>
                <input type="button" class="btn btn-primary btn-lg btn-block" onclick="checkLoginCode();" value="로그인" />
            </form>
        </div>
    </div>
</sec:authorize>



<div id="popmain" class="popmain">
    <table class="table table-hover" style="text-align: center;">
        <tr class="info">
            <td>
                <label>접속 코드</label>
            </td>
        </tr>
    </table>
    <div>
        <div class="col-md-10 col-md-offset-1">
            <form class="form-inline" autocomplete="off" onsubmit="return false;">
                <table style="width: 100%;" id="userForm">
                    <tr style="text-align: center">
                        <td>
                            <input type="text" class="form-control" id="str" value="" onkeyup="if(event.keyCode == 13) pressEnter()" />
                            <input type="button" class="btn btn-primary" onclick="this.disabled=true;this.value='Submitting...';checkLoginCode();" value="확인"/>
                        </td>
                    </tr>
                </table>
            </form>
        </div>
    </div>
</div>