<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Nearby Restaurants</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            margin: 20px;
        }
        h1 {
            text-align: center;
        }
        #restaurantList {
            display: grid;
            grid-template-columns: repeat(auto-fill, minmax(250px, 1fr)); /* 각 카드의 최소 너비는 250px */
            gap: 20px; /* 카드 사이의 간격 */
            padding: 20px;
        }
        .restaurant {
            border: 1px solid #ddd;
            padding: 15px;
            text-align: center;
        }

        .restaurant h3 {
            margin: 10px 0;
            font-size: 1.2em;
            color: #333;
        }
        .restaurant img {
            max-width: 100%;
            margin: 10px 0;
        }
        .restaurant p {
            margin: 5px 0;
            color: #666;
        }
    </style>
</head>
<body>
    <h1>green bobbb</h1>
    <div id="restaurantList">
        <!-- 거리 기준으로 정렬된 음식점 리스트 -->
        <c:forEach var="restaurant" items="${restaurants}">
            <div class="restaurant">
                <h3>${restaurant.name}</h3>
                <p><strong>Address:</strong> ${restaurant.address}</p>
                <p><strong>Rating:</strong> ${restaurant.rating}</p>
                <p><strong>Distance:</strong> ${restaurant.distance} km</p>
                
                <c:if test="${not empty restaurant.photoUrl}">
                    <img src="${restaurant.photoUrl}" alt="${restaurant.name} Photo" />
                </c:if>
            </div>
        </c:forEach>
    </div>
</body>
</html>
