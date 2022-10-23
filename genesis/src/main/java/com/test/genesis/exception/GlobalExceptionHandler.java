package com.test.genesis.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.web.firewall.RequestRejectedException;
import org.springframework.validation.BindException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;

import java.io.IOException;

/**
 * [Exception Strategy] 아래의 소스에서 참조
 * 출처: https://github.com/cheese10yun/spring-guide
 */

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    /**
     * javax.validation.Valid or @Validated 으로 binding error 발생시 발생한다.
     * HttpMessageConverter 에서 등록한 HttpMessageConverter binding 못할경우 발생
     * 주로 @RequestBody, @RequestPart 어노테이션에서 발생
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        final ErrorResponse response = ErrorResponse.of(ErrorCode.INVALID_INPUT_VALUE, e.getBindingResult());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    /**
     * @ModelAttribute 로 binding error 발생시 BindException 발생한다.
     * ref https://docs.spring.io/spring/docs/current/spring-framework-reference/web.html#mvc-ann-modelattrib-method-args
     */
    @ExceptionHandler(BindException.class)
    protected ResponseEntity<ErrorResponse> handleBindException(BindException e) {
        final ErrorResponse response = ErrorResponse.of(ErrorCode.INVALID_INPUT_VALUE, e.getBindingResult());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    /**
     * enum type 일치하지 않아 binding 못할 경우 발생
     * 주로 @RequestParam enum으로 binding 못했을 경우 발생
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    protected ResponseEntity<ErrorResponse> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException e) {
        final ErrorResponse response = ErrorResponse.of(e);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    /**
     * 지원하지 않은 HTTP method 호출 할 경우 발생
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    protected ResponseEntity<ErrorResponse> handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException e) {
        final ErrorResponse response = ErrorResponse.of(ErrorCode.METHOD_NOT_ALLOWED);
        return new ResponseEntity<>(response, HttpStatus.METHOD_NOT_ALLOWED);
    }

    /**
     * 지원하지 않는 MEDIATYPE을 호출한 경우 발생
     **/
    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    protected ResponseEntity<ErrorResponse> handleHttpMediaTypeNotSupportedException(HttpMediaTypeNotSupportedException e){
        final ErrorResponse response = ErrorResponse.of(ErrorCode.MEDIA_TYPE_NOT_SUPPORTED);
        return new ResponseEntity<>(response,HttpStatus.UNSUPPORTED_MEDIA_TYPE);
    }

    /**
     * Request값을 읽어올 수 없을때 발생. 아무것도 넣지않고 json에다가 전송했을때 발생한 이력이있음(body: none, formdata, json 셋 다)
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    protected ResponseEntity<ErrorResponse> handleHttpMessageNotReadableException(HttpMessageNotReadableException e){
        final ErrorResponse response = ErrorResponse.of(ErrorCode.NOT_READABLE_MESSAGE);
        return new ResponseEntity<>(response,HttpStatus.BAD_REQUEST);
    }

    /**
     * dto에서 필수로 필요한 파라미터의 키 값이 존재하지 않을 때 발생
     */
    @ExceptionHandler(MissingServletRequestParameterException.class)
    protected ResponseEntity<ErrorResponse> handleMissingServletRequestParameterException(){
        final ErrorResponse response = ErrorResponse.of(ErrorCode.MISSING_REQUEST_PARAMETER);
        return new ResponseEntity<>(response,HttpStatus.BAD_REQUEST);
    }

    /**
     * dto에서 필수로 필요한 파일의 키 값이 존재하지 않을 때 발생
     */
    @ExceptionHandler(MissingServletRequestPartException.class)
    protected ResponseEntity<ErrorResponse> handleMissingServletRequestPartException(){
        final ErrorResponse response = ErrorResponse.of(ErrorCode.MISSING_REQUEST_PART);
        return new ResponseEntity<>(response,HttpStatus.BAD_REQUEST);
    }

    /**
     * URL에 허용되지 않은 문자열이 포함된 경우 발생, 예를 들어 "//" 슬러쉬 두개가 들어간 경우가 있었다.
     */
    @ExceptionHandler(RequestRejectedException.class)
    protected ResponseEntity<ErrorResponse> handleRequestRejectedException(){
        final ErrorResponse response = ErrorResponse.of(ErrorCode.REQUEST_REJECT);
        return new ResponseEntity<>(response,HttpStatus.BAD_REQUEST);
    }

    /**
     * BusinessException과 이를 상속받은 모든 Exception 처리
     */
    @ExceptionHandler(BusinessException.class)
    protected ResponseEntity<ErrorResponse> handleBusinessException(final BusinessException e) {
        final ErrorCode errorCode = e.getErrorCode();
        final ErrorResponse response = ErrorResponse.of(errorCode);
        return new ResponseEntity<>(response, HttpStatus.valueOf(errorCode.getStatus()));
    }

    /** 파일 처리 과정 Exception **/
    @ExceptionHandler(IOException.class)
    protected ResponseEntity<ErrorResponse> handleIOException(IOException e){
        log.error("handleIOException", e);
        final ErrorResponse response = ErrorResponse.of(ErrorCode.FILE_PROCESS_ERROR);
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * 위에 언급한 Exception을 제외한 별도 처리가 필요한 모든 Exception은 제보를 통해 추가
     */
    @ExceptionHandler(Exception.class)
    protected ResponseEntity<ErrorResponse> handleException(Exception e) {
        log.error("handleException", e);
        final ErrorResponse response = ErrorResponse.of(ErrorCode.INTERNAL_SERVER_ERROR);
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    protected  ResponseEntity<ErrorResponse> handleMaxUploadSizeExceededException(MaxUploadSizeExceededException e){
        final ErrorResponse response = ErrorResponse.of(ErrorCode.FILE_MAX_SIZE_EXCEEDED);
        return new ResponseEntity<>(response,HttpStatus.BAD_REQUEST);
    }

}
