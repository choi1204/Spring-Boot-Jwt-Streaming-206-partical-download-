package com.test.genesis.exception;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;

@Getter
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum ErrorCode {

    //common
    INVALID_INPUT_VALUE(400, "COM-001", "잘못된 값을 요청했습니다."),
    INVALID_TYPE_VALUE(400, "COM_002", "잘못된 타입의 값을 요청했습니다."),
    INTERNAL_SERVER_ERROR(500, "COM-003", "서버에서 알 수 없는 문제가 발생했습니다."),
    METHOD_NOT_ALLOWED(405, "COM-004", "Request Method가 일치하지 않습니다."),
    MEDIA_TYPE_NOT_SUPPORTED(415,"COM-005","지원하지 않는 미디어 타입입니다."),
    NOT_READABLE_MESSAGE(400,"COM-006", "값을 읽을 수 없습니다. 올바른 형태인지 확인해주세요."),
    MISSING_REQUEST_PART(400,"COM-007", "필요한 파일의 키 값이 존재하지 않습니다. API DOCS를 다시 확인해주세요."),
    MISSING_REQUEST_PARAMETER(400,"COM-008","필요한 파라미터 키 값이 존재하지 않습니다. API DOCS를 다시 확인해주세요."),
    REQUEST_REJECT(400,"COM-009", "URL에 올바르지 않은 문자가 포함되어있습니다. 다시 확인해주세요. "),

    FILE_NOT_FOUND(400,"FIL-001","파일이 존재하지 않습니다."),
    FILE_PROCESS_ERROR(500, "FIL-002","파일 처리 과정에서 문제가 발생했습니다."),
    FILE_MAX_SIZE_EXCEEDED(400,"FIL-006","파일의 최대 크기를 초과하였습니다.");


    private final int status;
    private final String codeName;
    private final String message;    ErrorCode(int status, String codeName, String message) {
        this.status = status;
        this.codeName = codeName;
        this.message = message;
    }
}
