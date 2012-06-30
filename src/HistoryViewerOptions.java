import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;


public class HistoryViewerOptions {
    
   
    private static final String RESOLUTION = "resolution";
    
    
    
    Preferences myPrefs = Preferences.userNodeForPackage(this.getClass());
    
   
    
    public int getResolution()
    {
        return myPrefs.getInt(RESOLUTION, 10);
    }
    
    public void setResolution(int resolution)
    {
        myPrefs.putInt(RESOLUTION,resolution);
        try {
            myPrefs.flush();
        } catch (BackingStoreException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

}
