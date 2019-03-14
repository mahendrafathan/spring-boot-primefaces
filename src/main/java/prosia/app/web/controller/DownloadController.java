/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package prosia.app.web.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import org.primefaces.context.RequestContext;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

/**
 *
 * @author Randy
 */
@Controller
@Scope("session")
public class DownloadController implements InitializingBean {
    
    private final Logger log = LoggerFactory.getLogger(getClass());
    
    @Value("${doc.root.path}")
    private String rootPath;
    
    @Override
    public void afterPropertiesSet() throws Exception {
    }
    
    /**
     * Download file from the path.
     * @param filePath
     * @throws IOException 
     */
    public void downloadFile(String filePath) throws IOException {        
        if (filePath == null || filePath.isEmpty()) {
            return;
        }
        
        File file = new File(this.rootPath.concat(filePath));
        if (!file.exists()) {
            log.debug("File not found : {}", filePath);
            return;
        }
        log.debug("file_path : {}", filePath);

        FacesContext facesContext = FacesContext.getCurrentInstance();
        ExternalContext externalContext = facesContext.getExternalContext();
        
        try (InputStream inputStream = new FileInputStream(file)) {
            StreamedContent content = new DefaultStreamedContent(
                    inputStream, externalContext.getMimeType(file.getName()), file.getName());
            
            externalContext.setResponseContentType(content.getContentType());
            externalContext.setResponseHeader("Content-Disposition", "attachment;filename=\"" + content.getName() + "\"");
            if (content.getContentLength() != null) {
                externalContext.setResponseContentLength(content.getContentLength());
            }
            
            if (RequestContext.getCurrentInstance().isSecure()) {
                externalContext.setResponseHeader("Cache-Control", "public");
                externalContext.setResponseHeader("Pragma", "public");
            }
            
            byte[] buffer = new byte[2048];
            
            try (OutputStream outputStream = externalContext.getResponseOutputStream()) {
                int length;
                while ((length = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, length);
                }
            }
        }
        
        externalContext.setResponseStatus(200);
        externalContext.responseFlushBuffer();
        facesContext.responseComplete();    
    }
    
}
