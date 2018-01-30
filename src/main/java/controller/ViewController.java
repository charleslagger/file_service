package controller;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;
import com.vega.core.CoreException;
import com.vega.core.CoreResponse;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import service.UrlParamService;

@RestController
public class ViewController {
    private Logger log = Logger.getLogger(getClass());
    @Autowired
    private UrlParamService urlParamService;
    
    @GetMapping("/private/file/get_file")
    public CoreResponse viewFile(@RequestParam String originalFileId,
                                       @RequestParam String partnerId,
                                       HttpServletResponse response){
        try {
            urlParamService.getFile(originalFileId, partnerId, response);
            return new CoreResponse(true , "View file successfull");
        } catch (CoreException e) {

            log.info(" core exc controller");
            log.info(e.getMessage(), e);
            return new CoreResponse(false, e.getError_code(), e.getMessage());
        } catch (Exception e) {

            log.info(" exc controller");
            log.info(e.getMessage(), e);
            return new CoreResponse(false, "INTERNAL_SERVER_ERROR", e.getMessage());
        }
    }
    
    @GetMapping("/private/file/get_content_file")
    public String getFileContent(@RequestParam String originalFileId,
    		@RequestParam(required = false) String partnerId) throws CoreException, IOException{
    	
    	return urlParamService.getFileContent(originalFileId, partnerId);
    }
}
