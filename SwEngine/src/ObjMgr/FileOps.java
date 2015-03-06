//******************************************************************************
// Package Declaration
//******************************************************************************
package ObjMgr;
//******************************************************************************
// Import Specifications
//******************************************************************************	
import javax.swing.*;import javax.swing.event.*;
import javax.swing.filechooser.*;
import javax.accessibility.*;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.io.*;

import ComMgr.*;
	
/**
 *******************************************************************************
 * <B> Class Description: </B><p><pre>
 *
 * FileOps class is a File dialog box handler class for simple access  
 *
 * </pre>
 *******************************************************************************
 * <B> Author: </B><p><pre>
 *
 *  Howard Xu
 *
 * </pre>
 *******************************************************************************
 * <B> Resources: </B><ul>
 *
 * </ul>
 *******************************************************************************
 * <B> Notes: </B><ul>
 *
 * </ul>
 *******************************************************************************
*/
public class FileOps
{
	public int askConf(String s) {
		return JOptionPane.showConfirmDialog(null, s);
	}
	
	public File fileOpen(String s, String t, boolean bSave) {
		JFileChooser dlg = new JFileChooser();
		File myFile = new File(s); 
		if (myFile.exists()) 
			dlg.setCurrentDirectory(myFile);
		if (bSave) {
			dlg.setSelectedFile(new File(t));
			if (dlg.showSaveDialog(null) == 0) 
				return dlg.getSelectedFile();
		} 
		else
			if (dlg.showOpenDialog(null) == 0) 
				return dlg.getSelectedFile();
		return null;
	}
	
	public void fileSave(String p, String f, String s) {
		try {
			p += "\\"+f;
			File myFile = fileOpen(p+".xml", p+".java", true);
			if (myFile != null) {
				boolean b = true;
				if (myFile.exists()) 
					b = askConf("File " + myFile.getName() + " exist, overwrite it?") == 0;
				if (b){
					FileOutputStream out = new FileOutputStream(myFile);
					PrintWriter pout = new PrintWriter(out);
					pout.print(s);
					pout.close();
					out.close();					 
//					System.out.print(s);		// output
				}
			}
		}
		catch (Exception e) {
			if (LogTrace.getLog().chkLevel(LogTrace.EXECPT)) 
				LogTrace.getLog().Trace(LogTrace.EXECPT, 
					"File Error: " + e.getMessage()); 
		}
	}
}
