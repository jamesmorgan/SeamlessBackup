package com.morgan.design.seamlessbackup.service;

import java.util.Date;

public class SimpleDateTimeService implements DateTimeService {

	@Override
	public Date getDate() {
		return new Date();
	}

}
