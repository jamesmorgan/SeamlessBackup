package com.morgan.design.seamlessbackup.service;

import java.util.Date;

public class DateTimeServiceImpl implements DateTimeService {

	@Override
	public Date getDate() {
		return new Date();
	}

}
