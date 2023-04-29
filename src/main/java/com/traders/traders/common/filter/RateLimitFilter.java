package com.traders.traders.common.filter;

import static com.traders.traders.common.exception.ExceptionMessage.*;

import java.io.IOException;
import java.time.Duration;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.traders.traders.common.exception.ExceptionMessage;
import com.traders.traders.common.exception.ExceptionResponse;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Bucket4j;
import io.github.bucket4j.ConsumptionProbe;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class RateLimitFilter implements Filter {

	private static final ConcurrentHashMap<String, Bucket> bucketsByIp = new ConcurrentHashMap<>();
	private static final Bucket totalLimitBucket = createNewBucketForTotal();

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
			handleException(httpServletResponse, IP_RATE_LIMIT_EXCEEDED_EXCEPTION);
			return;
		}

		ConsumptionProbe probeForTotalBucket = totalLimitBucket.tryConsumeAndReturnRemaining(1);
		if (!probeForTotalBucket.isConsumed()) {
			log.warn("총 최대 요청 횟수를 초과하였습니다.");
			handleException(httpServletResponse, TOTAL_RATE_LIMIT_EXCEEDED_EXCEPTION);
			return;
		}

		chain.doFilter(request, response);
	}

	private static Bucket createNewBucketForIp() {
		//TODO - 부하테스트 후 적절한 값으로 수정 / 테스트 코드하기
		Bandwidth limit = Bandwidth.simple(3, Duration.ofSeconds(1)); // 1초에 3개
		return Bucket4j.builder().addLimit(limit).build();
	}

	private static Bucket createNewBucketForTotal() {
		Bandwidth limit = Bandwidth.simple(1000, Duration.ofSeconds(1)); // 1초에 1000개
		return Bucket4j.builder().addLimit(limit).build();
	}

	private String getClientIP(HttpServletRequest request) {
		return request.getRemoteAddr();
	}

	private void handleException(HttpServletResponse response, ExceptionMessage exceptionMessage) throws IOException {
		response.setStatus(exceptionMessage.getHttpStatus().value());
		response.setContentType("application/json; charset=utf-8");

		ExceptionResponse exceptionResponse = ExceptionResponse.builder()
			.httpStatus(exceptionMessage.getHttpStatus())
			.message(exceptionMessage.getMessage())
			.build();

		response.getWriter().write(new ObjectMapper().writeValueAsString(exceptionResponse));
	}
}
