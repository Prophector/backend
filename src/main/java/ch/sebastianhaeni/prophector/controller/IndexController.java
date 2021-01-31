package ch.sebastianhaeni.prophector.controller;

import ch.sebastianhaeni.prophector.service.ProphectorResourceLoader;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.io.IOException;

@Controller
@RequiredArgsConstructor
public class IndexController {

    private final ProphectorResourceLoader resourceLoader;

    @GetMapping(path = {"/", "/index.html", "/error",}, produces = MediaType.TEXT_HTML_VALUE)
    public ResponseEntity<String> index() throws IOException {
        var indexHtml = resourceLoader.readWebFileAsString("/index.html");
        return ResponseEntity.ok().body(indexHtml);
    }

}
