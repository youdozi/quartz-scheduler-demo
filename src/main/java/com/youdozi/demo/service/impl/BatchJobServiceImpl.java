package com.youdozi.demo.service.impl;

import org.springframework.stereotype.Service;

import com.youdozi.demo.service.BatchJobService;

@Service
public class BatchJobServiceImpl implements BatchJobService{

	@Override
	public void jobAction(String command) throws Exception {

		switch(command) {

			case "testJob" :
				System.out.println("batch start.." + System.currentTimeMillis());

				break;

		}
	}
}
