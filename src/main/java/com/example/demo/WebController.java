package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;


@Controller
public class WebController {
    @Autowired
    private ProjectResourceComponent handler;

    private final static String DASH_PATH = "C:\\Users\\valdz\\Desktop\\video\\";
    private final static String SUFFIX = "mpd";

    @GetMapping("/index")
    public String index() {
        return "index";
    }



    @RequestMapping(value = {"/dash/{folder}/{file}", "/dash/{file}"}, method = RequestMethod.GET)
    public void streaming(@PathVariable("file") String file, @PathVariable(value="folder", required = false) String folder,  HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        if(folder == null){
            folder = "MPEG-DASH";
        }
        request.setAttribute(ProjectResourceComponent.ATTR_FILE, new File(DASH_PATH + folder + "/"+ file));
        handler.handleRequest(request, response);
    }

    @GetMapping("/videocollection")
    public ModelAndView videoCollection() throws IOException {
        Map<String, Object> model = new HashMap<String, Object>();
        String viewName = "videocollection";
        VideoLibrary library = new VideoLibrary();
        Collection<Video> videos = library.getFiles(DASH_PATH, SUFFIX);

        model.put("videosItems", videos);

        return new ModelAndView(viewName, model);
    }

}
