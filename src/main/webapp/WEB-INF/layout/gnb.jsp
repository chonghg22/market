<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<script src="https://stackpath.bootstrapcdn.com/bootstrap/3.4.1/js/bootstrap.min.js"></script>
<script type="text/javascript" src="https://cdn.jsdelivr.net/momentjs/latest/moment.min.js"></script>
<script type="text/javascript" src="https://cdn.jsdelivr.net/npm/daterangepicker/daterangepicker.min.js"></script>
<link rel="stylesheet" type="text/css" href="https://cdn.jsdelivr.net/npm/daterangepicker/daterangepicker.css"/>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<script>
    $(function () {
        var start = moment($("#searchStartDay").val(), "YYYY-MM-DD");
        var end = moment($("#searchEndDay").val(), "YYYY-MM-DD");
        var weekSt = moment($("#searchWeekDay").val(), "YYYY-MM-DD");

        function cb(start, end, weekSt) {
            $('#searchStartDay').val(start.format('YYYY-MM-DD'));
            $('#searchEndDay').val(end.format('YYYY-MM-DD'));
            //$('#searchWeekDay').val(weekSt.format('YYYY-MM-DD'));
            $('#days').val(start.format('YYYY-MM-DD') + ' ~ ' + end.format('YYYY-MM-DD'));
            //$('#wDays').val(weekSt.format('YYYY-MM-DD') + ' ~ ' + end.format('YYYY-MM-DD'));
        }

        $('input[name="days"]').daterangepicker({
            locale   : {
                format          : 'YYYY-MM-DD',
                separator       : " ~ ",
                applyLabel      : "선택",
                cancelLabel     : "취소",
                customRangeLabel: "날짜 선택",
                daysOfWeek      : [
                    "일",
                    "월",
                    "화",
                    "수",
                    "목",
                    "금",
                    "토"
                ],
                monthNames      : [
                    "1월",
                    "2월",
                    "3월",
                    "4월",
                    "5월",
                    "6월",
                    "7월",
                    "8월",
                    "9월",
                    "10월",
                    "11월",
                    "12월"
                ],
            },
            startDate: start,
            endDate  : end,/*
			showDropdowns: true,*/
            ranges   : {
                '오늘' : [moment(), moment()],
                '어제' : [moment().subtract(1, 'days'), moment().subtract(1, 'days')],
                '지난달': [moment().subtract(1, 'month').startOf('month'), moment().subtract(1, 'month').endOf('month')],
                '이번달': [moment().startOf('month'), moment()],
                '1주' : [moment().subtract(6, 'days'), moment()],
                '1달' : [moment().subtract(1, 'month').subtract(-1, 'days'), moment()],
                '3달' : [moment().subtract(3, 'month').subtract(-1, 'days'), moment()],
                '6달' : [moment().subtract(6, 'month').subtract(-1, 'days'), moment()],
                '1년' : [moment().subtract(1, 'year').subtract(-1, 'days'), moment()]
            }
        }, cb);

        $('input[name="wDays"]').daterangepicker({
            locale   : {
                format          : 'YYYY-MM-DD',
                separator       : " ~ ",
                applyLabel      : "선택",
                cancelLabel     : "취소",
                customRangeLabel: "날짜 선택",
                daysOfWeek      : [
                    "일",
                    "월",
                    "화",
                    "수",
                    "목",
                    "금",
                    "토"
                ],
                monthNames      : [
                    "1월",
                    "2월",
                    "3월",
                    "4월",
                    "5월",
                    "6월",
                    "7월",
                    "8월",
                    "9월",
                    "10월",
                    "11월",
                    "12월"
                ],
            },
            startDate: weekSt,
            endDate  : end,/*
			showDropdowns: true,*/
            ranges   : {
                '오늘' : [moment(), moment()],
                '어제' : [moment().subtract(1, 'days'), moment().subtract(1, 'days')],
                '지난달': [moment().subtract(1, 'month').startOf('month'), moment().subtract(1, 'month').endOf('month')],
                '이번달': [moment().startOf('month'), moment()],
                '1주' : [moment().subtract(6, 'days'), moment()],
                '1달' : [moment().subtract(1, 'month').subtract(-1, 'days'), moment()],
                '3달' : [moment().subtract(3, 'month').subtract(-1, 'days'), moment()],
                '6달' : [moment().subtract(6, 'month').subtract(-1, 'days'), moment()],
                '1년' : [moment().subtract(1, 'year').subtract(-1, 'days'), moment()]
            }
        }, cb);

        cb(start, end, weekSt);

    });

    String.prototype.replaceAll = function (org, dest) {
        return this.split(org).join(dest);
    }
    $(function () {
        let value = $("#" + $(location).attr('pathname').replaceAll("/", ""));
        $(value).attr("selected", "selected");

        $("#speed").selectmenu({
            select: function (event, data) {
                if (data.item.element[0].id == "goodsUpdate") {

                    return;
                }
                location.href = data.item.value;
            }
            // change: function( event, data ) {
            //
            // }
        });

        $("#searchForm .form-control").keydown(function (e) {
            if (e.keyCode == 13) {
                $("#currentPage").val(1);
                $("#searchForm").submit();
            }
        });
    });

    function productsUpdate() {
        let result = confirm("상품 목록을 업데이트 하시겠습니까?");
        if (result) {
            $('.wrap-loading-notice').removeClass('display-none');
            $.ajax({
                type    : "POST",
                url     : "/product/productsSync",
                data    : {goodId: ""},
                dataType: "json",
                complete: function () {
                    $('.wrap-loading-notice').addClass('display-none');
                },
                success : function (result) {
                    alert("적용 되었습니다.");
                    location.href = "/product/productList";
                },
                error   : function (e) {
                    alert("정보 업데이트를 실패하였습니다.다시 시도해주세요.");
                    $('.wrap-loading-notice').addClass('display-none');
                }
            });
        } else {
            alert("신중히 해주세요");
        }

    }
