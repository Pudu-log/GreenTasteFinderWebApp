<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="utf-8">
        <title>관리자 페이지</title>
        <script src="https://cdn.jsdelivr.net/npm/axios/dist/axios.min.js"></script> <!-- Axios 추가 -->
        <link rel="stylesheet" href="/static/css/admin.css">
        <link rel="icon" href="data:,">
    </head>
    <body>
        <div class="admin-header">관리자 페이지</div>
        <div class="tab-menu">
            <button class="active" data-tab="user-management" onclick="showTab('user-management')">사용자 관리</button>
            <button data-tab="lecture-management" onclick="showTab('lecture-management')">강의실 관리</button>
        </div>
        <div class="content">
            <div id="user-management" class="tab-content">
                <h2>사용자 관리</h2>
                <table class="user-table">
                    <thead>
                        <tr>
                            <th>사용자 ID</th>
                            <th>이름</th>
                            <th>강의실</th>
                            <th>이메일</th>
                            <th>생일</th>
                            <th>생성날짜</th>
                            <th>권한</th>
                            <th>삭제</th>
                        </tr>
                    </thead>
                    <tbody>
                    </tbody>
                </table>
                <div id="paging-buttons" class="paging-buttons"></div>
                <div class="search-container">
                    <input type="text" id="searchInput" placeholder="검색 (아이디, 이름, 강의실)">
                    <button onclick="searchUsers()">검색</button>
                </div>
            </div>
            <!-- 강의실 관리 -->
            <div id="lecture-management" class="tab-content" style="display: none;">
                <h2>강의실 관리</h2>
                <!-- 강의실 추가 폼 -->
                <div class="room-form">
                    <input type="number" id="roomCode" placeholder="강의실 코드">
                    <input type="text" id="roomName" placeholder="강의실 이름">
                    <button onclick="insertRoom()">강의실 추가</button>
                </div>
                <table class="room-table">
                    <thead>
                        <tr>
                            <th>강의실 코드</th>
                            <th>강의실 이름</th>
                            <th>삭제</th>
                        </tr>
                    </thead>
                    <tbody id="room-table-body">
                    </tbody>
                </table>
            </div>
        </div>
    </body>
    <script src="/static/js/adminCommon.js"></script>
    <script src="/static/js/adminUser.js"></script>
    <script src="/static/js/adminRoom.js"></script>

</html>
