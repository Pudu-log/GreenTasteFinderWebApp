package com.example.demo.controller.mainPage;

import com.example.demo.model.Restaurant;
import com.example.demo.service.MainPageService;
import com.example.demo.utils.PagingBtn;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Comparator;
import java.util.List;

@Controller
public class MainPageController {

	private final MainPageService mainPageService;

	@Autowired
	public MainPageController(MainPageService mainPageService) {
		this.mainPageService = mainPageService;
	}

	@GetMapping("/")
	public String getRestaurants(@RequestParam(name = "sortBy", defaultValue = "rating") String sortBy, Model model) {
		return "mainPage";
	}
	

}
