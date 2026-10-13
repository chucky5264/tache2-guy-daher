package org.apache.tika.parser.ocr;

import java.lang.reflect.Method;
import java.util.HashMap;
import org.apache.tika.parser.ocr.TesseractOCRConfig;
import org.mockito.*;
import org.junit.jupiter.api.*;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import java.io.File;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.tika.exception.TikaConfigException;
import org.apache.tika.utils.StringUtils;

public class TesseractOCRConfig_getColorspace_25_0_Test {

    private TesseractOCRConfig config;

    @BeforeEach
    public void setUp() {
        config = new TesseractOCRConfig();
    }

    @Test
    public void testGetColorspace() throws Exception {
        // Arrange
        String expectedColorspace = "gray";
        // Act
        Method getColorspaceMethod = TesseractOCRConfig.class.getDeclaredMethod("getColorspace");
        getColorspaceMethod.setAccessible(true);
        String actualColorspace = (String) getColorspaceMethod.invoke(config);
        // Assert
        assertEquals(expectedColorspace, actualColorspace);
    }

    @Test
    public void testGetColorspaceWithSet() throws Exception {
        // Arrange
        String expectedColorspace = "rgb";
        // Act
        Method setColorspaceMethod = TesseractOCRConfig.class.getDeclaredMethod("setColorspace", String.class);
        setColorspaceMethod.setAccessible(true);
        setColorspaceMethod.invoke(config, expectedColorspace);
        Method getColorspaceMethod2 = TesseractOCRConfig.class.getDeclaredMethod("getColorspace");
        getColorspaceMethod2.setAccessible(true);
        String actualColorspace = (String) getColorspaceMethod2.invoke(config);
        // Assert
        assertEquals(expectedColorspace, actualColorspace);
    }

    @Test
    public void testGetColorspaceWithNull() throws Exception {
        // Arrange
        String expectedColorspace = "gray";
        // Act
        Method getColorspaceMethod = TesseractOCRConfig.class.getDeclaredMethod("getColorspace");
        getColorspaceMethod.setAccessible(true);
        String actualColorspace = (String) getColorspaceMethod.invoke(config);
        // Assert
        assertEquals(expectedColorspace, actualColorspace);
    }

    @Test
    public void testGetColorspaceWithDefault() throws Exception {
        // Arrange
        String expectedColorspace = "gray";
        // Act
        Method getColorspaceMethod = TesseractOCRConfig.class.getDeclaredMethod("getColorspace");
        getColorspaceMethod.setAccessible(true);
        String actualColorspace = (String) getColorspaceMethod.invoke(config);
        // Assert
        assertEquals(expectedColorspace, actualColorspace);
    }

    @Test
    public void testGetColorspaceWithSetAndNull() throws Exception {
        // Arrange
        String expectedColorspace = "rgb";
        // Act
        Method setColorspaceMethod = TesseractOCRConfig.class.getDeclaredMethod("setColorspace", String.class);
        setColorspaceMethod.setAccessible(true);
        setColorspaceMethod.invoke(config, expectedColorspace);
        Method getColorspaceMethod2 = TesseractOCRConfig.class.getDeclaredMethod("getColorspace");
        getColorspaceMethod2.setAccessible(true);
        String actualColorspace = (String) getColorspaceMethod2.invoke(config);
        // Assert
        assertEquals(expectedColorspace, actualColorspace);
    }

    @Test
    public void testGetColorspaceWithSetAndDefault() throws Exception {
        // Arrange
        String expectedColorspace = "rgb";
        // Act
        Method setColorspaceMethod = TesseractOCRConfig.class.getDeclaredMethod("setColorspace", String.class);
        setColorspaceMethod.setAccessible(true);
        setColorspaceMethod.invoke(config, expectedColorspace);
        Method getColorspaceMethod2 = TesseractOCRConfig.class.getDeclaredMethod("getColorspace");
    }
}
