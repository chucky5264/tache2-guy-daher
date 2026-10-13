package org.apache.tika.parser.ocr;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.File;
import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.mockito.*;
import org.junit.jupiter.api.*;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
import org.apache.commons.io.FilenameUtils;
import org.apache.tika.exception.TikaConfigException;
import org.apache.tika.utils.StringUtils;

@ExtendWith(MockitoExtension.class)
public class TesseractOCRConfig_isSkipOcr_29_0_Test {

    @InjectMocks
    private TesseractOCRConfig tesseractOCRConfig;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testIsSkipOcr() {
        // Test case 1: skipOcr is false
        tesseractOCRConfig.setSkipOcr(false);
        assertFalse(tesseractOCRConfig.isSkipOcr());
        // Test case 2: skipOcr is true
        tesseractOCRConfig.setSkipOcr(true);
        assertTrue(tesseractOCRConfig.isSkipOcr());
    }

    @Test
    public void testIsSkipOcrWithReflection() throws Exception {
        // Test case 1: skipOcr is false
        tesseractOCRConfig.setSkipOcr(false);
        boolean skipOcr = (boolean) tesseractOCRConfig.getClass().getMethod("isSkipOcr").invoke(tesseractOCRConfig);
        assertFalse(skipOcr);
        // Test case 2: skipOcr is true
        tesseractOCRConfig.setSkipOcr(true);
        skipOcr = (boolean) tesseractOCRConfig.getClass().getMethod("isSkipOcr").invoke(tesseractOCRConfig);
        assertTrue(skipOcr);
    }
}
