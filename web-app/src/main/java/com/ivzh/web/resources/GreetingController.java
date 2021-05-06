package com.ivzh.web.resources;

import com.ivzh.web.dtos.User;
import com.ivzh.web.helpers.HeaderCalculationHelper;
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
	@Autowired
	HeaderCalculationHelper headerCalculationHelper;


	@GetMapping("/")
	public String greeting(Model model) {
		model.addAttribute("user", new User());
		return "test";
	}

	@PostMapping("/")
	public String post(@ModelAttribute User user, Model model, @RequestHeader("User-Agent") String userAgent) {
		messageQueueSender.queueDelivery(user);
		headerCalculationHelper.addHeader4Delivery(userAgent);
		model.addAttribute("user", new User());
		return "test";
	}

}
