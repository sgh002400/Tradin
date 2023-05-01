package com.traders.traders.common.filter;

import static com.traders.traders.common.exception.ExceptionMessage.*;

import java.io.IOException;
import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.traders.traders.common.exception.ExceptionResponse;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.ConsumptionProbe;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class RateLimitFilter implements Filter {

	private static final Map<String, Bucket> bucketsByIp = new ConcurrentHashMap<>();

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws
		IOException, ServletException {

		HttpServletRequest httpServletRequest = (HttpServletRequest)request;
		HttpServletResponse httpServletResponse = (HttpServletResponse)response;

		String clientIp = getClientIP(httpServletRequest);
		Bucket requestBucket = bucketsByIp.computeIfAbsent(clientIp, k -> createNewBucketForIp());

		ConsumptionProbe probeForIpBucket = requestBucket.tryConsumeAndReturnRemaining(1);
		if (!probeForIpBucket.isConsumed()) {
			log.warn("IP당 최대 요청 횟수를 초과하였습니다.: {}", clientIp);
			handleException(httpServletResponse);
			return;
		}

		chain.doFilter(request, response);
	}

	private static Bucket createNewBucketForIp() {
		//TODO - 테스트 코드 작성하기
		Bandwidth limit = Bandwidth.simple(3, Duration.ofSeconds(1)); // 1초에 3개
		return Bucket.builder().addLimit(limit).build();
	}

	private String getClientIP(HttpServletRequest request) {
		return request.getRemoteAddr();
	}

	private void handleException(HttpServletResponse response) throws IOException {
		response.setStatus(IP_RATE_LIMIT_EXCEEDED_EXCEPTION.getHttpStatus().value());
		response.setContentType("application/json; charset=utf-8");

		ExceptionResponse exceptionResponse = ExceptionResponse.builder()
			.httpStatus(IP_RATE_LIMIT_EXCEEDED_EXCEPTION.getHttpStatus())
			.message(IP_RATE_LIMIT_EXCEEDED_EXCEPTION.getMessage())
			.build();

		response.getWriter().write(new ObjectMapper().writeValueAsString(exceptionResponse));
	}
}
