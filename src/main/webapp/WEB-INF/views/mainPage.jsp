	<%@page import="com.example.demo.dto.MemberDto" %>
	<%@ page contentType="text/html;charset=UTF-8" language="java" %>
	<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
	<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
	<!-- 
	작성자: 구경림
	작성일: 2024.11.20
	 -->
	<!DOCTYPE html>
	<html lang="ko">
	
	<head>
	    <meta charset="UTF-8">
	    <title>그린 근처 식당</title>
	    <link rel="stylesheet" href="/static/css/mainPage/mainPage.css">
	    <link rel="icon" href="data:,">
	</head>
	
	<body>
	    <jsp:include page="./layout/header.jsp"></jsp:include>
	    <jsp:include page="./layout/nav.jsp"></jsp:include>
	
		<script type="text/javascript">
		    const memberId = "${member != null ? member.id : ''}";
		    console.log("Member ID:", memberId);
		
		    if (memberId) {
		        sessionStorage.setItem('memberId', memberId);
		    }
		</script>
	
	        <div id="container">
	            <div id="memberInfo">
	                <p>
	       					<c:if test="${not empty loginedState}">
						                    <span style="font-size: 12px">
	                  ${member.id}님, 반갑습니다
	               </span>
	               <br>오늘의 식사는 무엇인가요?
	       					</c:if>
	
	                </p>
	            </div>
	            <div id="loadingSpinner" class="loading-modal">
	                <div class="loading-content">
	                    <p>로딩 중입니다...</p>
	                    <div class="spinner"></div>
	                </div>
	            </div>
				<div id="filerAndSerchBox">
				
					    <select id="sortSelect" class="custom-select" onchange="onSortChange()">
					        <option value="rating" ${sortBy == 'rating' ? 'selected' : ''}>별점 높은</option>
					        <option value="reviewCount" ${sortBy == 'reviewCount' ? 'selected' : ''}>리뷰 많은</option>
					        <option value="distance" ${sortBy == 'distance' ? 'selected' : ''}>가까운</option>
					    </select>
				    <span>
	
					<div id="tagContainer">
					    <button class="tag" data-keyword="" onclick="onTagClick(this)">#전체</button>
					    <button class="tag" data-keyword="chicken" onclick="onTagClick(this)">#치킨</button>
					    <button class="tag" data-keyword="noodle" onclick="onTagClick(this)">#면요리</button>
					    <button class="tag" data-keyword="pizza" onclick="onTagClick(this)">#피자</button>
					    <button class="tag" data-keyword="korean" onclick="onTagClick(this)">#한식</button>
					</div>
	
				    </span>
				</div>
	            <div id="restaurantList">
	                <!-- 거리 기준으로 정렬된 음식점 리스트 -->
	                <c:forEach var="restaurant" items="${restaurants}">
	                    <div class="restaurant" data-store-id="${restaurant.placeId}">
	                        <!-- 이미지 -->
	                        <c:choose>
	                            <c:when test="${not empty restaurant.photoUrl}">
	                                <a href="/detail/${restaurant.placeId }">
	                                    <img src="${restaurant.photoUrl}" alt="${restaurant.name} 사진" />
	                                </a>
	                            </c:when>
	                            <c:otherwise>
	                                  <img src="/static/images/default-restaurant.jpg" alt="기본 이미지" />
	                            </c:otherwise>
	                        </c:choose>
	
	                        <!-- 텍스트 정보 -->
	                        <div class="restaurant-content">
	                            <a href="/detail/${restaurant.placeId }" style="text-decoration-line : none;">
	                                <h4>${restaurant.name}</h4>
	                            </a>
	                            <div class="details">
	                                <div>
	                                    <span>⭐ ${restaurant.rating}</span>
	                                </div>
	                                <div>
	                                    <span>
	                                        <fmt:formatNumber value="${restaurant.distance}" type="number"
	                                            maxFractionDigits="2" /> m
	                                    </span>
	                                </div>
	                            </div>
	
	                            <!-- 음식점 세부정보 -->
	                            <div class="restaurant-info">
	                                <p>${restaurant.address}</p>
	                                <p>${restaurant.phoneNumber}</p>
	                                <p>${restaurant.openNow ? '영업중' : '영업종료'}</p>
	                                <p>
	                                    <c:choose>
	                                        <c:when test="${restaurant.priceLevel == 0}">
	                                            매우 저렴
	                                        </c:when>
	                                        <c:when test="${restaurant.priceLevel == 1}">
	                                            저렴
	                                        </c:when>
	                                        <c:when test="${restaurant.priceLevel == 2}">
	                                            보통
	                                        </c:when>
	                                        <c:when test="${restaurant.priceLevel == 3}">
	                                            비쌈
	                                        </c:when>
	                                        <c:when test="${restaurant.priceLevel == 4}">
	                                            매우 비쌈
	                                        </c:when>
	                                        <c:otherwise>
	                                            가격 정보 없음
	                                        </c:otherwise>
	                                    </c:choose>
	                                </p>
	                                <p>
	                                    <strong>리뷰(${restaurant.reviewCount})</strong>
	                                </p>
	
	                            </div>
	                        </div>
	                        <!-- 좋아요와 즐겨찾기 -->
	                        <div class="actions">
	                            <!-- 좋아요 -->
	                            <div class="action-btn ${restaurant.liked ? 'liked' : ''}" data-type="like"
	                                onclick="toggleAction('${restaurant.placeId}', 'G', this)">
	                                <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24">
	                                    <path
	                                        d="M12 21.35l-1.45-1.32C5.4 15.36 2 12.28 2 8.5 2 5.42 4.42 3 7.5 3c1.74 0 3.41.81 4.5 2.09C13.09 3.81 14.76 3 16.5 3 19.58 3 22 5.42 22 8.5c0 3.78-3.4 6.86-8.55 11.54L12 21.35z" />
	                                </svg>
	                                <span>${restaurant.liked ? "좋아요 취소" : "좋아요"}</span>
	                            </div>
	
	                            <!-- 즐겨찾기 -->
	                            <div class="action-btn ${restaurant.favorited ? 'favorited' : ''}"
	                                data-type="favorite"
	                                onclick="toggleAction('${restaurant.placeId}', 'F', this)">
	                                <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24">
	                                    <path
	                                        d="M12 17.27L18.18 21l-1.64-7.03L22 9.24l-7.19-.61L12 2 9.19 8.63 2 9.24l5.46 4.73L5.82 21z" />
	                                </svg>
	                                <span>${restaurant.favorited ? "즐겨찾기 취소" : "즐겨찾기"}</span>
	                            </div>
	                        </div>
	                    </div>
	                </c:forEach>
	            </div>
	        </div>
	
	        <!-- 페이지네이션 -->
	        <div class="pagination">
	            <!-- 이전 버튼 -->
	            <c:if test="${pagingBtn.prevBtn}">
	                <a
	                    href="/?page=${currentPage - 1}&sortBy=${param.sortBy}&keyword=${param.keyword}">이전</a>
	            </c:if>
	
	            <!-- 페이지 번호 -->
	            <c:forEach begin="${pagingBtn.startPage}" end="${pagingBtn.endPage}" var="i">
	                <c:choose>
	                    <c:when test="${i == currentPage}">
	                        <span class="current-page">${i}</span>
	                    </c:when>
	                    <c:otherwise>
	                        <a href="/?page=${i}&sortBy=${param.sortBy}&keyword=${param.keyword}">${i}</a>
	                    </c:otherwise>
	                </c:choose>
	            </c:forEach>
	
	            <!-- 다음 버튼 -->
	            <c:if test="${pagingBtn.nextBtn}">
	                <a
	                    href="/?page=${currentPage + 1}&sortBy=${param.sortBy}&keyword=${param.keyword}">다음</a>
	            </c:if>
	        </div>
	        </div>
	        <jsp:include page="./layout/footer.jsp"></jsp:include>
	        <script src="/static/js/mainPage/mainPage.js"></script>
	</body>
	
	</html>