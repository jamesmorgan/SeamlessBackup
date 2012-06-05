package com.morgan.design.seamlessbackup.domain;

import java.util.Date;

public class DateTimeServiceImpl implements DateTimeService {

	@Override
	public Date getDate() {
		return new Date();
	}

}
