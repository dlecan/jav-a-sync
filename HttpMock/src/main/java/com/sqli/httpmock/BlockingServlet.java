package com.sqli.httpmock;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.concurrent.TimeUnit;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(asyncSupported = true, urlPatterns = { "/block" })
public class BlockingServlet extends HttpServlet {

	@Override
	protected void service(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		int duration = Integer.parseInt(request.getParameter("howLong"));

		try {
			TimeUnit.SECONDS.sleep(duration);
		} catch (InterruptedException e) {
			throw new ServletException(e.getMessage(), e);
		}

		response.setStatus(HttpServletResponse.SC_OK);

		String message = "<html><body><h1>";
		message += "This request has been blocked for " + duration + " s";
		message += "</h1></body></html>";

		copy(new ByteArrayInputStream(message.getBytes("UTF-8")),
				response.getOutputStream());
	}

	private static long copy(InputStream source, OutputStream sink)
			throws IOException {
		long nread = 0L;
		byte[] buf = new byte[1024];
		int n;
		while ((n = source.read(buf)) > 0) {
			sink.write(buf, 0, n);
			nread += n;
		}
		return nread;
	}

}
