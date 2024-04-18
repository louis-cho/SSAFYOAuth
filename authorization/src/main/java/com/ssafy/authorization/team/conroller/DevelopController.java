package com.ssafy.authorization.team.conroller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ssafy.authorization.team.model.domain.SignUpDevelopDto;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
@RequestMapping("/api/team")
public class DevelopController {


	@GetMapping("/team")
	public String team(){

		return "team";
	}


	@GetMapping("/change/{userId}")
	public String developChange(@Valid @PathVariable Long userId, BindingResult bindingResult, Model model) {
		log.info("userId ttttt= {}", userId);
		if (bindingResult.hasErrors()) {
			model.addAttribute("errMessage", bindingResult.getAllErrors());
			return "develop_sign_up";
		} else {
			return "develop_main";
		}
	}

	@GetMapping("/sign-up")
	public String developPost(@Valid @ModelAttribute SignUpDevelopDto signUpDevelopDto, BindingResult bindingResult, Model model) {
		log.info("signUpDevelopDto = {}", signUpDevelopDto);
		if (bindingResult.hasErrors()) {
			model.addAttribute("errMessage", bindingResult.getAllErrors());
			return "develop_sign_up";
		} else {
			return "develop_main";
		}
	}

}
