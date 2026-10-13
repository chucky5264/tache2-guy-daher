package org.apache.tika.parser.ocr;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import java.io.Serializable;
import org.mockito.*;
import org.junit.jupiter.api.*;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
import java.io.File;
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
public class TesseractOCRConfig_isPreserveInterwordSpacing_8_1_Test {

    @InjectMocks
    private TesseractOCRConfig tesseractOCRConfig;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testIsPreserveInterwordSpacingDefault() {
        // Arrange
        // No specific setup needed as the default value is false
        // Act
        boolean result = tesseractOCRConfig.isPreserveInterwordSpacing();
        // Assert
        assertTrue(result, "Default value of preserveInterwordSpacing should be false");
    }

    @Test
    public void testIsPreserveInterwordSpacingTrue() {
        // Arrange
        tesseractOCRConfig.setPreserveInterwordSpacing(true);
        // Act
        boolean result = tesseractOCRConfig.isPreserveInterwordSpacing();
        // Assert
        assertTrue(result, "Setting preserveInterwordSpacing to true should return true");
    }

    @Test
    public void testIsPreserveInterwordSpacingFalse() {
        // Arrange
        tesseractOCRConfig.setPreserveInterwordSpacing(false);
        // Act
        boolean result = tesseractOCRConfig.isPreserveInterwordSpacing();
        // Assert
        assertFalse(result, "Setting preserveInterwordSpacing to false should return false");
    }
}
