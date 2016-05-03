package edu.cmu.jsphdev.picky.exception;

/**
 * Abstraction of all custom-exception that can have a fix() implementation for self-healing exceptions.
 */
public abstract class PickyException extends Exception {

    /**
     * Fix for each sub-type of exceptions.
     */
    public abstract void fix();
}
