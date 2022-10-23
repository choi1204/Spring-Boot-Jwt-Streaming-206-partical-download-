package com.test.genesis.exception;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.validation.BindingResult;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * [Exception Strategy] 아래의 소스에서 참조
 * 출처: https://github.com/cheese10yun/spring-guide
 */
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ErrorResponse {

    private String message;
    private String codeName;
    private int status;
    private List<FieldError> errors;

    // 일반적으로 @Valid 어노테이션으로 JSR 303의 검증에 대한 예외 Response
    private ErrorResponse(final ErrorCode code, final List<FieldError> errors) {
        this.message = code.getMessage();
        this.status = code.getStatus();
        this.errors = errors;
        this.codeName = code.getCodeName();
    }

    // 이외의 예외 Response
    private ErrorResponse(final ErrorCode code) {
        this.message = code.getMessage();
        this.status = code.getStatus();
        this.codeName = code.getCodeName();
        this.errors = new ArrayList<>();
    }

    // 현재까지는 BindException, MethodArgumentNotValidException으로 파악
    // 이에 반드시 한정되는 것이 아니니 exception의 method를 ExceptionHandler에서 체크해보고 적절한 of를 호출해야함
    public static ErrorResponse of(final ErrorCode code, final BindingResult bindingResult) {
        return new ErrorResponse(code, FieldError.of(bindingResult));
    }

    // General BusinessException
    public static ErrorResponse of(final ErrorCode code) {
        return new ErrorResponse(code);
    }

//    public static ErrorResponse of(final ErrorCode code, final List<FieldError> errors) {
//        return new ErrorResponse(code, errors);
//    }

    public static ErrorResponse of(MethodArgumentTypeMismatchException e) {
        final String value = e.getValue() == null ? "" : e.getValue().toString();
        final List<ErrorResponse.FieldError> errors = ErrorResponse.FieldError.of(e.getName(), value, e.getErrorCode());
        return new ErrorResponse(ErrorCode.INVALID_TYPE_VALUE, errors);
    }

    /**
     * Validation할 때, RequestDTO에 붙은 애노테이션 필드에서 발생한 에러와 관련한 정보를 저장
     * 여러 필드에서 오류가 날 수 있으니 항상 List로 반환한다. 없으면 빈 배열을 반환하고, null을 보내지 않는다.
     * ex) @Size, @Max, @NotBlank etc..
     * field - getFieldId: validation이 실패한 필드명
     * value - getRejectedValue(): validation이 실패한 필드의 (request)값
     * reason - getDefaultMessage: NotBlank,Size등 DtoRequest의 검증 애노테이션에 설정한 기본 메시지( ex) @Size(message="") )
     *
     **/
    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class FieldError {
        private String field;
        private String value;
        private String reason;

        private FieldError(String field, String value, String reason) {
            this.field = field;
            this.value = value;
            this.reason = reason;
        }

        // MethodArgumentTypeMismatchException처럼 특수한 경우
        public static List<FieldError> of(String field, String value, String reason) {
            List<FieldError> fieldErrors = new ArrayList<>();
            fieldErrors.add(new FieldError(field,value,reason));
            return fieldErrors;
        }

        public static List<FieldError> of(BindingResult bindingResult){
            List<org.springframework.validation.FieldError> fieldErrors = bindingResult.getFieldErrors();
            return fieldErrors.stream().map(fieldError ->
                    new FieldError(
                            fieldError.getField(),
                            fieldError.getRejectedValue()==null?"":fieldError.getRejectedValue().toString(),
                            fieldError.getDefaultMessage()
                    )
            ).collect(Collectors.toList());
        }

    }
}
