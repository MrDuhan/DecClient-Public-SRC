package me.decclient.api.managers.impl;

/**
 * {@link java.io.Closeable} without Exceptions.
 */
public interface ICloseable
{
    /**
     * Closes this without throwing an exception.
     */
    void close();

    boolean isOpen();

}