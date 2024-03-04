package ru.vk.itmo.test.dht.dao;

/** Binary search in SSTable result information.
 */
public record FindResult(boolean found, long index) { }
