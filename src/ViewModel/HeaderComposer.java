package ViewModel;
import org.zkoss.zk.ui.*;  
import org.zkoss.zk.ui.util.*;  
import org.zkoss.zul.*;  
@SuppressWarnings("serial")
public class HeaderComposer extends GenericForwardComposer{  
	  
    
    Label lblHeader;  
      
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
        String[] parameter = (String[]) param.get("test");  
          
        if (parameter != null)  
            lblHeader.setValue( "Congratulations! Your parameters value is " + parameter[0] );  
        else  
            lblHeader.setValue( "No parameters found. URL should be something like http://yourserver/yoursite/main.zul?parameter=param-value" );  
    }  
}
