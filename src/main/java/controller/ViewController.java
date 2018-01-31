package controller;

import java.io.File;
import java.io.IOException;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.vega.core.CoreException;
import service.UrlParamService;

@RestController
public class ViewController {
    private Logger log = Logger.getLogger(getClass());
    @Autowired
    private UrlParamService urlParamService;
    
    @GetMapping("/private/file/get_file")
    public File getFile(@RequestParam String originalFileId,
                                       @RequestParam String partnerId) throws CoreException, IOException{
    	log.info("==>>>File: originalFileId: " + originalFileId + " &partnerId: " + partnerId);
        return urlParamService.getFile(originalFileId, partnerId);
    }
    
    @GetMapping(value = "/private/file/get_content_file")
    public File getFileContent(@RequestParam String originalFileId,
    		@RequestParam(required = false) String partnerId) throws CoreException, IOException{
    	
    	return urlParamService.getFileContent(originalFileId, partnerId);
    }
}
