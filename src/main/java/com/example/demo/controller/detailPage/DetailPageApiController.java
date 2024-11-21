package com.example.demo.controller.detailPage;

import com.example.demo.dao.detailPage.IDetailPageActDao;
import com.example.demo.dto.Act.ActDto;
import com.example.demo.dto.Act.TotalActDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/detailpage")
public class DetailPageApiController {

    private final IDetailPageActDao detailPageActDao;

    @Autowired
    public DetailPageApiController(IDetailPageActDao detailPageActDao) {
        this.detailPageActDao = detailPageActDao;
    }

    @GetMapping("/likeAllSelect")
    public int likeAllSelect(ActDto act) {
        return detailPageActDao.likeAllSelect(act);
    }

    @GetMapping("/selectAct")
    public ActDto selectAct(ActDto act) {
        return detailPageActDao.selectAct(act);
    }

    @GetMapping("/selectTotalAct")
    public TotalActDto selectTotalAct(ActDto act) {
        return detailPageActDao.selectTotalAct(act);
    }

    @PostMapping("/insertAct")
    public int insertAct(@RequestBody ActDto act) {
        return detailPageActDao.insertAct(act);
    }

    @DeleteMapping("/deleteAct")
    public int deleteAct(ActDto act) {
        return detailPageActDao.deleteAct(act);
    }
}
