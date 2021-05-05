package com.ivzh.web.resources;

import com.ivzh.web.dtos.User;
import com.ivzh.web.queues.MessageQueueSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class GreetingController {

	@Autowired
	MessageQueueSender messageQueueSender;


	@GetMapping("/")
	public String greeting(Model model) {
		model.addAttribute("user", new User());
		return "test";
	}

	@PostMapping("/")
	public String create(@ModelAttribute User user, Model model, @RequestHeader("User-Agent") String userAgent) {
		messageQueueSender.queueDelivery(user);
		model.addAttribute("user", new User());
		return "test";
	}

}
