<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>그린 근처 식당..</title>
    <style>
    /*임시 스타일!!!!!!!!*/
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
            gap: 20px; 
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
	    <c:forEach var="restaurant" items="${restaurants}">
	        <div class="restaurant">
	            <!-- 상세 페이지로 이동하는 링크 추가 -->
	            <a href="/restaurant/detail/${restaurant.id}">
	                <h3>${restaurant.name}</h3>
	            </a>
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
