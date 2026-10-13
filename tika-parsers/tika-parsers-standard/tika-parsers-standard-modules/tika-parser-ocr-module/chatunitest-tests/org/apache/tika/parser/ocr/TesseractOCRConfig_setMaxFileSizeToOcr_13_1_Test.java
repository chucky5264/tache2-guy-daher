package org.apache.tika.parser.ocr;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import java.io.File;
import java.util.HashSet;
import java.util.Set;
import org.mockito.*;
import org.junit.jupiter.api.*;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.tika.exception.TikaConfigException;
import org.apache.tika.utils.StringUtils;

@ExtendWith(MockitoExtension.class)
public class TesseractOCRConfig_setMaxFileSizeToOcr_13_1_Test {

    @InjectMocks
    private TesseractOCRConfig config;

    @BeforeEach
    public void setUp() {
        // Initialize any necessary mocks or configurations
    }

    @Test
    public void testSetMaxFileSizeToOcr() {
        // Arrange
        long expectedValue = 1000L;
        // Act
        config.setMaxFileSizeToOcr(expectedValue);
        // Assert
        assertEquals(expectedValue, config.getMaxFileSizeToOcr());
    }

    @Test
    public void testGetMaxFileSizeToOcr() {
        // Arrange
        long expectedValue = 1000L;
        config.setMaxFileSizeToOcr(expectedValue);
        // Act
        long actualValue = config.getMaxFileSizeToOcr();
        // Assert
        assertEquals(expectedValue, actualValue);
    }

    @Test
    public void testSetMaxFileSizeToOcrWithNegativeValue() {
        // Arrange
        long expectedValue = -1000L;
        // Act
        config.setMaxFileSizeToOcr(expectedValue);
        // Assert
        assertEquals(expectedValue, config.getMaxFileSizeToOcr());
    }

    @Test
    public void testSetMaxFileSizeToOcrWithZeroValue() {
        // Arrange
        long expectedValue = 0L;
        // Act
        config.setMaxFileSizeToOcr(expectedValue);
        // Assert
        assertEquals(expectedValue, config.getMaxFileSizeToOcr());
    }

    @Test
    public void testSetMaxFileSizeToOcrWithMaxValue() {
        // Arrange
        long expectedValue = Long.MAX_VALUE;
        // Act
        config.setMaxFileSizeToOcr(expectedValue);
        // Assert
        assertEquals(expectedValue, config.getMaxFileSizeToOcr());
    }

    @Test
    public void testSetMaxFileSizeToOcrWithMinValue() {
        // Arrange
        long expectedValue = Long.MIN_VALUE;
        // Act
        config.setMaxFileSizeToOcr(expectedValue);
        // Assert
        assertEquals(expectedValue, config.getMaxFileSizeToOcr());
    }
}
