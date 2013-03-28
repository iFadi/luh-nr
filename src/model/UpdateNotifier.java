package model;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.UnknownHostException;

/**
 * $Id$
 * $LastChangedDate$
 * 
 * @author Fadi M. H. Asbih
 * @email fadi_asbih@yahoo.de
 * @version $Revision$
 * @copyright $Date$
 * 
 * TERMS AND CONDITIONS:
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * 
 */
public class UpdateNotifier {
	private String projectURL;
	private Version version;
	
	public UpdateNotifier(Version version) {
		setVersion(version);
		setProjectURL("http://luh-nr.googlecode.com/files/LUH-NR_");
	}
	
	/**
	 * 
	 * @param update
	 * @return if there is a connection to the server
	 */
	public boolean check(String update)
    {
            try {
                    //make a URL to a known source
                    URL url = new URL(update);

                    //open a connection to that source
                    HttpURLConnection urlConnect = (HttpURLConnection)url.openConnection();

                    //trying to retrieve data from the source. If there
                    //is no connection, this line will fail
                    Object objData = urlConnect.getContent();
                    System.out.println(objData);

            } catch (UnknownHostException e) {
                    return false;
            }
            catch (IOException e) {
                    return false;
            }
            return true;
    }
	
	/**
	 * 
	 * @return true if new version is available
	 */
	public boolean IsNewVersionAvailable() {
		if(check(getProjectURL()+(getVersion().getMajor()+1)+"."+getVersion().getMinor()+"."+getVersion().getBug()+".jar")) 
			return true;
		else if(check(getProjectURL()+getVersion().getMajor()+"."+(getVersion().getMinor()+1)+"."+getVersion().getBug()+".jar"))
			return true;
		else if(check(getProjectURL()+getVersion().getMajor()+"."+getVersion().getMinor()+"."+(getVersion().getBug()+1)+".jar"))
			return true;
		else
			return false;
	}

	/**
	 * @return the version
	 */
	public Version getVersion() {
		return version;
	}

	/**
	 * @param version the version to set
	 */
	public void setVersion(Version version) {
		this.version = version;
	}

	/**
	 * @return the projectURL
	 */
	public String getProjectURL() {
		return projectURL;
	}

	/**
	 * @param projectURL the projectURL to set
	 */
	public void setProjectURL(String projectURL) {
		this.projectURL = projectURL;
	}	
}
