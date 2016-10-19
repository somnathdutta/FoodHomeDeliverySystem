package ViewModel;

import java.sql.Connection;

import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.ExecutionArgParam;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.select.Selectors;

import Bean.AdminSettingsBean;

public class ItemDetailsViewModel {
	Session session = null;
	
	private Connection connection = null;
	
	@AfterCompose
	public void initSetup(@ContextParam(ContextType.VIEW) Component view,
			@ExecutionArgParam("lunch")AdminSettingsBean lunchCategory,
			@ExecutionArgParam("dinner")AdminSettingsBean dinnerCategory) throws Exception {

		Selectors.wireComponents(view, this, false);
		
		session= Sessions.getCurrent();
		
		connection=(Connection) session.getAttribute("sessionConnection");
		
		connection.setAutoCommit(true);
		
	}
}
