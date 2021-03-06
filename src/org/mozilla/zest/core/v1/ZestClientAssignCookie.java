/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package org.mozilla.zest.core.v1;

import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebDriver;

/**
 * Click on the specified client element.
 * @author simon
 *
 */
public class ZestClientAssignCookie extends ZestClient {

	private String windowHandle;
	private String variableName;
	private String cookieName;

	public ZestClientAssignCookie(String windowHandle, String variableName, String cookieName) {
		super();
		this.windowHandle = windowHandle;
		this.variableName = variableName;
		this.cookieName = cookieName;
	}
	
	public ZestClientAssignCookie() {
		super();
	}

	public String getWindowHandle() {
		return windowHandle;
	}

	public void setWindowHandle(String windowHandle) {
		this.windowHandle = windowHandle;
	}

	public String getVariableName() {
		return variableName;
	}

	public void setVariableName(String variableName) {
		this.variableName = variableName;
	}

	public String getCookieName() {
		return cookieName;
	}

	public void setCookieName(String cookieName) {
		this.cookieName = cookieName;
	}

	@Override
	public String invoke(ZestRuntime runtime) throws ZestClientFailException {
		WebDriver wd = runtime.getWebDriver(this.getWindowHandle());
		String val = "";
		
		Cookie cookie = wd.manage().getCookieNamed(cookieName);
		if (cookie != null) {
			val = cookie.getValue();
			runtime.setVariable(this.variableName, val);
		}

		return val;
	}

	@Override
	public ZestStatement deepCopy() {
		return new ZestClientAssignCookie(windowHandle, variableName, cookieName);
	}

	@Override
	public boolean isPassive() {
		return false;
	}

}
