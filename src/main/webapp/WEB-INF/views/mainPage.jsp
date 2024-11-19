<%@ page contentType="text/html;charset=UTF-8" language="java" %>
	<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
		<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

			<!DOCTYPE html>
			<html lang="ko">

			<head>
				<meta charset="UTF-8">
				<title>그린 근처 식당</title>
				<style>
					body {
						margin: 0 auto;
					}

					#container {
						margin-top: 100px;
						display: flex;
						align-items: center;
						justify-content: center;
						flex-direction: column;
					}

					#restaurantList {
						display: grid;
						grid-template-columns: repeat(auto-fill, minmax(200px, 1fr));
						gap: 30px;
						padding: 20px;
						max-width: 900px;
						margin: 0 auto;
					}

					.restaurant {
						background-color: white;
						border-radius: 2px;
						display: flex;
						flex-direction: column;
						justify-content: space-between;
						overflow: hidden;
					}

					.restaurant img,
					.no-image {
						width: 100%;
						height: 150px;
						object-fit: cover;
						border-bottom: 1px solid #ddd;
						display: flex;
						align-items: center;
						justify-content: center;
						font-size: 0.9rem;
						color: #666;
						background-color: #f0f0f0;
					}

					.no-image {
						font-size: 0.8em;
						color: #999;
						text-align: center;
					}

					.restaurant-content {
						padding: 10px;
						text-align: center;
					}

					.restaurant h4 {
						font-size: 1em;
						margin: 10px 0;
						color: #333;
					}

					.restaurant p {
						margin: 5px 0;
						font-size: 0.9em;
						color: #666;
					}

					.details {
						display: flex;
						justify-content: space-around;
						align-items: center;
						margin-top: 10px;
					}

					.details div {
						text-align: center;
						font-size: 0.8em;
					}

					.actions {
						display: flex;
						justify-content: space-around;
						padding: 10px;
					}

					.action-btn {
						cursor: pointer;
						display: flex;
						align-items: center;
						font-size: 0.8em;
						transition: color 0.3s, transform 0.3s;
					}

					.action-btn svg {
						width: 16px;
						height: 16px;
						margin-right: 5px;
					}

					.liked svg {
						fill: red;
					}

					.favorited svg {
						fill: gold;
					}

					.pagination {
						text-align: center;
						font-size: 0.6rem;
						margin: 10px 0;
					}

					.pagination a,
					.pagination span {
						display: inline-block;
						margin: 0 5px;
						padding: 3px 6px;
						text-decoration: none;
						color: #333;
						border: 1px solid #ddd;
						border-radius: 4px;
					}

					.pagination a:hover {
						background-color: #f0f0f0;
					}

					.pagination .current-page {
						font-weight: bold;
						color: white;
						background-color: #7e8ca0;
						border-color: #818481;
					}

					#filerAndSerchBox {
						display: flex;
						flex-direction: column;
						justify-content: center;
						align-items: center;
					}
				</style>
			</head>

			<body>
				<jsp:include page="./layout/header.jsp"></jsp:include>
				<jsp:include page="./layout/nav.jsp"></jsp:include>

				<div id="container">
					<div id="filerAndSerchBox">
						<span> <select id="sortSelect" onchange="sortRestaurants()">
								<option value="rating">별점순</option>
								<option value="reviewCount" }>리뷰 수순</option>
								<option value="distance" }>거리 순</option>
							</select> <input type="text" id="searchKeyword" placeholder="검색어를 입력하세요" />
							<button>검색</button>
						</span>
					</div>
					<div id="restaurantList">
						<!-- 거리 기준으로 정렬된 음식점 리스트 -->
						<c:forEach var="restaurant" items="${restaurants}">
							<div class="restaurant">
								<!-- 이미지 -->
								<c:choose>
									<c:when test="${not empty restaurant.photoUrl}">
										<img src="${restaurant.photoUrl}" alt="${restaurant.name} 사진" />
									</c:when>
									<c:otherwise>
										<div class="no-image">이미지가 존재하지 않습니다</div>
									</c:otherwise>
								</c:choose>

								<!-- 텍스트 정보 -->
								<div class="restaurant-content">
									<h4>${restaurant.name}</h4>
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
									<div class="action-btn ${restaurant.liked ? 'liked' : ''}"
										onclick="toggleLike(this)">
										<svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24">
											<path
												d="M12 21.35l-1.45-1.32C5.4 15.36 2 12.28 2 8.5 2 5.42 4.42 3 7.5 3c1.74 0 3.41.81 4.5 2.09C13.09 3.81 14.76 3 16.5 3 19.58 3 22 5.42 22 8.5c0 3.78-3.4 6.86-8.55 11.54L12 21.35z" />
										</svg>
										<span>${restaurant.liked ? "좋아요 취소" : "좋아요"}</span>
									</div>

									<!-- 즐겨찾기 -->
									<div class="action-btn ${restaurant.favorited ? 'favorited' : ''}"
										onclick="toggleFavorite(this)">
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
						<a href="/page/${currentPage - 1}?sortBy=${param.sortBy}" class="pagination-nav">이전</a>
					</c:if>

					<!-- 페이지 번호 -->
					<c:forEach begin="${pagingBtn.startPage}" end="${pagingBtn.endPage}" var="i">
						<c:choose>
							<c:when test="${i == currentPage}">
								<span class="current-page">${i}</span>
							</c:when>
							<c:otherwise>
								<a href="/page/${i}?sortBy=${param.sortBy}" class="page-link">${i}</a>
							</c:otherwise>
						</c:choose>
					</c:forEach>

					<!-- 다음 버튼 -->
					<c:if test="${pagingBtn.nextBtn}">
						<a href="/page/${currentPage + 1}?sortBy=${param.sortBy}" class="pagination-nav">다음</a>
					</c:if>
				</div>

				<script>
					// 좋아요 토글
					function toggleLike(button) {
						button.classList.toggle('liked');
					}

					// 즐겨찾기 토글
					function toggleFavorite(button) {
						button.classList.toggle('favorited');
					}
				</script>


				<jsp:include page="./layout/footer.jsp"></jsp:include>
			</body>

			</html>