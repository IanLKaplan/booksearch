/** \file
 * 
 * Mar 16, 2018
 *
 * Copyright Ian Kaplan 2018
 *
 * @author Ian Kaplan, www.bearcave.com, iank@bearcave.com
 */
package booksearch.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * <h4>
 * IndexController
 * </h4>
 * <p>
 * The controller for the main application index page. This page consists of forms, which are supported by the BookSearchController.
 * As a result, there is nothing for the index controller to do.
 * </p>
 * <p>
 * May 22, 2018
 * </p>
 * 
 * @author Ian Kaplan, iank@bearcave.com
 */
@Controller
public class IndexController extends BookControllerBase {

    @GetMapping("/")
    public String index(Model model) {
        return "index";
    }
}
