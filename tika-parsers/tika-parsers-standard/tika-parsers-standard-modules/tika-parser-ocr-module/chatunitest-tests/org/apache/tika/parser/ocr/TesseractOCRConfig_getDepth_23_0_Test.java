package org.apache.tika.parser.ocr;

import org.apache.tika.parser.ocr.TesseractOCRConfig;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import java.lang.reflect.Method;
import java.util.HashMap;
import org.mockito.*;
import org.junit.jupiter.api.*;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
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

@ExtendWith(MockitoExtension.class)
public class TesseractOCRConfig_getDepth_23_0_Test {

    private TesseractOCRConfig config;

    @BeforeEach
    public void setUp() {
        config = new TesseractOCRConfig();
    }

    @Test
    public void testGetDepth() throws Exception {
        // Arrange
        int expectedDepth = 4;
        // Act
        Method method = TesseractOCRConfig.class.getDeclaredMethod("getDepth");
        method.setAccessible(true);
        int actualDepth = (int) method.invoke(config);
        // Assert
        assertEquals(expectedDepth, actualDepth);
        assertNotNull(actualDepth);
    }
}
