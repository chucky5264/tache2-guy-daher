package org.apache.tika.parser.ocr;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.*;
import org.junit.jupiter.api.*;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
import java.io.File;
import java.io.Serializable;
import java.util.HashMap;
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

@ExtendWith(MockitoExtension.class)
public class // Additional tests can be added here to cover other scenarios, such as null or empty strings
TesseractOCRConfig_getPageSeparator_5_1_Test {

    @Mock
    private TesseractOCRConfig tesseractOCRConfig;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetPageSeparator() {
        // Arrange
        when(tesseractOCRConfig.getPageSeparator()).thenReturn("samplePageSeparator");
        // Act
        String result = tesseractOCRConfig.getPageSeparator();
        // Assert
        assertEquals("samplePageSeparator", result);
    }

    @Test
    public void testGetPageSeparatorDefault() {
        // Arrange
        when(tesseractOCRConfig.getPageSeparator()).thenReturn("");
        // Act
        String result = tesseractOCRConfig.getPageSeparator();
        // Assert
        assertEquals("", result);
    }
}
