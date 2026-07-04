package com.automob.mechanic.controller;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import java.io.IOException;

@RestController
public class WebController {
    @GetMapping("/")           public void home(HttpServletResponse r)        throws IOException { r.sendRedirect("/index.html"); }
    @GetMapping("/admin")      public void admin(HttpServletResponse r)       throws IOException { r.sendRedirect("/admin/index.html"); }
    @GetMapping("/services")   public void services(HttpServletResponse r)    throws IOException { r.sendRedirect("/services.html"); }
    @GetMapping("/booking")    public void booking(HttpServletResponse r)     throws IOException { r.sendRedirect("/booking.html"); }
    @GetMapping("/thankyou")   public void thankyou(HttpServletResponse r)    throws IOException { r.sendRedirect("/thankyou.html"); }
    @GetMapping("/PM")         public void pm(HttpServletResponse r)          throws IOException { r.sendRedirect("/PM.html"); }
    @GetMapping("/bodyrepair") public void bodyrepair(HttpServletResponse r)  throws IOException { r.sendRedirect("/bodyrepair.html"); }
    @GetMapping("/carcare")    public void carcare(HttpServletResponse r)     throws IOException { r.sendRedirect("/carcare.html"); }
}
