package com.ivzh.batchprocessing.processors;

import com.ivzh.shared.dtos.User;
import io.micrometer.core.instrument.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.batch.item.ItemProcessor;

public class PersonItemProcessor implements ItemProcessor<String, User> {

	private static final User EMPTY = new User("", "");

	private static final Logger log = LoggerFactory.getLogger(PersonItemProcessor.class);

	@Override
	public User process(final String person) {
		if (StringUtils.isNotBlank(person)) {
			String[] split = person.split(",");
			User result = new User(split[0], split[1]);
			log.info(String.format("Converted user: %s", result));
			return result;
		} else {
			return EMPTY;
		}
	}

}
