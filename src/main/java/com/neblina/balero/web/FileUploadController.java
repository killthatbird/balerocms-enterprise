/**
 * Balero CMS Project: Proyecto 100% Mexicano de código libre.
 * Página Oficial: http://www.balerocms.com
 *
 * @author      Anibal Gomez <anibalgomez@icloud.com>
 * @copyright   Copyright (C) 2015 Neblina Software. Derechos reservados.
 * @license     Licencia BSD; vea LICENSE.txt
 */

package com.neblina.balero.web;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

@Controller
public class FileUploadController {

    private static final Logger log = LogManager.getLogger(FileUploadController.class.getName());

    @RequestMapping(value="/upload", method=RequestMethod.GET)
    @ResponseBody
    public String provideUploadInfo() {
        log.debug("GET /upload {}");
        return "You can upload a file by posting to this same URL.";
    }

    /**
     * Uploading images asyncronic method for summernote
     * @author Anibal Gomez <anibalgomez@icloud.com>
     * References:
     * https://spring.io/guides/gs/uploading-files/
     * http://stackoverflow.com/questions/21628222/summernote-image-upload
     * https://developer.mozilla.org/en-US/docs/Web/API/FormData/append
     * https://github.com/CollectionFS/Meteor-CollectionFS/issues/489
     * http://stackoverflow.com/questions/14089146/file-loading-by-getclass-getresource
     * @param file
     * @return
     */
    @Secured({"ROLE_ADMIN", "ROLE_USER"})
    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    @ResponseBody
    public HttpStatus handleFileUpload(@RequestParam("file") MultipartFile file){
        log.debug("POST /upload {}");
        String filePath = FileUploadController.class.getResource("/static/images/uploads/").getPath() + file.getOriginalFilename();
        if (!file.isEmpty()) {
            try {
                byte[] bytes = file.getBytes();
                BufferedOutputStream stream =
                        new BufferedOutputStream(new FileOutputStream(new File(filePath)));
                stream.write(bytes);
                stream.close();
                log.debug("Creating file: " + filePath);
                log.debug("You successfully uploaded " + file.getOriginalFilename() + "!");
                return HttpStatus.OK;
            } catch (Exception e) {
                log.debug("You failed to upload " + file.getOriginalFilename() + " => " + e.getMessage());
                return HttpStatus.FORBIDDEN;
            }
        } else {
            log.debug("You failed to upload " + file.getOriginalFilename() + " because the file was empty.");
            return HttpStatus.FORBIDDEN;
        }
    }

}