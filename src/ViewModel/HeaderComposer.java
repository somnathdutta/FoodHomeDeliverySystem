package ViewModel;
import java.util.HashMap;
import java.util.Map;

import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.*;  
import org.zkoss.zk.ui.util.*;  
import org.zkoss.zul.*;  
@SuppressWarnings("serial")
public class HeaderComposer extends GenericForwardComposer{  
	  
    
    Label lblHeader;  
    private String email;
    private String hashCode;
    
    @Override  
    public void doAfterCompose(Component comp) throws Exception {  
          
        try {  
            super.doAfterCompose(comp);  
        }   
        catch (Exception e) {  
            e.printStackTrace();  
        }  
          
        /* 
         * retrieve url parameters 
         */  
        String[] parameter = (String[]) param.get("rc");
        System.out.println("Parameter " + parameter);
        hashCode = parameter[0];
        System.out.println("Has Code " + hashCode);
        /*if (parameter != null)  
            lblHeader.setValue( "Congratulations! Your parameters value is " + parameter[0] );  
        else  
            lblHeader.setValue( "No parameters found. URL should be something like http://yourserver/yoursite/main.zul?parameter=param-value" );*/
        
        
        Map<String, String> map = new HashMap<String, String>();
        map.put("hashcode", hashCode);
        
        Window window = (Window) Executions.createComponents("forGotPassWord.zul", null, map);
        window.doModal();
        
    }
    
    
    
    
    
    @Command
    @NotifyChange("*")
    public void onClickUpdate(){
    	
    }
    
    
	public Label getLblHeader() {
		return lblHeader;
	}
	public void setLblHeader(Label lblHeader) {
		this.lblHeader = lblHeader;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}





	public String getHashCode() {
		return hashCode;
	}





	public void setHashCode(String hashCode) {
		this.hashCode = hashCode;
	}  
}
