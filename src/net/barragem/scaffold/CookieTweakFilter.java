package net.barragem.scaffold;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Baseado em: http://forums.sun.com/thread.jspa?threadID=197150&start=15
 * */
public class CookieTweakFilter implements Filter {

	@Override
	public void init(FilterConfig arg0) throws ServletException {
	}

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException,
			ServletException {
		MyRequestWrapper myrequest = new MyRequestWrapper((HttpServletRequest) request);
		myrequest.setResponse((HttpServletResponse) response);
		chain.doFilter(myrequest, response);
	}

	@Override
	public void destroy() {
	}

	class MyRequestWrapper extends HttpServletRequestWrapper {
		private HttpServletResponse response = null;

		public MyRequestWrapper(HttpServletRequest request) {
			super(request);
		}

		public void setResponse(HttpServletResponse response) {
			this.response = response;
		}

		public HttpSession getSession() {
			HttpSession session = super.getSession();
			processSessionCookie(session);
			return session;
		}

		public HttpSession getSession(boolean create) {
			HttpSession session = super.getSession(create);
			processSessionCookie(session);
			return session;
		}

		private void processSessionCookie(HttpSession session) {
			if (null == response || null == session) {
				// No response or session object attached, skip the pre processing
				return;
			}
			// cookieOverWritten - Flag to filter multiple "Set-Cookie" headers
			Object cookieOverWritten = getAttribute("COOKIE_OVERWRITTEN_FLAG");
			if (null == cookieOverWritten && session.isNew()) {
				// Might have created the cookie in SSL protocol and tomcat will loose the session
				// if there is change in protocol from HTTPS to HTTP. To avoid this, trick the browser
				// using the HTTP and HTTPS session cookie.
				Cookie cookie = new Cookie("JSESSIONID", session.getId());
				cookie.setMaxAge(-1); // Life of the browser or timeout
				cookie.setSecure(false);
				if ((getContextPath() != null) && (getContextPath().length() > 0)) {
					cookie.setPath(getContextPath());
				} else {
					cookie.setPath("/");
				}
				response.addCookie(cookie); // Adding an "Set-Cookie" header to the response
				setAttribute("COOKIE_OVERWRITTEN_FLAG", "true");// To avoid multiple "Set-Cookie" header
			}
		}
	}
}