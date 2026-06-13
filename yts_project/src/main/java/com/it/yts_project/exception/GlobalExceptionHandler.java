package com.it.yts_project.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.HashMap;
import java.util.Map;

/**
 * 全局异常处理器
 * 统一处理所有Controller抛出的异常，提供友好的错误响应
 * 
 * 注意：此处理器作为兜底机制，Controller中的try-catch仍然保留作为第一层处理
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /**
     * 处理参数验证异常（IllegalArgumentException）
     * 通常用于业务参数验证失败
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, Object>> handleIllegalArgument(IllegalArgumentException e) {
        log.warn("参数验证失败: {}", e.getMessage());
        Map<String, Object> response = new HashMap<>();
        response.put("success", false);
        response.put("message", e.getMessage());
        return ResponseEntity.badRequest().body(response);
    }

    /**
     * 处理数据完整性违反异常（唯一约束、外键约束等）
     */
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Map<String, Object>> handleDataIntegrityViolation(DataIntegrityViolationException e) {
        log.error("数据完整性违反", e);
        Map<String, Object> response = new HashMap<>();
        response.put("success", false);
        
        String message = e.getMessage();
        if (message != null) {
            if (message.contains("unique") || message.contains("UNIQUE") || message.contains("duplicate")) {
                response.put("message", "数据已存在，不能重复添加");
            } else if (message.contains("foreign key") || message.contains("FOREIGN KEY")) {
                response.put("message", "存在关联数据，无法删除");
            } else if (message.contains("not-null") || message.contains("NOT NULL")) {
                response.put("message", "必填字段不能为空");
            } else {
                response.put("message", "数据验证失败，请检查输入数据");
            }
        } else {
            response.put("message", "数据验证失败，请检查输入数据");
        }
        
        return ResponseEntity.badRequest().body(response);
    }

    /**
     * 处理Bean Validation验证异常
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleMethodArgumentNotValid(MethodArgumentNotValidException e) {
        log.warn("参数验证失败: {}", e.getMessage());
        Map<String, Object> response = new HashMap<>();
        response.put("success", false);
        
        StringBuilder errorMessage = new StringBuilder("参数验证失败: ");
        e.getBindingResult().getFieldErrors().forEach(error -> {
            errorMessage.append(error.getField()).append(" ").append(error.getDefaultMessage()).append("; ");
        });
        
        response.put("message", errorMessage.toString().trim());
        return ResponseEntity.badRequest().body(response);
    }

    /**
     * 处理业务运行时异常（RuntimeException）
     * 通常用于业务逻辑错误
     */
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Map<String, Object>> handleRuntimeException(RuntimeException e) {
        log.error("业务异常", e);
        Map<String, Object> response = new HashMap<>();
        response.put("success", false);
        
        // 对于业务异常，返回异常消息（通常是用户友好的）
        String message = e.getMessage();
        if (message != null && !message.isEmpty()) {
            response.put("message", message);
        } else {
            response.put("message", "操作失败，请稍后重试");
        }
        
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }

    /**
     * 处理所有其他异常（Exception）
     * 系统异常，不暴露内部错误信息
     */
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<Map<String, Object>> handleException(Exception e) {
        log.error("系统异常", e);
        Map<String, Object> response = new HashMap<>();
        response.put("success", false);
        response.put("message", "系统错误，请联系管理员");
        
        // 开发环境可以返回详细错误信息，生产环境不返回
        // 可以通过配置控制是否返回详细错误
        if (log.isDebugEnabled()) {
            response.put("error", e.getClass().getSimpleName());
            response.put("detail", e.getMessage());
        }
        
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
}

