package model;
/**
 * $Id: Version.java 101 2013-02-09 13:22:33Z Fadi.Asbih@gmail.com $
 * $LastChangedDate: 2013-02-09 14:22:33 +0100 (Sa, 09 Feb 2013) $
 * 
 * @author Fadi M. H. Asbih
 * @email fadi_asbih@yahoo.de
 * @version $Revision: 101 $
 * @copyright $Date: 2013-02-09 14:22:33 +0100 (Sa, 09 Feb 2013) $
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
public class Version {

	private int major;
	private int minor;
	private int bug;
	
	public Version() {
		
	}
	
	public Version(int major, int minor, int bug) {
		setMajor(major);
		setMinor(minor);
		setBug(bug);
	}

	/**
	 * @return the major
	 */
	public int getMajor() {
		return major;
	}

	/**
	 * @param major the major to set
	 */
	public void setMajor(int major) {
		this.major = major;
	}

	/**
	 * @return the minor
	 */
	public int getMinor() {
		return minor;
	}

	/**
	 * @param minor the minor to set
	 */
	public void setMinor(int minor) {
		this.minor = minor;
	}

	/**
	 * @return the bug
	 */
	public int getBug() {
		return bug;
	}

	/**
	 * @param bug the bug to set
	 */
	public void setBug(int bug) {
		this.bug = bug;
	}
	
	public String toString() {
		return getMajor()+"."+getMinor()+"."+getBug();
	}
}
