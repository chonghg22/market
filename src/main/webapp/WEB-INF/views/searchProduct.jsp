<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="sec" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <title>Bootstrap Template</title>
    <script src="https://code.jquery.com/jquery-3.6.4.min.js"></script>
    <!-- Bootstrap CSS -->
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css">
    <style>
        body {
            padding-top: 56px; /* 메뉴바의 높이에 맞게 body를 내림 */
        }

        .board-container {
            margin-top: 20px;
        }
    </style>
</head>
<body>

<!-- Navigation Bar -->
<nav class="navbar navbar-expand-lg navbar-light bg-light fixed-top">
    <a class="navbar-brand" href="#">Your Logo</a>
    <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarNav" aria-controls="navbarNav" aria-expanded="false" aria-label="Toggle navigation">
        <span class="navbar-toggler-icon"></span>
    </button>
    <div class="collapse navbar-collapse" id="navbarNav">
        <ul class="navbar-nav">
            <li class="nav-item active">
                <a class="nav-link" href="#">Home <span class="sr-only">(current)</span></a>
            </li>
            <li class="nav-item">
                <a class="nav-link" href="#">Menu Item 1</a>
            </li>
            <li class="nav-item">
                <a class="nav-link" href="#">Menu Item 2</a>
            </li>
            <!-- 2-level dropdown -->
            <li class="nav-item dropdown">
                <a class="nav-link dropdown-toggle" href="#" id="navbarDropdown" role="button" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
                    Submenu
                </a>
                <div class="dropdown-menu" aria-labelledby="navbarDropdown">
                    <a class="dropdown-item" href="#">Submenu Item 1</a>
                    <a class="dropdown-item" href="#">Submenu Item 2</a>
                </div>
            </li>
        </ul>
    </div>
</nav>

<!-- Main Content -->
<div class="container board-container">
    <!-- Search Bar -->
    <div class="row mb-3">
        <div class="col-md-6">
            <div class="input-group">
                <input type="text" class="form-control" placeholder="Search" id="searchWord">
                <div class="input-group-append">
                    <button class="btn btn-primary" type="button" id="searchButton">Search</button>
                </div>
            </div>
        </div>
        <div class="col-md-6">
            <select class="form-control">
                <option value="">All Categories</option>
                <option value="category1">Category 1</option>
                <option value="category2">Category 2</option>
            </select>
        </div>
    </div>

    <!-- Board Template using Table -->
    <table class="table" >
        <!-- Table Header -->
        <thead>
        <tr>
            <th scope="col">Image</th>
            <th scope="col">Post Title</th>
            <th scope="col">Description</th>
            <th scope="col">Action</th>
        </tr>
        </thead>
        <!-- Table Body -->
        <tbody id="postTable">
        <!-- Sample Row -->
        <tr>
            <td>
                <img src="https://via.placeholder.com/150" alt="Placeholder Image">
            </td>
            <td>Post Title 1</td>
            <td>Lorem ipsum dolor sit amet, consectetur adipiscing elit.</td>
            <td>
                <a href="detail.html" class="btn btn-primary">Read More</a>
            </td>
        </tr>
        <!-- Add more rows as needed -->
        </tbody>
    </table>
</div>
<script>
    $(document).ready(function() {
        // 초기 데이터로 테이블 채우기
        // loadData();

        // 검색 버튼 클릭 시 동작

        $('#searchButton').on('click', function() {
            // 여기에서 Ajax 요청을 보내고, 받아온 데이터를 appendDataToTable 함수를 이용하여 테이블에 추가
            // 실제 서버에서는 원하는 엔드포인트 및 데이터 포맷에 맞게 요청을 수정해야 합니다.
            loadData();
        });
    });
    function loadData() {
        // JSONPlaceholder 가짜 REST API를 사용하는 예제
        $.ajax({
            url: '/searchProduct',
            data: {"searchWord": $("#searchWord").val()},
            method: 'GET',
            success: function(data) {
                appendDataToTable(data.productList);
                console.log(data.productList);
            },
            error: function(error) {
                console.error('Error fetching data:', error);
            }
        });
    }

    function appendDataToTable(data) {
        var tbody = $('#postTable');

        // 기존 데이터 제거
        tbody.empty();

        // 데이터 순회하며 행 추가
        for (var i = 0; i < data.length; i++) {
            var row = '<tr>' +
                // '<td>' +
                // '<img src="' + data[i].link + '" alt="Placeholder Image">' +
                // '</td>' +
                // <td>Post Title 1</td>
                // <td>Lorem ipsum dolor sit amet, consectetur adipiscing elit.</td>
                // <td>
                //     <a href="detail.html" class="btn btn-primary">Read More</a>
                // </td>
                //     '<td>' + data[i].id + '</td>' +
                    '<td>' + data[i].title + '</td>' +
                //     '<td>' + data[i].body + '</td>' +
                '</tr>';
            tbody.append(row);
        }
    }
</script>
<!-- Bootstrap JS and dependencies -->
<script src="https://code.jquery.com/jquery-3.3.1.slim.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/@popperjs/core@2.11.6/dist/umd/popper.min.js"></script>
<script src="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/js/bootstrap.min.js"></script>
<script src="https://code.jquery.com/jquery-3.6.4.min.js"></script>
</body>
</html>


