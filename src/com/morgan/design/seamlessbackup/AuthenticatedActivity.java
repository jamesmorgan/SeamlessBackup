package com.morgan.design.seamlessbackup;

public interface AuthenticatedActivity {

	void onAuthenticationSuccessful();

	void onAuthenticationFailed(IllegalStateException e);
}
