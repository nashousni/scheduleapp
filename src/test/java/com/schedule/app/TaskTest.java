package com.schedule.app;

import static org.junit.Assert.*;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThatCode;

import java.util.UUID;


public class TaskTest {

    @Test
    public void test() {
        Task task = new Task(UUID.randomUUID().toString(), "anniv", "", 12435);

        assertEquals("anniv", task.getName());
        assertTrue(task.getDescription().isEmpty());
        assertEquals(12435, task.getEventDate());

        task.setName("anniversary");
        assertEquals("anniversary", task.getName());

        task.setDescription(" hotel reservation ");
        assertEquals(" hotel reservation ", task.getDescription());

        task.setEventDate(124356);
        assertEquals(124356, task.getEventDate());

        Task task2 = new Task(UUID.randomUUID().toString(), "anniversary", "", 124356);
        assertTrue(task.equals(task2));

    }
}
