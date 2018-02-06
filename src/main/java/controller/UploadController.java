package controller;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.vega.core.CoreException;
import com.vega.core.CoreResponse;
import db.entities.FileCore;
import db.entities.UrlParam;
import db.entities.UrlParam.UrlParams;
import service.FileService;
import service.PartnerService;
import service.UrlParamService;
import util.JsonMapper;

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

    //TODO: upload multiple File
    @PostMapping("/private/file/upload")
    public CoreResponse saveFile(@RequestBody UrlParams responseCore){
        try {
            log.info("Upload file and param: " + JsonMapper.writeValueAsString(responseCore));
            
            return new CoreResponse(true, fileService.saveFile(responseCore));
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
    
    @PostMapping("/private/file/upload_one_file")
    public CoreResponse saveFile(@ModelAttribute UrlParam urlParam){
        try {
            log.info("Upload file and param: " + JsonMapper.writeValueAsString(urlParam));

            return fileService.saveFile(urlParam);
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

    //send notification to core
    @PostMapping("private/file/send_noti_loan")
    public CoreResponse sendNotificationToCore(@RequestBody FileCore.LoanDocuments loanDocuments){
        try {
            log.info("List<LoanDocuments>: " + JsonMapper.writeValueAsString(loanDocuments));
            fileService.sentNotificationLoanDocuments(loanDocuments);
            return new CoreResponse(true, "Send noti list loanDocuments successfully");
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
    
    @PostMapping("private/file/send_noti_contract")
    public CoreResponse sendNotificationToCore(@RequestBody FileCore.AckUpload ackUpload){
        try {
            log.info("AcknowledgeUploaded: " + JsonMapper.writeValueAsString(ackUpload));
            fileService.sentNotificationContract(ackUpload);
            return new CoreResponse(true, "Send noti ackUpload contracts successfully");
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
    
    @GetMapping("private/file/send_noti_disbursement")
    public CoreResponse sendNotificationToCore(@RequestParam String ackUrl,
    		@RequestParam Long orderId, @RequestParam String disbursementDocumentId){
        try {
            log.info("Disbursement " + disbursementDocumentId + " &ackUrl: " + ackUrl + " &orderId: " + orderId);
            fileService.sentNotificationDisbusement(ackUrl, orderId, disbursementDocumentId);
            return new CoreResponse(true, "Send noti ackUpload contracts successfully");
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
