/*
 * Created on 16-Aug-2004
 *
 */
package com.pluralsight.security;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author kevinj
 */
public class TestCreateStreams {

    File testFile;
    File testFileOutputNotExist;

    private static String NOT_EXIST_TEST_FILE = "notExistDestFile";

    public TestCreateStreams() throws Exception {
        testFile = new File("testfile");
        testFileOutputNotExist = new File(NOT_EXIST_TEST_FILE);
        testFile.delete();
        testFileOutputNotExist.delete();

        testFile.createNewFile();
    }

    @Test
    public void testWhenInputFileNameIsNull() throws IOException {
        SecurityBase h = new SecurityBase();

        InputStream is = h.createInputStream(null);
        assertEquals(System.in, is);
        is.close();
    }

    @Test
    public void testWhenInputFileDoesNotExist() throws IOException {
        SecurityBase h = new SecurityBase();
        File f = new File("doesnotexist");
        assertFalse(f.exists());

        try {
            InputStream is = h.createInputStream(NOT_EXIST_TEST_FILE);
        } catch (FileNotFoundException fe) {
        }
    }

    @Test
    public void testWhenInputFileDoesExist() throws IOException {
        SecurityBase h = new SecurityBase();
        assertTrue(testFile.exists());

        InputStream is = h.createInputStream(testFile.getAbsolutePath());
        assertNotSame(System.in, is);
        is.close();
    }

    @Test
    public void testWhenDestFileIsNull() throws IOException {
        SecurityBase h = new SecurityBase();
        OutputStream os = h.createOutputStream(null);
        assertEquals(System.out, os);
    }

    @Test
    public void testWhenDestFileDoesNotExistNoOverwrite() throws IOException {
        SecurityBase h = new SecurityBase();
        OutputStream os = h.createOutputStream(testFileOutputNotExist.getAbsolutePath());
        os.close();
        assertTrue(testFileOutputNotExist.exists());

    }

    @Test
    public void testWhenDestFileDoesNotExistOverwrite() throws IOException {
        SecurityBase h = new SecurityBase();
        h.setOverwrite(true);
        OutputStream os = h.createOutputStream(testFileOutputNotExist.getAbsolutePath());
        assertTrue(testFileOutputNotExist.exists());
        os.close();
    }

    @Test
    public void testWhenDestFileExistsNoOverwrite()
            throws FileNotFoundException {
        SecurityBase h = new SecurityBase();
        try {
            OutputStream os = h.createOutputStream(testFile.getAbsolutePath());
            fail();
        } catch (IOException e) {
        }
    }

    @Test
    public void testWhenDestFileExistsOverwrite() throws IOException {
        SecurityBase h = new SecurityBase();
        h.setOverwrite(true);
        OutputStream os = h.createOutputStream(testFile.getAbsolutePath());
        assertTrue(testFile.exists());
        os.close();
    }

    @AfterEach
    protected void tearDown() throws Exception {
        testFile.delete();
        testFileOutputNotExist.delete();
    }

}