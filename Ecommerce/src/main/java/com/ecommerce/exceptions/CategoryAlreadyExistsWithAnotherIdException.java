package com.ecommerce.exceptions;

public class CategoryAlreadyExistsWithAnotherIdException extends RuntimeException
{
    public CategoryAlreadyExistsWithAnotherIdException(String message)
    {
        super(message);
    }
}
