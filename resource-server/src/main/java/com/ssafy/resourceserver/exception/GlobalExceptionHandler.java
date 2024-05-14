package com.ssafy.resourceserver.exception;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

	/**
	 * [Exception] Default 500
	 *
	 * @param e 잡지 못한 ERROR
	 * @return /error/{HttpStatus.CODE}.html
	 */
	@ExceptionHandler(Exception.class)
	protected String handleException(Model model, Exception e) {
		System.out.println("GlobalExceptionHandler.handleException");
		log.error(e.getMessage());
		model.addAttribute("errorCode", ErrorCode.INTERNAL_SERVER_ERROR);
		model.addAttribute("message", e.getMessage());
		model.addAttribute("description", "처리 오류.");
		return "500";
	}

	/**
	 * [Exception] 잘못된 주소로 요청 한 경우
	 *
	 * @param e NoHandlerFoundException
	 * @return /error/{HttpStatus.CODE}.html
	 */
	@ExceptionHandler(NoHandlerFoundException.class)
	protected String handleNoHandlerFoundException(Model model, NoHandlerFoundException e) {
		log.error("handleNoHandlerFoundException");
		model.addAttribute("errorCode", ErrorCode.NOT_FOUND_ERROR);
		model.addAttribute("message", e.getMessage());
		model.addAttribute("description", "없는 URI에 접근하였습니다. 요청 주소를 확인해주세요.");
		return "/error/404";
	}

	@ExceptionHandler(NoResourceFoundException.class)
	protected String handleNoResourceFoundException(Model model, NoResourceFoundException e) {
		log.error("handleNoResourceFoundException");
		model.addAttribute("errorCode", ErrorCode.NOT_FOUND_ERROR);
		model.addAttribute("message", e.getMessage());
		model.addAttribute("description", "없는 URI에 접근하였습니다. 요청 주소를 확인해주세요.");
		return "/error/404";
	}
}
