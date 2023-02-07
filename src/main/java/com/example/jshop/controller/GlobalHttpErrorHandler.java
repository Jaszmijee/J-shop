package com.example.jshop.controller;

import com.example.jshop.exception.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class GlobalHttpErrorHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<Object> handleAccessDeniedException(AccessDeniedException exception) {
        return new ResponseEntity<>("Access denied", HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(CategoryExistsException.class)
    public ResponseEntity<Object> handleCategoryExistsException(CategoryExistsException exception) {
        return new ResponseEntity<>("Category already exists", HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InvalidArgumentException.class)
    public ResponseEntity<Object> handleInvalidArgumentException(InvalidArgumentException exception) {
        return new ResponseEntity<>("Provide proper name", HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(CategoryNotFoundException.class)
    public ResponseEntity<Object> handleCategoryNotFoundException(CategoryNotFoundException exception) {
        return new ResponseEntity<>("Category not found", HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ProductAlreadyExistsException.class)
    public ResponseEntity<Object> handleProductAlreadyExistsException(ProductAlreadyExistsException exception) {
        return new ResponseEntity<>("Product exists, update product instead", HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(CartNotFoundException.class)
    public ResponseEntity<Object> handleCartNotFoundException(CartNotFoundException exception) {
        return new ResponseEntity<>("Cart with given ID Not Found", HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ItemNotAvailableException.class)
    public ResponseEntity<Object> handleItemNotAvailableException(ItemNotAvailableException exception) {
        return new ResponseEntity<>("Currently item is not available", HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NotEnoughItemsException.class)
    public ResponseEntity<Object> handleNotEnoughItemsException(NotEnoughItemsException exception) {
        return new ResponseEntity<>("The quantity of selected items is currently not available", HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<Object> handleProductNotFoundException(ProductNotFoundException exception) {
        return new ResponseEntity<>("Product with given Id not found", HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(CategoryException.class)
    public ResponseEntity<Object> handleCategoryException(CategoryException exception) {
        return new ResponseEntity<>("Deleting category \"Unknown\" denied", HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InvalidQuantityException.class)
    public ResponseEntity<Object> handleInvalidQuantityException(InvalidQuantityException exception) {
        return new ResponseEntity<>("Provided quantity is out of range 0 - 2 147 483 647", HttpStatus.BAD_REQUEST);
    }
}

