package com.example.demo.controller.detailPage;

import com.example.demo.dao.detailPage.IDetailPageActDao;
import com.example.demo.dto.ActDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/detailpage")
public class DetailPageApiController {

    @Autowired
    IDetailPageActDao detailPageActDao;

    @GetMapping("/likeAllSelect")
    public int likeAllSelect(ActDto act) {
        return detailPageActDao.likeAllSelect(act);
    }

    @GetMapping("/selectAct")
    public ActDto selectAct(ActDto act) {
        return detailPageActDao.selectAct(act);
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
