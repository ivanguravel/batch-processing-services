package com.ivzh.batchprocessing.processors;

import com.ivzh.batchprocessing.dtos.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.batch.item.ItemProcessor;

public class PersonItemProcessor implements ItemProcessor<byte[], User> {

	private static final Logger log = LoggerFactory.getLogger(PersonItemProcessor.class);

	@Override
	public User process(final byte[] person) throws Exception {
		String[] split = new String(person).split(",");
		User result = new User(split[0], split[1]);
		log.info(String.format("Converted user: %s", result));
		return result;
	}

}
