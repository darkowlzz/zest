/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package org.mozilla.zest.core.v1;

import java.util.regex.Pattern;

import org.openqa.selenium.WebDriver;

/**
 * A class which allows you to get a session associated with a popup window
 * @author simon
 *
 */
public class ZestClientWindowHandle extends ZestClient {

	private String windowHandle = null;
	private String url = null;
	private boolean regex = false;
	
	public ZestClientWindowHandle() {
		super();
	}

	public ZestClientWindowHandle(String windowHandle, String url, boolean regex) {
		super();
		this.windowHandle = windowHandle;
		this.url = url;
		this.regex = regex;
	}

	public String getWindowHandle() {
		return windowHandle;
	}

	public void setWindowHandle(String windowHandle) {
		this.windowHandle = windowHandle;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public boolean isRegex() {
		return regex;
	}

	public void setRegex(boolean regex) {
		this.regex = regex;
	}

	@Override
	public ZestStatement deepCopy() {
		return new ZestClientWindowHandle(this.getWindowHandle(), this.getUrl(), this.isRegex());
	}

	@Override
	public boolean isPassive() {
		return false;
	}

	public String invoke(ZestRuntime runtime) throws ZestClientFailException {
		WebDriver window;
		Pattern p = null;
		if (this.isRegex()) {
			p = Pattern.compile(this.url);
		}
		boolean found = false;
		for (WebDriver wd : runtime.getWebDrivers()) {
			for (String wh : wd.getWindowHandles()) {
				window = wd.switchTo().window(wh);
				if (this.isRegex()) {
					if (p.matcher(window.getCurrentUrl()).matches()) {
						runtime.addWebDriver(this.windowHandle, wd);
						runtime.debug("Matched window " + window.getWindowHandle() + " url: " + window.getCurrentUrl());
						found = true;
						break;
					} else {
						runtime.debug("Didnt match window " + window.getWindowHandle() + " url: " + window.getCurrentUrl());
					}
				} else {
					if (window.getCurrentUrl().equals(url)) {
						runtime.addWebDriver(this.windowHandle, wd);
						runtime.debug("Matched window " + window.getWindowHandle() + " url: " + window.getCurrentUrl());
						found = true;
						break;
					} else {
						runtime.debug("Didnt match window " + window.getWindowHandle() + " url: " + window.getCurrentUrl());
					}
				}
			}
		}
		if (!found) {
			throw new ZestClientFailException(this, "Failed to find window " + this.getUrl() + " regex=" + this.isRegex());
		}
		
		return this.windowHandle;
	}

}
