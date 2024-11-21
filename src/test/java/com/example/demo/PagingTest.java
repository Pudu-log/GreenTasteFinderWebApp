package com.example.demo;

import com.example.demo.utils.PagingBtn;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
public class PagingTest {

    @Test
    public void 에취() {
        PagingBtn pgBtn = new PagingBtn(99, 2);
        assertTrue(pgBtn.getTotalPage() == 10);
        assertTrue(pgBtn.getStartPage() == 1);
        assertTrue(pgBtn.getEndPage() == 10);
    }

    @Test
    public void 에취2() {
        PagingBtn pgBtn = new PagingBtn(255, 23);
        assertTrue(pgBtn.getTotalPage() == 26);
        assertTrue(pgBtn.getStartPage() == 21);
        assertTrue(pgBtn.getEndPage() == 26);
        assertTrue(!pgBtn.isNextBtn());
    }


    @Test
    public void 에취3() {
        PagingBtn pgBtn = new PagingBtn(255, 17);
        assertTrue(pgBtn.getTotalPage() == 26);
        assertTrue(pgBtn.getStartPage() == 11);
        assertTrue(pgBtn.getEndPage() == 20);
        assertTrue(pgBtn.isNextBtn());
        assertTrue(pgBtn.isPrevBtn());
    }

    @Test
    public void 에취4() {
        PagingBtn pgBtn = new PagingBtn(110, 1);
        assertTrue(pgBtn.getTotalPage() == 26);
        assertTrue(pgBtn.getStartPage() == 1);
        assertTrue(pgBtn.getEndPage() == 10);
        assertTrue(pgBtn.isNextBtn());
        assertTrue(!pgBtn.isPrevBtn());
    }

    @Test
    public void 에취5() {
        PagingBtn pgBtn = new PagingBtn(110, 10);
        assertTrue(pgBtn.getTotalPage() == 11);
        assertTrue(pgBtn.getStartPage() == 1);
        assertTrue(pgBtn.getEndPage() == 10);
        assertTrue(pgBtn.isNextBtn());
        assertTrue(!pgBtn.isPrevBtn());
    }


}