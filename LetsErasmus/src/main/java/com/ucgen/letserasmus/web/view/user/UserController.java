package com.ucgen.letserasmus.web.view.user;

import javax.faces.bean.ManagedBean;

import org.apache.log4j.Level;

import com.ucgen.common.util.CommonUtil;
import com.ucgen.common.util.FileLogger;
import com.ucgen.letserasmus.web.view.BaseController;

@ManagedBean
public class UserController extends BaseController {
	
	private static String PARAM_USER_ID = "userId";
	
	public Long getUserId() {
		try {
			String paramUserId = super.getRequestParameter(PARAM_USER_ID);
			if (paramUserId != null) {
				return Long.valueOf(paramUserId);
			} else {
				return null;
			}
		} catch (Exception e) {
			FileLogger.log(Level.ERROR, "UserController-getUserId()- Error: " + CommonUtil.getExceptionMessage(e));
			return null;
		}
	}
	
}
