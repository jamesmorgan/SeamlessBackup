package com.morgan.design.seamlessbackup.naming;

import java.util.Date;

public interface FileNameParser {

	Date getDate(String fileName);

	String getMD5(String fileName);

}