</script>
<style>
    .wrap-loading-notice { /*화면 전체를 어둡게 합니다.*/
        position: fixed;
        left: 0;
        right: 0;
        top: 0;
        bottom: 0;
        background: rgba(0, 0, 0, 0.2); /*not in ie */
        z-index: 0;
    }

    .wrap-loading-notice div { /*로딩 이미지*/
        position: fixed;
        top: 50%;
        left: 50%;
        margin-left: -100px;
        margin-top: -100px;
        z-index: 4;
    }

    .display-none { /*감추기*/
        display: none;
    }
    a{
        color: black;
    }
</style>

<div class="wrap-loading-notice display-none">
    <div><img src="/img/ajax-loader.gif"/></div>
</div>

<!-- Header Navbar -->
<nav role="navigation">
    <div>
        <ul class="nav nav-tabs">
            <li><a href="/" >MARKET</a></li>
            <li class="dropdown">
                <a href="#" class="dropdown-toggle" data-toggle="dropdown">
                    <span class="glyphicon glyphicon-shopping-cart"></span>주문조회<span class="caret"></span>
                </a>
                <ul class="dropdown-menu">
                    <li><a href="/order/orderList">주문결제내역</a></li>
                </ul>
            </li>
            <li class="dropdown">
                <a href="#" class="dropdown-toggle" data-toggle="dropdown">
                    <span class="glyphicon glyphicon-user"></span>CS<span class="caret"></span>
                </a>
                <ul class="dropdown-menu">
                    <li><a href="/qna/qnaList">QNA</a></li>
                    <li><a href="/qna/qnaTemplateList">QNA 템플릿</a></li>
                </ul>
            </li>
            <li class="pull-right">
                <a href="/logout" class="btn-primary">로그아웃</a>
            </li>
        </ul>
    </div>
    <br/>
</nav>

