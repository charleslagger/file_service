package controller;

import exception.CoreException;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import service.UrlParamService;
import util.CoreResponse;

@RestController
public class ViewController {
    private Logger log = Logger.getLogger(getClass());
    @Autowired
    private UrlParamService urlParamService;
    @GetMapping("/file/get_file_url")
    public CoreResponse getFileContent(@RequestParam("file_id") long fileId,
                                       @RequestParam("partner_id") String partnerId){
        try {

            return new CoreResponse(true , urlParamService.getFileContent(fileId, partnerId));
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
}
