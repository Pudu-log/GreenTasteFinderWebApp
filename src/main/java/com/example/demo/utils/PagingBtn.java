package com.example.demo.utils;

import lombok.Data;

/**
 * 페이징 버튼 생성 유틸리티 클래스
 */
@Data
public class PagingBtn {

    private int totalCount;   // 총 데이터 개수
    private int totalPage;    // 총 페이지 수
    private int currentPage;  // 현재 페이지 번호
    private int startPage;    // 시작 페이지 번호
    private int endPage;      // 끝 페이지 번호
    private int pageSize = 10; // 한 페이지에 보여줄 데이터 개수 (기본값: 10)
    private int btnSize = 10;  // 페이지 버튼 최대 개수 (기본값: 10)
    private boolean prevBtn;  // 이전 버튼 활성화 여부
    private boolean nextBtn;  // 다음 버튼 활성화 여부
    private boolean firstPageBtn; // 맨처음 버튼 활성화 여부
    private boolean lastPageBtn;  // 맨끝 버튼 활성화 여부

    /**
     * 생성자
     *
     * @param totalCount  총 데이터 개수
     * @param currentPage 현재 페이지 번호
     */
    public PagingBtn(int totalCount, int currentPage) {
        this.totalCount = totalCount;
        this.currentPage = currentPage;
        createPagingBtn(totalCount);
    }

    /**
     * 페이징 버튼 계산
     *
     * @param totalCount 총 데이터 개수
     */
    public void createPagingBtn(int totalCount) {
        this.totalPage = (int) Math.ceil(totalCount / (double) pageSize); // 총 페이지 수 계산
        this.startPage = ((currentPage - 1) / btnSize) * btnSize + 1;    // 시작 페이지 번호 계산
        this.endPage = Math.min(startPage + btnSize - 1, totalPage);     // 끝 페이지 번호 계산
        this.prevBtn = startPage != 1;                                  // 이전 버튼 활성화 여부
        this.nextBtn = endPage != totalPage;                            // 다음 버튼 활성화 여부
        this.firstPageBtn = startPage > 10;                             // 맨처음 버튼 활성화 여부 (11페이지 이상일 때 활성화)
        this.lastPageBtn = endPage < totalPage; // '끝' 버튼 조건: 현재 페이지가 endPage 범위 안에 없을 때 활성화
    }
}
