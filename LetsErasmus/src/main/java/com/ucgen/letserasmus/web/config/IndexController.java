package com.ucgen.letserasmus.web.config;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping({"/"})
public class IndexController
{
	
  @RequestMapping(method={org.springframework.web.bind.annotation.RequestMethod.GET})
  public String getIndexPage()
  {
    return "body.xhtml";
  }

  @RequestMapping(path={"/pages/DutyList"}, method={org.springframework.web.bind.annotation.RequestMethod.GET})
  public String getIndexPage2() {
    return "DutyList.jsp";
  }
  
}
