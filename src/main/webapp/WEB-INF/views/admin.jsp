<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="utf-8">
        <title>어드민 페이지</title>
        <script src="https://cdn.jsdelivr.net/npm/axios/dist/axios.min.js"></script> <!-- Axios 추가 -->
        <style>
            body {
                font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
                margin: 0;
                padding: 0;
                background-color: #1e1e2f;
                color: #e0e0e0;
            }
            .admin-header {
                background-color: #2b2b40;
                color: #ffffff;
                padding: 20px;
                text-align: center;
                font-size: 1.8em;
                font-weight: bold;
                border-bottom: 2px solid #5050f0;
            }
            .tab-menu {
                display: flex;
                background-color: #2b2b40;
            }
            .tab-menu button {
                flex: 1;
                padding: 15px 0;
                border: none;
                background-color: #2b2b40;
                cursor: pointer;
                color: #8a8ab5;
                font-size: 1.2em;
                transition: all 0.3s;
            }
            .tab-menu button:hover, .tab-menu button.active {
                background-color: #3a3a55;
                color: #ffffff;
                border-bottom: 3px solid #5050f0;
            }
            .content {
                padding: 30px;
            }
            .user-table, .room-table {
                width: 100%;
                border-collapse: collapse;
                background-color: #292942;
                color: #ffffff;
                border-radius: 8px;
                overflow: hidden;
                margin-top: 20px;
            }
            .user-table th, .user-table td, .room-table th, .room-table td {
                border: 1px solid #3a3a55;
                padding: 15px;
                text-align: left;
            }
            .user-table th, .room-table th {
                background-color: #3a3a55;
                font-weight: bold;
            }
            .user-table tr:hover, .room-table tr:hover {
                background-color: #404062;
            }
            h2 {
                color: #ffffff;
                border-bottom: 2px solid #5050f0;
                display: inline-block;
                padding-bottom: 5px;
            }
            p {
                color: #c0c0d6;
            }
            .room-form {
                margin-top: 20px;
            }
            .room-form input, .room-form button {
                padding: 10px;
                margin-right: 10px;
            }
        </style>
    </head>
    <body>
        <div class="admin-header">어드민 페이지</div>
        <div class="tab-menu">
            <button class="active" data-tab="user-management" onclick="showTab('user-management')">사용자 관리</button>
            <button data-tab="lecture-management" onclick="showTab('lecture-management')">강의실 관리</button>
        </div>
        <div class="content">
            <!-- 사용자 관리 -->
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
            </div>
            <!-- 강의실 관리 -->
            <div id="lecture-management" class="tab-content" style="display: none;">
                <h2>강의실 관리</h2>
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
                <!-- 강의실 추가 폼 -->
                <div class="room-form">
                    <input type="text" id="roomCode" placeholder="강의실 코드">
                    <input type="text" id="roomName" placeholder="강의실 이름">
                    <button onclick="insertRoom()">강의실 추가</button>
                </div>
            </div>
        </div>
    </body>
    <script src="/static/js/admin.js"> </script>
</html>
