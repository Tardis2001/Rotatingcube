package org.matheus;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class MainTest {

    @Test
    public void mainContextLoadsAndUnloads() {
        try (Main main = new Main()) {
            main.init();
        }
    }

    @Test
    public void intentionallyFailingTest() {
        fail("TDD Dictates you must have a failing test before you write any new code");
    }
}