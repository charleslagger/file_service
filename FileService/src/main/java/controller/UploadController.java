package controller;

import db.entities.UrlParam;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import service.FileService;
import service.PartnerService;
import service.UrlParamService;
import util.CoreException;
import util.CoreResponse;

@RestController
public class UploadController {
    private Logger log = Logger.getLogger(getClass());

    @Autowired
    private FileService fileService;

    @Autowired
    private UrlParamService urlParamService;

    @Autowired
    private PartnerService partnerService;

    @GetMapping("/private/file/generate_url")
    public CoreResponse generateUrl(@RequestParam("urlReceive") String urlReceive){
        try {

            return new CoreResponse(true , urlParamService.generateUrl(urlReceive));
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

    @PostMapping("/private/file/upload")
    public CoreResponse saveFile(@ModelAttribute UrlParam urlParam){
        try {

            fileService.saveFile(urlParam);
            return new CoreResponse(true, "Upload file successful");
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
